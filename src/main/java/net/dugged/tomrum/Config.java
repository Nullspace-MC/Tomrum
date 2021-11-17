package net.dugged.tomrum;

import net.dugged.tomrum.GuiConfigFactory.IgnoreInGui;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
	@IgnoreInGui
	private final Configuration config;
	public boolean alwaysPickBlockMaxStack = false;
	public boolean compassTeleport = true;
	public boolean creativeNoclip = true;
	public boolean extendedChat = true;
	public boolean flightInertiaCancellation = false;
	public boolean ignoreEntityWhenPlacing = true;

	public Config(File path) {
		config = new Configuration(path);
		this.sync(true);
	}

	public void sync(final boolean load) {
		if (load && !config.isChild) {
			config.load();
		}

		this.alwaysPickBlockMaxStack = config.getBoolean("alwaysPickBlockMaxStack", Configuration.CATEGORY_GENERAL, this.alwaysPickBlockMaxStack, "Always pickblock a full stack in creative mode.");
		this.compassTeleport = config.getBoolean("compassTeleport", Configuration.CATEGORY_GENERAL, this.compassTeleport, "Lets you teleport to blocks and players in creative mode while holding a compass (similar to WorldEdit and later CutelessMod.");
		this.creativeNoclip = config.getBoolean("creativeNoclip", Configuration.CATEGORY_GENERAL, this.creativeNoclip, "Allows you to noclip through blocks while flying in creative mode.");
		this.extendedChat = config.getBoolean("extendedChat", Configuration.CATEGORY_GENERAL, this.extendedChat, "Extend chat 8x length (splits up messages and thus cannot be used for long commands).");
		this.flightInertiaCancellation = config.getBoolean("flightInertiaCancellation", Configuration.CATEGORY_GENERAL, this.flightInertiaCancellation, "Stops creative flight drift.");
		this.ignoreEntityWhenPlacing = config.getBoolean("ignoreEntityWhenPlacing", Configuration.CATEGORY_GENERAL, this.ignoreEntityWhenPlacing, "Allows you to place blocks inside yourself in creative mode.");

		if (config.hasChanged()) {
			config.save();
		}
	}

	public Configuration getInternalConfiguration() {
		return config;
	}
}
