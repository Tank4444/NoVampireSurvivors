package ru.chuikov.app.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import ru.chuikov.app.GameSettings;
import ru.chuikov.app.objects.Enemy;
import ru.chuikov.app.objects.GameObject;
import ru.chuikov.app.objects.Player;
import ru.chuikov.app.objects.Projectile;


public class ContactManager {

    World world;
    private final String TAG = "WORLD";

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                GameObject a = (GameObject) fixA.getUserData();
                GameObject b = (GameObject) fixB.getUserData();

                String nameA = "unknown";
                if (a instanceof Enemy) nameA = "ENEMY";
                else if (a instanceof Projectile) nameA = "PROJECTILE";
                else if (a instanceof Player) nameA = "PLAYER";
                String nameB = "unknown";
                if (b instanceof Enemy) nameB = "ENEMY";
                else if (b instanceof Projectile) nameB = "PROJECTILE";
                else if (b instanceof Player) nameB = "PLAYER";

                Gdx.app.log(TAG, "Detect collision " + nameA + " with " + nameB);
                a.hit(b);
                b.hit(a);
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

    }

}
