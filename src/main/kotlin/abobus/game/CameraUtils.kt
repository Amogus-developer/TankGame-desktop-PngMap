package abobus.game

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Camera.toWorld(x: Float, y: Float): Vector2 {
    val result = Vector3(x, y, 0f)
    unproject(result)
    return Vector2(result.x, result.y)
}
