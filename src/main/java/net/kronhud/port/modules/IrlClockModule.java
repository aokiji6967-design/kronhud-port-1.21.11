package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IrlClockModule extends TextListModule {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override public String id() { return "irl_clock"; }
    @Override public String displayName() { return "Real-world Clock"; }
    @Override public List<String> lines() {
        return List.of("Clock: " + LocalTime.now().format(FMT));
    }
}
