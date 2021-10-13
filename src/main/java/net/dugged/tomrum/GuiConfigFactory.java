package net.dugged.tomrum;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiConfigFactory implements IModGuiFactory {
	@Override
	public void initialize(final Minecraft instance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return TomrumGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
		return null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface IgnoreInGui {
	}

	public static class TomrumGuiConfig extends GuiConfig {
		public TomrumGuiConfig(final GuiScreen parent) {
			super(parent, getConfigElements(), Reference.MODID, false, false, Reference.NAME);
		}

		private static List<IConfigElement> getConfigElements() {
			return Arrays.stream(Config.class.getDeclaredFields())
					.filter(f -> !f.isAnnotationPresent(IgnoreInGui.class))
					.map(Field::getName)
					.map(f -> Tomrum.CONFIG.getInternalConfiguration().get(Configuration.CATEGORY_GENERAL, f, ""))
					.map(ConfigElement::new)
					.collect(Collectors.toList());
		}
	}
}
