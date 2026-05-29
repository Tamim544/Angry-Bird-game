package com.angrybirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines and builds a game level.
 * A Level holds configuration for the slingshot, birds, blocks, and pigs
 * and creates all Box2D bodies when built.
 */
public class Level {

    /** Data class for a block placement. */
    public static class BlockPlacement {
        public float x, y, width, height;
        public Block.Material material;

        public BlockPlacement(float x, float y, float width, float height, Block.Material material) {
            this.x = x; this.y = y;
            this.width = width; this.height = height;
            this.material = material;
        }
    }

    /** Data class for a pig placement. */
    public static class PigPlacement {
        public float x, y, radius;
        public int health;

        public PigPlacement(float x, float y, float radius, int health) {
            this.x = x; this.y = y;
            this.radius = radius;
            this.health = health;
        }
    }

    private Vector2 slingshotPos;
    private List<Bird.BirdType> birdTypes;
    private List<BlockPlacement> blockPlacements;
    private List<PigPlacement> pigPlacements;
    private float groundY;
    private float groundWidth;

    // Built objects
    private Slingshot slingshot;
    private List<Bird> birds;
    private List<Block> blocks;
    private List<Pig> pigs;

    public Level() {
        birdTypes = new ArrayList<>();
        blockPlacements = new ArrayList<>();
        pigPlacements = new ArrayList<>();
        birds = new ArrayList<>();
        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        groundY = 0.5f;
        groundWidth = 20f;
    }

    public void setSlingshotPosition(float x, float y) {
        this.slingshotPos = new Vector2(x, y);
    }

    public void addBird(Bird.BirdType type) {
        birdTypes.add(type);
    }

    public void addBlock(float x, float y, float w, float h, Block.Material mat) {
        blockPlacements.add(new BlockPlacement(x, y, w, h, mat));
    }

    public void addPig(float x, float y, float radius, int health) {
        pigPlacements.add(new PigPlacement(x, y, radius, health));
    }

    public void setGroundY(float y) { this.groundY = y; }
    public void setGroundWidth(float w) { this.groundWidth = w; }

    /**
     * Build all Box2D bodies and game objects for this level.
     */
    public void build(World world, Texture birdTexture, Texture pigTexture,
                      Texture woodTexture, Texture glassTexture, Texture stoneTexture,
                      Texture slingshotTexture) {

        // Create ground
        createGround(world);

        // Create slingshot
        slingshot = new Slingshot(slingshotTexture, slingshotPos.x, slingshotPos.y);

        // Create birds (position them off-screen or at slingshot queue)
        float birdRadius = 0.25f;
        for (int i = 0; i < birdTypes.size(); i++) {
            float birdX = slingshotPos.x - 1.0f - (i * 0.7f);
            float birdY = groundY + birdRadius;
            Bird bird = new Bird(world, birdTexture, birdX, birdY, birdRadius, birdTypes.get(i));
            birds.add(bird);
        }

        // Create blocks
        for (BlockPlacement bp : blockPlacements) {
            Texture tex;
            switch (bp.material) {
                case GLASS: tex = glassTexture; break;
                case STONE: tex = stoneTexture; break;
                default: tex = woodTexture; break;
            }
            Block block = new Block(world, tex, bp.x, bp.y, bp.width, bp.height, bp.material);
            blocks.add(block);
        }

        // Create pigs
        for (PigPlacement pp : pigPlacements) {
            Pig pig = new Pig(world, pigTexture, pp.x, pp.y, pp.radius, pp.health);
            pigs.add(pig);
        }
    }

    private void createGround(World world) {
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(groundWidth / 2f, groundY);

        Body groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(groundWidth / 2f, 0.2f);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.friction = 0.8f;
        groundFixture.restitution = 0.1f;

        groundBody.createFixture(groundFixture);
        groundShape.dispose();
    }

    // Getters
    public Slingshot getSlingshot() { return slingshot; }
    public List<Bird> getBirds() { return birds; }
    public List<Block> getBlocks() { return blocks; }
    public List<Pig> getPigs() { return pigs; }
    public float getGroundY() { return groundY; }
}
