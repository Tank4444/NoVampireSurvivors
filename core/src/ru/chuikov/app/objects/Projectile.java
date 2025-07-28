package ru.chuikov.app.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;
import ru.chuikov.app.utils.Utils;

public class Projectile extends GameObject {

    int damage;

    Enemy target;

    public int speed = GameSettings.PROJECTILE_DEFAULT_SPEED;
    public boolean isAlive;
    private final String TAG = "PROJECTILE";

    private Texture explosion;
    private int explosion_width;
    private int explosion_height;

    public Projectile(int x, int y, int damage, Enemy target, World world) {
        super(GameResources.GRENADE_IMG_PATH, x, y,
                GameSettings.GRENADE_WIDTH, GameSettings.GRENADE_HEIGHT,
                GameSettings.CATEGORY_PROJECTILE,
                GameSettings.MASK_PROJECTILE,
                world);
        this.explosion = new Texture(GameResources.EXPLOSION_IMG_PATH);
        this.explosion_width = GameSettings.EXPLOSION_WIDTH;
        this.explosion_height = GameSettings.EXPLOSION_HEIGHT;
        this.damage = damage;
        this.target = target;
        this.isAlive = true;
        Gdx.app.log(TAG, "Created projectile to enemy x = " + target.getX() + " , y = " + target.getY());
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (isAlive) batch.draw(texture,
                getX() - (width / 2f),
                getY() - (height / 2f),
                width,
                height);
        else batch.draw(explosion,
                getX() - (width / 2f),
                getY() - (height / 2f),
                explosion_width,
                explosion_height);
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
        int x = Utils.getCoordX(getX(), getY(), target.getX(), target.getY(), speed);
        int y = Utils.getCoordY(getX(), getY(), target.getX(), target.getY(), speed);
        setX(x);
        setY(y);

        if (x < 0 || x > GameSettings.SCREEN_WIDTH) isAlive = false;
        else if (y < 0 || y > GameSettings.SCREEN_HEIGHT) isAlive = false;

        if (!target.isAlive) this.isAlive = false;


        if (Utils.distance(getX(), getY(), target.getX(), target.getY()) < (width * 2)) {
            target.hit(this);
            this.hit(target);
        }
    }
}
