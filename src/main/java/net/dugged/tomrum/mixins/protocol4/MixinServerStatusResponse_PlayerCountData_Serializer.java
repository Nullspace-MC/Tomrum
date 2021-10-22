package net.dugged.tomrum.mixins.protocol4;

import com.mojang.util.UUIDTypeAdapter;
import net.dugged.tomrum.Tomrum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(targets = "net/minecraft/network/ServerStatusResponse$PlayerCountData$Serializer")
public abstract class MixinServerStatusResponse_PlayerCountData_Serializer {
	@Redirect(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/ServerStatusResponse$PlayerCountData;", remap = false, at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;", remap = false))
	private UUID onDeserialize(final String uuid) {
		return Tomrum.INSTANCE.v4Protocol ? UUIDTypeAdapter.fromString(uuid) : UUID.fromString(uuid);
	}
}
