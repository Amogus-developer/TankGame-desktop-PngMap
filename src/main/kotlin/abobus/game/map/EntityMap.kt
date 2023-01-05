package abobus.game.map

import java.awt.image.BufferedImage

fun createMap(image: BufferedImage, tileWidth: Float, tileHeight: Float): List<BlankEntity> {
    val w = image.width
    val h = image.height
    val result = ArrayList<BlankEntity>()
    for (y in 0 until h) for(x in 0 until w)
        result.add(BlankEntity(x * tileWidth, y * tileHeight, tileWidth, tileHeight, image.getRGB(x, y)))
    return result
}