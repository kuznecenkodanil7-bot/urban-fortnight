package com.example.minesweeper;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinesweeperMod implements ModInitializer {
    public static final String MOD_ID = "minesweeper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Minesweeper Mod initialized!");
    }
}
