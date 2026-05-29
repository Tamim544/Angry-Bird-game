package com.angrybirds;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Handles Box2D collision events to apply damage to pigs and blocks.
 */
public class GameContactListener implements ContactListener {

    private static float elapsedTime = 0f;

    public static void reset() {
        elapsedTime = 0f;
    }

    public static void update(float delta) {
        elapsedTime += delta;
    }

    @Override
    public void beginContact(Contact contact) {
        if (elapsedTime < 0.3f) return; // Prevent initial spawn settlement damage

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();

        // Get relative velocity of impact safely
        float relativeVelocity = bodyA.getLinearVelocity().dst(bodyB.getLinearVelocity());

        // Apply damage based on what collided
        applyDamage(userDataA, userDataB, relativeVelocity);
        applyDamage(userDataB, userDataA, relativeVelocity);
    }

    private void applyDamage(Object target, Object impactor, float relativeVelocity) {
        if (target == null) return;

        if (target instanceof Pig) {
            float coeff = 1.0f;
            if (impactor instanceof Bird) {
                coeff = 1.2f;
            } else if (impactor instanceof Block) {
                coeff = 1.0f; // Blocks falling on pigs deal full damage for responsive chain-reaction kills
            } else if (impactor instanceof Pig) {
                coeff = 0.8f;
            } else { // ground (null impactor) or other static objects
                coeff = 1.0f;
            }
            ((Pig) target).applyCollisionDamage(relativeVelocity * coeff);
        }
        else if (target instanceof Block) {
            float coeff = 1.0f;
            if (impactor instanceof Bird) {
                coeff = 1.2f;
            } else if (impactor instanceof Block) {
                coeff = 0.6f;
            } else if (impactor instanceof Pig) {
                coeff = 0.5f;
            } else { // ground (null) or other static objects
                coeff = 0.8f;
            }
            ((Block) target).applyCollisionDamage(relativeVelocity * coeff);
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
