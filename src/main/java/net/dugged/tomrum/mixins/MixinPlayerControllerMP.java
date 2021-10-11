package net.dugged.tomrum.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
	@Redirect(method = "onPlayerRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBlock;func_150936_a(Lnet/minecraft/world/World;IIIILnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z"))
	private boolean allowCreativeNoClip(final ItemBlock item, final World world, final int x, final int y, final int z, final int side, final EntityPlayer player, final ItemStack stack) {
		return player.capabilities.isCreativeMode || item.func_150936_a(world, x, y, z, side, player, stack);
	}
}
