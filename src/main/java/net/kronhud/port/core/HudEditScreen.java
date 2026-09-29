package net.kronhud.port.core;

import net.minecraft.client.gui.Click;
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

    private static void border(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color);
        ctx.fill(x + w - 1, y, x + w, y + h, color);
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
            border(ctx, b.x(), b.y(), b.width(), b.height(), 0xFF000000);
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
    public boolean mouseClicked(Click click, boolean doubled) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();
        int button = click.buttonInfo().button();
        for (HudModule m : HudManager.getInstance().all()) {
            Rect b = HudManager.getInstance().trueBounds(m);
            if (b.contains(mouseX, mouseY)) {
                if (button == 1) {
                    ModuleState st = ConfigManager.get(m.id());
                    st.enabled = !st.enabled;
                    return true;
                }
                if (button == 0) {
                    dragging = m;
                    dragOffX = mouseX - b.x();
                    dragOffY = mouseY - b.y();
                    return true;
                }
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (dragging != null) {
            HudManager.getInstance().setPositionFromTrue(dragging,
                    (int) click.x() - dragOffX, (int) click.y() - dragOffY);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = null;
        return super.mouseReleased(click);
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
