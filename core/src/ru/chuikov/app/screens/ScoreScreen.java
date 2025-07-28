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

public class ScoreScreen extends ScreenAdapter {


    private MyGdxGame myGdxGame;
    private BackgroundView backgroundView;

    private ImageView blackpanelImageView;
    private TextView mainTextView;
    private TextView scoreTextView;
    private ButtonView clearButtonView;
    private ButtonView backButtonView;

    public ScoreScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        backgroundView = new BackgroundView(GameResources.BACKGROUND_IMG_PATH);
        blackpanelImageView = new ImageView(140, 200,
                GameSettings.SCREEN_WIDTH - 240,
                GameSettings.SCREEN_HEIGHT - 400,
                GameResources.BLACKOUT_MIDDLE_IMG_PATH
        );
        mainTextView = new TextView(myGdxGame.largeWhiteFont,
                245,
                GameSettings.SCREEN_HEIGHT - 300,
                "Best score"
        );
        String scores = "";
        if (MemoryManager.loadRecordsTable() == null) {
            MemoryManager.saveTableOfRecords(new ArrayList<Integer>());
        }
        for (int i : MemoryManager.loadRecordsTable()) scores += "\n" + i;
        scoreTextView = new TextView(myGdxGame.commonWhiteFont,
                295,
                GameSettings.SCREEN_HEIGHT - 400,
                scores
        );
        clearButtonView = new ButtonView(
                295,
                GameSettings.SCREEN_HEIGHT - 900,
                160, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH,
                "Clear"
        );
        backButtonView = new ButtonView(
                295,
                GameSettings.SCREEN_HEIGHT - 1000,
                160, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH,
                "Back"
        );
    }

    @Override
    public void render(float delta) {
        handleInput();
        String scores = "";
        for (int i : MemoryManager.loadRecordsTable()) scores += "\n" + i;
        scoreTextView.setText(scores);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        blackpanelImageView.draw(myGdxGame.batch);
        mainTextView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        clearButtonView.draw(myGdxGame.batch);
        backButtonView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (clearButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                scoreTextView.setText("");
            }
            if (backButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
        }
    }
}
