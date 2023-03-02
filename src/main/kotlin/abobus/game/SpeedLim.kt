package abobus.game

fun friction(player: Entity){
         if (player.body.linearVelocity.x < 0.000000f) {
            if (player.body.linearVelocity.x != 0.000000f) player.body.applyForceToCenter(10f, 0f, true)
        }

        if (player.body.linearVelocity.x > 0.000000f) {
            if (player.body.linearVelocity.x != 0.000000f) player.body.applyForceToCenter(-10f, 0f, true)
        }

        if (player.body.linearVelocity.y < 0.000000f) {
            if (player.body.linearVelocity.y != 0.000000f) player.body.applyForceToCenter(0f, 10f, true)
        }

        if (player.body.linearVelocity.y > 0.000000f) {
            if (player.body.linearVelocity.y != 0.000000f) player.body.applyForceToCenter(0f, -10f, true)
    }
}
fun speedLimiter(player: Entity) {
    if (player.body.linearVelocity.x < -25) {
        if (player.body.linearVelocity.x != 0f) player.body.applyForceToCenter(100f, 0f, true)
    }
    if (player.body.linearVelocity.x > 25) {
        if (player.body.linearVelocity.x != 0f) player.body.applyForceToCenter(-100f, 0f, true)
    }
    if (player.body.linearVelocity.y < -25) {
        if (player.body.linearVelocity.y != 0f) player.body.applyForceToCenter(0f, 100f, true)
    }
    if (player.body.linearVelocity.y > 25) {
        if (player.body.linearVelocity.y != 0f) player.body.applyForceToCenter(0f, -100f, true)
    }
}