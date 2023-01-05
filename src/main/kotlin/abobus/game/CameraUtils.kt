package abobus.game

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Camera.toWorld(x: Float, y: Float): Vector2 {
    val result = Vector3(x, y, 0f)
    unproject(result)
    return Vector2(result.x, result.y)
}

fun Camera.toWorld(point: Vector2): Vector2 = toWorld(point.x, point.y)

fun Vector2.asWorld(camera: Camera): Vector2 {
    val result = Vector3(x, y, 0f)
    camera.unproject(result)
    set(result.x, result.y)
    return this
}