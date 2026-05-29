
package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private Stage mainStage;
    private MainGame mainGame;
    private SpriteBatch spriteBatch;
    private float elapsedGameTime;
    private Texture startButtonTexture, customButtonTexture, settingsButtonTexture, exitButtonTexture, backgroundTexture;
    private ImageButton startGameButton, customGameButton, settingsButton, exitGameButton;
    private Image backgroundImage;
    private Sound buttonClickSound;

    // Constants for button sizing and positioning
    private static final float FRAME_DURATION = 0.1F;
    private static final float BUTTON_WIDTH = 170.0F;
    private static final float BUTTON_HEIGHT = 50.0F;
    private static final float CUSTOM_BUTTON_WIDTH = 140.0F;
    private static final float SETTINGS_BUTTON_WIDTH = 150.0F;
    private static final float EXIT_BUTTON_WIDTH = 70.0F;
    private static final float BUTTON_START_Y = 244.0F;
    private static final float BUTTON_SPACING_Y = 45.0F;

    public MainMenuScreen(final MainGame mainGame) {
        this.mainGame = mainGame;
        this.mainStage = new Stage(new ScreenViewport());
        this.spriteBatch = new SpriteBatch();
        this.buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));

        initializeTextures();
        setupBackground();
        setupButtons();
        addListeners();

        Gdx.input.setInputProcessor(this.mainStage);
    }

    private void initializeTextures() {
        startButtonTexture = new Texture(Gdx.files.internal("Start Game.png"));
        customButtonTexture = new Texture(Gdx.files.internal("Custom.png"));
        settingsButtonTexture = new Texture(Gdx.files.internal("Settings.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("Exit.png"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
    }

    private void setupBackground() {
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(mainStage.getWidth(), mainStage.getHeight());
        backgroundImage.setPosition(0.0F, 0.0F);
        mainStage.addActor(backgroundImage);
    }

    private void setupButtons() {
        startGameButton = createButton(startButtonTexture, BUTTON_WIDTH, BUTTON_HEIGHT, 220.0F, BUTTON_START_Y);
        customGameButton = createButton(customButtonTexture, CUSTOM_BUTTON_WIDTH, BUTTON_HEIGHT, 231.0F, BUTTON_START_Y - BUTTON_SPACING_Y);
        settingsButton = createButton(settingsButtonTexture, SETTINGS_BUTTON_WIDTH, BUTTON_HEIGHT, 232.0F, BUTTON_START_Y - 2 * BUTTON_SPACING_Y);
        exitGameButton = createButton(exitButtonTexture, EXIT_BUTTON_WIDTH, BUTTON_HEIGHT, 264.0F, BUTTON_START_Y - 3 * BUTTON_SPACING_Y);

        mainStage.addActor(startGameButton);
        mainStage.addActor(customGameButton);
        mainStage.addActor(settingsButton);
        mainStage.addActor(exitGameButton);
    }

    private ImageButton createButton(Texture texture, float width, float height, float x, float y) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.setSize(width, height);
        button.setPosition(x, y);
        return button;
    }

    private void addListeners() {
        startGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mainGame.playSound(buttonClickSound);
                mainGame.setScreen(new loadpage(mainGame, new Roadmap(mainGame)));
            }
        });

        customGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mainGame.playSound(buttonClickSound);
                mainGame.setScreen(new loadpage(mainGame, new adding(mainGame)));
            }
        });

        settingsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mainGame.playSound(buttonClickSound);
                mainGame.setScreen(new settingScreen(mainGame));
            }
        });

        exitGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mainGame.playSound(buttonClickSound);
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        elapsedGameTime += delta;
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        mainStage.act(delta);
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.mainStage);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mainStage.dispose();
        spriteBatch.dispose();
        startButtonTexture.dispose();
        customButtonTexture.dispose();
        settingsButtonTexture.dispose();
        exitButtonTexture.dispose();
        backgroundTexture.dispose();
        buttonClickSound.dispose();
    }
}
