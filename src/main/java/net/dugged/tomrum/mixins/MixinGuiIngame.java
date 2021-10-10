package net.dugged.tomrum.mixins;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
	@ModifyVariable(method = "renderScoreboard", ordinal = 0, index = 6, name = "collection", at = @At(value = "INVOKE", target = "Ljava/util/Collection;size()I", ordinal = 0, remap = false))
	private Collection<Score> fixHiddenScoreboards(final Collection<Score> collection) {
		return collection.stream().skip(Math.abs(collection.size() - 15)).collect(Collectors.toList());
	}
}
