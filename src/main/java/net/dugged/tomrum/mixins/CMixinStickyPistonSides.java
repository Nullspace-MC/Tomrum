package net.dugged.tomrum.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

		@Inject(method = "getPistonBaseIcon", at = @At("RETURN"), cancellable = true)
		private static void allowStickyTextures(final String texture, final CallbackInfoReturnable<IIcon> cir) {
			if ("piston_side_sticky".equals(texture)) {
				cir.setReturnValue(((IMixinBlock) Blocks.sticky_piston).getBlockIcon());
			}
		}

		@ModifyArg(method = "registerIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/IIconRegister;registerIcon(Ljava/lang/String;)Lnet/minecraft/util/IIcon;", ordinal = 0))
		private String makeSticky(final String texture) {
			return this.isSticky && "piston_side".equals(texture) ? "tomrum:piston_side_sticky" : texture;
		}
	}

	@Mixin(BlockPistonExtension.class)
	public static abstract class MixinBlockPistonExtension {
		@Inject(method = "getIcon", at = @At("RETURN"), cancellable = true)
		private void slimeTheExtension(final int side, final int meta, final CallbackInfoReturnable<IIcon> cir) {
            if ((meta & 8) != 0) {
                int k = BlockPistonExtension.getDirectionMeta(meta);
                if (k < 6 && side == Facing.oppositeSide[k]) {
                    cir.setReturnValue(BlockPistonBase.getPistonBaseIcon("piston_top_sticky"));
                } else if ("piston_side".equals(cir.getReturnValue().getIconName())) {
                    cir.setReturnValue(BlockPistonBase.getPistonBaseIcon("piston_side_sticky"));
                }
            }
		}
	}
}
