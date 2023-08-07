package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.HasTeam;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin implements HasTeam {

    @Unique
    private String team;

    @Shadow public abstract boolean selectsTeam();

    @Redirect(method = "build", at = @At(value = "NEW", target = "(IZZLjava/util/function/Predicate;Lnet/minecraft/predicate/NumberRange$FloatRange;Ljava/util/function/Function;Lnet/minecraft/util/math/Box;Ljava/util/function/BiConsumer;ZLjava/lang/String;Ljava/util/UUID;Lnet/minecraft/entity/EntityType;Z)Lnet/minecraft/command/EntitySelector;"))
    public EntitySelector a(int count, boolean includesNonPlayers, boolean localWorldOnly, Predicate<Entity> basePredicate, NumberRange.FloatRange distance, Function<Vec3d, Vec3d> positionOffset, Box box, BiConsumer<Vec3d, List<? extends Entity>> sorter, boolean senderOnly, String playerName, UUID uuid, EntityType type, boolean usesAt){
        var selector = new EntitySelector(count, includesNonPlayers, localWorldOnly, basePredicate, distance, positionOffset, box, sorter, senderOnly, playerName, uuid, type, usesAt);
        if (selectsTeam()) {
            ((HasTeam) selector).setTeam(this.getTeam());
        }
        return selector;
    }

    @Override
    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String getTeam() {
        return team;
    }
}
