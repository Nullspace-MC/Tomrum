package net.dugged.tomrum;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

public class TomrumLoadingPlugin implements IFMLLoadingPlugin {
	public TomrumLoadingPlugin() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.tomrum.json");
	}

	// @formatter:off
	@Override public String getAccessTransformerClass() { return null; }
	@Override public String[] getASMTransformerClass() { return null; }
	@Override public void injectData(final Map<String, Object> data) {}
	@Nullable @Override public String getSetupClass() { return null; }
	@Override public String getModContainerClass() { return null; }
	// @formatter:on
}
