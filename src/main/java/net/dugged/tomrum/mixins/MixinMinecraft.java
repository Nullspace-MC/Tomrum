package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;fireKeyInput()V", remap = false))
	private void fireTheKeyPlease(final CallbackInfo ci) {
		Tomrum.INSTANCE.onKeyPress();
	}
}
