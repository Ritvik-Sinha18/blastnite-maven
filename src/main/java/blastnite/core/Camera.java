package blastnite.core;

import blastnite.entity.Entity;

/**
 * Decides which part of the level is on screen. Everything in the world is
 * drawn shifted left by the camera's x, so the player stays in the middle
 * while the level scrolls past.
 */
public class Camera {
    private final double viewWidth;
    private double x;

    public Camera(double viewWidth) {
        this.viewWidth = viewWidth;
    }

    /** Centers on the target, but never shows anything past the level's edges. */
    public void follow(Entity target, double levelWidth) {
        double wanted = target.getX() + target.getWidth() / 2 - viewWidth / 2;
        double maxX = Math.max(0, levelWidth - viewWidth);
        x = Math.max(0, Math.min(maxX, wanted));
    }

    public double getX() {
        return x;
    }
}
