package ru.chuikov.app.objects;

import static ru.chuikov.app.utils.Utils.getCoordX;
import static ru.chuikov.app.utils.Utils.getCoordY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.utils.Utils;

public class Enemy extends GameObject {

    int speed = 2;

    private final String TAG = "ENEMY";

    public boolean isAlive = true;

    public Enemy(int x, int y, World world) {
        super(GameResources.ZOMBIE_IMG_PATH, x, y, GameSettings.ZOMBIE_WIDTH,
                GameSettings.ZOMBIE_HEIGHT,
                GameSettings.CATEGORY_ENEMY,
                GameSettings.MASK_ENEMY,
                world);
    }

    public static Enemy enemyRandom(World world) {
        int angle = Utils.getRandomAngle();
        return new Enemy(
                (int) Utils.getXByAngle(GameSettings.SCREEN_WIDTH / 2,
                        Utils.randomInt(300, 600), angle),
                (int) Utils.getYByAngle(GameSettings.SCREEN_HEIGHT / 2,
                        Utils.randomInt(300, 700), angle),
                world
        );
    }

    @Override
    public void hit(GameObject object) {
        super.hit(object);
        if (object instanceof Player) {
            Gdx.app.log(TAG, "Detect hit with player");
            isAlive = false;
        } else if (object instanceof Projectile) {
            Gdx.app.log(TAG, "Detect hit with projectile");
            isAlive = false;
        }
    }

    public void moveToPlayer(int playerX, int playerY) {
        int dx = getCoordX(getX(), getY(), playerX, playerY, speed);
        int dy = getCoordY(getX(), getY(), playerX, playerY, speed);
        setX(dx);
        setY(dy);
    }


}
