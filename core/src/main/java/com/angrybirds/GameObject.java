package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Base class for all physics-enabled game objects.
 * Bridges a Box2D Body with a LibGDX Sprite for rendering.
 */
public abstract class GameObject {
    protected Body body;
    protected Sprite sprite;
    protected float width;   // in world (meters)
    protected float height;  // in world (meters)
    protected int health;
    protected boolean alive = true;
    protected boolean markedForRemoval = false;

    /** Pixels per meter — our rendering scale factor. */
    public static final float PPM = 100f;

    public GameObject(Body body, Texture texture, float widthMeters, float heightMeters, int health) {
        this.body = body;
        this.width = widthMeters;
        this.height = heightMeters;
        this.health = health;

        this.sprite = new Sprite(texture);
        // Sprite size in pixel-units that map to meters via PPM
        this.sprite.setSize(widthMeters * PPM, heightMeters * PPM);
        this.sprite.setOriginCenter();

        // Store a reference back to this GameObject on the body's user data
        body.setUserData(this);
    }

    /** Synchronize sprite position/rotation with the Box2D body. */
    public void update() {
        if (body != null) {
            sprite.setPosition(
                body.getPosition().x * PPM - sprite.getWidth() / 2f,
                body.getPosition().y * PPM - sprite.getHeight() / 2f
            );
            sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        }
    }

    public void render(SpriteBatch batch) {
        if (alive) {
            update();
            sprite.draw(batch);
        }
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            alive = false;
            markedForRemoval = true;
        }
    }

    public boolean isAlive() { return alive; }
    public boolean isMarkedForRemoval() { return markedForRemoval; }
    public Body getBody() { return body; }
    public int getHealth() { return health; }
    public float getWidthMeters() { return width; }
    public float getHeightMeters() { return height; }

    public void dispose() {
        if (sprite.getTexture() != null) {
            // Texture disposal is managed centrally, not here
        }
    }
}
