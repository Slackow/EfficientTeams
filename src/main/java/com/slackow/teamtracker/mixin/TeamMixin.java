package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.Tracker;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(Team.class)
public class TeamMixin implements Tracker {
    @Unique
    public Set<Entity> loadedEntities = new ObjectOpenHashSet<>();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public Set<Entity> getLoadedEntities() {
        return loadedEntities;
    }
}
