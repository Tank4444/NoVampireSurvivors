package ru.chuikov.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.MyGdxGame;
import ru.chuikov.app.components.BackgroundView;
import ru.chuikov.app.components.ButtonView;
import ru.chuikov.app.components.ImageView;
import ru.chuikov.app.components.TextView;
import ru.chuikov.app.managers.MemoryManager;

public class SettingsScreen extends ScreenAdapter {
    private MyGdxGame myGdxGame;
    TextView titleTextView;
    BackgroundView backgroundImageView;
    ImageView darkPanelImageView;
    ButtonView returnButton;
    ButtonView musicSettingView;
    ButtonView soundSettingView;

    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundImageView = new BackgroundView(GameResources.BACKGROUND_IMG_PATH);
        darkPanelImageView = new ImageView(140, 400,
                GameSettings.SCREEN_WIDTH - 280,
                GameSettings.SCREEN_HEIGHT - 800,
                GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        titleTextView = new TextView(myGdxGame.largeWhiteFont, 260,
                GameSettings.SCREEN_HEIGHT - 500,
                GameSettings.DEFAULT_MAIN_TEXT_SETTINGS
                );

        musicSettingView = new ButtonView(280, GameSettings.SCREEN_HEIGHT - 600,
                160, 70, myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "music: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );
        soundSettingView = new ButtonView(280, GameSettings.SCREEN_HEIGHT - 700,
                160, 70, myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "sound: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );

        returnButton = new ButtonView(280, GameSettings.SCREEN_HEIGHT - 800,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH
                , "Back"
        );

    }

    @Override
    public void render(float delta) {
        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundImageView.draw(myGdxGame.batch);
        darkPanelImageView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);


        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                myGdxGame.audioManager.updateSoundFlag();
            }
        }
    }

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }

}
