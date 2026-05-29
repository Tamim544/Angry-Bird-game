//
//package com.angrybirds;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
//public class PauseScreen implements Screen {
//    private Stage stage;
//    private MainGame game;
//    private Texture blurTexture, continueTexture, settingsTexture, mainMenuTexture, restartTexture;
//    private Image blurImage;
//    private Sound clickSound;
//
//    public PauseScreen(final MainGame game) {
//        this.game = game;
//        initializeAssets();
//        createStage();
//        createButtons();
//        setupInputProcessor();
//    }
//
//    private void initializeAssets() {
//        blurTexture = new Texture(Gdx.files.internal("GameScreen/1.png"));
//        continueTexture = new Texture(Gdx.files.internal("GameScreen/c.png"));
//        settingsTexture = new Texture(Gdx.files.internal("Settings.png"));
//        mainMenuTexture = new Texture(Gdx.files.internal("GameScreen/MM.png"));
//        restartTexture = new Texture(Gdx.files.internal("GameScreen/R.png"));
//
//        clickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
//    }
//
//    private void createStage() {
//        stage = new Stage(new ScreenViewport());
//
//        // Set up background with smooth fade-in
//        blurImage = new Image(blurTexture);
//        blurImage.setSize(stage.getWidth() + 200, stage.getHeight());
//        blurImage.setPosition(0, 0);
//        blurImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
//
//        stage.addActor(blurImage);
//    }
//
//    private void createButtons() {
//        // Creating buttons with different transition animations
//        ImageButton continueButton = createButton(continueTexture, 230, 305, 180, 70);
//        ImageButton restartButton = createButton(restartTexture, 237, 240, 160, 70);
//        ImageButton settingsButton = createButton(settingsTexture, 230, 175, 180, 70);
//        ImageButton mainMenuButton = createButton(mainMenuTexture, 215, 115, 220, 70);
//
//        // Adding actions and animations to each button
//        addFadeInEffect(continueButton, 0.4f);
//        addFadeInEffect(restartButton, 0.6f);
//        addFadeInEffect(settingsButton, 0.8f);
//        addFadeInEffect(mainMenuButton, 1.0f);
//
//        // Button click listeners
//        continueButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                handleButtonClick();
//                game.setScreen(new GameScreen(game));
//            }
//        });
//
//        restartButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                handleButtonClick();
//                game.setScreen(new GameScreen(game));
//            }
//        });
//
//        settingsButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                handleButtonClick();
//                game.setScreen(new SettingScreen2(game));
//            }
//        });
//
//        mainMenuButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                handleButtonClick();
//                manageMusicTransition();
//                game.setScreen(new loadpage(game, new MainMenuScreen(game)));
//            }
//        });
//
//        // Add buttons to stage
//        stage.addActor(continueButton);
//        stage.addActor(restartButton);
//        stage.addActor(settingsButton);
//        stage.addActor(mainMenuButton);
//    }
//
//    private ImageButton createButton(Texture texture, float x, float y, float width, float height) {
//        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
//        button.setPosition(x, y);
//        button.setSize(width, height);
//        return button;
//    }
//
//    private void addFadeInEffect(ImageButton button, float delay) {
//        button.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(delay), Actions.fadeIn(0.5f)));
//    }
//
//    private void handleButtonClick() {
//        clickSound.play();
//    }
//
//    private void manageMusicTransition() {
//        if (game.getSecondaryMusic() != null) {
//            game.getSecondaryMusic().pause();
//            System.out.println("New background music paused.");
//        }
//
//        if (game.getPrimaryMusic() != null) {
//            game.getPrimaryMusic().play();
//            System.out.println("Main background music is playing.");
//        }
//    }
//
//    private void setupInputProcessor() {
//        Gdx.input.setInputProcessor(stage);
//    }
//
//    @Override
//    public void show() {
//        // This is triggered when the screen is shown
//    }
//
//    @Override
//    public void render(float delta) {
//        // Custom background color and clearing
//        Gdx.gl.glClearColor(0.1F, 0.1F, 0.1F, 1.0F);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        // Act and draw stage
//        stage.act(delta);
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//
//    @Override
//    public void pause() {
//    }
//
//    @Override
//    public void resume() {
//    }
//
//    @Override
//    public void hide() {
//    }
//
//    @Override
//    public void dispose() {
//        // Dispose of resources properly
//        stage.dispose();
//        clickSound.dispose();
//        blurTexture.dispose();
//        continueTexture.dispose();
//        settingsTexture.dispose();
//        mainMenuTexture.dispose();
//        restartTexture.dispose();
//    }
//}


