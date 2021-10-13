package net.dugged.tomrum.mixins;

import net.dugged.tomrum.Tomrum;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

public abstract class CMixinCreativeNoclip {
	@Mixin(PlayerControllerMP.class)
	public static abstract class MixinPlayerControllerMP {
		@Redirect(method = "onPlayerRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBlock;func_150936_a(Lnet/minecraft/world/World;IIIILnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z"))
		private boolean allowCreativeNoclip(final ItemBlock item, final World world, final int x, final int y, final int z, final int side, final EntityPlayer player, final ItemStack stack) {
			return Tomrum.CONFIG.creativeNoclip && player.capabilities.isCreativeMode || item.func_150936_a(world, x, y, z, side, player, stack);
		}
	}

	@Mixin(TileEntityPiston.class)
	public static abstract class MixinTileEntityPiston {
		@Redirect(method = "func_145863_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
		private List<Entity> stopNoclipPistonMovement(final World world, final Entity entity, final AxisAlignedBB bb) {
			return Tomrum.CONFIG.creativeNoclip ? world.getEntitiesWithinAABBExcludingEntity(entity, bb, e -> !e.noClip) : world.getEntitiesWithinAABBExcludingEntity(entity, bb);
		}
	}
}
