package abobus.game

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World

fun World.createCircle(x: Float, y: Float, radius: Float = 2F, restitution: Float = 0.5f): Body {

    val bDef =  BodyDef()
    bDef.type = BodyDef.BodyType.DynamicBody
    bDef.position.set(x, y)

    val circle = createBody(bDef)

    val fDef = FixtureDef()
    fDef.shape = CircleShape().also { it.radius = radius }
    fDef.density = 0.2f //Вес в кг
    fDef.friction = 1.3f //Трение
    fDef.restitution = restitution //Прыгучесть

    circle.createFixture(fDef)

    return circle
}