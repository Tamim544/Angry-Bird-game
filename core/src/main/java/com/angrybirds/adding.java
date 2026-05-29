//package com.angrybirds;
//
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
//public class adding implements Screen {
//    private Stage stage;
//    private MainGame game;
//    private Texture d_map;
//    private Texture f_map;
//    private Texture s_map;
//    private Texture blur;
//    private Texture back;
//    private Image d_map_i;
//    private Image s_map_i;
//    private Image blur_i;
//    private Sound clickSound;
//
//    public adding(final MainGame game) {
//        this.game = game;
//        this.stage = new Stage(new ScreenViewport());
//        this.clickSound = Gdx.audio.newSound(Gdx.files.internal("s.mp3"));
//        this.d_map = new Texture(Gdx.files.internal("Custom/3.png"));
//        this.f_map = new Texture(Gdx.files.internal("Custom/2.png"));
//        this.s_map = new Texture(Gdx.files.internal("Custom/4.png"));
//        this.blur = new Texture(Gdx.files.internal("Custom/1.png"));
//        this.back = new Texture(Gdx.files.internal("c6.png"));
//        ImageButton f_Button = new ImageButton(new TextureRegionDrawable(this.f_map));
//        ImageButton back_button = new ImageButton(new TextureRegionDrawable(this.back));
//        f_Button.setPosition(-30.0F, -90.0F);
//        f_Button.setSize(320.0F, 600.0F);
//        back_button.setPosition(20.0F, 380.0F);
//        back_button.setSize(40.0F, 120.0F);
//        this.blur_i = new Image(this.blur);
//        this.blur_i.setSize(this.stage.getWidth(), this.stage.getHeight());
//        this.blur_i.setPosition(0.0F, 0.0F);
//        this.stage.addActor(this.blur_i);
//        this.s_map_i = new Image(this.s_map);
//        this.s_map_i.setPosition(420.0F, 54.0F);
//        this.s_map_i.setSize(189.0F, 311.0F);
//        this.stage.addActor(this.s_map_i);
//        this.d_map_i = new Image(this.d_map);
//        this.d_map_i.setPosition(228.0F, 54.0F);
//        this.d_map_i.setSize(189.0F, 311.0F);
//        this.stage.addActor(this.d_map_i);
//        f_Button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                adding.this.clickSound.play();
//            }
//        });
//        back_button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                adding.this.clickSound.play();
//                game.setScreen(new MainMenuScreen(game));
//            }
//        });
//        this.stage.addActor(f_Button);
//        this.stage.addActor(back_button);
//        this.addHoverEffect1(f_Button, 320.0F, 600.0F);
//        this.addHoverEffect(this.s_map_i, 189.0F, 311.0F);
//        this.addHoverEffect(this.d_map_i, 189.0F, 311.0F);
//        Gdx.input.setInputProcessor(this.stage);
//    }
//    private void addHoverEffect1(final ImageButton button, final float originalWidth, final float originalHeight) {
//        button.addListener(new InputListener() {
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                button.addAction(Actions.parallel(
//                    Actions.sizeTo(originalWidth + 20.0F, originalHeight + 20.0F, 0.1F),  // Smooth size change
//                    Actions.moveBy(-10.0F, -10.0F, 0.1F),  // Smooth position change
//                    Actions.rotateBy(10.0F, 0.1F),  // Slight rotation
//                    Actions.fadeIn(0.1F)  // Fade in effect
//                ));
//            }
//
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                button.addAction(Actions.parallel(
//                    Actions.sizeTo(originalWidth, originalHeight, 0.1F),  // Reset size smoothly
//                    Actions.moveBy(10.0F, 10.0F, 0.1F),  // Reset position smoothly
//                    Actions.rotateBy(-10.0F, 0.1F),  // Reset rotation
//                    Actions.fadeOut(0.1F)  // Fade out effect
//                ));
//            }
//        });
//    }
//
//    // Modified hover effect with animation for images
//    private void addHoverEffect(final Image image, final float originalWidth, final float originalHeight) {
//        image.addListener(new InputListener() {
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                image.addAction(Actions.parallel(
//                    Actions.sizeTo(originalWidth + 20.0F, originalHeight + 20.0F, 0.1F),  // Smooth size change
//                    Actions.moveBy(-10.0F, -10.0F, 0.1F),  // Smooth position change
//                    Actions.rotateBy(10.0F, 0.1F),  // Rotate slightly
//                    Actions.fadeIn(0.1F)  // Fade in effect
//                ));
//            }
//
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                image.addAction(Actions.parallel(
//                    Actions.sizeTo(originalWidth, originalHeight, 0.1F),  // Reset size smoothly
//                    Actions.moveBy(10.0F, 10.0F, 0.1F),  // Reset position smoothly
//                    Actions.rotateBy(-10.0F, 0.1F),  // Reset rotation
//                    Actions.fadeOut(0.1F)  // Fade out effect
//                ));
//            }
//        });
//    }
//
//    public void show() {
//    }
//
//    public void render(float delta) {
//        Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
//        Gdx.gl.glClear(16384);
//        this.stage.act(delta);
//        this.stage.draw();
//    }
//
//    public void resize(int width, int height) {
//        this.stage.getViewport().update(width, height, true);
//        this.blur_i.setSize(this.stage.getWidth(), this.stage.getHeight());
//    }
//
//    public void pause() {
//    }
//
//    public void resume() {
//    }
//
//    public void hide() {
//    }
//
//    public void dispose() {
//        this.stage.dispose();
//        if (this.d_map != null) {
//            this.d_map.dispose();
//        }
//
//        if (this.f_map != null) {
//            this.f_map.dispose();
//        }
//
//        if (this.s_map != null) {
//            this.s_map.dispose();
//        }
//
//        if (this.blur != null) {
//            this.blur.dispose();
//        }
//
//        if (this.back != null) {
//            this.back.dispose();
//        }
//
//        this.clickSound.dispose();
//    }
//}


package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class adding implements Screen {
    private Stage stage;
    private MainGame game;
    private Texture desertMapTexture, forestMapTexture, snowMapTexture, backgroundBlur, backButtonTexture;
    private Image desertMap, snowMap, blurBackground;
    private ImageButton forestButton;
    private Sound buttonSound;

    public adding(final MainGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        // Initialize assets
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
        desertMapTexture = new Texture(Gdx.files.internal("Map/3.png"));
        forestMapTexture = new Texture(Gdx.files.internal("Map/2.png"));
        snowMapTexture = new Texture(Gdx.files.internal("Map/4.png"));
        backgroundBlur = new Texture(Gdx.files.internal("Map/1.png"));
        backButtonTexture = new Texture(Gdx.files.internal("c6.png"));

        // Set up stage and UI elements
        setupBackground();
        setupMapImages();
        setupButtons();
        setupHoverEffects();

        // Set input processor
        Gdx.input.setInputProcessor(this.stage);
    }

    private void setupBackground() {
        // Set up background with blur effect
        blurBackground = new Image(backgroundBlur);
        blurBackground.setSize(stage.getWidth(), stage.getHeight());
        blurBackground.setPosition(0, 0);
        stage.addActor(blurBackground);
    }

    private void setupMapImages() {
        // Add desert map image
        desertMap = new Image(desertMapTexture);
        desertMap.setPosition(228, 54);
        desertMap.setSize(189, 311);
        desertMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(buttonSound);
                game.setScreen(new loadpage(game, new LevelEndScreen(game)));
            }
        });
        stage.addActor(desertMap);

        // Add snow map image
        snowMap = new Image(snowMapTexture);
        snowMap.setPosition(420, 54);
        snowMap.setSize(189, 311);
        snowMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(buttonSound);
                game.setScreen(new loadpage(game, new LevelEndScreen(game)));
            }
        });
        stage.addActor(snowMap);
    }

    private void setupButtons() {
        // Set up forest map button
        forestButton = new ImageButton(new TextureRegionDrawable(forestMapTexture));
        forestButton.setPosition(-30, -90);
        forestButton.setSize(320, 600);
        forestButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(buttonSound);
                game.setScreen(new loadpage(game, new LevelEndScreen(game)));
            }
        });
        stage.addActor(forestButton);

        // Set up back button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(20, 380);
        backButton.setSize(40, 120);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(buttonSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);
    }
    private void setupHoverEffects() {
        addFocusHoverEffect(forestButton, 320f, 600f);
        addFocusHoverEffect(desertMap, 189f, 311f);
        addFocusHoverEffect(snowMap, 189f, 311f);
    }

    private void addFocusHoverEffect(final Actor targetActor, final float originalWidth, final float originalHeight) {
        targetActor.setOrigin(originalWidth / 2f, originalHeight / 2f);
        targetActor.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                targetActor.clearActions();
                targetActor.addAction(Actions.parallel(
                    Actions.scaleTo(1.1f, 1.1f, 0.15f),
                    Actions.alpha(1.0f, 0.15f),
                    Actions.rotateTo(3f, 0.15f)
                ));
                dimOtherMaps(targetActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                restoreAllMaps();
            }
        });
    }

    private void dimOtherMaps(Actor activeMap) {
        Actor[] maps = { forestButton, desertMap, snowMap };
        for (Actor map : maps) {
            if (map != activeMap && map != null) {
                map.clearActions();
                map.addAction(Actions.alpha(0.4f, 0.15f));
            }
        }
    }

    private void restoreAllMaps() {
        Actor[] maps = { forestButton, desertMap, snowMap };
        for (Actor map : maps) {
            if (map != null) {
                map.clearActions();
                map.addAction(Actions.parallel(
                    Actions.scaleTo(1.0f, 1.0f, 0.15f),
                    Actions.alpha(1.0f, 0.15f),
                    Actions.rotateTo(0f, 0.15f)
                ));
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Set background color
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(16384);

        // Act and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        blurBackground.setSize(stage.getWidth(), stage.getHeight());
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // Dispose of resources
        stage.dispose();
        if (desertMapTexture != null) desertMapTexture.dispose();
        if (forestMapTexture != null) forestMapTexture.dispose();
        if (snowMapTexture != null) snowMapTexture.dispose();
        if (backgroundBlur != null) backgroundBlur.dispose();
        if (backButtonTexture != null) backButtonTexture.dispose();
        buttonSound.dispose();
    }
}
