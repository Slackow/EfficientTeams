package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.Tracker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract AbstractTeam getScoreboardTeam();

    @Inject(method = "setRemoved", at = @At("HEAD"))
    public void onKill(Entity.RemovalReason reason, CallbackInfo ci){
        if (getScoreboardTeam() instanceof Tracker team) {
            if (team.getLoadedEntities() != null) {
                team.getLoadedEntities().remove((Entity)(Object)this);
            }
        }
    }


    @Inject(method = "readNbt", at = @At("TAIL"))
    public void onLoad(NbtCompound nbt, CallbackInfo ci){
        if (getScoreboardTeam() instanceof Tracker team) {
            if (team.getLoadedEntities() != null) {
                team.getLoadedEntities().add((Entity) (Object) this);
            }
        }
    }
}
