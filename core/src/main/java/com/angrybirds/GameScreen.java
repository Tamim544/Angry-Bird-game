package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The main gameplay screen with Box2D physics, slingshot mechanics,
 * and Angry Birds-style level gameplay.
 */
public class GameScreen implements Screen {

    // Game reference
    private MainGame appInstance;
    private int levelNumber;

    // Physics
    private World world;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private float accumulator = 0f;

    // Camera & rendering
    private OrthographicCamera gameCamera;
    private FitViewport gameViewport;
    private SpriteBatch gameBatch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private boolean showDebug = false;

    // UI layer
    private Stage uiStage;
    private ImageButton pauseBtn;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    // Textures
    private Texture backgroundTexture;
    private Texture birdTexture, pigTexture;
    private Texture woodTexture, glassTexture, stoneTexture;
    private Texture slingshotTexture;
    private Texture pauseIconTexture;
    private Texture groundTexture;
    private Texture winScreenTexture, loseScreenTexture;

    // Game objects
    private Level level;
    private Slingshot slingshot;
    private List<Bird> birds;
    private List<Block> blocks;
    private List<Pig> pigs;
    private List<Body> bodiesToRemove;

    // Slingshot input state
    private Bird currentBird;
    private int currentBirdIndex = 0;
    private boolean isDragging = false;
    private Vector2 dragPosition = new Vector2();

    // Camera follow
    private boolean followingBird = false;
    private float cameraReturnTimer = 0f;
    private static final float CAMERA_RETURN_DELAY = 1.5f;

    // Game state
    private enum GameState { AIMING, BIRD_FLYING, WAITING, WON, LOST }
    private GameState gameState = GameState.AIMING;
    private float waitTimer = 0f;
    private static final float WAIT_AFTER_SETTLE = 1.5f;

    // Sound
    private Sound tapSound;
    private Sound launchSound;
    private Sound collisionSound;

    // World dimensions
    private static final float WORLD_WIDTH = 12.8f;  // meters
    private static final float WORLD_HEIGHT = 7.2f;   // meters

    public GameScreen(final MainGame appInstance) {
        this(appInstance, 1);
    }

    public GameScreen(final MainGame appInstance, int levelNumber) {
        this.appInstance = appInstance;
        this.levelNumber = levelNumber;
        this.bodiesToRemove = new ArrayList<>();

        initPhysics();
        initCamera();
        initTextures();
        initLevel();
        initUI();
        initInput();
    }

