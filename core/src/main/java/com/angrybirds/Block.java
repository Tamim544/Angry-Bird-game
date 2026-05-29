package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

/**
 * A destructible block (Wood, Glass, or Stone).
 * Creates a rectangular dynamic Box2D body with material-specific properties.
 */
public class Block extends GameObject {

    public enum Material {
        WOOD(80, 1.0f, 0.6f, 0.1f),
        GLASS(40, 0.8f, 0.3f, 0.05f),
        STONE(200, 2.5f, 0.7f, 0.05f);

        public final int maxHealth;
        public final float density;
        public final float friction;
        public final float restitution;

        Material(int maxHealth, float density, float friction, float restitution) {
            this.maxHealth = maxHealth;
            this.density = density;
            this.friction = friction;
            this.restitution = restitution;
        }
    }

    private Material material;
    private static final float DAMAGE_VELOCITY_THRESHOLD = 1.5f;

    public Block(World world, Texture texture, float x, float y, float widthMeters, float heightMeters, Material material) {
        super(createBody(world, x, y, widthMeters, heightMeters, material), texture, widthMeters, heightMeters, material.maxHealth);
        this.material = material;
    }

    private static Body createBody(World world, float x, float y, float w, float h, Material mat) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2f, h / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = mat.density;
        fixtureDef.friction = mat.friction;
        fixtureDef.restitution = mat.restitution;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    /**
     * Calculate damage from a collision based on relative velocity.
     * Glass breaks easier, stone is tougher.
     */
    public void applyCollisionDamage(float relativeVelocity) {
        if (relativeVelocity > DAMAGE_VELOCITY_THRESHOLD) {
            float materialMultiplier;
            switch (material) {
                case GLASS: materialMultiplier = 2.0f; break;
                case STONE: materialMultiplier = 0.5f; break;
                default: materialMultiplier = 1.0f; break;
            }
            int damage = (int)(relativeVelocity * 10 * materialMultiplier);
            takeDamage(damage);
        }
    }

    public Material getMaterial() { return material; }
}
