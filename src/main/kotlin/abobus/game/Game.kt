package abobus.game

import abobus.game.map.createMap
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ObjectMap
import org.lwjgl.opengl.GL20
import java.io.File
import java.security.SecureRandom
import javax.imageio.ImageIO


class Game: Game() {

    private lateinit var world: World
    private lateinit var camera: OrthographicCamera
    private lateinit var spriteBatch: SpriteBatch

    private val textures = ObjectMap<Int, Texture>()

    lateinit var player: Entity
    private val enemies = ArrayList<Entity>()
    private val walls = ArrayList<Entity>()

    private val random = SecureRandom()
    private val forceController = ForceController()

    private fun loadMap(path: String){
        val p = textures[Entity.PLAYER]
        val e = textures[Entity.ENEMY]
        val w = textures[Entity.WALL]

        val m = textures[Entity.MOR]
        val f = textures[Entity.FLOW]
        val t = textures[Entity.TREE]
        val wa = textures[Entity.WATER]

        createMap(ImageIO.read(File(path)), 5F, 5F).forEach {
            when(it.color) {
                Entity.PLAYER -> player = Entity(p, world.createCircle(it.x, it.y))
                Entity.ENEMY -> enemies.add(Entity(e, world.createCircle(
                    it.x, it.y, 1.4f, 0.57F), random.nextFloat(10F, 20F)))
                Entity.WALL -> walls.add(Entity(w, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Entity.WATER -> walls.add(Entity(wa, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Entity.MOR -> walls.add(Entity(m, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Entity.FLOW -> walls.add(Entity(f, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Entity.TREE -> walls.add(Entity(t, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
            }
        }
    }

    override fun create() {
        textures.put(Entity.PLAYER, Texture("src/main/resources/icons/tank.png"))
        textures.put(Entity.ENEMY, Texture("src/main/resources/icons/amogus2.png"))
        textures.put(Entity.WALL, Texture("src/main/resources/icons/wall.png"))

        textures.put(Entity.WATER, Texture("src/main/resources/icons/water.jpg"))
        textures.put(Entity.MOR, Texture("src/main/resources/icons/morshrum.jpg"))
        textures.put(Entity.FLOW, Texture("src/main/resources/icons/flower.jpg"))
        textures.put(Entity.TREE, Texture("src/main/resources/icons/tree.jpg"))

        world = World(Vector2(0f, 0f), true)

        spriteBatch = SpriteBatch()

        camera = OrthographicCamera(50f, 25f)
        camera.position.set(Vector2(0f, 0f), 0f)

        loadMap("map2.png")

        Gdx.input.inputProcessor = forceController
        Gdx.gl.glClearColor(0.18f, 0.56f, 0.4f, 225f)
    }

    private fun update(){
        world.step(1/60f, 4, 4) //Раз за секунду просчет координат тел
        camera.position.set(player.getPosition(), 0F)
        camera.update()
        spriteBatch.projectionMatrix = camera.combined

        player.applyForceToCenter(forceController.getForce())
        enemies.forEach { it.moveAt(player.getPosition()) }
        speedLimiter(player)
        friction(player)
    }
    override fun render() {
        update()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.begin()

        walls.forEach { it.render(spriteBatch) }
        enemies.forEach { it.render(spriteBatch) }
        player.render(spriteBatch)

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
        spriteBatch.dispose()
        textures.values().forEach { it.dispose() }
    }
    companion object {
        private const val thingsWidth = 10
        private const val amountOfThings = 10
    }
}