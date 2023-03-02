package abobus.game.map

class BlankEntity(val x: Float = 0F,
                  val y: Float = 0F,
                  val width: Float = 0F,
                  val height: Float = 0F,
                  val color: Int = 0) {

    override fun toString(): String {
        return "BlankEntity(x=$x, y=$y, width=$width, height=$height, color=$color)"
    }
}