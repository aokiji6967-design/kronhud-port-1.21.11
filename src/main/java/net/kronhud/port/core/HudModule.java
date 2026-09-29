package net.kronhud.port.core;

import net.minecraft.client.gui.DrawContext;

public interface HudModule {
    String id();
    String displayName();
    int contentWidth();
    int contentHeight();
    void render(DrawContext ctx, float tickDelta);
    default void tick() {}
    default boolean enabledByDefault() { return true; }
}