package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private Stage pauseStage;
    private MainGame mainGame;
    private Texture backgroundTexture, resumeButtonTexture, settingsButtonTexture, menuButtonTexture, restartButtonTexture;
    private Image backgroundImg;
    private Sound buttonSound;

    public PauseScreen(final MainGame mainGame) {
        this.mainGame = mainGame;
        loadAssets();
        createPauseStage();
        setupButtons();
        configureInputProcessor();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("GameScreen/1.png"));
        resumeButtonTexture = new Texture(Gdx.files.internal("GameScreen/c.png"));
        settingsButtonTexture = new Texture(Gdx.files.internal("Settings.png"));
        menuButtonTexture = new Texture(Gdx.files.internal("GameScreen/MM.png"));
        restartButtonTexture = new Texture(Gdx.files.internal("GameScreen/R.png"));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
    }

    private void createPauseStage() {
        pauseStage = new Stage(new ScreenViewport());

        // Create and fade in the background image
        backgroundImg = new Image(backgroundTexture);
        backgroundImg.setSize(pauseStage.getWidth() + 200, pauseStage.getHeight());
        backgroundImg.setPosition(0, 0);
        backgroundImg.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
        pauseStage.addActor(backgroundImg);
    }

    private void setupButtons() {
        ImageButton resumeButton = setupButton(resumeButtonTexture, 230, 305, 180, 70);
        ImageButton restartButton = setupButton(restartButtonTexture, 237, 240, 160, 70);
        ImageButton settingsButton = setupButton(settingsButtonTexture, 230, 175, 180, 70);
        ImageButton mainMenuButton = setupButton(menuButtonTexture, 215, 115, 220, 70);

        // Add animations to buttons with different delays
        animateButton(resumeButton, 0.4f);
        animateButton(restartButton, 0.6f);
        animateButton(settingsButton, 0.8f);
        animateButton(mainMenuButton, 1.0f);

        // Button click listeners
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleButtonClick();
                mainGame.setScreen(new GameScreen(mainGame, 1));
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleButtonClick();
                mainGame.setScreen(new GameScreen(mainGame, 1));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleButtonClick();
                mainGame.setScreen(new SettingScreen2(mainGame));
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleButtonClick();
                transitionToMainMenu();
                mainGame.setScreen(new loadpage(mainGame, new MainMenuScreen(mainGame)));
            }
        });

        // Add buttons to stage
        pauseStage.addActor(resumeButton);
        pauseStage.addActor(restartButton);
        pauseStage.addActor(settingsButton);
        pauseStage.addActor(mainMenuButton);
    }

    private ImageButton setupButton(Texture texture, float x, float y, float width, float height) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.setPosition(x, y);
        button.setSize(width, height);
        return button;
    }

    private void animateButton(ImageButton button, float delay) {
        button.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(delay), Actions.fadeIn(0.5f)));
    }

    private void handleButtonClick() {
        buttonSound.play();
    }

    private void transitionToMainMenu() {
        if (mainGame.getSecondaryMusic() != null) {
            mainGame.getSecondaryMusic().pause();
            System.out.println("New background music paused.");
        }

        if (mainGame.getPrimaryMusic() != null) {
            mainGame.getPrimaryMusic().play();
            System.out.println("Main background music is playing.");
        }
    }

    private void configureInputProcessor() {
        Gdx.input.setInputProcessor(pauseStage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1F, 0.1F, 0.1F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        pauseStage.act(delta);
        pauseStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        pauseStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        pauseStage.dispose();
        buttonSound.dispose();
        backgroundTexture.dispose();
        resumeButtonTexture.dispose();
        settingsButtonTexture.dispose();
        menuButtonTexture.dispose();
        restartButtonTexture.dispose();
    }
}
