package abobus.game

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {

    val config = Lwjgl3ApplicationConfiguration()

    config.setForegroundFPS(2 * 60)
    config.setIdleFPS(30)
    config.useVsync(false)
    config.setTitle("world test")
    config.setResizable(true)
    config.setWindowedMode(1600,1000)
    config.setWindowIcon(Files.FileType.Local, "src/main/resources/icons/icon.jpg")

    Lwjgl3Application(Game(), config)
}