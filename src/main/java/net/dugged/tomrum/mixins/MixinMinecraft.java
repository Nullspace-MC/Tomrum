package net.dugged.tomrum.mixins;

import net.dugged.tomrum.CompassTeleport;
import net.dugged.tomrum.Tomrum;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@ModifyArg(method = "middleClickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendSlotPacket(Lnet/minecraft/item/ItemStack;I)V"))
	private ItemStack setMaximumStackSize(final ItemStack stack) {
		if (!Tomrum.CONFIG.alwaysPickBlockMaxStack) {
			return stack;
		}

		stack.stackSize = stack.getMaxStackSize();
		return stack;
	}

	@Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
	private void onLeftClick(final CallbackInfo ci) {
		if (!Tomrum.INSTANCE.compass.onLeftClick()) {
			ci.cancel();
		}
	}

	@ModifyArg(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;sendClickBlockToController(Z)V"))
	private boolean shouldClick(final boolean leftClick) {
		return leftClick && CompassTeleport.shouldLeftClickNormally();
	}
}
