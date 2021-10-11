package net.dugged.tomrum.mixins;

import net.minecraft.client.renderer.tileentity.TileEntityRendererPiston;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class CMixinBlinkingPistonFix {
	@Mixin(TileEntityPiston.class)
	public static abstract class MixinTileEntityPiston extends TileEntity {
		@Inject(method = "updateEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeTileEntity(III)V"), cancellable = true)
		private void mc54546(final CallbackInfo ci) {
			if (this.worldObj.isRemote) {
				ci.cancel();
			}
		}
	}

	@Mixin(TileEntityRendererPiston.class)
	public static abstract class MixinTileEntityRendererPiston {
		@ModifyConstant(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityPiston;DDDF)V", constant = @Constant(floatValue = 1F, ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getMaterial()Lnet/minecraft/block/material/Material;", ordinal = 0)))
		private float fixPistonBlink(final float value) {
			return Float.MAX_VALUE;
		}
	}
}
