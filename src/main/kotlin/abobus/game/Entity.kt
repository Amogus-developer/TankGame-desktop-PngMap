package abobus.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Entity(private val texture: Texture,
             private val body: Body) {

    private          var angle = Vector2()
    private          var size = 64f
    private          var halfSize = size / 2f

    private lateinit var textureR: TextureRegion
    var starter = 1

    fun create(){
        textureR = TextureRegion(texture)
    }
    fun render(spriteBatch: SpriteBatch) {
        if (starter == 1){
            create()
            starter = 0
        }
        spriteBatch.draw(
            textureR,
            body.position.x - 32f,
            body.position.y - 32f,
            halfSize,
            halfSize,
            size,
            size,
            0.087f,
            0.087f,
            angle.angleDeg()
        )
    }

    fun getPosition(): Vector2 = body.position

    fun applyForceToCenter(force: Vector2) {
        body.applyForceToCenter(force, true)
    }

    fun moveAt(enemies: ArrayList<Entity>) {
        val x = (MathUtils.random() * 20f) - MathUtils.random() * 20f
        val y = (MathUtils.random() * 20f) - MathUtils.random() * 20f
        enemies.forEach { body.applyForceToCenter(x, y, true) }
    }

    fun rotateTo(mousePosition: Vector2) {
        angle.angleDeg() + 90
        angle.set(mousePosition).sub(body.position.x, body.position.y)
    }
}