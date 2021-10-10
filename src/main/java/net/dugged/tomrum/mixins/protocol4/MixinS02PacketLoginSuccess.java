package net.dugged.tomrum.mixins.protocol4;

import com.mojang.util.UUIDTypeAdapter;
import net.dugged.tomrum.Tomrum;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(S02PacketLoginSuccess.class)
public abstract class MixinS02PacketLoginSuccess {
	// Stolen/inspired by https://github.com/killjoy1221/Protocol4/blob/master/src/main/java/mnm/mods/protocol/protocol/v4/LoginSuccess_4.java

	@Redirect(method = "readPacketData", at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;", remap = false))
	private UUID onReadPacketData(final String uuid) {
		return Tomrum.INSTANCE.v4Protocol ? UUIDTypeAdapter.fromString(uuid) : UUID.fromString(uuid);
	}
}
