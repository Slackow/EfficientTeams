package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.Tracker;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "addPlayerToTeam", at = @At("RETURN"))
    public void addToTeam(String playerName, Team team, CallbackInfoReturnable<Boolean> cir) {
        if (playerName.contains("-")) {
            try {
                UUID uuid = UUID.fromString(playerName);
                Entity entity = byUUID(uuid);
                if (entity != null) {
                    ((Tracker) team).getLoadedEntities().add(entity);
                }
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Inject(method = "removePlayerFromTeam", at = @At("RETURN"))
    public void removeFromTeam(String playerName, Team team, CallbackInfo ci){
        if (playerName.contains("-")) {
            try {
                UUID uuid = UUID.fromString(playerName);
                Entity entity = byUUID(uuid);
                if (entity != null) {
                    ((Tracker) team).getLoadedEntities().remove(entity);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Unique
    private Entity byUUID(UUID uuid) {
        for (ServerWorld serverWorld : server.getWorlds()){
            Entity entity = serverWorld.getEntity(uuid);
            if (entity == null) continue;
            return entity;
        }
        return null;
    }
}
