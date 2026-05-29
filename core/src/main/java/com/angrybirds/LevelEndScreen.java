package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelEndScreen implements Screen {

    private Stage displayStage;
    private MainGame mainGame;
    private Texture bgOverlayTexture, returnBtnTexture, titleTexture, buttonTexture;
    private BitmapFont buttonFont;
    private Image bgOverlayImage, titleImage;
    private ImageButton returnButton;
    private TextButton[] levelButtons;
    private Sound buttonClickSound;

    public LevelEndScreen(final MainGame mainGame) {
        this.mainGame = mainGame;
        setupStage();
        loadAssets();
        createUIComponents();
        addUIListeners();
        Gdx.input.setInputProcessor(displayStage);
    }

    private void setupStage() {
        displayStage = new Stage(new ScreenViewport());
    }

    private void loadAssets() {
        bgOverlayTexture = new Texture(Gdx.files.internal("blur.png")); // Blurred background
        returnBtnTexture = new Texture(Gdx.files.internal("c6.png"));
        titleTexture = new Texture(Gdx.files.internal("selectlevel.png"));
        buttonTexture = new Texture(Gdx.files.internal("wood_block.png")); // Wood block button asset
        buttonFont = new BitmapFont();
        buttonFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        buttonFont.getData().setScale(3.0f); // Large readable numbers
        
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
    }

    private void createUIComponents() {
        // Add background overlay with fade-in animation
        bgOverlayImage = new Image(bgOverlayTexture);
        bgOverlayImage.setSize(displayStage.getWidth(), displayStage.getHeight());
        bgOverlayImage.addAction(Actions.fadeIn(0.5f));
        displayStage.addActor(bgOverlayImage);

        // Add title image "Select Level"
        titleImage = new Image(titleTexture);
        float titleWidth = 360f;
        float titleHeight = 160f;
        titleImage.setSize(titleWidth, titleHeight);
        titleImage.setPosition((displayStage.getWidth() - titleWidth) / 2f, 500f);
        titleImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.8f)));
        displayStage.addActor(titleImage);

        // Define wood text button style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = buttonFont;
        btnStyle.fontColor = new Color(0.2f, 0.1f, 0.05f, 1.0f); // Dark woody-carved brown color
        btnStyle.up = new TextureRegionDrawable(buttonTexture);

        // Create level buttons
        levelButtons = new TextButton[5];
        float btnWidth = 130f;
        float btnHeight = 130f;
        float spacing = 45f;
        float totalWidth = 5 * btnWidth + 4 * spacing;
        float startX = (displayStage.getWidth() - totalWidth) / 2f;
        float startY = 260f;

        for (int i = 0; i < 5; i++) {
            final int levelNum = i + 1;
            levelButtons[i] = new TextButton(String.valueOf(levelNum), btnStyle);
            levelButtons[i].setPosition(startX + i * (btnWidth + spacing), startY);
            levelButtons[i].setSize(btnWidth, btnHeight);
            levelButtons[i].addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f + i * 0.1f)));
            displayStage.addActor(levelButtons[i]);
            addHoverEffect(levelButtons[i], btnWidth, btnHeight);
        }

        // Return button
        returnButton = createButton(returnBtnTexture, 50f, 50f, 85f, 85f);
        returnButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        displayStage.addActor(returnButton);
        addHoverEffect(returnButton, 85f, 85f);
    }

    private ImageButton createButton(Texture texture, float posX, float posY, float width, float height) {
        ImageButton btn = new ImageButton(new TextureRegionDrawable(texture));
        btn.setPosition(posX, posY);
        btn.setSize(width, height);
        return btn;
    }

    private void addHoverEffect(final Actor button, final float originalWidth, final float originalHeight) {
        button.setOrigin(originalWidth / 2f, originalHeight / 2f);
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.clearActions();
                button.addAction(Actions.parallel(
                    Actions.scaleTo(1.1f, 1.1f, 0.1f),
                    Actions.rotateTo(4f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.clearActions();
                button.addAction(Actions.parallel(
                    Actions.scaleTo(1.0f, 1.0f, 0.1f),
                    Actions.rotateTo(0f, 0.1f)
                ));
            }
        });
    }

    private void addUIListeners() {
        // Add listeners to level buttons
        for (int i = 0; i < 5; i++) {
            final int levelNum = i + 1;
            levelButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mainGame.playSound(buttonClickSound);
                    mainGame.setScreen(new loadpage(mainGame, new GameScreen(mainGame, levelNum)));
                }
            });
        }

        // Add listener to return button
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainGame.playSound(buttonClickSound);
                mainGame.setScreen(new loadpage(mainGame, new Roadmap(mainGame)));
            }
        });
    }

    @Override
    public void show() {
        mainGame.getPrimaryMusic().pause();
        mainGame.getSecondaryMusic().setLooping(true);
        mainGame.getSecondaryMusic().play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        displayStage.act(delta);
        displayStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        displayStage.getViewport().update(width, height, true);
        bgOverlayImage.setSize(displayStage.getWidth(), displayStage.getHeight());
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        displayStage.dispose();
        bgOverlayTexture.dispose();
        titleTexture.dispose();
        buttonTexture.dispose();
        buttonFont.dispose();
        returnBtnTexture.dispose();
        buttonClickSound.dispose();
    }
}
