package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

public abstract class CMixinStickyPistonSides {
	@Mixin(Block.class)
	public interface IMixinBlock {
		@Accessor
		IIcon getBlockIcon();
	}

	@Mixin(BlockPistonBase.class)
	public static abstract class MixinBlockPistonBase {
		@Shadow
		@Final
		private boolean isSticky;

		@ModifyArg(method = "registerIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/IIconRegister;registerIcon(Ljava/lang/String;)Lnet/minecraft/util/IIcon;", ordinal = 0))
		private String makeStickySide(final String texture) {
			return this.isSticky && "piston_side".equals(texture) ? "tomrum:piston_side_sticky" : texture;
		}

		@Inject(method = "getPistonBaseIcon", at = @At("RETURN"), cancellable = true)
		private static void allowStickyTextures(final String texture, final CallbackInfoReturnable<IIcon> cir) {
			if ("piston_side_sticky".equals(texture)) {
				cir.setReturnValue(((IMixinBlock) Blocks.sticky_piston).getBlockIcon());
			}
		}

		@Redirect(method = "onBlockEventReceived", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z", ordinal = 0), slice = @Slice(to = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockPistonBase;isSticky:Z", opcode = Opcodes.GETFIELD, ordinal = 0)))
		private boolean slimeTheRetraction(final World world, final int x, final int y, final int z, final Block block, final int meta, final int flags) {
			return world.setBlock(x, y, z, block, meta | (world.isRemote && this.isSticky ? 8 : 0), flags);
		}
	}

	@Mixin(BlockPistonExtension.class)
	public static abstract class MixinBlockPistonExtension {
		@Inject(method = "getIcon", at = @At("RETURN"), cancellable = true)
		private void slimeTheExtension(final int side, final int meta, final CallbackInfoReturnable<IIcon> cir) {
			if ((meta & 8) != 0) {
				final String iconName = cir.getReturnValue().getIconName();
				if ("piston_side".equals(iconName)) {
					cir.setReturnValue(BlockPistonBase.getPistonBaseIcon("piston_side_sticky"));
				} else if ("piston_top_normal".equals(iconName)) {
					cir.setReturnValue(BlockPistonBase.getPistonBaseIcon("piston_top_sticky"));
				}
			}
		}
	}

	@Mixin(RenderBlocks.class)
	public static abstract class MixinRenderBlocks {
		@Inject(method = "renderPistonExtension", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/IBlockAccess;getBlockMetadata(III)I"), locals = LocalCapture.CAPTURE_FAILHARD)
		public void determineIfSticky(final Block block, final int x, final int y, final int z, final boolean isShort, final CallbackInfoReturnable<Boolean> cir, final int meta) {
			Tomrum.INSTANCE.pistonExtensionTexture = (meta & 8) != 0 ? "piston_side_sticky" : "piston_side";
		}

		@ModifyArg(method = {"renderPistonRodUD", "renderPistonRodSN", "renderPistonRodEW"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockPistonBase;getPistonBaseIcon(Ljava/lang/String;)Lnet/minecraft/util/IIcon;"))
		private String onRenderPistonExtension(final String value) {
			return Tomrum.INSTANCE.pistonExtensionTexture;
		}
	}
}
