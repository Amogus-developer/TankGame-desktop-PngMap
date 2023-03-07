package abobus.game

import abobus.game.map.createMap
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.lwjgl.opengl.GL20
import java.io.File
import java.security.SecureRandom
import javax.imageio.ImageIO

private lateinit var hpBar: Texture

class Game: Game() {

    private lateinit var world: World
    private lateinit var camera: OrthographicCamera
    private lateinit var spriteBatch: SpriteBatch

    private val textures = ObjectMap<Int, Texture>()
    private val mapName = "map_lab.png"

    private lateinit var mapTexture: Texture
    private lateinit var iconP: Texture

    lateinit var player: Entity
    private val enemies = ArrayList<Entity>()
    private val walls = ArrayList<Entity>()

    private val random = SecureRandom()
    private val forceController = ForceController()

    private fun loadMap(path: String){
        val p = textures[Constants.PLAYER]
        val e = textures[Constants.ENEMY]
        val w = textures[Constants.WALL]

        val m = textures[Constants.MOR]
        val f = textures[Constants.FLOW]
        val t = textures[Constants.TREE]
        val wa = textures[Constants.WATER]

        createMap(ImageIO.read(File(path)), 5F, 5F).forEach {
            when(it.color) {
                Constants.PLAYER -> player = Entity(p, world.createCirclePlayer(it.x, it.y))
                Constants.ENEMY -> enemies.add(Entity(e, world.createCircle(
                    it.x, it.y, 1.4f, 0.57F), random.nextFloat(40F, 60F)))
                Constants.WALL -> walls.add(Entity(w, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constants.WATER -> walls.add(Entity(wa, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constants.MOR -> walls.add(Entity(m, world.createWallHP(
                    it.x, it.y, 2.5F, 2.5F)))
                Constants.FLOW -> walls.add(Entity(f, world.createWallHP(
                    it.x, it.y, 2.5F, 2.5F)))
                Constants.TREE -> walls.add(Entity(t, world.createWallHP(
                    it.x, it.y, 2.5F, 2.5F)))
            }
        }
    }

    override fun create() {
        textures.put(Constants.PLAYER, Texture("icons/entity/tank.png"))
        textures.put(Constants.ENEMY, Texture("icons/entity/amogus2.png"))

        textures.put(Constants.WALL, Texture("icons/blocks/wall.png"))
        textures.put(Constants.WATER, Texture("icons/blocks/water.jpg"))
        textures.put(Constants.MOR, Texture("icons/blocks/morshrum.jpg"))
        textures.put(Constants.FLOW, Texture("icons/blocks/flower.jpg"))
        textures.put(Constants.TREE, Texture("icons/blocks/tree.jpg"))

        mapTexture = Texture(mapName)
        iconP = Texture("icons/icon.jpg")
        hpBar = Texture("icons/hp_bar/hp_bar1.jpg")

        world = World(Vector2(0f, 0f), true)
        world.setContactListener(ContactListener())

        spriteBatch = SpriteBatch()

        camera = OrthographicCamera(50f, 25f)
        camera.position.set(Vector2(0f, 0f), 0f)

        loadMap(mapName)

        Gdx.input.inputProcessor = forceController
        Gdx.gl.glClearColor(0.18f, 0.56f, 0.4f, 225f)
    }



    private fun mapSideIcon(){
        spriteBatch.draw(mapTexture,
            player.getPosition().x+34,
            player.getPosition().y+12,
            15f, 15f)
        spriteBatch.draw(iconP,
            (player.getPosition().x+33.1f)*1.021f,
            (player.getPosition().y+25.7f)*1.021f,
            1f, 1f)
    }
    private fun hpBar(texture: Texture){
        spriteBatch.draw(texture,
            player.getPosition().x+39,
            player.getPosition().y+9,
            10f, 3f)
    }
    fun onContact(num: Int){
        hpBar = Texture("icons/hp_bar/hp_bar${num}.jpg")
    }
    private fun update(){
        world.step(1/60f, 4, 4) //Раз за секунду просчет координат тел
        camera.position.set(player.getPosition(), 0F)
        camera.update()
        spriteBatch.projectionMatrix = camera.combined

        player.applyForceToCenter(forceController.getForce())
        enemies.forEach { it.moveAt(player.getPosition()) }
    }
    override fun render() {
        update()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.begin()
        walls.forEach { it.render(spriteBatch) }
        enemies.forEach { it.render(spriteBatch) }
        player.render(spriteBatch)
        spriteBatch.end()

        spriteBatch.begin()
        mapSideIcon()
        hpBar(hpBar)
        spriteBatch.end()
    }
    override fun resize(width: Int, height: Int) {
        val camWidth: Float = thingsWidth.toFloat() * amountOfThings.toFloat()
        val camHeight = camWidth * (height.toFloat() / width.toFloat())
        camera.viewportWidth = camWidth
        camera.viewportHeight = camHeight
        camera.update()
    }
    override fun dispose() {
        world.dispose()
        mapTexture.dispose()
        iconP.dispose()
        hpBar.dispose()
        spriteBatch.dispose()
        textures.values().forEach { it.dispose() }
    }
    companion object {
        private const val thingsWidth = 10
        private const val amountOfThings = 10
    }
}