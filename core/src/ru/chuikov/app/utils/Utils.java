package ru.chuikov.app.utils;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

import ru.chuikov.app.objects.Enemy;
import ru.chuikov.app.objects.Player;

public class Utils {
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
        Vector2 vector = new Vector2(x0, y0);
        vector.nor();
        vector.set(new Vector2(x1, y1));
        return vector.angleDeg();

    }

    public static int randomInt(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }

    public static double getXByAngle(int xCenter, int rad, int angle) {
        return xCenter + rad * Math.cos(angle);
    }

    public static double getYByAngle(int yCenter, int rad, int angle) {
        return yCenter + rad * Math.sin(angle);
    }

    public static int getRandomAngle() {
        return random.nextInt(360);
    }

    public static int distance(int xA, int yA, int xB, int yB) {
        Vector2 vector2 = new Vector2(xA, yA);
        return (int) vector2.dst2(xB, yB);
    }

    public static Enemy findNearestEnemy(Player player, ArrayList<Enemy> enemies) {
        if (enemies.isEmpty()) return null;
        Enemy near = enemies.get(0);
        int minDist = distance(player.getX(), player.getY(), near.getX(), near.getY());
        for (Enemy e : enemies) {
            int dist = distance(player.getX(), player.getY(), e.getX(), e.getY());
            if (dist < minDist) {
                minDist = dist;
                near = e;
            }
        }
        return near;
    }
}
