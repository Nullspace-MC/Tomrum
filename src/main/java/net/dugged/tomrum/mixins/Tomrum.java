package net.dugged.tomrum.mixins;

import cpw.mods.fml.common.Mod;
import net.dugged.tomrum.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Tomrum {
	public static final Logger LOGGER = LogManager.getLogger();
}
