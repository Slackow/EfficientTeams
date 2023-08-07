package com.slackow.teamtracker;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Teamtracker implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("teamtracker");
    @Override
    public void onInitialize() {
        LOGGER.info("Initialized!");
    }
}
