package net.dugged.tomrum;

import net.dugged.tomrum.GuiConfigFactory.IgnoreInGui;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
	@IgnoreInGui
	private final Configuration config;
	public boolean creativeNoclip = true;
	public boolean extendedChat = true;
	public boolean flightInertiaCancellation = false;
	public boolean ignoreEntityWhenPlacing = true;
	public boolean alwaysPickBlockMaxStack = false;

	public Config(File path) {
		config = new Configuration(path);
		this.sync(true);
	}

	public void sync(final boolean load) {
		if (load && !config.isChild) {
			config.load();
		}

		final List<String> order = new ArrayList<>();
		Property prop;

		prop = config.get(Configuration.CATEGORY_GENERAL, "flightInertiaCancellation", this.flightInertiaCancellation);
		prop.comment = "Stops creative flight drift.";
		this.flightInertiaCancellation = prop.getBoolean(this.flightInertiaCancellation);
		order.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "creativeNoclip", this.creativeNoclip);
		prop.comment = "Allows you to noclip through blocks while flying in creative mode.";
		this.creativeNoclip = prop.getBoolean(this.creativeNoclip);
		order.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "ignoreEntityWhenPlacing", this.ignoreEntityWhenPlacing);
		prop.comment = "Allows you to place blocks inside yourself in creative mode.";
		this.ignoreEntityWhenPlacing = prop.getBoolean(this.ignoreEntityWhenPlacing);
		order.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "alwaysPickBlockMaxStack", this.alwaysPickBlockMaxStack);
		prop.comment = "Always pickblock a full stack in creative mode.";
		this.alwaysPickBlockMaxStack = prop.getBoolean(this.alwaysPickBlockMaxStack);
		order.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "extendedChat", this.extendedChat);
		prop.comment = "Extend chat 8x length (splits up messages and thus cannot be used for long commands).";
		this.extendedChat = prop.getBoolean(this.extendedChat);
		order.add(prop.getName());

		config.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, order);
		if (config.hasChanged()) {
			config.save();
		}
	}

	public Configuration getInternalConfiguration() {
		return config;
	}
}
