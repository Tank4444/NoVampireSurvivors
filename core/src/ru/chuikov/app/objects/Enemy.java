package ru.chuikov.app.objects;

import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameResources;
import ru.chuikov.app.GameSettings;

public class Enemy extends GameObject {

    int speed = 2;

    public boolean isAlive = true;

    public Enemy(int x, int y, World world) {
        super(GameResources.ZOMBIE_IMG_PATH, x, y, GameSettings.ZOMBIE_WIDTH, GameSettings.ZOMBIE_HEIGHT, (short) 4, world);
    }

    @Override
    public void hit(GameObject object) {
        super.hit(object);
        if (object instanceof Player){
            System.out.println("Hit detected by enemy");
            isAlive = false;
        }
    }

    public void moveToPlayer(int playerX, int playerY) {
        double angle = Math.atan2(getY() - playerY, getX() - playerX);
        if (angle < 0) angle = angle + 360;

        double d = Math.sqrt(
                (
                        (playerX - getX()) * (playerX - getX())
                ) + (
                        (playerY - getY()) * (playerY - getY())
                )
        );

        int dx = (int) (getX() + (playerX - getX()) / d * speed);
        int dy = (int) (getY() + (playerY - getY()) / d * speed);
        setX(dx);
        setY(dy);
        //System.out.println("angle = " + angle + ", x = " + getX() + ", y = " + getY());
    }

    private double getAngle(int playerX, int playerY) {
        double cos = Math.round(
                (this.getX() * playerX + this.getY() * playerY) /
                        (Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY())
                                * Math.sqrt(playerX * playerX + playerY * playerY))
        );
        cos = Math.acos(cos);
        return (cos * 180) / Math.PI;

    }


}
