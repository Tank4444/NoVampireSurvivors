package ru.chuikov.app.screens;


import static ru.chuikov.app.utils.MouseUtils.getScreenCoordinateToWorldCoordinate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSession;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.GameState;
import ru.chuikov.app.MyGdxGame;
import ru.chuikov.app.components.BackgroundView;
import ru.chuikov.app.components.ButtonView;
import ru.chuikov.app.components.HealthBar;
import ru.chuikov.app.components.ImageView;
import ru.chuikov.app.components.TextView;
import ru.chuikov.app.managers.ContactManager;
import ru.chuikov.app.managers.MemoryManager;
import ru.chuikov.app.objects.Enemy;
import ru.chuikov.app.objects.Player;
import ru.chuikov.app.objects.Projectile;
import ru.chuikov.app.utils.Joystick;
import ru.chuikov.app.utils.Utils;

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

    TextView scoreTextView;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;
    ButtonView restartButton;

    // ENDED state UI
    TextView endGameTextView;
    ButtonView endGameHomeButton;
    ButtonView endGameRestartButton;


    //Game logic
    ArrayList<Enemy> enemies;
    ArrayList<Projectile> projectiles;
    Random random;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        random = new Random();
        enemies = new ArrayList<Enemy>();
        projectiles = new ArrayList<Projectile>();
        contactManager = new ContactManager(myGdxGame.world);
        backgroundView = new BackgroundView(GameResources.BACKGROUND_IMG_PATH);
        player = new Player(
                GameSettings.SCREEN_WIDTH / 2, GameSettings.SCREEN_HEIGHT / 2,
                myGdxGame.world
        );
        shapeRenderer = new ShapeRenderer();
        joystick = new Joystick(GameSettings.SCREEN_WIDTH / 2, 300, 200);

        for (int i = 0; i < 3; i++) {
            int angle = Utils.getRandomAngle();
            enemies.add(Enemy.enemyRandom(myGdxGame.world));
        }

        healthBar = new HealthBar(0, GameSettings.SCREEN_HEIGHT - 50,
                GameSettings.SCREEN_WIDTH,
                50
        );

        pauseButton = new ButtonView(
                5, GameSettings.SCREEN_HEIGHT - 110,
                48, 48,
                GameResources.PAUSE_IMG_PATH
        );

        scoreTextView = new TextView(myGdxGame.commonWhiteFont,
                GameSettings.SCREEN_WIDTH - 100, GameSettings.SCREEN_HEIGHT - 100);
        //Init pause
        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);

        pauseTextView = new TextView(myGdxGame.largeWhiteFont,
                (GameSettings.SCREEN_WIDTH / 2.5f), GameSettings.SCREEN_HEIGHT - 400,
                "Pause");
        continueButton = new ButtonView(
                (GameSettings.SCREEN_WIDTH / 2.5f), GameSettings.SCREEN_HEIGHT - 500,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );
        restartButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2.5f, GameSettings.SCREEN_HEIGHT - 600,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Restart"
        );
        homeButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2.5f, GameSettings.SCREEN_HEIGHT - 700,
                150, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        //Init end
        endGameTextView = new TextView(myGdxGame.largeWhiteFont,
                (GameSettings.SCREEN_WIDTH / 2.5f), GameSettings.SCREEN_HEIGHT - 400,
                GameSettings.DEFAULT_ENDGAME_TEXT
        );
        endGameHomeButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2.5f, GameSettings.SCREEN_HEIGHT - 600,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        endGameRestartButton = new ButtonView(
                GameSettings.SCREEN_WIDTH / 2.5f, GameSettings.SCREEN_HEIGHT - 700,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Restart"
        );


    }

    @Override
    public void show() {
        restartGame();
    }

    private void restartGame() {
        player.setX(GameSettings.SCREEN_WIDTH / 2);
        player.setY(GameSettings.SCREEN_HEIGHT / 2);
        player.health = player.max_health;
        for (int i = 0; i < enemies.size(); i++) {
            myGdxGame.world.destroyBody(enemies.get(i).body);
            enemies.remove(i--);
        }
        enemies = new ArrayList<Enemy>();
        for (int i = 0; i < projectiles.size(); i++) {
            myGdxGame.world.destroyBody(projectiles.get(i).body);
            projectiles.remove(i--);
        }
        endGameTextView.setText(GameSettings.DEFAULT_ENDGAME_TEXT);
        myGdxGame.world.destroyBody(player.body);
        player = new Player(GameSettings.SCREEN_WIDTH / 2, GameSettings.SCREEN_HEIGHT / 2, myGdxGame.world);
        gameSession.startGame();
    }

    @Override
    public void render(float delta) {
        //Update all data

        if (gameSession.state == GameState.PLAYING) {
            //User position
            if (joystick.getHorizontalInputInt()==0 &&
                joystick.getVerticalInputInt()==0
            )
            {
                player.isMoving = false;

            }else {
                player.isMoving = true;
                player.move(
                        joystick.getHorizontalInputInt(),
                        joystick.getVerticalInputInt()
                );
            }

            //Update enemy info
            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isAlive) {
                    myGdxGame.world.destroyBody(enemies.get(i).body);
                    enemies.remove(enemies.get(i));
                    gameSession.updateScore();
                    gameSession.destructionRegistration();
                    if (myGdxGame.audioManager.isSoundOn)
                        myGdxGame.audioManager.explosionSound.play(0.2f);
                } else
                    enemies.get(i).moveToPlayer(player.getX(), player.getY());
            }
            //Update projectile info
            for (int i = 0; i < projectiles.size(); i++) {
                if (!projectiles.get(i).isAlive) {
                    myGdxGame.world.destroyBody(projectiles.get(i).body);
                    projectiles.remove(projectiles.get(i));
                } else
                    projectiles.get(i).update();
            }

            Enemy nearestEnemy = Utils.findNearestEnemy(player, enemies);
            if (nearestEnemy != null) {
                if (Utils.distance(player.getX(), player.getY(), nearestEnemy.getX(), nearestEnemy.getY()) < GameSettings.PLAYER_WIDTH) {
                    nearestEnemy.hit(player);
                    player.hit(nearestEnemy);
                }
                if (player.readyToFire()) {
                    projectiles.add(new Projectile(
                            player.getX() - 30,
                            player.getY(),
                            10, nearestEnemy,
                            myGdxGame.world
                    ));
                    if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
                }
            }
            ;

            if (gameSession.shouldSpawnEnemy()) enemies.add(Enemy.enemyRandom(myGdxGame.world));
            // Update joystick
            Vector2 mouseCoord = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 mouseToWorld = getScreenCoordinateToWorldCoordinate(myGdxGame.camera, mouseCoord);
            joystick.update(mouseToWorld);

            if (!player.isAlive) {
                gameSession.endGame();
                endGameTextView.setText(endGameTextView.getText() + gameSession.getScore());
            }
        }

        //launch
        healthBar.setHealth(player.health, player.max_health);

        handleInput();

        scoreTextView.setText("Счёт = " + gameSession.getScore());

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
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    if (restartButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        restartGame();
                    }
                    break;

                case ENDED:
                    if (endGameHomeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    if (endGameRestartButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        restartGame();
                    }
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
        pauseButton.draw(myGdxGame.batch);
        player.draw(myGdxGame.batch);
        for (Enemy e : enemies) e.draw(myGdxGame.batch);
        for (Projectile p : projectiles) p.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);


        switch (gameSession.state) {
            case PLAYING:
                break;
            case PAUSED:
                fullBlackoutView.draw(myGdxGame.batch);
                pauseTextView.draw(myGdxGame.batch);
                homeButton.draw(myGdxGame.batch);
                continueButton.draw(myGdxGame.batch);
                restartButton.draw(myGdxGame.batch);
                break;
            case ENDED:
                fullBlackoutView.draw(myGdxGame.batch);
                endGameTextView.draw(myGdxGame.batch);
                endGameHomeButton.draw(myGdxGame.batch);
                endGameRestartButton.draw(myGdxGame.batch);
                break;
        }
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
