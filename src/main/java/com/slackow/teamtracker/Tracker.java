package com.slackow.teamtracker;

import net.minecraft.entity.Entity;

import java.util.Set;

public interface Tracker {
    default Set<Entity> getLoadedEntities() {
        return null;
    }
}
