package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class settingScreen implements Screen {

    private Stage stage;
    private MainGame game;
    private Screen previousScreen;

    private Texture blurTexture, buttonOnTexture, buttonOffTexture, backBtnTexture, btnBgTexture;
    private ImageButton toggleBtn, backBtn;
    private TextButton volumeDownBtn, volumeUpBtn;

    private BitmapFont labelFont, btnFont;
    private Sound clickSound;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout glyphLayout;

    // Layout positions
    private float containerX, containerY;
    private float musicLabelX, musicLabelY;
    private float toggleBtnX, toggleBtnY;
    private float volumeLabelX, volumeLabelY;
    private float volDownX, volUpX, volY;

    public settingScreen(final MainGame game) {
        this(game, null);
    }

    public settingScreen(final MainGame game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        setupStage();
        loadAssets();
        calculateLayout();
        createUIComponents();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        glyphLayout = new GlyphLayout();
    }

    private void loadAssets() {
        if (previousScreen instanceof PauseScreen) {
            blurTexture = new Texture(Gdx.files.internal("GameScreen/blur.png"));
        } else {
            blurTexture = new Texture(Gdx.files.internal("blur.png"));
        }
        buttonOnTexture = new Texture(Gdx.files.internal("Settings/4.png"));
        buttonOffTexture = new Texture(Gdx.files.internal("Settings/6.png"));
        backBtnTexture = new Texture(Gdx.files.internal("Settings/2.png"));
        btnBgTexture = new Texture(Gdx.files.internal("wood_block.png"));

        labelFont = new BitmapFont();
        labelFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        labelFont.getData().setScale(1.8f);

        btnFont = new BitmapFont();
        btnFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        btnFont.getData().setScale(2.0f);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
    }

    private void calculateLayout() {
        containerX = (stage.getWidth() - 460f) / 2f;
        containerY = (stage.getHeight() - 280f) / 2f;

        musicLabelX = containerX + 40f;
        musicLabelY = containerY + 175f;

        toggleBtnX = containerX + 290f;
        toggleBtnY = containerY + 150f;

        volumeLabelX = containerX + 40f;
        volumeLabelY = containerY + 95f;

        volDownX = containerX + 270f;
        volUpX = containerX + 380f;
        volY = containerY + 70f;
    }

    private void createUIComponents() {
        // Toggle button (On/Off depending on game mute state)
        Texture activeToggleTex = game.isMusicMuted() ? buttonOffTexture : buttonOnTexture;
        toggleBtn = new ImageButton(new TextureRegionDrawable(activeToggleTex));
        toggleBtn.setSize(120f, 46f);
        toggleBtn.setPosition(toggleBtnX, toggleBtnY);
        stage.addActor(toggleBtn);
        addHoverEffect(toggleBtn, 120f, 46f);

        // Volume adjustment buttons
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = btnFont;
        btnStyle.fontColor = new Color(0.2f, 0.1f, 0.05f, 1.0f);
        btnStyle.up = new TextureRegionDrawable(btnBgTexture);

        volumeDownBtn = new TextButton("-", btnStyle);
        volumeDownBtn.setSize(45f, 45f);
        volumeDownBtn.setPosition(volDownX, volY);
        stage.addActor(volumeDownBtn);
        addHoverEffect(volumeDownBtn, 45f, 45f);

        volumeUpBtn = new TextButton("+", btnStyle);
        volumeUpBtn.setSize(45f, 45f);
        volumeUpBtn.setPosition(volUpX, volY);
        stage.addActor(volumeUpBtn);
        addHoverEffect(volumeUpBtn, 45f, 45f);

        // Back button (red square with X)
        backBtn = new ImageButton(new TextureRegionDrawable(backBtnTexture));
        backBtn.setSize(45f, 45f);
        backBtn.setPosition(containerX + 385f, containerY + 215f);
        stage.addActor(backBtn);
        addHoverEffect(backBtn, 45f, 45f);
    }

    private void addHoverEffect(final Actor actor, final float originalWidth, final float originalHeight) {
        actor.setOrigin(originalWidth / 2f, originalHeight / 2f);
        actor.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                actor.clearActions();
                actor.addAction(Actions.parallel(
                    Actions.scaleTo(1.1f, 1.1f, 0.1f),
                    Actions.rotateTo(4f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                actor.clearActions();
                actor.addAction(Actions.parallel(
                    Actions.scaleTo(1.0f, 1.0f, 0.1f),
                    Actions.rotateTo(0f, 0.1f)
                ));
            }
        });
    }

    private void addListeners() {
        toggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(clickSound);
                boolean isMuted = !game.isMusicMuted();
                game.setMusicMuted(isMuted);
                Texture activeToggleTex = isMuted ? buttonOffTexture : buttonOnTexture;
                toggleBtn.setStyle(new ImageButton.ImageButtonStyle(null, null, null,
                    new TextureRegionDrawable(activeToggleTex), null, null));
            }
        });

        volumeDownBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(clickSound);
                float vol = Math.max(0.0f, game.getMusicVolume() - 0.2f);
                game.setMusicVolume(vol);
                if (vol > 0.0f && game.isMusicMuted()) {
                    game.setMusicMuted(false);
                    toggleBtn.setStyle(new ImageButton.ImageButtonStyle(null, null, null,
                        new TextureRegionDrawable(buttonOnTexture), null, null));
                }
            }
        });

        volumeUpBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(clickSound);
                float vol = Math.min(1.0f, game.getMusicVolume() + 0.2f);
                game.setMusicVolume(vol);
                if (vol > 0.0f && game.isMusicMuted()) {
                    game.setMusicMuted(false);
                    toggleBtn.setStyle(new ImageButton.ImageButtonStyle(null, null, null,
                        new TextureRegionDrawable(buttonOnTexture), null, null));
                }
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(clickSound);
                if (previousScreen != null) {
                    game.setScreen(previousScreen);
                } else {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. Draw blur background manually
        stage.getBatch().begin();
        stage.getBatch().draw(blurTexture, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();

        // 2. Draw woody container frame using ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        
        // Outer border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.35f, 0.18f, 0.08f, 1.0f)); // Thick brown border
        shapeRenderer.rect(containerX - 6f, containerY - 6f, 460f + 12f, 280f + 12f);
        
        // Inner body
        shapeRenderer.setColor(new Color(0.15f, 0.08f, 0.04f, 0.85f)); // Dark transparent brown body
        shapeRenderer.rect(containerX, containerY, 460f, 280f);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 3. Act and draw UI buttons / controls on stage
        stage.act(delta);
        stage.draw();

        // 4. Draw texts on top of box
        stage.getBatch().begin();
        // Title
        labelFont.getData().setScale(2.2f);
        labelFont.setColor(new Color(0.9f, 0.7f, 0.2f, 1.0f)); // Gold title color
        glyphLayout.setText(labelFont, "SETTINGS");
        labelFont.draw(stage.getBatch(), "SETTINGS", containerX + (460f - glyphLayout.width) / 2f, containerY + 245f);

        // Option Labels
        labelFont.getData().setScale(1.8f);
        labelFont.setColor(new Color(0.95f, 0.90f, 0.85f, 1.0f)); // High contrast cream labels
        labelFont.draw(stage.getBatch(), "MUSIC TRACK", musicLabelX, musicLabelY);
        labelFont.draw(stage.getBatch(), "VOLUME LEVEL", volumeLabelX, volumeLabelY);
        stage.getBatch().end();

        // 5. Draw Volume segments
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 5; i++) {
            float stepVolume = (i + 1) * 0.2f;
            if (!game.isMusicMuted() && game.getMusicVolume() >= stepVolume - 0.01f) {
                shapeRenderer.setColor(new Color(0.3f, 0.7f, 0.2f, 1.0f)); // Bright green
            } else {
                shapeRenderer.setColor(new Color(0.3f, 0.2f, 0.12f, 1.0f)); // Subtle dark brown segment background
            }
            shapeRenderer.rect(volDownX + 53f + i * 13f, volY + 12f, 8f, 20f);
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        blurTexture.dispose();
        buttonOnTexture.dispose();
        buttonOffTexture.dispose();
        backBtnTexture.dispose();
        btnBgTexture.dispose();
        labelFont.dispose();
        btnFont.dispose();
        clickSound.dispose();
    }
}
