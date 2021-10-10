package net.dugged.tomrum.mixins.protocol4;

import net.minecraft.tileentity.TileEntitySkull;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntitySkull.class)
public abstract class MixinTileEntitySkull {
	// Stolen/inspired by https://github.com/killjoy1221/Protocol4/blob/master/src/main/java/mnm/mods/protocol/protocol/v4/UpdateTileEntity_4.java

	@Redirect(method = "readFromNBT", at = @At(value = "FIELD", target = "Lnet/minecraft/tileentity/TileEntitySkull;skullType:I", opcode = Opcodes.GETFIELD))
	private int onGetProfileData(final TileEntitySkull skull) {
		// TODO make custom players head actually render their skins
		return 69;
	}
}
