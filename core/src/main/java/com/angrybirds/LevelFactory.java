package com.angrybirds;

/**
 * Factory that creates pre-defined level configurations.
 */
public class LevelFactory {

    /**
     * Create Level 1 — a simple introductory level.
     * Birds: 2 Red Birds
     */
    public static Level createLevel1() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (2 birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.RED);

        // Structure — right side of screen
        // Base blocks (two horizontal wood blocks)
        level.addBlock(7.0f, 1.2f, 0.3f, 1.0f, Block.Material.WOOD);   // Left pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(8.5f, 1.2f, 0.3f, 1.0f, Block.Material.WOOD);   // Right pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(7.75f, 1.8f, 2.0f, 0.2f, Block.Material.WOOD);  // Horizontal beam: Bottom 1.7f, Top 1.9f

        // Top section
        level.addBlock(7.4f, 2.3f, 0.3f, 0.8f, Block.Material.WOOD);   // Left top pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(8.1f, 2.3f, 0.3f, 0.8f, Block.Material.WOOD);   // Right top pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(7.75f, 2.775f, 1.2f, 0.15f, Block.Material.WOOD); // Top beam: Bottom 2.7f, Top 2.85f

        // Pigs (10 health, aligned correctly to prevent initial overlap)
        level.addPig(7.75f, 2.1f, 0.2f, 10);   // Bottom pig: resting on first beam (Bottom Y = 1.9f)
        level.addPig(7.75f, 3.03f, 0.18f, 10); // Top pig: resting on top beam (Bottom Y = 2.85f)

