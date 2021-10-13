package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
}
