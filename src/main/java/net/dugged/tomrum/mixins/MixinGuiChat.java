package net.dugged.tomrum.mixins;

import com.google.common.base.Splitter;
import net.dugged.tomrum.Tomrum;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {
	@ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;setMaxStringLength(I)V"))
	private int increaseLimit(final int maxLength) {
		return Tomrum.CONFIG.extendedChat ? 8 * maxLength : maxLength;
	}

	@Redirect(method = "submitChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;sendChatMessage(Ljava/lang/String;)V"))
	private void increaseLimit(final EntityClientPlayerMP player, final String msg) {
		Splitter.fixedLength(100).split(msg).forEach(player::sendChatMessage);
	}
}
