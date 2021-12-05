package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.network.LanServerDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer {
	@Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/LanServerDetector$ThreadLanServerFind;start()V"))
	private void onStartSearchingForLanServers(final LanServerDetector.ThreadLanServerFind instance) {
		if (!Tomrum.CONFIG.hideLanServers) {
			instance.start();
		}
	}
}
