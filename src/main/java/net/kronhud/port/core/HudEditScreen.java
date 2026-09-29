package net.kronhud.port.core;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class HudEditScreen extends Screen {
    private HudModule dragging = null;
    private int dragOffX, dragOffY;

    public HudEditScreen() {
        super(Text.literal("Edit HUD"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawTextWithShadow(textRenderer,
                "Drag to move \u00b7 Scroll to resize \u00b7 Right-click to toggle off \u00b7 Esc to save",
                6, 6, 0xFFFFFF);

        for (HudModule m : HudManager.getInstance().all()) {
            ModuleState st = ConfigManager.get(m.id());
            Rect b = HudManager.getInstance().trueBounds(m);
            boolean hovered = b.contains(mouseX, mouseY);
            int bg = st.enabled ? (hovered ? 0x8033AAFF : 0x55FFFFFF) : 0x55FF4444;
            ctx.fill(b.x(), b.y(), b.x() + b.width(), b.y() + b.height(), bg);
            ctx.drawBorder(b.x(), b.y(), b.width(), b.height(), 0xFF000000);
            if (st.enabled) {
                ctx.getMatrices().pushMatrix();
                ctx.getMatrices().translate(b.x(), b.y());
                ctx.getMatrices().scale((float) st.scale, (float) st.scale);
                try { m.render(ctx, delta); } catch (Exception ignored) {}
                ctx.getMatrices().popMatrix();
            } else {
                ctx.drawCenteredTextWithShadow(textRenderer, m.displayName() + " (off)",
                        b.x() + b.width() / 2, b.y() + b.height() / 2 - 4, 0xFFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HudModule m : HudManager.getInstance().all()) {
            Rect b = HudManager.getInstance().trueBounds(m);
            if (b.contains((int) mouseX, (int) mouseY)) {
                if (button == 1) {
                    ModuleState st = ConfigManager.get(m.id());
                    st.enabled = !st.enabled;
                    return true;
                }
                if (button == 0) {
                    dragging = m;
                    dragOffX = (int) mouseX - b.x();
                    dragOffY = (int) mouseY - b.y();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging != null) {
            HudManager.getInstance().setPositionFromTrue(dragging,
                    (int) mouseX - dragOffX, (int) mouseY - dragOffY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (HudModule m : HudManager.getInstance().all()) {
            Rect b = HudManager.getInstance().trueBounds(m);
            if (b.contains((int) mouseX, (int) mouseY)) {
                ModuleState st = ConfigManager.get(m.id());
                st.scale = MathHelper.clamp(st.scale + Math.signum(verticalAmount) * 0.05, 0.3, 4.0);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }
}
