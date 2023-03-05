package abobus.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

fun World.createCircle(x: Float, y: Float, radius: Float = 2F, restitution: Float = 0.2f): Body {
    val bDef =  BodyDef()
    bDef.type = BodyDef.BodyType.DynamicBody
    bDef.linearDamping = 1.5f
    bDef.angularDamping = 1.5f
    bDef.position.set(x, y)

    val circle = createBody(bDef)

    val fDef = FixtureDef()
    fDef.shape = CircleShape().also { it.radius = radius }
    fDef.density = 0.2f //Вес в кг
    fDef.friction = 1.3f //Трение
    fDef.restitution = restitution //Прыгучесть

    circle.createFixture(fDef).userData = 20

    return circle
}
fun World.createCirclePlayer(x: Float, y: Float, radius: Float = 2F, restitution: Float = 0.2f): Body {
    val bDef =  BodyDef()
    bDef.type = BodyDef.BodyType.DynamicBody
    bDef.linearDamping = 1.5f
    bDef.angularDamping = 1.5f
    bDef.position.set(x, y)

    val circle = createBody(bDef)

    val fDef = FixtureDef()
    fDef.shape = CircleShape().also { it.radius = radius }
    fDef.density = 0.2f //Вес в кг
    fDef.friction = 1.3f //Трение
    fDef.restitution = restitution //Прыгучесть

    circle.createFixture(fDef).userData = 10

    return circle
}

fun World.createWall(x: Float, y: Float, halfWidth: Float, halfHeight: Float): Body {
    val bodyDef =  BodyDef()
    bodyDef.type = BodyDef.BodyType.StaticBody
    bodyDef.position.set(x, y)

    val body = createBody(bodyDef)

    val fixtureDef = FixtureDef()
    fixtureDef.friction = 0.5F //Трение
    fixtureDef.restitution = 0.4F //Прыгучесть
    fixtureDef.shape = PolygonShape().apply { setAsBox(halfWidth, halfHeight, Vector2.Zero, 0F) }

    body.createFixture(fixtureDef).userData = 0
    return body
}
