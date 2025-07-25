package ru.chuikov.app.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar extends View {
    protected BitmapFont font;
    private float currentHealth = 0;
    private float maxHealth = 0;
    private float healthWidth;
    private float border;


    public HealthBar(float x, float y, float width, float height) {
        super(x, y, width, height);
    }


    public void draw(ShapeRenderer shapeRenderer) {

        // Рисуем фон (черная рамка)
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x - border, y - border, width + 2 * border, height + 2 * border);

        // Рисуем задний план (серый)
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);

        // Рисуем текущее здоровье (цвет зависит от уровня)
        if (currentHealth > maxHealth * 0.6) {
            shapeRenderer.setColor(Color.GREEN);
        } else if (currentHealth > maxHealth * 0.3) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.RED);
        }
        shapeRenderer.rect(x, y, healthWidth, height);
    }

    public void setHealth(float current, float max) {
        this.currentHealth = current;
        this.maxHealth = max;
        healthWidth = (currentHealth / maxHealth) * width;
    }
}
