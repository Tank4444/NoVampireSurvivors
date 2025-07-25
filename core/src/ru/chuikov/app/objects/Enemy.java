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
        if (object instanceof Player) {
            System.out.println("Hit detected by enemy");
            isAlive = false;
        }
    }

    public void moveToPlayer(int playerX, int playerY) {
        int dx = getCoordX(getX(),getY(),playerX,playerY, speed);
        int dy = getCoordY(getX(),getY(),playerX,playerY, speed);
        setX(dx);
        setY(dy);
        //System.out.println("angle = " + angle + ", x = " + getX() + ", y = " + getY());
    }

    public static int getCoordX(int x0, int y0, int x1, int y1, int rad) {
        double d = Math.sqrt(
                (
                        (x1 - x0) * (x1 - x0)
                ) + (
                        (y1 - y0) * (y1 - y0)
                )
        );
        return (int) (x0 + (x1 - x0) / d * rad);
    }

    public static int getCoordY(int x0, int y0, int x1, int y1, int rad) {
        double d = Math.sqrt(
                (
                        (x1 - x0) * (x1 - x0)
                ) + (
                        (y1 - y0) * (y1 - y0)
                )
        );
        return (int) (y0 + (y1 - y0) / d * rad);
    }

    public static double getAngle(int x0, int y0, int x1, int y1) {
        double angle = Math.atan2(y1 - y0, x1 - x0);
        if (angle < 0) angle = angle + 360;
        return angle;

    }


}
