package com.example.minesweeper.client;

import com.example.minesweeper.client.keybind.KeybindHandler;
import net.fabricmc.api.ClientModInitializer;

public class MinesweeperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeybindHandler.register();
    }
}
