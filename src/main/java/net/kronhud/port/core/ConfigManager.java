package net.kronhud.port.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("kronhudport.json");
    private static final Type TYPE = new TypeToken<LinkedHashMap<String, ModuleState>>() {}.getType();

    private static Map<String, ModuleState> states = new LinkedHashMap<>();

    public static void load() {
        if (!Files.exists(FILE)) return;
        try (Reader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            Map<String, ModuleState> loaded = GSON.fromJson(r, TYPE);
            if (loaded != null) states = loaded;
        } catch (IOException e) {
            System.err.println("[kronhudport] Failed to load config: " + e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
                GSON.toJson(states, TYPE, w);
            }
        } catch (IOException e) {
            System.err.println("[kronhudport] Failed to save config: " + e);
        }
    }

    public static ModuleState get(String id) {
        return states.computeIfAbsent(id, k -> new ModuleState());
    }
}
