package net.kronhud.port.core;

public record Rect(int x, int y, int width, int height) {
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}