        return level;
    }

    /**
     * Create Level 2 — a more complex level with glass and stone.
     * Birds: 3 Birds (2 Red, 1 Yellow)
     */
    public static Level createLevel2() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (3 birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);

        // Left structure (glass)
        level.addBlock(6.5f, 1.1f, 0.2f, 0.8f, Block.Material.GLASS);  // Left pillar: Bottom 0.7f, Top 1.5f
        level.addBlock(7.5f, 1.1f, 0.2f, 0.8f, Block.Material.GLASS);  // Right pillar: Bottom 0.7f, Top 1.5f
        level.addBlock(7.0f, 1.575f, 1.4f, 0.15f, Block.Material.GLASS); // Beam: Bottom 1.5f, Top 1.65f

        // Right structure (stone fortress)
        level.addBlock(9.0f, 1.2f, 0.3f, 1.0f, Block.Material.STONE);  // Left pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(10.5f, 1.2f, 0.3f, 1.0f, Block.Material.STONE); // Right pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(9.75f, 1.8f, 2.0f, 0.2f, Block.Material.STONE);  // Beam: Bottom 1.7f, Top 1.9f

        // Wood blocks on top of stone
        level.addBlock(9.4f, 2.3f, 0.25f, 0.8f, Block.Material.WOOD);  // Left top pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(10.1f, 2.3f, 0.25f, 0.8f, Block.Material.WOOD); // Right top pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(9.75f, 2.775f, 1.2f, 0.15f, Block.Material.WOOD); // Top beam: Bottom 2.7f, Top 2.85f

        // Pigs (10 health, aligned correctly to prevent initial overlap)
        level.addPig(7.0f, 1.85f, 0.2f, 10);     // Left glass pig: resting on glass beam (Bottom Y = 1.65f)
        level.addPig(9.75f, 2.12f, 0.22f, 10);  // Mid stone pig: resting on stone beam (Bottom Y = 1.9f)
        level.addPig(9.75f, 3.03f, 0.18f, 10);   // Top wood pig: resting on top beam (Bottom Y = 2.85f)

        return level;
    }

    /**
     * Create Level 3 — Wood and Glass Tower.
     * Birds: 4 Birds (2 Red, 2 Yellow)
     */
    public static Level createLevel3() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (4 birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);

        // Tower structure (Wood & Glass)
        level.addBlock(8.0f, 1.2f, 0.25f, 1.0f, Block.Material.WOOD);   // Left pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(9.5f, 1.2f, 0.25f, 1.0f, Block.Material.WOOD);   // Right pillar: Bottom 0.7f, Top 1.7f
        level.addBlock(8.75f, 1.8f, 1.8f, 0.2f, Block.Material.WOOD);   // First beam: Bottom 1.7f, Top 1.9f

        level.addBlock(8.2f, 2.3f, 0.2f, 0.8f, Block.Material.GLASS);   // Left mid pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(9.3f, 2.3f, 0.2f, 0.8f, Block.Material.GLASS);   // Right mid pillar: Bottom 1.9f, Top 2.7f
        level.addBlock(8.75f, 2.775f, 1.4f, 0.15f, Block.Material.GLASS); // Second beam: Bottom 2.7f, Top 2.85f

        level.addBlock(8.4f, 3.15f, 0.25f, 0.6f, Block.Material.WOOD);  // Left top pillar: Bottom 2.85f, Top 3.45f
        level.addBlock(9.1f, 3.15f, 0.25f, 0.6f, Block.Material.WOOD);  // Right top pillar: Bottom 2.85f, Top 3.45f
        level.addBlock(8.75f, 3.525f, 1.0f, 0.15f, Block.Material.WOOD); // Top beam: Bottom 3.45f, Top 3.6f

        // Pigs (10 health, aligned correctly to prevent initial overlap)
        level.addPig(8.75f, 2.1f, 0.2f, 10);      // Bottom pig: resting on first beam (Bottom Y = 1.9f)
        level.addPig(8.75f, 3.03f, 0.18f, 10);     // Middle pig: resting on second beam (Bottom Y = 2.85f)
        level.addPig(8.75f, 3.75f, 0.15f, 10);    // Top pig: resting on top beam (Bottom Y = 3.6f)

        return level;
    }

    /**
     * Create Level 4 — Stone Pillars with a Wooden Bridge.
     * Birds: 5 Birds (3 Red, 2 Yellow)
     */
    public static Level createLevel4() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (5 birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);

        // Two stone pillars with a wooden bridge
        level.addBlock(7.0f, 1.2f, 0.35f, 1.0f, Block.Material.STONE);  // Pillar left: Bottom Y = 0.7f, Top Y = 1.7f
        level.addBlock(10.0f, 1.2f, 0.35f, 1.0f, Block.Material.STONE); // Pillar right: Bottom Y = 0.7f, Top Y = 1.7f
        level.addBlock(8.5f, 1.8f, 3.3f, 0.2f, Block.Material.WOOD);   // Bridge: Bottom Y = 1.7f, Top Y = 1.9f

        // Wood blocks on top of pillars (above the bridge)
        level.addBlock(7.0f, 2.3f, 0.25f, 0.8f, Block.Material.WOOD);  // Left block: Bottom Y = 1.9f, Top Y = 2.7f
        level.addBlock(10.0f, 2.3f, 0.25f, 0.8f, Block.Material.WOOD); // Right block: Bottom Y = 1.9f, Top Y = 2.7f

        // Pigs (10 health, aligned correctly to prevent initial overlap)
        level.addPig(7.0f, 2.88f, 0.18f, 10);  // On top of left wood block (Bottom Y = 2.7f)
        level.addPig(10.0f, 2.88f, 0.18f, 10); // On top of right wood block (Bottom Y = 2.7f)
        level.addPig(8.5f, 2.12f, 0.22f, 10);  // On the bridge (Bottom Y = 1.9f)

        return level;
    }

    /**
     * Create Level 5 — Castle Fortress.
     * Birds: 6 Birds (3 Red, 3 Yellow)
     */
    public static Level createLevel5() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (6 birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);

        // Castle structure
        // Ground floor
        level.addBlock(6.5f, 1.2f, 0.3f, 1.0f, Block.Material.STONE);  // Pillar 1: Bottom 0.7f, Top 1.7f
        level.addBlock(8.0f, 1.2f, 0.3f, 1.0f, Block.Material.STONE);  // Pillar 2: Bottom 0.7f, Top 1.7f
        level.addBlock(9.5f, 1.2f, 0.3f, 1.0f, Block.Material.STONE);  // Pillar 3: Bottom 0.7f, Top 1.7f
        level.addBlock(11.0f, 1.2f, 0.3f, 1.0f, Block.Material.STONE); // Pillar 4: Bottom 0.7f, Top 1.7f
        level.addBlock(8.75f, 1.825f, 5.0f, 0.25f, Block.Material.STONE); // Roof: Bottom 1.7f, Top 1.95f

        // First floor
        level.addBlock(7.25f, 2.35f, 0.25f, 0.8f, Block.Material.WOOD); // Mid Pillar 1: Bottom 1.95f, Top 2.75f
        level.addBlock(8.75f, 2.35f, 0.25f, 0.8f, Block.Material.WOOD); // Mid Pillar 2: Bottom 1.95f, Top 2.75f
        level.addBlock(10.25f, 2.35f, 0.25f, 0.8f, Block.Material.WOOD); // Mid Pillar 3: Bottom 1.95f, Top 2.75f
        level.addBlock(8.75f, 2.85f, 3.5f, 0.2f, Block.Material.WOOD);   // Mid Roof: Bottom 2.75f, Top 2.95f

        // Top floor
        level.addBlock(8.0f, 3.25f, 0.2f, 0.6f, Block.Material.GLASS);   // Top Pillar 1: Bottom 2.95f, Top 3.55f
        level.addBlock(9.5f, 3.25f, 0.2f, 0.6f, Block.Material.GLASS);   // Top Pillar 2: Bottom 2.95f, Top 3.55f
        level.addBlock(8.75f, 3.625f, 1.8f, 0.15f, Block.Material.GLASS); // Top Roof: Bottom 3.55f, Top 3.7f

        // Pigs (10 health, aligned correctly to prevent initial overlap)
        level.addPig(7.25f, 0.88f, 0.18f, 10);  // Ground left pig: resting on ground (Bottom Y = 0.7f)
        level.addPig(10.25f, 0.88f, 0.18f, 10); // Ground right pig: resting on ground (Bottom Y = 0.7f)
        level.addPig(8.75f, 2.15f, 0.2f, 10);   // First floor pig: resting on stone roof (Bottom Y = 1.95f)
        level.addPig(8.75f, 3.13f, 0.18f, 10);  // Top floor pig: resting on wood roof (Bottom Y = 2.95f)

        return level;
    }
}
