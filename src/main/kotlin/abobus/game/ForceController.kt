package abobus.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2

class ForceController(private val force: Float = 30F): InputAdapter() {

    private val bodyForce = Vector2()
    private val mousePosition = Vector2()

    override fun keyDown(keycode: Int): Boolean {
        bodyForce.apply {
            when (keycode) {
                A -> x -= force
                D -> x += force
                W -> y += force
                S -> y -= force
                ESCAPE -> Gdx.app.exit()
            }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        bodyForce.apply {
            when (keycode) {
                A -> x += force
                D -> x -= force
                W -> y -= force
                S -> y += force
            }
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        mousePosition.set(screenX.toFloat(),Gdx.graphics.height - screenY.toFloat())
        return false
    }

    fun getMousePosition(): Vector2{
        return mousePosition
    }

    fun getForce() = bodyForce
}