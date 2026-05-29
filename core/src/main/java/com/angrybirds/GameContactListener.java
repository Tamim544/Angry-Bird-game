package com.angrybirds;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Handles Box2D collision events to apply damage to pigs and blocks.
 */
public class GameContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();

        // Get relative velocity of impact
        float relativeVelocity = bodyA.getLinearVelocity().sub(bodyB.getLinearVelocity()).len();

        // Apply damage based on what collided
        applyDamage(userDataA, userDataB, relativeVelocity);
        applyDamage(userDataB, userDataA, relativeVelocity);
    }

    private void applyDamage(Object target, Object impactor, float relativeVelocity) {
        if (target == null) return;

        // Bird hitting a Pig
        if (target instanceof Pig && impactor instanceof Bird) {
            ((Pig) target).applyCollisionDamage(relativeVelocity);
        }
        // Bird hitting a Block
        else if (target instanceof Block && impactor instanceof Bird) {
            ((Block) target).applyCollisionDamage(relativeVelocity);
        }
        // Block hitting a Pig (e.g., a block falls on a pig)
        else if (target instanceof Pig && impactor instanceof Block) {
            ((Pig) target).applyCollisionDamage(relativeVelocity * 0.7f);
        }
        // Block hitting another Block
        else if (target instanceof Block && impactor instanceof Block) {
            ((Block) target).applyCollisionDamage(relativeVelocity * 0.3f);
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
