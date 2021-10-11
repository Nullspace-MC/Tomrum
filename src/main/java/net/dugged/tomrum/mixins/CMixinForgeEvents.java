package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class CMixinForgeEvents {
	@Mixin(EntityPlayer.class)
	public static abstract class MixinEntityPlayer {
		@Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;onPlayerPreTick(Lnet/minecraft/entity/player/EntityPlayer;)V", remap = false))
		private void fireOnPlayerPreTick(final CallbackInfo ci) {
			Tomrum.INSTANCE.onPlayerPreTick((EntityPlayer) (Object) this);
		}
	}

	@Mixin(Minecraft.class)
	public static abstract class MixinMinecraft {
		@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;fireKeyInput()V", remap = false))
		private void fireOnKeyPress(final CallbackInfo ci) {
			Tomrum.INSTANCE.onKeyPress();
		}
	}
}
