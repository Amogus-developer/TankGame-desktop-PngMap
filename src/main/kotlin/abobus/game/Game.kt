package abobus.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import org.lwjgl.opengl.GL20
import java.security.SecureRandom

class Game: Game() {

    private lateinit var world: World
    private lateinit var box2DDebugRenderer: Box2DDebugRenderer

    private lateinit var wall: Body

    private          var thingsWidth = 10
    private          var amountOfThings = 10

    private lateinit var camera: OrthographicCamera
    private lateinit var spriteBatch: SpriteBatch

    private val forceController = ForceController()
    private lateinit var player: Entity

    private lateinit var playerTankTexture: Texture
    private lateinit var enemyTexture: Texture

    private val enemies = ArrayList<Entity>()
    private val random = SecureRandom()


    override fun create() {
        Gdx.gl.glClearColor(0.18f, 0.18f, 0.18f, 1f)

        playerTankTexture = Texture("src/main/resources/icons/tank.png")
        enemyTexture = Texture("src/main/resources/icons/amogus2.png")
        world = World(Vector2(0f, -0f), true)
        box2DDebugRenderer = Box2DDebugRenderer()
        spriteBatch = SpriteBatch()

        camera = OrthographicCamera(50f, 25f)
        camera.position.set(Vector2(0f, 0f), 0f)

        wall = createWall()
        player = Entity(playerTankTexture, world.createCircle(50F, 50F))

        enemyCrate()

        Gdx.input.inputProcessor = forceController
    }
    private fun enemyCrate(){
        for (i in 0 until 10) {
            enemies.add(Entity(enemyTexture, world.createCircle(
                random.nextFloat(10F, 90F), random.nextFloat(10F, 90F),
                restitution = 0.17F, radius = 1.4f)))
        }
    }

    private fun update(){
        world.step(1/60f, 4, 4) //Раз за секунду просчет координат тел
        camera.position.set(player.getPosition(), 0F)
        camera.update()
        spriteBatch.projectionMatrix = camera.combined

        player.applyForceToCenter(forceController.getForce())
        player.rotateTo(forceController.getMousePosition().asWorld(camera))

        enemies.forEach {
            it.moveAt(enemies)
        }
    }

    override fun render() {
        update()

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        box2DDebugRenderer.render(world, camera.combined)

        spriteBatch.begin()
        player.render(spriteBatch)
        enemies.forEach { it.render(spriteBatch) }

        spriteBatch.end()
    }

    override fun pause() {
    }

    override fun resume() {
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
        playerTankTexture.dispose()
        box2DDebugRenderer.dispose()
    }

    private fun createWall(): Body {
        val bDef =  BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        bDef.position.set(0f, 0f)

        wall = world.createBody(bDef)

        val fDef = FixtureDef()
        val shape = ChainShape()

        val vertices = floatArrayOf(
            0f, 100f,
            0f, 0f,
            100f, 0f,
            100f, 100f,
            0f, 100f
        )
        shape.createChain(vertices)

        fDef.shape = shape

        wall.createFixture(fDef)
        return wall
    }
}