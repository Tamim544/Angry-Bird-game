
package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelEndScreen implements Screen {

    private Stage displayStage;
    private MainGame mainGame;
    private Texture bgOverlayTexture, levelOneBtnTexture, levelTwoBtnTexture, returnBtnTexture;
    private Image bgOverlayImage;
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
        bgOverlayTexture = new Texture(Gdx.files.internal("c5.png"));
        levelOneBtnTexture = new Texture(Gdx.files.internal("c7.png"));
        levelTwoBtnTexture = new Texture(Gdx.files.internal("c8.png"));
        returnBtnTexture = new Texture(Gdx.files.internal("c6.png"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
    }

    private void createUIComponents() {
        // Add background overlay with fade-in animation
        bgOverlayImage = new Image(bgOverlayTexture);
        bgOverlayImage.setSize(displayStage.getWidth(), displayStage.getHeight());
        bgOverlayImage.addAction(Actions.fadeIn(0.5f));
        displayStage.addActor(bgOverlayImage);

        // Create buttons with adjusted positions
        ImageButton levelOneButton = createButton(levelOneBtnTexture, 90f, 200f, 280f, 380f);
        ImageButton levelTwoButton = createButton(levelTwoBtnTexture, 320f, 200f, 290f, 380f);
        ImageButton returnButton = createButton(returnBtnTexture, 570f, 170f, 70f, 60f);

        // Apply fade-in animation to buttons
        levelOneButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        levelTwoButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        returnButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));

        // Add buttons to the stage
        displayStage.addActor(levelOneButton);
        displayStage.addActor(levelTwoButton);
        displayStage.addActor(returnButton);
    }

    private ImageButton createButton(Texture texture, float posX, float posY, float width, float height) {
        ImageButton btn = new ImageButton(new TextureRegionDrawable(texture));
        btn.setPosition(posX, posY);
        btn.setSize(width, height);
        return btn;
    }

    private void addUIListeners() {
        // Retrieve buttons from the stage actors list by index
        ImageButton levelOneButton = (ImageButton) displayStage.getActors().get(1);
        ImageButton levelTwoButton = (ImageButton) displayStage.getActors().get(2);
        ImageButton returnButton = (ImageButton) displayStage.getActors().get(3);

        // Add listener to level one button
        levelOneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                mainGame.setScreen(new loadpage(mainGame, new GameScreen(mainGame, 1)));
            }
        });

        // Add listener to level two button
        levelTwoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
                mainGame.setScreen(new loadpage(mainGame, new GameScreen(mainGame, 2)));
                // Additional actions can be added here if needed
            }
        });

        // Add listener to return button
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClickSound.play();
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
        levelOneBtnTexture.dispose();
        levelTwoBtnTexture.dispose();
        returnBtnTexture.dispose();
        buttonClickSound.dispose();
    }
}
