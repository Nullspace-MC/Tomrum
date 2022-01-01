package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.renderer.entity.RenderWither;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderWither.class)
public abstract class MixinRenderWither {
	@ModifyArg(method = "doRender(Lnet/minecraft/entity/boss/EntityWither;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/BossStatus;setBossStatus(Lnet/minecraft/entity/boss/IBossDisplayData;Z)V"))
	private boolean conditionallyDarkenSky(final boolean skyModifier) {
		return !Tomrum.CONFIG.ignoreWitherSky && skyModifier;
	}
}
