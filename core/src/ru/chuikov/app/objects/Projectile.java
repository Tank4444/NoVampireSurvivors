package ru.chuikov.app.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.utils.Utils;

public class Projectile extends GameObject {

    int damage;

    Enemy target;

    int speed;
    public boolean isAlive;
    private final String TAG = "PROJECTILE";

    public Projectile(int x, int y, int damage, Enemy target, World world) {
        super(GameResources.LIVE_IMG_PATH, x, y,
                30, 30,
                GameSettings.CATEGORY_PROJECTILE,
                GameSettings.MASK_PROJECTILE,
                world);

        this.damage = damage;
        this.target = target;
        this.speed = 10;
        this.isAlive = true;
        Gdx.app.log(TAG, "Created projectile to enemy x = " + target.getX() + " , y = " + target.getY());
    }

    @Override
    public void hit(GameObject object) {
        super.hit(object);
        if (object instanceof Enemy) {
            Gdx.app.log(TAG, "Detect hit with Enemy");
            isAlive = false;
        }
    }

    public void update() {
        int x = Utils.getCoordX(getX(),getY(),target.getX(),target.getY(), speed);
        int y = Utils.getCoordY(getX(),getY(),target.getX(),target.getY(), speed);
        setX(x);
        setY(y);

        if (x < 0 || x > GameSettings.SCREEN_WIDTH) isAlive = false;
        else if (y < 0 || y > GameSettings.SCREEN_HEIGHT) isAlive = false;

        if (Utils.distance(getX(),getY(), target.getX(),target.getY())< (width*2)){
            target.hit(this);
            this.hit(target);
        }
    }
}
