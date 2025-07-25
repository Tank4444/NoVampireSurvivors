package ru.chuikov.app.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Joystick {
    // Initialize variables
    Vector2 parentPosition;
    float radius;

    Vector2 thumbstickPosition;
    float thumbstickSize;

    float horizontalInput = 0;
    float verticalInput = 0;

    boolean isPressed = false;


    public Joystick(float x, float y, float radius) {
        // Set values
        this.parentPosition = new Vector2(x, y);
        this.radius = radius;

        this.thumbstickPosition = new Vector2(this.parentPosition.x, this.parentPosition.y);
        //this.thumbstickSize = 20;
        this.thumbstickSize = radius / 4;
    }

    // Runs every frame
    public void update(Vector2 touchPosition) {
        boolean isInJoystick = touchPosition.dst(parentPosition) <= radius;

        // Detect if the joystick is pressed or not
        if (Gdx.input.justTouched() && isInJoystick) {
            isPressed = true;
        } else if (!Gdx.input.isTouched()) {
            isPressed = false;
        }

        // Change the position of the thumbsticks
        if (isPressed) {
            // Follow the touchPosition
            if (isInJoystick) {
                thumbstickPosition = touchPosition;
            }
            // If the touchPosition is outside the joystick, it should still follow but don't leave it's radius
            else {
                float angle = angleToPoint(parentPosition, touchPosition);
                thumbstickPosition.x = (float) (parentPosition.x + Math.cos(angle) * radius);
                thumbstickPosition.y = (float) (parentPosition.y + Math.sin(angle) * radius);
            }
        }
        // Reset thumbstick position
        else {
            thumbstickPosition = parentPosition;
        }

        // Update horizontalInput and verticalInput
        horizontalInput = (thumbstickPosition.x - parentPosition.x) / radius;
        verticalInput = (thumbstickPosition.y - parentPosition.y) / radius;

        //System.out.println(getThumbstickAngle());
    }

    // Draws every frame
    public void draw(ShapeRenderer shapeRenderer) {
        // Draw joystick background
        shapeRenderer.setColor(new Color(0x00000055));
        shapeRenderer.circle(parentPosition.x, parentPosition.y, radius);

        // Draw thumbstick
        shapeRenderer.setColor(new Color(0xffffffdd));
        shapeRenderer.circle(thumbstickPosition.x, thumbstickPosition.y, thumbstickSize);
    }

    // Gets the horizontalInput value
    public float getHorizontalInput() {
        return horizontalInput;
    }

    public int getHorizontalInputInt() {
        return Math.round(horizontalInput);
    }

    // Gets the verticalInput value
    public float getVerticalInput() {
        return verticalInput;
    }

    public int getVerticalInputInt() {
        return Math.round(verticalInput);
    }

    // Set the position for the joystick
    public void setPosition(float x, float y) {
        this.parentPosition.x = x;
        this.parentPosition.y = y;
    }

    // Gets the angle of the thumbstick
    public float getThumbstickAngle() {
        float localThumbstickX = thumbstickPosition.x - parentPosition.x;
        float localThumbstickY = thumbstickPosition.y - parentPosition.y;
        return new Vector2(localThumbstickX, localThumbstickY).angleDeg();
    }

    // Return the radius of the thumbstick
    public float getRadius() {
        return radius;
    }

    // Return the angle of a vector
    private float angle(Vector2 vector) {
        return MathUtils.atan2(vector.y, vector.x);
    }

    // Return the point of the angle
    private float angleToPoint(Vector2 vectorA, Vector2 vectorB) {
        return angle(new Vector2(vectorB.x - vectorA.x, vectorB.y - vectorA.y));
    }
}



