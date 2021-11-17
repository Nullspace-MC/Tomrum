package net.dugged.tomrum;

import net.dugged.tomrum.mixins.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
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

		final Minecraft mc = Minecraft.getMinecraft();
		final EntityClientPlayerMP player = mc.thePlayer;
		final MovingObjectPosition raytrace = player.rayTrace(500D, ((IMixinMinecraft) mc).getTimer().renderPartialTicks);
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
		while (!world.checkBlockCollision(bb)) {
			bb.offset(0F, 1F, 0F);
		}

		player.sendChatMessage(String.format("/tp %s %s %s", x, bb.minY, z));
	}
}
