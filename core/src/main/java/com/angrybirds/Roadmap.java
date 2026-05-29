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
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//
//public class Roadmap implements Screen {
//    private Stage stage;
//    private MainGame game;
//    private Texture d_map;
//    private Texture f_map;
//    private Texture s_map;
//    private Texture blur;
//    private Texture Select_map;
//    private Texture back;
//    private Image d_map_i;
//    private Image s_map_i;
//    private Image blur_i;
//    private Image Select_map_i;
//    private Image f_map_i;
//    private Image back_i;
//    private Sound clickSound;
//
//    public Roadmap(final MainGame game) {
//        this.game = game;
//        this.stage = new Stage(new ScreenViewport());
//        this.clickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
//        this.d_map = new Texture(Gdx.files.internal("Component 2.png"));
//        this.f_map = new Texture(Gdx.files.internal("Component 1.png"));
//        this.s_map = new Texture(Gdx.files.internal("Component 3.png"));
//        this.blur = new Texture(Gdx.files.internal("blur.png"));
//        this.Select_map = new Texture(Gdx.files.internal("sm.png"));
//        this.back = new Texture(Gdx.files.internal("c6.png"));
//        ImageButton f_Button = new ImageButton(new TextureRegionDrawable(this.f_map));
//        ImageButton back_button = new ImageButton(new TextureRegionDrawable(this.back));
//        f_Button.setPosition(02.0F, 65.0F);
//        f_Button.setSize(250.0F, 380.0F);
//        back_button.setPosition(20.0F, 380.0F);
//        back_button.setSize(40.0F, 120.0F);
//
//        this.blur_i = new Image(this.blur);
//        this.blur_i.setSize(this.stage.getWidth(), this.stage.getHeight());
//        this.blur_i.setPosition(0.0F, 0.0F);
//        this.stage.addActor(this.blur_i);
//        this.s_map_i = new Image(this.s_map);
//        this.s_map_i.setPosition(405.0F, 112.0F);
//        this.s_map_i.setSize(220.0F, 285.0F);
//        this.stage.addActor(this.s_map_i);
//        this.d_map_i = new Image(this.d_map);
//        this.d_map_i.setPosition(199.0F, 112.0F);
//        this.d_map_i.setSize(220.0F, 285.0F);
//        this.stage.addActor(this.d_map_i);
//        this.Select_map_i = new Image(this.Select_map);
//        this.Select_map_i.setPosition(224.0F, 405.0F);
//        this.Select_map_i.setSize(180.0F, 80.0F);
//        this.stage.addActor(this.Select_map_i);
//        f_Button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                Roadmap.this.clickSound.play();
//                game.setScreen(new loadpage(game, new LevelEndScreen(game)));
//            }
//        });
//        back_button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                Roadmap.this.clickSound.play();
//                game.setScreen(new loadpage(game, new MainMenuScreen(game)));
//            }
//        });
//        this.stage.addActor(f_Button);
//        this.stage.addActor(back_button);
//        this.addHoverEffect1(f_Button, 250.0F, 380.0F);
//        this.addHoverEffect(this.s_map_i, 220.0F, 285.0F);
//        this.addHoverEffect(this.d_map_i, 220.0F, 285.0F);
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
//        this.d_map.dispose();
//        this.f_map.dispose();
//        this.s_map.dispose();
//        this.blur.dispose();
//        this.Select_map.dispose();
//        this.back.dispose();
//        this.clickSound.dispose();
//    }
//}
//
//

