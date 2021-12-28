package net.dugged.tomrum.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {
	@Inject(method = "checkHotbarKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;handleMouseClick(Lnet/minecraft/inventory/Slot;III)V", shift = At.Shift.AFTER))
	private void onSwapStacks(final int keyCode, final CallbackInfoReturnable<Boolean> cir) {
		Minecraft.getMinecraft().thePlayer.inventoryContainer.detectAndSendChanges();
	}
}
