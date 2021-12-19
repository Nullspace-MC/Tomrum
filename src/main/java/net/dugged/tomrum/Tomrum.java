package net.dugged.tomrum;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.dugged.tomrum.mixins.ISoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = "net.dugged.tomrum.GuiConfigFactory")
public class Tomrum {
	@Mod.Instance
	public static Tomrum INSTANCE;
	public static Config CONFIG;
	public static Logger LOGGER;
	private final KeyBinding reloadAudioEngineKey = new KeyBinding("key.tomrum.reload_audio", Keyboard.KEY_B, "key.categories.misc");
	private final ChunkBorderRenderer chunkBorderRenderer = new ChunkBorderRenderer();
	private GuiScreen previousScreen;
	private long clientTicks;
	public final CompassTeleport compass = new CompassTeleport();
	public String pistonExtensionTexture;
	public boolean v4Protocol = true;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		CONFIG = new Config(event.getSuggestedConfigurationFile());
		LOGGER = event.getModLog();
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		ClientRegistry.registerKeyBinding(this.reloadAudioEngineKey);
	}

	@SubscribeEvent
	public void onConfigChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (Reference.MODID.equals(event.modID)) {
			CONFIG.sync(false);
		}
	}

	@SubscribeEvent
	public void onKeyPress(final InputEvent.KeyInputEvent event) {
		if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_G)) {
			this.chunkBorderRenderer.toggleVisibility();
		}

		if (this.reloadAudioEngineKey.isPressed()) {
			((ISoundHandler) Minecraft.getMinecraft().getSoundHandler()).getSoundManager().reloadSoundSystem();
		}
	}

	@SubscribeEvent
	public void onPlayerPreTick(final TickEvent.PlayerTickEvent event) {
		if (event.phase != Phase.START) {
			return;
		}

		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer player = event.player;
		final boolean isCreativelyFlying = player.capabilities.isCreativeMode && player.capabilities.isFlying;
		player.noClip = CONFIG.creativeNoclip && isCreativelyFlying;
		if (isCreativelyFlying) {
			player.capabilities.setFlySpeed(player.isSprinting() ? 0.08F : 0.05F);
		}

		if (CONFIG.flightInertiaCancellation && player.capabilities.isFlying) {
			final GameSettings settings = mc.gameSettings;
			if (!(GameSettings.isKeyDown(settings.keyBindForward) || GameSettings.isKeyDown(settings.keyBindBack) || GameSettings.isKeyDown(settings.keyBindLeft) || GameSettings.isKeyDown(settings.keyBindRight))) {
				player.motionX = player.motionZ = 0D;
			}
		}
	}

	@SubscribeEvent
	public void onRenderWorld(final RenderWorldLastEvent event) {
		this.chunkBorderRenderer.render(event.partialTicks);
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
		if (CONFIG.creativeNoclip && event.overlayType == OverlayType.BLOCK && event.player.capabilities.isCreativeMode) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onChat(final ClientChatReceivedEvent event) {
		if (!CompassTeleport.hasTeleportingCompass()) {
			return;
		}

		if (event.message instanceof ChatComponentTranslation) {
			ChatComponentTranslation message = (ChatComponentTranslation) event.message;
			if ("chat.type.admin".equals(message.getKey())) {
				message = (ChatComponentTranslation) message.getFormatArgs()[1];
			}

			if ("commands.tp.success.coordinates".equals(message.getKey())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onChangeScreen(final GuiOpenEvent event) {
		final GuiScreen previous = Minecraft.getMinecraft().currentScreen;
		if (!(previous instanceof GuiMultiplayer)) {
			this.previousScreen = previous;
		}
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		final Minecraft mc = Minecraft.getMinecraft();
		if (event.phase == Phase.START && mc.currentScreen instanceof GuiMultiplayer && clientTicks++ % 600L == 0L) {
			mc.displayGuiScreen(new GuiMultiplayer(this.previousScreen));
		}
	}
}
