package net.kronhud.port;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.kronhud.port.core.ConfigManager;
import net.kronhud.port.core.HudEditScreen;
import net.kronhud.port.core.HudManager;
import net.kronhud.port.modules.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KronHudPort implements ClientModInitializer {

    private static KeyBinding editKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        HudManager manager = HudManager.getInstance();
        manager.register(new FpsModule());
        manager.register(new PingModule());
        manager.register(new CoordsModule());
        manager.register(new IrlClockModule());
        manager.register(new GameClockModule());
        manager.register(new ReachModule());
        manager.register(new SpeedModule());
        manager.register(new ArrowCounterModule());
        manager.register(new PotionsModule());
        manager.register(new ArmorModule());
        manager.register(new CompassModule());

        editKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.kronhudport.edit",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_COMMA,
                KeyBinding.Category.MISC
        ));

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.of("kronhudport", "widgets"),
                manager::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            manager.tickAll();
            while (editKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new HudEditScreen());
                }
            }
        });

        // Belt-and-suspenders: also save on world/server disconnect.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null && wasInWorld) {
                ConfigManager.save();
            }
            wasInWorld = client.world != null;
        });
    }

    private static boolean wasInWorld = false;
}
