package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The slingshot — a static anchor point that provides the bird's launch position.
 * Handles rendering the slingshot sprite and the elastic band.
 */
public class Slingshot {
    private Vector2 anchorPosition; // Center of slingshot pocket in world coords (meters)
    private Texture texture;
    private float drawWidth;   // in meters
    private float drawHeight;  // in meters
    private static final float MAX_DRAG_DISTANCE = 1.5f; // meters

    public Slingshot(Texture texture, float anchorX, float anchorY) {
        this.texture = texture;
        this.anchorPosition = new Vector2(anchorX, anchorY);
        this.drawWidth = 0.6f;
        this.drawHeight = 1.5f;
    }

    public void render(SpriteBatch batch) {
        float px = anchorPosition.x - drawWidth / 2f;
        // Draw the slingshot base below the anchor point
        float py = (anchorPosition.y - 0.5f) - drawHeight / 2f;
        batch.draw(texture, px, py, drawWidth, drawHeight);
    }

    /**
     * Clamp a drag position to the maximum drag distance from the anchor.
     */
    public Vector2 clampDragPosition(Vector2 dragWorldPos) {
        Vector2 diff = new Vector2(dragWorldPos).sub(anchorPosition);
        if (diff.len() > MAX_DRAG_DISTANCE) {
            diff.nor().scl(MAX_DRAG_DISTANCE);
        }
        return new Vector2(anchorPosition).add(diff);
    }

    /**
     * Calculate the launch impulse based on how far the bird was dragged from the anchor.
     * The impulse is in the opposite direction of the drag.
     */
    public Vector2 calculateLaunchImpulse(Vector2 dragWorldPos) {
        Vector2 diff = new Vector2(anchorPosition).sub(dragWorldPos);
        float power = 6.0f; // Impulse multiplier
        return diff.scl(power);
    }

    public Vector2 getAnchorPosition() { return anchorPosition; }
    public float getMaxDragDistance() { return MAX_DRAG_DISTANCE; }
}
