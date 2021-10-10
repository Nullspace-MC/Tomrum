package net.dugged.tomrum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Tomrum {
	@Mod.Instance
	public static Tomrum INSTANCE;
	public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);
	private final ChunkBorderRenderer chunkBorderRenderer = new ChunkBorderRenderer();
	public boolean v4Protocol = true;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void onKeyPress() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_G)) {
			this.chunkBorderRenderer.toggleVisibility();
		}
	}

	@SubscribeEvent
	public void onRenderWorld(final RenderWorldLastEvent event) {
		this.chunkBorderRenderer.render(event.partialTicks);
	}

	public void onPlayerPreTick(final EntityPlayer player) {
		player.noClip = player.capabilities.isCreativeMode && player.capabilities.isFlying;
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
		if (event.overlayType == OverlayType.BLOCK && event.player.capabilities.isCreativeMode) {
			event.setCanceled(true);
		}
	}
}
