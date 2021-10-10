package net.dugged.tomrum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class ChunkBorderRenderer {
	private boolean shouldRender = false;

	public void toggleVisibility() {
		this.shouldRender = !this.shouldRender;
	}

	public void render(final float partialTicks) {
		if (!this.shouldRender) {
			return;
		}

		final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		final double dX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		final double dY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		final double dZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		final double bot = -dY;
		final double top = 256D - dY;
		final double cX = player.chunkCoordX * 16 - dX;
		final double cZ = player.chunkCoordZ * 16 - dZ;

		final Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glLineWidth(1F);
		tessellator.startDrawing(GL11.GL_CURRENT_BIT | GL11.GL_POINT_BIT);

		for (int x = -16; x <= 32; x += 16) {
			for (int z = -16; z <= 32; z += 16) {
				tessellator.setColorRGBA_F(255, 0, 0, 0);
				tessellator.addVertex(cX + x, bot, cZ + z);

				tessellator.setColorRGBA_F(255, 0, 0, 127);
				tessellator.addVertex(cX + x, bot, cZ + z);
				tessellator.addVertex(cX + x, top, cZ + z);

				tessellator.setColorRGBA_F(255, 0, 0, 0);
				tessellator.addVertex(cX + x, top, cZ + z);
			}
		}

		for (int x = 2; x < 16; x += 2) {
			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX + x, bot, cZ);

			tessellator.setColorOpaque(255, 255, 0);
			tessellator.addVertex(cX + x, bot, cZ);
			tessellator.addVertex(cX + x, top, cZ);

			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX + x, top, cZ);
			tessellator.addVertex(cX + x, bot, cZ + 16D);

			tessellator.setColorOpaque(255, 255, 0);
			tessellator.addVertex(cX + x, bot, cZ + 16D);
			tessellator.addVertex(cX + x, top, cZ + 16D);

			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX + x, top, cZ + 16D);
		}

		for (int z = 2; z < 16; z += 2) {
			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX, bot, cZ + z);

			tessellator.setColorOpaque(255, 255, 0);
			tessellator.addVertex(cX, bot, cZ + z);
			tessellator.addVertex(cX, top, cZ + z);

			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX, top, cZ + z);
			tessellator.addVertex(cX + 16D, bot, cZ + z);

			tessellator.setColorOpaque(255, 255, 0);
			tessellator.addVertex(cX + 16D, bot, cZ + z);
			tessellator.addVertex(cX + 16D, top, cZ + z);

			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX + 16D, top, cZ + z);
		}

		for (int y = 0; y <= 256; y += 2) {
			final double yLine = y - dY;
			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX, yLine, cZ);

			tessellator.setColorOpaque(255, 255, 0);
			tessellator.addVertex(cX, yLine, cZ);
			tessellator.addVertex(cX, yLine, cZ + 16D);
			tessellator.addVertex(cX + 16D, yLine, cZ + 16D);
			tessellator.addVertex(cX + 16D, yLine, cZ);
			tessellator.addVertex(cX, yLine, cZ);

			tessellator.setColorRGBA_F(255, 255, 0, 0);
			tessellator.addVertex(cX, yLine, cZ);
		}

		tessellator.draw();
		GL11.glLineWidth(2F);
		tessellator.startDrawing(GL11.GL_CURRENT_BIT | GL11.GL_POINT_BIT);

		for (int x = 0; x <= 16; x += 16) {
			for (int z = 0; z <= 16; z += 16) {
				tessellator.setColorRGBA_F(63, 63, 255, 0);
				tessellator.addVertex(cX + x, bot, cZ + z);

				tessellator.setColorOpaque(63, 63, 255);
				tessellator.addVertex(cX + x, bot, cZ + z);
				tessellator.addVertex(cX + x, top, cZ + z);

				tessellator.setColorRGBA_F(63, 63, 255, 0);
				tessellator.addVertex(cX + x, top, cZ + z);
			}
		}

		for (int y = 0; y <= 256; y += 16) {
			final double yLine = y - dY;
			tessellator.setColorRGBA_F(63, 63, 255, 0);
			tessellator.addVertex(cX, yLine, cZ);

			tessellator.setColorOpaque(63, 63, 255);
			tessellator.addVertex(cX, yLine, cZ);
			tessellator.addVertex(cX, yLine, cZ + 16D);
			tessellator.addVertex(cX + 16D, yLine, cZ + 16D);
			tessellator.addVertex(cX + 16D, yLine, cZ);
			tessellator.addVertex(cX, yLine, cZ);

			tessellator.setColorRGBA_F(63, 63, 255, 0);
			tessellator.addVertex(cX, yLine, cZ);
		}

		tessellator.draw();
		GL11.glLineWidth(1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
