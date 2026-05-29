package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * A Bird that can be launched from the slingshot.
 * Creates a circular dynamic Box2D body.
 */
public class Bird extends GameObject {

    public enum BirdType {
        RED, BLUE, YELLOW
    }

    private BirdType type;
    private boolean launched = false;
    private boolean settled = false;
    private float settledTimer = 0f;
    private static final float SETTLE_TIME = 2.0f; // seconds after landing before considered done
    private static final float SETTLE_VELOCITY_THRESHOLD = 0.3f;

    public Bird(World world, Texture texture, float x, float y, float radius, BirdType type) {
        super(createBody(world, x, y, radius), texture, radius * 2, radius * 2, 1);
        this.type = type;
    }

    private static Body createBody(World world, float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.bullet = true; // For fast-moving projectile, use CCD

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Don't apply gravity until launched
        body.setGravityScale(0f);
        body.setAwake(false);

        return body;
    }

    /** Launch the bird with the given impulse vector. */
    public void launch(Vector2 impulse) {
        launched = true;
        body.setGravityScale(1f);
        body.setAwake(true);
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /** Check if the bird has settled (stopped moving) after launch. */
    public void updateSettled(float delta) {
        if (!launched) return;

        float speed = body.getLinearVelocity().len();
        if (speed < SETTLE_VELOCITY_THRESHOLD) {
            settledTimer += delta;
            if (settledTimer >= SETTLE_TIME) {
                settled = true;
            }
        } else {
            settledTimer = 0f;
        }
    }

    /** Position the bird at the slingshot anchor (before launch). */
    public void setPosition(float x, float y) {
        body.setTransform(x, y, 0);
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
    }

    public boolean isLaunched() { return launched; }
    public boolean isSettled() { return settled; }
    public BirdType getType() { return type; }

    /** Get position in world coordinates. */
    public Vector2 getPosition() {
        return body.getPosition();
    }
}
