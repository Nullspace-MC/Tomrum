package net.dugged.tomrum.mixins.protocol4;

import com.mojang.util.UUIDTypeAdapter;
import net.dugged.tomrum.Tomrum;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(S0CPacketSpawnPlayer.class)
public abstract class MixinS0CPacketSpawnPlayer {
	// Stolen/inspired by https://github.com/killjoy1221/Protocol4/blob/master/src/main/java/mnm/mods/protocol/protocol/v4/SpawnPlayer_4.java

	@Redirect(method = "readPacketData", at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;", remap = false))
	private UUID onReadPacketData(final String uuid) {
		return Tomrum.INSTANCE.v4Protocol ? UUIDTypeAdapter.fromString(uuid) : UUID.fromString(uuid);
	}

	@Redirect(method = "readPacketData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readVarIntFromBuffer()I", ordinal = 1))
	private int adjustPropertyCount(final PacketBuffer buffer) {
		return Tomrum.INSTANCE.v4Protocol ? 0 : buffer.readVarIntFromBuffer();
	}

	@Redirect(method = "writePacketData", at = @At(value = "INVOKE", target = "Ljava/util/UUID;toString()Ljava/lang/String;", remap = false))
	private String onWritePacketData(final UUID uuid) {
		return Tomrum.INSTANCE.v4Protocol ? UUIDTypeAdapter.fromUUID(uuid) : uuid.toString();
	}
}
