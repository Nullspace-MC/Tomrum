package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {
	@Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;onPlayerPreTick(Lnet/minecraft/entity/player/EntityPlayer;)V", remap = false))
	private void onPlayerPreTick(final CallbackInfo ci) {
		Tomrum.INSTANCE.onPlayerPreTick((EntityPlayer) (Object) this);
	}
}
