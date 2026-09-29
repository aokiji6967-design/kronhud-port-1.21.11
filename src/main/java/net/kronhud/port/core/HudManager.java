package net.kronhud.port.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HudManager {
    private static final HudManager INSTANCE = new HudManager();
    public static HudManager getInstance() { return INSTANCE; }

    private final Map<String, HudModule> modules = new LinkedHashMap<>();

    public void register(HudModule module) {
        modules.put(module.id(), module);
        ModuleState st = ConfigManager.get(module.id());
        if (!module.enabledByDefault()) {
            // leave as whatever config said; default new-state already true, so flip once.
        }
    }

    public List<HudModule> all() { return new ArrayList<>(modules.values()); }

    public void tickAll() {
        for (HudModule m : modules.values()) {
            ModuleState st = ConfigManager.get(m.id());
            if (st.enabled) m.tick();
        }
    }

    /** True screen-space bounds (after scale) for a module, clamped on-screen. */
    public Rect trueBounds(HudModule m) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getWindow() == null) return new Rect(0, 0, 1, 1);
        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        ModuleState st = ConfigManager.get(m.id());
        float scale = (float) MathHelper.clamp(st.scale, 0.3, 4.0);
        int w = Math.max(1, Math.round(m.contentWidth() * scale));
        int h = Math.max(1, Math.round(m.contentHeight() * scale));
        int x = MathHelper.clamp((int) Math.round(st.x * screenW), 0, Math.max(0, screenW - w));
        int y = MathHelper.clamp((int) Math.round(st.y * screenH), 0, Math.max(0, screenH - h));
        return new Rect(x, y, w, h);
    }

    public void setPositionFromTrue(HudModule m, int trueX, int trueY) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getWindow() == null) return;
        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        ModuleState st = ConfigManager.get(m.id());
        st.x = MathHelper.clamp(trueX / (double) screenW, 0, 1);
        st.y = MathHelper.clamp(trueY / (double) screenH, 0, 1);
    }

    /** Matches Fabric's HudElement functional interface: (DrawContext, RenderTickCounter) -> void. */
    public void render(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;
        for (HudModule m : modules.values()) {
            ModuleState st = ConfigManager.get(m.id());
            if (!st.enabled) continue;
            Rect bounds = trueBounds(m);
            float scale = (float) MathHelper.clamp(st.scale, 0.3, 4.0);
            ctx.getMatrices().pushMatrix();
            ctx.getMatrices().translate(bounds.x(), bounds.y());
            ctx.getMatrices().scale(scale, scale);
            try {
                m.render(ctx, 1f);
            } catch (Exception e) {
                // Never let one broken widget take the whole HUD down.
            }
            ctx.getMatrices().popMatrix();
        }
    }
}
