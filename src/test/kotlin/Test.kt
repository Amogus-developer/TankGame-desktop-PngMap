import abobus.game.map.createMap
import org.junit.Test
import java.io.File
import javax.imageio.ImageIO

class Test {

    @Test
    fun test() {
        val image = ImageIO.read(File("map2.png"))
        val entities = createMap(image, 20F, 20F)
        entities.forEach {
            println(it)
        }
    }
}