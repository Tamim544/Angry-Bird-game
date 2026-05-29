package com.angrybirds;

/**
 * Factory that creates pre-defined level configurations.
 */
public class LevelFactory {

    /**
     * Create Level 1 — a simple introductory level.
     * Layout: Slingshot on the left, a small wooden structure with 2 pigs.
     */
    public static Level createLevel1() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (3 red birds)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.RED);

        // Structure — right side of screen
        // Base blocks (two horizontal wood blocks)
        level.addBlock(7.0f, 1.0f, 0.3f, 0.8f, Block.Material.WOOD);   // left pillar
        level.addBlock(8.5f, 1.0f, 0.3f, 0.8f, Block.Material.WOOD);   // right pillar
        level.addBlock(7.75f, 1.6f, 2.0f, 0.2f, Block.Material.WOOD);  // horizontal beam

        // Top section
        level.addBlock(7.4f, 2.0f, 0.3f, 0.6f, Block.Material.WOOD);   // left top pillar
        level.addBlock(8.1f, 2.0f, 0.3f, 0.6f, Block.Material.WOOD);   // right top pillar
        level.addBlock(7.75f, 2.5f, 1.2f, 0.15f, Block.Material.WOOD); // top beam

        // Pigs
        level.addPig(7.75f, 1.15f, 0.2f, 100);  // bottom pig (inside structure)
        level.addPig(7.75f, 2.15f, 0.18f, 80);   // top pig

        return level;
    }

    /**
     * Create Level 2 — a more complex level with glass and stone.
     */
    public static Level createLevel2() {
        Level level = new Level();
        level.setGroundY(0.5f);
        level.setGroundWidth(20f);
        level.setSlingshotPosition(2.5f, 1.8f);

        // Birds (4 birds for this harder level)
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.YELLOW);
        level.addBird(Bird.BirdType.RED);
        level.addBird(Bird.BirdType.BLUE);

        // Left structure (glass)
        level.addBlock(6.5f, 1.0f, 0.2f, 0.8f, Block.Material.GLASS);
        level.addBlock(7.5f, 1.0f, 0.2f, 0.8f, Block.Material.GLASS);
        level.addBlock(7.0f, 1.6f, 1.4f, 0.15f, Block.Material.GLASS);

        // Right structure (stone fortress)
        level.addBlock(9.0f, 1.0f, 0.3f, 1.0f, Block.Material.STONE);
        level.addBlock(10.5f, 1.0f, 0.3f, 1.0f, Block.Material.STONE);
        level.addBlock(9.75f, 1.7f, 2.0f, 0.2f, Block.Material.STONE);

        // Wood blocks on top of stone
        level.addBlock(9.4f, 2.1f, 0.25f, 0.6f, Block.Material.WOOD);
        level.addBlock(10.1f, 2.1f, 0.25f, 0.6f, Block.Material.WOOD);
        level.addBlock(9.75f, 2.6f, 1.2f, 0.15f, Block.Material.WOOD);

        // Pigs
        level.addPig(7.0f, 1.1f, 0.2f, 60);     // glass structure pig (easy)
        level.addPig(9.75f, 1.15f, 0.22f, 120);  // stone structure pig (harder)
        level.addPig(9.75f, 2.25f, 0.18f, 80);   // top of stone pig

        return level;
    }
}
