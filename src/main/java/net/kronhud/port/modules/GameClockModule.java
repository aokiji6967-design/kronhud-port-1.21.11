package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;

import java.util.List;

public class GameClockModule extends TextListModule {
    @Override public String id() { return "game_clock"; }
    @Override public String displayName() { return "In-game Clock"; }
    @Override public List<String> lines() {
        if (client.world == null) return List.of("Day time: --");
        long t = (client.world.getTimeOfDay() + 6000) % 24000;
        int hour24 = (int) (t / 1000) % 24;
        int mins = (int) ((t % 1000) * 60 / 1000);
        String ampm = hour24 < 12 ? "AM" : "PM";
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;
        return List.of(String.format("Day time: %02d:%02d %s", hour12, mins, ampm));
    }
}
