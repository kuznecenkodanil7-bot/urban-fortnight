package com.example.minesweeper.client.keybind;

import com.example.minesweeper.client.screen.MinesweeperScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static KeyBinding minesweeperKey;

    public static void register() {
        minesweeperKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minesweeper.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.minesweeper.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (minesweeperKey.wasPressed()) {
                if (client.player != null) {
                    MinecraftClient.getInstance().setScreen(new MinesweeperScreen());
                }
            }
        });
    }
}
