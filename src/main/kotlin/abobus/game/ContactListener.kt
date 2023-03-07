package abobus.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold

class ContactListener: ContactListener{
    private var a = 1
    private val game = Game()
    override fun beginContact(contact: Contact) {
        val fixA = contact.fixtureA
        val fixB = contact.fixtureB
        if (fixA == null || fixB == null) return

        if (fixA.userData == 20 && fixB.userData == 10){
            a = a + 1

            if (a == 9){Gdx.app.exit()}

            else {
                return game.onContact(a)
            }

        }

        if (fixA.userData == 1 && fixB.userData == 10){
            a = a - 1
            if (a == 0){
                a = a + 1
            }
            return game.onContact(a)
        }
    }

    override fun endContact(contact: Contact?) {

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }
}