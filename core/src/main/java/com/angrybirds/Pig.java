package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

/**
 * A Pig that the player must destroy.
 * Creates a circular dynamic Box2D body with health tracking.
 */
public class Pig extends GameObject {

    private static final int DEFAULT_HEALTH = 10;
    private static final float DAMAGE_VELOCITY_THRESHOLD = 0.5f;

    public Pig(World world, Texture texture, float x, float y, float radius) {
        super(createBody(world, x, y, radius), texture, radius * 2, radius * 2, DEFAULT_HEALTH);
    }

    public Pig(World world, Texture texture, float x, float y, float radius, int health) {
        super(createBody(world, x, y, radius), texture, radius * 2, radius * 2, health);
    }

    private static Body createBody(World world, float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    /**
     * Calculate damage from a collision based on the relative velocity.
     * Higher velocity = more damage.
     */
    public void applyCollisionDamage(float relativeVelocity) {
        if (relativeVelocity > DAMAGE_VELOCITY_THRESHOLD) {
            int damage = (int)(relativeVelocity * 15);
            takeDamage(damage);
        }
    }
}
