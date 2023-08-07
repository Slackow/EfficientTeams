package com.slackow.teamtracker.mixin;

import com.slackow.teamtracker.HasTeam;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntitySelectorOptions.class)
public class EntitySelectorOptionsMixin {

    @Inject(method = "method_9951",locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/command/EntitySelectorReader;setSelectsTeam(Z)V", shift = At.Shift.AFTER))
    private static void readTeam(EntitySelectorReader reader, CallbackInfo ci, boolean negation, String team) {
        ((HasTeam) reader).setTeam(team);
    }
}
