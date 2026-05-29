
package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingScreen2 implements Screen {
    private Stage uiStage;
    private MainGame game;
    private Texture backgroundBlur, settingsBackground, titleCard, buttonOn, buttonOff, backButtonTexture;
    private Image backgroundBlurImage, settingsBackgroundImage, titleCardImage;
    private ImageButton buttonOn1, buttonOff1, buttonOn2, buttonOff2;
    private Sound buttonClickSound;

    public SettingScreen2(final MainGame game) {
        this.game = game;
        this.uiStage = new Stage(new ScreenViewport());
        this.buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));

        loadTextures();
        initializeUIComponents();
        setupButtonListeners();
        setupInputProcessor();
    }

    private void loadTextures() {
        this.backButtonTexture = new Texture(Gdx.files.internal("Settings/2.png"));
        this.backgroundBlur = new Texture(Gdx.files.internal("GameScreen/1.png")); // Background
        this.settingsBackground = new Texture(Gdx.files.internal("Settings/1.png")); // Settings background
        this.titleCard = new Texture(Gdx.files.internal("Settings/3.png")); // Title card
        this.buttonOn = new Texture(Gdx.files.internal("Settings/4.png"));
        this.buttonOff = new Texture(Gdx.files.internal("Settings/6.png"));
    }

    private void initializeUIComponents() {
        // Create the images and buttons for the UI
        backgroundBlurImage = createImage(backgroundBlur, uiStage.getWidth(), uiStage.getHeight(), 0, 0, 1f);
        settingsBackgroundImage = createImage(settingsBackground, 285.0F, 250.0F, 183.0F, 120.0F, 1.5f);
        titleCardImage = createImage(titleCard, 200.0F, 70.0F, 225.0F, 120.0F, 0.5f);

        buttonOn1 = createButton(buttonOn, 257.0F, 268.0F);
        buttonOff1 = createButton(buttonOff, 257.0F, 268.0F, false);

        buttonOn2 = createButton(buttonOn, 257.0F, 216.0F);
        buttonOff2 = createButton(buttonOff, 257.0F, 216.0F, false);

        ImageButton backButton = createButton(backButtonTexture, 500.0F, 267.0F, 40.0F, 120.0F);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                game.setScreen(new GameScreen(game, 1));
                game.getPrimaryMusic().setVolume(1.0F);
            }
        });
        backButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f))); // Smooth fade-in
        this.uiStage.addActor(backButton);
    }

    private Image createImage(Texture texture, float width, float height, float x, float y, float fadeInDuration) {
        Image image = new Image(texture);
        image.setSize(width, height);
        image.setPosition(x, y);
        image.addAction(Actions.fadeIn(fadeInDuration));
        uiStage.addActor(image);
        return image;
    }

    private ImageButton createButton(Texture texture, float x, float y) {
        return createButton(texture, x, y, 203.0F, 77.0F, 0.5f, true);
    }

    private ImageButton createButton(Texture texture, float x, float y, boolean visible) {
        return createButton(texture, x, y, 203.0F, 77.0F, 0.5f, visible);
    }

    private ImageButton createButton(Texture texture, float x, float y, float width, float height) {
        return createButton(texture, x, y, width, height, 0.5f, true);
    }

    private ImageButton createButton(Texture texture, float x, float y, float width, float height, float fadeInDuration, boolean visible) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.setPosition(x, y);
        button.setSize(width, height);
        button.setVisible(visible);
        button.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(fadeInDuration)));
        uiStage.addActor(button);
        return button;
    }

    private void setupButtonListeners() {
        // Configure button listeners for toggling on/off buttons
        addToggleListener(buttonOn1, buttonOff1);
        addToggleListener(buttonOn2, buttonOff2);
        addToggleListener(buttonOff1, buttonOn1);
        addToggleListener(buttonOff2, buttonOn2);
    }

    private void addToggleListener(final ImageButton buttonToShow, final ImageButton buttonToHide) {
        buttonToHide.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                buttonToHide.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f)); // Shrink on click
                buttonToHide.setVisible(false);
                buttonToShow.setVisible(true);
                buttonToShow.addAction(Actions.scaleTo(1f, 1f, 0.2f)); // Scale back to normal
            }
        });
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(16384);
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        backgroundBlurImage.setSize(uiStage.getWidth(), uiStage.getHeight());
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        uiStage.dispose();
        backgroundBlur.dispose();
        settingsBackground.dispose();
        titleCard.dispose();
        buttonOn.dispose();
        buttonOff.dispose();
        buttonClickSound.dispose();
        game.getPrimaryMusic().setVolume(1.0F);
    }
}
