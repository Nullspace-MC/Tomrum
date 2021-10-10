package net.dugged.tomrum.mixins.protocol4;

import net.dugged.tomrum.Tomrum;
import net.minecraft.network.ServerStatusResponse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class)
public abstract class MixinServerStatusResponse_MinecraftProtocolVersionIdentifier {
	// Stolen/inspired by https://github.com/killjoy1221/Protocol4/blob/master/src/main/java/mnm/mods/protocol/protocol/v4/ServerInfo_4.java

	@Final
	@Mutable
	@Shadow
	private int protocol;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(final CallbackInfo ci) {
		if (!Tomrum.INSTANCE.v4Protocol) {
			return;
		}

		if (this.protocol == 4) {
			this.protocol = 5;
		} else if (this.protocol == 5) {
			this.protocol = 4;
		}
	}
}
