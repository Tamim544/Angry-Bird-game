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
    private InputMultiplexer inputMultiplexer;
    private boolean showDebug = false;

    // UI layer
    private Stage uiStage;
    private ImageButton pauseBtn;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    // Textures
    private Texture backgroundTexture;
    private Texture birdTexture, yellowBirdTexture, pigTexture;
    private Texture woodTexture, glassTexture, stoneTexture;
    private Texture slingshotTexture;
    private Texture pauseIconTexture;
    private Texture groundTexture;
    private Texture winScreenTexture, loseScreenTexture;
    private Texture replayBtnTexture, menuBtnTexture, nextBtnTexture;

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
    private static final float WAIT_AFTER_SETTLE = 0.5f;

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
        GameContactListener.reset(); // Reset contact listener startup clock
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
        backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));

        birdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("yellow_bird.png"));
        pigTexture = new Texture(Gdx.files.internal("green_pig.png"));
        
        woodTexture = new Texture(Gdx.files.internal("wood_block.png"));
        stoneTexture = new Texture(Gdx.files.internal("stone_block.png"));
        glassTexture = new Texture(Gdx.files.internal("wood_block.png")); // Fallback

        groundTexture = createColorTexture(100, 160, 60);    // Keep programmatic green ground
        slingshotTexture = createColorTexture(90, 60, 30);   // Keep programmatic brown slingshot

        pauseIconTexture = new Texture(Gdx.files.internal("GameScreen/p.png"));
        winScreenTexture = new Texture(Gdx.files.internal("GameScreen/victory.png"));
        loseScreenTexture = new Texture(Gdx.files.internal("GameScreen/loose.png"));
        replayBtnTexture = new Texture(Gdx.files.internal("GameScreen/R.png"));
        menuBtnTexture = new Texture(Gdx.files.internal("GameScreen/MM.png"));
        nextBtnTexture = new Texture(Gdx.files.internal("GameScreen/c.png"));

        tapSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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
            case 3: levelData = LevelFactory.createLevel3(); break;
            case 4: levelData = LevelFactory.createLevel4(); break;
            case 5: levelData = LevelFactory.createLevel5(); break;
            default: levelData = LevelFactory.createLevel1(); break;
        }

        levelData.build(world, birdTexture, yellowBirdTexture, pigTexture, woodTexture, glassTexture, stoneTexture, slingshotTexture);

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
                appInstance.playSound(tapSound);
                appInstance.setScreen(new PauseScreen(appInstance, GameScreen.this));
            }
        });
        uiStage.addActor(pauseBtn);
    }

    private void initInput() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(uiStage); // UI first priority
        inputMultiplexer.addProcessor(new GameInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);
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

            appInstance.playSound(tapSound);

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
        // Update contact listener clock
        GameContactListener.update(delta);

        // Step physics
        accumulator += delta;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }

        // Remove destroyed bodies
        removeDeadBodies();

        // Check if all pigs are dead
        checkWinCondition();

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
            currentBird.takeDamage(999); // Vanish / destroy the settled bird body
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

    private void checkWinCondition() {
        if (gameState == GameState.WON || gameState == GameState.LOST) {
            return;
        }

        boolean allPigsDead = true;
        for (Pig pig : pigs) {
            if (pig.isAlive()) {
                allPigsDead = false;
                break;
            }
        }

        if (allPigsDead) {
            gameState = GameState.WON;
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
        for (Bird bird : birds) {
            if (bird.isMarkedForRemoval() && bird.getBody() != null) {
                bodiesToRemove.add(bird.getBody());
                bird.body = null;
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
        // Draw sky background filling the viewport relative to camera
        float camX = gameCamera.position.x - WORLD_WIDTH / 2f;
        float camY = gameCamera.position.y - WORLD_HEIGHT / 2f;
        gameBatch.draw(backgroundTexture, camX, camY, WORLD_WIDTH, WORLD_HEIGHT);
    }

    private void renderGround() {
        // Draw ground as a green strip covering the whole level width
        float groundHeight = level.getGroundY() + 0.2f;
        gameBatch.draw(groundTexture, 0, 0, 30f, groundHeight); // 30f ensures it's wide enough
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
        if (gameState != GameState.WON && gameState != GameState.LOST) {
            return;
        }

        float camX = gameCamera.position.x - WORLD_WIDTH / 2f;
        float camY = gameCamera.position.y - WORLD_HEIGHT / 2f;

        // 1. Draw dark background overlay
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.5f);
        shapeRenderer.rect(camX, camY, WORLD_WIDTH, WORLD_HEIGHT);
        shapeRenderer.end();

        // 2. Draw centered dialog banner
        float bannerW = 5.0f;
        float bannerH = 3.3f;
        float bx = camX + (WORLD_WIDTH - bannerW) / 2f;
        float by = camY + (WORLD_HEIGHT - bannerH) / 2f + 0.4f;

        gameBatch.begin();
        if (gameState == GameState.WON) {
            gameBatch.draw(winScreenTexture, bx, by, bannerW, bannerH);
        } else {
            gameBatch.draw(loseScreenTexture, bx, by, bannerW, bannerH);
        }

        // 3. Draw buttons below banner
        float btnSize = 0.8f;
        float btnY = by - 0.9f;
        boolean hasNextButton = (gameState == GameState.WON && levelNumber < 5);

        float menuBtnX, replayBtnX, nextBtnX = 0f;

        if (hasNextButton) {
            float spacing = 0.5f;
            float totalW = 3 * btnSize + 2 * spacing;
            float startX = camX + (WORLD_WIDTH - totalW) / 2f;
            menuBtnX = startX;
            replayBtnX = startX + btnSize + spacing;
            nextBtnX = startX + 2 * (btnSize + spacing);

            gameBatch.draw(menuBtnTexture, menuBtnX, btnY, btnSize, btnSize);
            gameBatch.draw(replayBtnTexture, replayBtnX, btnY, btnSize, btnSize);
            gameBatch.draw(nextBtnTexture, nextBtnX, btnY, btnSize, btnSize);
        } else {
            float spacing = 0.5f;
            float totalW = 2 * btnSize + spacing;
            float startX = camX + (WORLD_WIDTH - totalW) / 2f;
            menuBtnX = startX;
            replayBtnX = startX + btnSize + spacing;

            gameBatch.draw(menuBtnTexture, menuBtnX, btnY, btnSize, btnSize);
            gameBatch.draw(replayBtnTexture, replayBtnX, btnY, btnSize, btnSize);
        }
        gameBatch.end();

        // 4. Handle button clicks
        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameCamera.unproject(touchPoint);

            if (isTouched(touchPoint.x, touchPoint.y, menuBtnX, btnY, btnSize, btnSize)) {
                appInstance.playSound(tapSound);
                appInstance.setScreen(new loadpage(appInstance, new LevelEndScreen(appInstance)));
            } else if (isTouched(touchPoint.x, touchPoint.y, replayBtnX, btnY, btnSize, btnSize)) {
                appInstance.playSound(tapSound);
                appInstance.setScreen(new loadpage(appInstance, new GameScreen(appInstance, levelNumber)));
            } else if (hasNextButton && isTouched(touchPoint.x, touchPoint.y, nextBtnX, btnY, btnSize, btnSize)) {
                appInstance.playSound(tapSound);
                appInstance.setScreen(new loadpage(appInstance, new GameScreen(appInstance, levelNumber + 1)));
            }
        }
    }

    private boolean isTouched(float tx, float ty, float bx, float by, float bw, float bh) {
        return tx >= bx && tx <= bx + bw && ty >= by && ty <= by + bh;
    }

    // ============ SCREEN LIFECYCLE ============

    @Override
    public void show() {
        if (inputMultiplexer != null) {
            Gdx.input.setInputProcessor(inputMultiplexer);
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

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
        if (replayBtnTexture != null) replayBtnTexture.dispose();
        if (menuBtnTexture != null) menuBtnTexture.dispose();
        if (nextBtnTexture != null) nextBtnTexture.dispose();
        if (tapSound != null) tapSound.dispose();
    }
}
