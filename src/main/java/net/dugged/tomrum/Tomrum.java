package net.dugged.tomrum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Tomrum {
	public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);
	@Mod.Instance
	public static Tomrum INSTANCE;
	private final ChunkBorderRenderer chunkBorderRenderer = new ChunkBorderRenderer();

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
}
