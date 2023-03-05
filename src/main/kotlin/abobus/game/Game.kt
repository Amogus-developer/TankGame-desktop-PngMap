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


class Game: Game() {

    private lateinit var world: World
    private lateinit var camera: OrthographicCamera
    private lateinit var spriteBatch: SpriteBatch

    private val textures = ObjectMap<Int, Texture>()
    private val mapName = "map2.png"

    private lateinit var mapTexture: Texture
    private lateinit var iconP: Texture
    private lateinit var hpBar: Texture

    lateinit var player: Entity
    private val enemies = ArrayList<Entity>()
    private val walls = ArrayList<Entity>()

    private val random = SecureRandom()
    private val forceController = ForceController()

    private fun loadMap(path: String){
        val p = textures[Constance.PLAYER]
        val e = textures[Constance.ENEMY]
        val w = textures[Constance.WALL]

        val m = textures[Constance.MOR]
        val f = textures[Constance.FLOW]
        val t = textures[Constance.TREE]
        val wa = textures[Constance.WATER]

        createMap(ImageIO.read(File(path)), 5F, 5F).forEach {
            when(it.color) {
                Constance.PLAYER -> player = Entity(p, world.createCirclePlayer(it.x, it.y))
                Constance.ENEMY -> enemies.add(Entity(e, world.createCircle(
                    it.x, it.y, 1.4f, 0.57F), random.nextFloat(40F, 60F)))
                Constance.WALL -> walls.add(Entity(w, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constance.WATER -> walls.add(Entity(wa, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constance.MOR -> walls.add(Entity(m, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constance.FLOW -> walls.add(Entity(f, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
                Constance.TREE -> walls.add(Entity(t, world.createWall(
                    it.x, it.y, 2.5F, 2.5F)))
            }
        }
    }

    override fun create() {
        textures.put(Constance.PLAYER, Texture("src/main/resources/icons/entity/tank.png"))
        textures.put(Constance.ENEMY, Texture("src/main/resources/icons/entity/amogus2.png"))

        textures.put(Constance.WALL, Texture("src/main/resources/icons/blocks/wall.png"))
        textures.put(Constance.WATER, Texture("src/main/resources/icons/blocks/water.jpg"))
        textures.put(Constance.MOR, Texture("src/main/resources/icons/blocks/morshrum.jpg"))
        textures.put(Constance.FLOW, Texture("src/main/resources/icons/blocks/flower.jpg"))
        textures.put(Constance.TREE, Texture("src/main/resources/icons/blocks/tree.jpg"))

        mapTexture = Texture(mapName)
        iconP = Texture("src/main/resources/icons/icon.jpg")
        hpBar = Texture("src/main/resources/icons/hp_bar/hp_bar1.jpg")

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
    private fun hpBar(){
        spriteBatch.draw(hpBar,
            player.getPosition().x+39,
            player.getPosition().y+9,
            10f, 3f)
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
        hpBar()
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