    private void initPhysics() {
        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new GameContactListener());
        debugRenderer = new Box2DDebugRenderer();
    }

    private void initCamera() {
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, gameCamera);
        gameCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        gameCamera.update();

        gameBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    private void initTextures() {
        // Use existing background or create a solid sky-blue
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        // For now, use simple colored textures — we'll use the programmatic ones
        birdTexture = createColorTexture(220, 50, 50);       // Red bird
        pigTexture = createColorTexture(80, 180, 80);        // Green pig
        woodTexture = createColorTexture(160, 110, 50);      // Brown wood
        glassTexture = createColorTexture(150, 210, 240);    // Light blue glass
        stoneTexture = createColorTexture(130, 130, 130);    // Gray stone
        groundTexture = createColorTexture(100, 160, 60);    // Green ground
        slingshotTexture = createColorTexture(90, 60, 30);   // Dark brown slingshot

        pauseIconTexture = new Texture(Gdx.files.internal("GameScreen/p.png"));
        winScreenTexture = new Texture(Gdx.files.internal("GameScreen/victory.png"));
        loseScreenTexture = new Texture(Gdx.files.internal("GameScreen/loose.png"));

        tapSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        glyphLayout = new GlyphLayout();
    }

    /**
     * Creates a simple 1x1 colored texture to use for game objects.
     */
    private Texture createColorTexture(int r, int g, int b) {
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(2, 2, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(r / 255f, g / 255f, b / 255f, 1f);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    private void initLevel() {
        Level levelData;
        switch (levelNumber) {
            case 2: levelData = LevelFactory.createLevel2(); break;
            default: levelData = LevelFactory.createLevel1(); break;
        }

        levelData.build(world, birdTexture, pigTexture, woodTexture, glassTexture, stoneTexture, slingshotTexture);

        this.level = levelData;
        this.slingshot = levelData.getSlingshot();
        this.birds = levelData.getBirds();
        this.blocks = levelData.getBlocks();
        this.pigs = levelData.getPigs();

        // Load first bird to slingshot
        if (!birds.isEmpty()) {
            currentBirdIndex = 0;
            currentBird = birds.get(0);
            currentBird.setPosition(slingshot.getAnchorPosition().x, slingshot.getAnchorPosition().y);
        }
    }

    private void initUI() {
        uiStage = new Stage(new ScreenViewport());

        // Pause button
        pauseBtn = new ImageButton(new TextureRegionDrawable(pauseIconTexture));
        pauseBtn.setPosition(10, Gdx.graphics.getHeight() - 70);
        pauseBtn.setSize(60, 60);
        pauseBtn.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tapSound.play();
                appInstance.setScreen(new PauseScreen(appInstance));
            }
        });
        uiStage.addActor(pauseBtn);
    }

    private void initInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); // UI first priority
        multiplexer.addProcessor(new GameInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    // ============ GAME INPUT PROCESSOR ============

    private class GameInputProcessor extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (gameState != GameState.AIMING || currentBird == null) return false;

            Vector3 worldCoords = gameCamera.unproject(new Vector3(screenX, screenY, 0));
            Vector2 touchWorld = new Vector2(worldCoords.x, worldCoords.y);

            // Check if touch is near the bird/slingshot
            float dist = touchWorld.dst(currentBird.getPosition());
            if (dist < 1.0f) {
                isDragging = true;
                dragPosition.set(touchWorld);
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (!isDragging) return false;

            Vector3 worldCoords = gameCamera.unproject(new Vector3(screenX, screenY, 0));
            dragPosition.set(worldCoords.x, worldCoords.y);

            // Clamp to max drag distance
            Vector2 clamped = slingshot.clampDragPosition(dragPosition);
            dragPosition.set(clamped);

            // Move the bird to the drag position
            currentBird.setPosition(dragPosition.x, dragPosition.y);

            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (!isDragging) return false;
            isDragging = false;

            // Calculate launch impulse
            Vector2 impulse = slingshot.calculateLaunchImpulse(dragPosition);

            // Don't launch if barely dragged
            if (impulse.len() < 0.5f) {
                currentBird.setPosition(slingshot.getAnchorPosition().x, slingshot.getAnchorPosition().y);
                return true;
            }

            // Launch the bird!
            currentBird.launch(impulse);
            gameState = GameState.BIRD_FLYING;
            followingBird = true;

            tapSound.play();

            return true;
        }
    }

    // ============ UPDATE LOGIC ============

    @Override
    public void render(float delta) {
        // Clamp delta to prevent spiral of death
        delta = Math.min(delta, 0.25f);

        update(delta);

        // Clear screen
        Gdx.gl.glClearColor(0.4f, 0.7f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game world
        gameCamera.update();
        gameBatch.setProjectionMatrix(gameCamera.combined);

        gameBatch.begin();
        renderBackground();
        renderGround();
        renderGameObjects();
        gameBatch.end();

        // Render slingshot band
        renderSlingshotBand();

        // Render trajectory dots when dragging
        if (isDragging) {
            renderTrajectory();
        }

        // Debug renderer (toggle with 'D' key)
        if (showDebug) {
            debugRenderer.render(world, gameCamera.combined);
        }

        // Render UI overlay
        uiStage.act(delta);
        uiStage.draw();

        // Render game state overlay (win/lose)
        renderGameStateOverlay();
    }

    private void update(float delta) {
        // Step physics
        accumulator += delta;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }

        // Remove destroyed bodies
        removeDeadBodies();

        // Update game state
        switch (gameState) {
            case BIRD_FLYING:
                updateBirdFlying(delta);
                break;
            case WAITING:
                updateWaiting(delta);
                break;
            case AIMING:
            case WON:
            case LOST:
                break;
        }

        // Update camera
        updateCamera(delta);
    }

    private void updateBirdFlying(float delta) {
        if (currentBird == null) return;

        currentBird.updateSettled(delta);

        // Check if bird went out of bounds
        Vector2 pos = currentBird.getPosition();
        if (pos.x > WORLD_WIDTH + 2 || pos.x < -2 || pos.y < -2) {
            currentBird.takeDamage(999); // Force removal
            advanceToNextBird();
            return;
        }

        // Check if bird settled
        if (currentBird.isSettled()) {
            advanceToNextBird();
        }
    }

    private void advanceToNextBird() {
        followingBird = false;
        cameraReturnTimer = 0f;

        // Check win condition — all pigs dead?
        boolean allPigsDead = true;
        for (Pig pig : pigs) {
            if (pig.isAlive()) {
                allPigsDead = false;
                break;
            }
        }

        if (allPigsDead) {
            gameState = GameState.WON;
            return;
        }

        // Advance to next bird
        currentBirdIndex++;
        if (currentBirdIndex < birds.size()) {
            gameState = GameState.WAITING;
            waitTimer = 0f;
        } else {
            // No more birds — check if we lost
            gameState = GameState.LOST;
        }
    }

    private void updateWaiting(float delta) {
        waitTimer += delta;
        if (waitTimer >= WAIT_AFTER_SETTLE) {
            currentBird = birds.get(currentBirdIndex);
            currentBird.setPosition(slingshot.getAnchorPosition().x, slingshot.getAnchorPosition().y);
            gameState = GameState.AIMING;
        }
    }

    private void removeDeadBodies() {
        // Collect bodies to remove
        for (Block block : blocks) {
            if (block.isMarkedForRemoval() && block.getBody() != null) {
                bodiesToRemove.add(block.getBody());
            }
        }
        for (Pig pig : pigs) {
            if (pig.isMarkedForRemoval() && pig.getBody() != null) {
                bodiesToRemove.add(pig.getBody());
            }
        }

        // Destroy bodies
        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();

        // Clean up lists
        Iterator<Block> blockIt = blocks.iterator();
        while (blockIt.hasNext()) {
            Block b = blockIt.next();
            if (b.isMarkedForRemoval()) {
                blockIt.remove();
            }
        }

        Iterator<Pig> pigIt = pigs.iterator();
        while (pigIt.hasNext()) {
            Pig p = pigIt.next();
            if (p.isMarkedForRemoval()) {
                pigIt.remove();
            }
        }
    }

    private void updateCamera(float delta) {
        float targetX, targetY;

        if (followingBird && currentBird != null && currentBird.isLaunched()) {
            // Follow the bird
            targetX = currentBird.getPosition().x;
            targetY = Math.max(currentBird.getPosition().y, WORLD_HEIGHT / 2f);
            // Clamp camera
            targetX = MathUtils.clamp(targetX, WORLD_WIDTH / 2f, WORLD_WIDTH * 1.2f);
            targetY = MathUtils.clamp(targetY, WORLD_HEIGHT / 2f, WORLD_HEIGHT);
        } else {
            // Return to default position
            targetX = WORLD_WIDTH / 2f;
            targetY = WORLD_HEIGHT / 2f;
        }

        // Smooth camera movement
        gameCamera.position.x += (targetX - gameCamera.position.x) * 3f * delta;
        gameCamera.position.y += (targetY - gameCamera.position.y) * 3f * delta;
    }

    // ============ RENDERING ============

    private void renderBackground() {
        // Draw sky background filling the viewport
        gameBatch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    }

    private void renderGround() {
        // Draw ground as a green strip
        float groundHeight = level.getGroundY() + 0.2f; // top of ground body + half-height
        gameBatch.draw(groundTexture, 0, 0, WORLD_WIDTH, groundHeight);
    }

    private void renderGameObjects() {
        // Render slingshot (behind bird)
        slingshot.render(gameBatch);

        // Render blocks
        for (Block block : blocks) {
            block.render(gameBatch);
        }

        // Render pigs
        for (Pig pig : pigs) {
            pig.render(gameBatch);
        }

        // Render birds (the queue, on the ground)
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            if (bird.isAlive()) {
                bird.render(gameBatch);
            }
        }
    }

    private void renderSlingshotBand() {
        if (currentBird == null || currentBird.isLaunched()) return;

        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Vector2 anchor = slingshot.getAnchorPosition();
        Vector2 birdPos = currentBird.getPosition();

        // Draw elastic bands (two lines from slingshot forks to bird)
        float forkOffset = 0.15f;
        shapeRenderer.setColor(0.3f, 0.2f, 0.1f, 1f);
        shapeRenderer.rectLine(
            anchor.x - forkOffset, anchor.y,
            birdPos.x, birdPos.y,
            0.04f
        );
        shapeRenderer.rectLine(
            anchor.x + forkOffset, anchor.y,
            birdPos.x, birdPos.y,
            0.04f
        );

        shapeRenderer.end();
    }

    private void renderTrajectory() {
        if (currentBird == null) return;

        Vector2 impulse = slingshot.calculateLaunchImpulse(dragPosition);
        if (impulse.len() < 0.5f) return;

        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 0.6f);

        // Simulate trajectory dots
        float birdMass = currentBird.getBody().getMass();
        if (birdMass <= 0) birdMass = 1f;
        Vector2 vel = new Vector2(impulse).scl(1f / birdMass);
        Vector2 pos = new Vector2(dragPosition);
        float dt = 0.05f;

        for (int i = 0; i < 30; i++) {
            // Only draw every other dot for dotted effect
            if (i % 2 == 0) {
                float dotSize = 0.04f * (1f - i / 40f); // Dots get smaller
                shapeRenderer.circle(pos.x, pos.y, dotSize, 8);
            }
            // Simple projectile physics
            vel.y -= 9.8f * dt;
            pos.x += vel.x * dt;
            pos.y += vel.y * dt;

            if (pos.y < level.getGroundY()) break;
        }

        shapeRenderer.end();
    }

    private void renderGameStateOverlay() {
        if (gameState == GameState.WON) {
            gameBatch.begin();
            gameBatch.draw(winScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            gameBatch.end();

            // Add a delay then transition
            // For now, clicking anywhere goes to level select
        } else if (gameState == GameState.LOST) {
            gameBatch.begin();
            gameBatch.draw(loseScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            gameBatch.end();
        }

        // Handle win/lose screen clicks
        if (gameState == GameState.WON || gameState == GameState.LOST) {
            if (Gdx.input.justTouched()) {
                appInstance.setScreen(new LevelEndScreen(appInstance));
            }
        }
    }

    // ============ SCREEN LIFECYCLE ============

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        gameCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        uiStage.getViewport().update(width, height, true);
        pauseBtn.setPosition(10, height - 70);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (world != null) world.dispose();
        if (gameBatch != null) gameBatch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
        if (uiStage != null) uiStage.dispose();
        if (font != null) font.dispose();

        // Dispose programmatic textures
        if (birdTexture != null) birdTexture.dispose();
        if (pigTexture != null) pigTexture.dispose();
        if (woodTexture != null) woodTexture.dispose();
        if (glassTexture != null) glassTexture.dispose();
        if (stoneTexture != null) stoneTexture.dispose();
        if (groundTexture != null) groundTexture.dispose();
        if (slingshotTexture != null) slingshotTexture.dispose();

        // Dispose loaded textures
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (pauseIconTexture != null) pauseIconTexture.dispose();
        if (winScreenTexture != null) winScreenTexture.dispose();
        if (loseScreenTexture != null) loseScreenTexture.dispose();
        if (tapSound != null) tapSound.dispose();
    }
}