package com.angrybirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Roadmap implements Screen {
    private Stage roadmapStage;
    private MainGame mainGame;
    private Texture dayMapTexture, forestMapTexture, seaMapTexture, blurTexture, selectMapTexture, backTexture;
    private Image dayMapImage, seaMapImage, blurImage, selectMapImage, forestMapImage;
    private Sound buttonClickSound;

    public Roadmap(final MainGame mainGame) {
        this.mainGame = mainGame;
        roadmapStage = new Stage(new ScreenViewport());
        loadAssets();
        createUIComponents();
        setupButtonListeners();
        setupHoverEffects();
        Gdx.input.setInputProcessor(roadmapStage);
    }

    private void loadAssets() {
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
        dayMapTexture = new Texture(Gdx.files.internal("level2.png"));
        forestMapTexture = new Texture(Gdx.files.internal("level1.png"));
        seaMapTexture = new Texture(Gdx.files.internal("level3.png"));
        blurTexture = new Texture(Gdx.files.internal("blur.png"));
        selectMapTexture = new Texture(Gdx.files.internal("selectlevel.png"));
        backTexture = new Texture(Gdx.files.internal("c6.png"));
    }

    private void createUIComponents() {
        // Create and position images
        blurImage = createImage(blurTexture, 0.0F, 0.0F, roadmapStage.getWidth(), roadmapStage.getHeight());
        seaMapImage = createImage(seaMapTexture, 405.0F, 112.0F, 220.0F, 285.0F);
        dayMapImage = createImage(dayMapTexture, 199.0F, 112.0F, 220.0F, 285.0F);
        selectMapImage = createImage(selectMapTexture, 224.0F, 405.0F, 180.0F, 80.0F);

        // Create buttons and position them
        ImageButton forestButton = createButton(forestMapTexture, 2.0F, 65.0F, 250.0F, 380.0F);
        ImageButton backButton = createButton(backTexture, 20.0F, 380.0F, 40.0F, 120.0F);

        // Add all components to the stage
        roadmapStage.addActor(blurImage);
        roadmapStage.addActor(seaMapImage);
        roadmapStage.addActor(dayMapImage);
        roadmapStage.addActor(selectMapImage);
        roadmapStage.addActor(forestButton);
        roadmapStage.addActor(backButton);
    }

    private Image createImage(Texture texture, float x, float y, float width, float height) {
        Image image = new Image(texture);
        image.setPosition(x, y);
        image.setSize(width, height);
        return image;
    }

    private ImageButton createButton(Texture texture, float x, float y, float width, float height) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.setPosition(x, y);
        button.setSize(width, height);
        return button;
    }

    private void setupButtonListeners() {
        roadmapStage.getActors().forEach(actor -> {
            if (actor instanceof ImageButton) {
                ImageButton button = (ImageButton) actor;
                if (button.getWidth() == 250.0F) {  // Forest button size check
                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            buttonClickSound.play();
                            mainGame.setScreen(new loadpage(mainGame, new GameScreen(mainGame, 1)));
                        }
                    });
                } else if (button.getWidth() == 40.0F) {  // Back button size check
                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            buttonClickSound.play();
                            mainGame.setScreen(new loadpage(mainGame, new MainMenuScreen(mainGame)));
                        }
                    });
                }
            }
        });
    }

    private void setupHoverEffects() {
        roadmapStage.getActors().forEach(actor -> {
            if (actor instanceof ImageButton && ((ImageButton) actor).getWidth() == 250.0F) {
                addHoverEffectToButton((ImageButton) actor, 250.0F, 380.0F);
            } else if (actor instanceof Image && actor.getWidth() == 220.0F) {  // Check size to target specific images
                addHoverEffectToImage((Image) actor, 220.0F, 285.0F);
            }
        });
    }

    private void addHoverEffectToButton(final ImageButton button, final float originalWidth, final float originalHeight) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                    Actions.sizeTo(originalWidth + 20.0F, originalHeight + 20.0F, 0.1F),
                    Actions.moveBy(-10.0F, -10.0F, 0.1F),
                    Actions.rotateBy(10.0F, 0.1F),
                    Actions.fadeIn(0.1F)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                    Actions.sizeTo(originalWidth, originalHeight, 0.1F),
                    Actions.moveBy(10.0F, 10.0F, 0.1F),
                    Actions.rotateBy(-10.0F, 0.1F),
                    Actions.fadeOut(0.1F)
                ));
            }
        });
    }

    private void addHoverEffectToImage(final Image image, final float originalWidth, final float originalHeight) {
        image.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                image.addAction(Actions.parallel(
                    Actions.sizeTo(originalWidth + 20.0F, originalHeight + 20.0F, 0.1F),
                    Actions.moveBy(-10.0F, -10.0F, 0.1F),
                    Actions.rotateBy(10.0F, 0.1F),
                    Actions.fadeIn(0.1F)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                image.addAction(Actions.parallel(
                    Actions.sizeTo(originalWidth, originalHeight, 0.1F),
                    Actions.moveBy(10.0F, 10.0F, 0.1F),
                    Actions.rotateBy(-10.0F, 0.1F),
                    Actions.fadeOut(0.1F)
                ));
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        Gdx.gl.glClear(16384);
        roadmapStage.act(delta);
        roadmapStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        roadmapStage.getViewport().update(width, height, true);
        blurImage.setSize(roadmapStage.getWidth(), roadmapStage.getHeight());
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        roadmapStage.dispose();
        dayMapTexture.dispose();
        forestMapTexture.dispose();
        seaMapTexture.dispose();
        blurTexture.dispose();
        selectMapTexture.dispose();
        backTexture.dispose();
        buttonClickSound.dispose();
    }
}
