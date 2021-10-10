package net.dugged.tomrum.mixins.protocol4;

import net.dugged.tomrum.Tomrum;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(C00Handshake.class)
public abstract class MixinC00Handshake {
	// Stolen/inspired by https://github.com/killjoy1221/Protocol4/blob/master/src/main/java/mnm/mods/protocol/protocol/v4/Handshake_4.java

	@Shadow
	private int protocolVersion;

	@Inject(method = "<init>(ILjava/lang/String;ILnet/minecraft/network/EnumConnectionState;)V", at = @At("RETURN"))
	private void onInit(final int protocol, final String ip, final int port, final EnumConnectionState state, CallbackInfo ci) {
		if (Tomrum.INSTANCE.v4Protocol) {
			this.protocolVersion = 4;
		}
	}
}
