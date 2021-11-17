package net.dugged.tomrum;

import net.dugged.tomrum.mixins.IMixinMinecraft;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CompassTeleport {
	public static boolean shouldLeftClickNormally() {
		final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		return !Tomrum.CONFIG.compassTeleport || !player.capabilities.isCreativeMode || player.getHeldItem() == null || player.getHeldItem().getItem() != Items.compass;
	}

	public boolean onLeftClick() {
		if (shouldLeftClickNormally()) {
			return true;
		}

		final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		final MovingObjectPosition raytrace = this.rayTrace(player, 500D);
		if (raytrace == null) {
			return true;
		}

		this.teleportToSafeLocation(player, raytrace.blockX, raytrace.blockY, raytrace.blockZ);
		return false;
	}

	private void teleportToSafeLocation(final EntityClientPlayerMP player, final float x, final float y, final float z) {
		final World world = player.worldObj;
		final float clampedY = MathHelper.clamp_float(y, 0, world.getHeight());
		final float halfWidth = player.width / 2F;
		final AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - halfWidth, clampedY, z - halfWidth, x + halfWidth, clampedY + player.height, z + halfWidth);
		while (world.checkBlockCollision(bb)) {
			bb.offset(0F, 1F, 0F);
		}

		player.sendChatMessage(String.format("/tp %s %s %s", x, bb.minY, z));
	}

	public MovingObjectPosition rayTrace(final EntityClientPlayerMP player, final double distance) {
		final float partialTicks = ((IMixinMinecraft) Minecraft.getMinecraft()).getTimer().renderPartialTicks;
		final Vec3 from = player.getPosition(partialTicks);
		final Vec3 look = player.getLook(partialTicks);
		final Vec3 to = from.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
		return this.rayTraceBlocks(player.worldObj, (int) distance, from, to);
	}

	public MovingObjectPosition rayTraceBlocks(final World world, int steps, final Vec3 from, final Vec3 to) {
		if (Double.isNaN(from.xCoord) || Double.isNaN(from.yCoord) || Double.isNaN(from.zCoord) || Double.isNaN(to.xCoord) || Double.isNaN(to.yCoord) || Double.isNaN(to.zCoord)) {
			return null;
		}

		final int toX = MathHelper.floor_double(to.xCoord);
		final int toY = MathHelper.floor_double(to.yCoord);
		final int toZ = MathHelper.floor_double(to.zCoord);
		int fromX = MathHelper.floor_double(from.xCoord);
		int fromY = MathHelper.floor_double(from.yCoord);
		int fromZ = MathHelper.floor_double(from.zCoord);
		{
			final Block block = world.getBlock(fromX, fromY, fromZ);
			if (block.canStopRayTrace(world.getBlockMetadata(fromX, fromY, fromZ), false)) {
				final MovingObjectPosition raytrace = block.collisionRayTrace(world, fromX, fromY, fromZ, from, to);
				if (raytrace != null) {
					return raytrace;
				}
			}
		}

		MovingObjectPosition currentRaytrace = null;
		while (steps-- >= 0) {
			if (Double.isNaN(from.xCoord) || Double.isNaN(from.yCoord) || Double.isNaN(from.zCoord)) {
				return null;
			}

			if (fromX == toX && fromY == toY && fromZ == toZ) {
				return currentRaytrace;
			}

			final double initial = 999D;
			double stepX = initial;
			double stepY = initial;
			double stepZ = initial;
			double multX = initial;
			double multY = initial;
			double multZ = initial;
			final double dX = to.xCoord - from.xCoord;
			final double dY = to.yCoord - from.yCoord;
			final double dZ = to.zCoord - from.zCoord;

			if (toX != fromX) {
				stepX = fromX + (toX > fromX ? 1D : -1D);
				multX = (stepX - from.xCoord) / dX;
			}

			if (toY != fromY) {
				stepY = fromY + (toY > fromY ? 1D : -1D);
				multY = (stepY - from.yCoord) / dY;
			}

			if (toZ != fromZ) {
				stepZ = fromZ + (toZ > fromZ ? 1D : -1D);
				multZ = (stepZ - from.zCoord) / dZ;
			}

			byte sideHit;
			if (multX < multY && multX < multZ) {
				sideHit = (byte) (toX > fromX ? 4 : 5);
				from.xCoord = stepX;
				from.yCoord += dY * multX;
				from.zCoord += dZ * multX;
			} else if (multY < multZ) {
				sideHit = (byte) (toY > fromY ? 0 : 1);
				from.xCoord += dX * multY;
				from.yCoord = stepY;
				from.zCoord += dZ * multY;
			} else {
				sideHit = (byte) (toZ > fromZ ? 2 : 3);
				from.xCoord += dX * multZ;
				from.yCoord += dY * multZ;
				from.zCoord = stepZ;
			}

			fromX = MathHelper.floor_double(from.xCoord) - (sideHit == 5 ? 1 : 0);
			fromY = MathHelper.floor_double(from.yCoord) - (sideHit == 1 ? 1 : 0);
			fromZ = MathHelper.floor_double(from.zCoord) - (sideHit == 3 ? 1 : 0);

			final Block block = world.getBlock(fromX, fromY, fromZ);
			if (!block.canStopRayTrace(world.getBlockMetadata(fromX, fromY, fromZ), false)) {
				currentRaytrace = new MovingObjectPosition(fromX, fromY, fromZ, sideHit, from, false);
			} else {
				final MovingObjectPosition raytrace = block.collisionRayTrace(world, fromX, fromY, fromZ, from, to);
				if (raytrace != null) {
					return raytrace;
				}
			}
		}

		return currentRaytrace;
	}
}
