package ru.chuikov.app;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final float STEP_TIME_240 = 1f / 240f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SHIP_FORCE_RATIO = 10;
    public static float TRASH_VELOCITY = 20;
    public static long STARTING_TRASH_APPEARANCE_COOL_DOWN = 1500; // in [ms] - milliseconds
    public static int BULLET_VELOCITY = 200; // in [m/s] - meter per second
    public static int SHOOTING_COOL_DOWN = 1000; // in [ms] - milliseconds


    //Категории обьктов
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_ENEMY = 0x0002;
    public static final short CATEGORY_PROJECTILE = 0x0004;

    //Маски категорий
    public static final short MASK_PLAYER = CATEGORY_ENEMY; // Игрок сталкивается только с врагами
    public static final short MASK_PROJECTILE = CATEGORY_ENEMY; // Снаряды сталкиваются ТОЛЬКО с врагами
    public static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_PROJECTILE; // Враги с игроком и снарядами

    // Object sizes
    public static final int ZOMBIE_WIDTH = 96;
    public static final int PLAYER_WIDTH = 96;
    public static final int ZOMBIE_HEIGHT = 128;
    public static final int PLAYER_HEIGHT = 128;
    public static final int BULLET_HEIGHT = 78;
    public static final int BULLET_WIDTH = 20;
    public static final int EXPLOSION_HEIGHT = 60;
    public static final int EXPLOSION_WIDTH = 60;



}
