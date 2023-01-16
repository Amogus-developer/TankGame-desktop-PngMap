package abobus.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape

class Entity(private val texture: Texture,
             val body: Body,
             private val speed: Float = 100F) {

    private val radius: Float
    private val doubleRadius: Float



    init {
        val s = body.fixtureList[0].shape
        radius = if (s.type == Shape.Type.Circle) s.radius else {
            s as PolygonShape
            val point = Vector2()
            s.getVertex(1, point)
            point.x
        }
        doubleRadius = radius * 2
    }



    fun render(spriteBatch: SpriteBatch) {
        val cX = body.position.x
        val cY = body.position.y
        spriteBatch.draw(texture,
            cX - radius, cY - radius,
            cX, cY,
            doubleRadius, doubleRadius,
            1F, 1F,
            0F,//body.angle * MathUtils.radiansToDegrees,
            0, 0,
            texture.width, texture.height,
            false,  false)
    }

    fun getPosition(): Vector2 = body.position

    fun applyForceToCenter(force: Vector2) {
        body.applyForceToCenter(force, true)
    }

    fun moveAt(position: Vector2) {
        val v = Vector2(position.x - body.position.x, position.y - body.position.y)
        v.nor()
        v.scl(speed)
        applyForceToCenter(v)
    }



    companion object {
        const val PLAYER = -14254336
        const val ENEMY = -65536
        const val WALL = -16777216

        const val MOR = -10240
        const val FLOW = -4784384
        const val TREE = -16711681
        const val WATER = -16767233
    }
}