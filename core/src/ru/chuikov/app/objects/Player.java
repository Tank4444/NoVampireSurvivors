package ru.chuikov.app.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;

public class Player extends GameObject {
    public int max_health = GameSettings.PLAYER_DEFAULT_MAX_HEALTH;
    public int health = max_health;


    int speed = GameSettings.PLAYER_DEFAULT_SPEED;
    private final String TAG = "PLAYER";
    public int countdown = GameSettings.PLAYER_DEFAULT_COUNTDOWN;
    public int realCountdown;

    public boolean isAlive;

    public ArrayList<Texture> textures;

    public int textureNumber;

    public final int countNextTexture = GameSettings.PLAYER_DEFAULT_TEXTURE_CHANGE;

    public int realTimerTextureChange;

    public boolean isMoving;
    public Player(int x, int y, World world) {
        super(GameResources.PLAYER_IMG_PATH_0,
                x,
                y,
                GameSettings.PLAYER_WIDTH,
                GameSettings.PLAYER_HEIGHT,
                GameSettings.CATEGORY_PLAYER,
                GameSettings.MASK_PLAYER,
                world);
        textures = new ArrayList<Texture>();
        textures.add(this.texture);
        textures.add(new Texture(GameResources.PLAYER_IMG_PATH_1));
        textures.add(new Texture(GameResources.PLAYER_IMG_PATH_2));
        textures.add(new Texture(GameResources.PLAYER_IMG_PATH_3));
        this.isAlive = true;
        textureNumber = 0;
        realCountdown = countdown;
        realTimerTextureChange = countNextTexture;
        isMoving = false;

    }


    @Override
    public void hit(GameObject object) {
        super.hit(object);
        if (object instanceof Enemy) {
            Gdx.app.log(TAG, "Detect hit with Enemy");
            health -= 10;
            if (health <= 0) isAlive = false;
        }
    }

    public boolean readyToFire() {
        if (realCountdown <= 0) {
            realCountdown = countdown;
            return true;
        } else {
            realCountdown--;
            return false;
        }
    }

    public void move(int xInput, int yInput) {

        int x = this.getX() + (xInput * speed);
        int y = this.getY() + (yInput * speed);

        if (x <= 0) x = 0;
        else if (x >= GameSettings.SCREEN_WIDTH) x = GameSettings.SCREEN_WIDTH;

        if (y <= 0) y = 0;
        else if (y >= GameSettings.SCREEN_HEIGHT) y = GameSettings.SCREEN_HEIGHT;

        this.setX(x);
        this.setY(y);

        if (isMoving){
            realTimerTextureChange--;

            if (realTimerTextureChange<=0){
                textureNumber++;
                if (textureNumber >=textures.size()) textureNumber = 0;
                this.setTexture(textures.get(textureNumber));
                realTimerTextureChange = countNextTexture;
            }
        }




    }

}
