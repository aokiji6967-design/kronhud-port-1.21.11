package net.kronhud.port.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Base for widgets that are just a background box with a few lines of text. */
public abstract class TextListModule implements HudModule {
    protected final MinecraftClient client = MinecraftClient.getInstance();
    private static final int PAD = 3;
    private static final int BG = 0x90000000;
    private static final int FG = 0xFFFFFFFF;

    public abstract List<String> lines();

    @Override
    public int contentWidth() {
        TextRenderer font = client.textRenderer;
        int w = 20;
        for (String s : lines()) w = Math.max(w, font.getWidth(s));
        return w + PAD * 2;
    }

    @Override
    public int contentHeight() {
        List<String> l = lines();
        int lh = client.textRenderer.fontHeight + 2;
        return Math.max(lh, l.size() * lh) + PAD * 2 - 2;
    }

    @Override
    public void render(DrawContext ctx, float tickDelta) {
        List<String> l = lines();
        TextRenderer font = client.textRenderer;
        int lh = font.fontHeight + 2;
        int w = contentWidth();
        int h = contentHeight();
        ctx.fill(0, 0, w, h, BG);
        int y = PAD;
        for (String s : l) {
            ctx.drawTextWithShadow(font, s, PAD, y, FG);
            y += lh;
        }
    }
}
