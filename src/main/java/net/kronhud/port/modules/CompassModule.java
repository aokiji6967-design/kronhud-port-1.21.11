package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class CompassModule extends TextListModule {
    private static final String[] DIRS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    @Override public String id() { return "compass"; }
    @Override public String displayName() { return "Compass"; }
    @Override public List<String> lines() {
        if (client.player == null) return List.of("Facing: --");
        float yaw = MathHelper.wrapDegrees(client.player.getYaw());
        int index = Math.round(yaw / 45f) & 7;
        int heading = Math.round(yaw);
        if (heading < 0) heading += 360;
        return List.of(DIRS[index] + "  " + heading + "\u00b0");
    }
}
