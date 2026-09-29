package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.client.network.PlayerListEntry;

import java.util.List;

public class PingModule extends TextListModule {
    @Override public String id() { return "ping"; }
    @Override public String displayName() { return "Ping"; }
    @Override public List<String> lines() {
        int ping = 0;
        if (client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry e = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (e != null) ping = e.getLatency();
        }
        return List.of("Ping: " + ping + " ms");
    }
}
