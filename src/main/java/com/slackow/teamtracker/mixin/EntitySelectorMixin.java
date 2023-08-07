package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.HasTeam;
import com.slackow.teamtracker.Tracker;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements HasTeam {
    @Shadow @Final private Function<Vec3d, Vec3d> positionOffset;

    @Shadow protected abstract Predicate<Entity> getPositionPredicate(Vec3d pos);

    @Unique
    public String team = null;

//    @Unique
//    private static BiConsumer<String, ServerCommandSource> logSay = (msg, source) -> source.getServer().getPlayerManager().broadcast(SignedMessage.ofUnsigned(msg), source, MessageType.params(MessageType.SAY_COMMAND, source));
//    @Unique
//    private static final BiConsumer<String, ServerCommandSource> log = (msg, source) -> Teamtracker.LOGGER.info(msg);
    @Inject(method = "getUnfilteredEntities", at = @At(target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;", value = "INVOKE_ASSIGN", shift = At.Shift.BEFORE), cancellable = true)
    public void checkTeam(ServerCommandSource source, CallbackInfoReturnable<List<? extends Entity>> cir){
//        log.accept("Getting entities:", source);
        if (team != null) {
//            log.accept("Team parameter found", source);
            Team team = source.getServer().getScoreboard().getTeam(this.team);
            if (team == null) {
                cir.setReturnValue(List.of());
                return;
            }
//            log.accept("Team object acquired", source);

            Set<Entity> loadedEntities = ((Tracker) team).getLoadedEntities();
            if (loadedEntities != null) {
//                log.accept("Returning reduced list! :)", source);

                Vec3d vec3d = this.positionOffset.apply(source.getPosition());
                Predicate<Entity> predicate = this.getPositionPredicate(vec3d);
                cir.setReturnValue(loadedEntities.stream().filter(predicate).toList());
            }
        }
    }
    @Override
    public void setTeam(String team) {
        this.team = team;
    }
}
