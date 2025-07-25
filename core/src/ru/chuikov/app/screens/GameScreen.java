package ru.chuikov.app.screens;


import static ru.chuikov.app.utils.MouseUtils.getScreenCoordinateToWorldCoordinate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSession;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.MyGdxGame;
import ru.chuikov.app.components.BackgroundView;
import ru.chuikov.app.components.ButtonView;
import ru.chuikov.app.components.HealthBar;
import ru.chuikov.app.components.ImageView;
import ru.chuikov.app.components.LiveView;
import ru.chuikov.app.components.RecordsListView;
import ru.chuikov.app.components.TextView;
import ru.chuikov.app.managers.ContactManager;
import ru.chuikov.app.objects.Enemy;
import ru.chuikov.app.objects.Player;
import ru.chuikov.app.utils.Joystick;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    GameSession gameSession;
    ShapeRenderer shapeRenderer;
    ContactManager contactManager;

    // PLAY state UI
    BackgroundView backgroundView;
    ButtonView pauseButton;
    HealthBar healthBar;
    Player player;
    Joystick joystick;

    // PAUSED state UI


    // ENDED state UI





    //Game logic
    ArrayList<Enemy> enemies;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        enemies = new ArrayList<Enemy>();
        contactManager = new ContactManager(myGdxGame.world);
        backgroundView = new BackgroundView(GameResources.BACKGROUND_IMG_PATH);
        player = new Player(
                GameSettings.SCREEN_WIDTH / 2, GameSettings.SCREEN_HEIGHT / 2,
                myGdxGame.world
        );
        shapeRenderer = new ShapeRenderer();
        joystick = new Joystick(GameSettings.SCREEN_WIDTH / 2, 300, 200);

        enemies.add(new Enemy(1, 1, myGdxGame.world));

        healthBar = new HealthBar(0, GameSettings.SCREEN_HEIGHT - 50,
                GameSettings.SCREEN_WIDTH,
                50
        );

        pauseButton = new ButtonView(
                0, 0,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

    }

    @Override
    public void show() {
        restartGame();
    }
    private void restartGame() {


        gameSession.startGame();
    }
    @Override
    public void render(float delta) {
        //Update all data
        //User position
        player.move(
                joystick.getHorizontalInputInt(),
                joystick.getVerticalInputInt()
        );


        for (Enemy e : enemies) {
            if (!e.isAlive) {
                myGdxGame.world.destroyBody(e.body);
                enemies.remove(e);
            }
            e.moveToPlayer(player.getX(), player.getY());
        }

        healthBar.setHealth(player.health, player.max_health);

        // Update joystick
        Vector2 mouseCoord = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 mouseToWorld = getScreenCoordinateToWorldCoordinate(myGdxGame.camera, mouseCoord);
        joystick.update(mouseToWorld);


        handleInput();
        myGdxGame.stepWorld();


        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    //player.move(myGdxGame.touch);
                    break;

                case PAUSED:

                    break;

                case ENDED:

                    break;
            }

        }
    }

    private void draw() {
        //Draw all objects
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);
        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        player.draw(myGdxGame.batch);
        for (Enemy e : enemies) e.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // Draw joystick
        shapeRenderer.setProjectionMatrix(myGdxGame.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        joystick.draw(shapeRenderer);
        healthBar.draw(shapeRenderer);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


}
