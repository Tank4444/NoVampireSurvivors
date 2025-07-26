package ru.chuikov.app.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSession;
import ru.chuikov.app.GameSettings;

public class Player extends GameObject {

    public int health = 100;
    public int max_health = 100;

    int speed = 10;
    private final String TAG= "PLAYER";
    public int countdown;
    public int real_countdown;

    public Player(int x, int y, World world) {
        super(GameResources.PLAYER_IMG_PATH,
                x,
                y,
                GameSettings.PLAYER_WIDTH,
                GameSettings.PLAYER_HEIGHT,
                GameSettings.CATEGORY_PLAYER,
                GameSettings.MASK_PLAYER,
                world);
        countdown = 50;
        real_countdown = countdown;

    }

    @Override
    public void hit(GameObject object) {
        super.hit(object);
        if (object instanceof Enemy) {
            Gdx.app.log(TAG, "Detect hit with Enemy");
            health -= 10;
        }
    }

    public boolean readyToFire(){
        if (real_countdown==0){
            real_countdown = countdown;
            return true;
        }else {
            real_countdown--;
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

    }

}
