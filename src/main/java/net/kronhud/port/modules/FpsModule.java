package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import java.util.List;

public class FpsModule extends TextListModule {
    @Override public String id() { return "fps"; }
    @Override public String displayName() { return "FPS"; }
    @Override public List<String> lines() {
        return List.of("FPS: " + client.getCurrentFps());
    }
}
