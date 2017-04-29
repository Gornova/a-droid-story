package it.randomtower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import it.randomtower.util.FLight;

public class GameScreen implements Screen {

	private Game game;
	SpriteBatch batch;
	Texture sheet;
	private int scale = 4;
	private int tileSize = 10;
	private static List<Entity> entities;
	public Controller controller;
	private Player player;
	private BitmapFont font;
	private TextureRegion healtBarFull;
	private TextureRegion healtBarMedium;
	private TextureRegion healtBarCritical;
	private TextureRegion collectBarEmpy;
	private TextureRegion collectBarOne;
	private TextureRegion collectBarTwo;
	private TextureRegion collectBarThree;
	private LevelLoadingMessage levelLoadingMessage;
	private Texture gameBackground;
	private Texture light;
	private FrameBuffer lightBuffer;
	private TextureRegion lightBufferRegion;

	private int level;
	public static final int TOTAL_LEVELS = 25;
	public static int unlockedLevel = 2;

	public static Sound soundDoorClosed;
	public static Sound soundError;
	public static Sound soundTeleport;
	public static Sound soundHeal;
	public static Sound soundExplosion;
	public static Sound soundCollect;
	public static Music music;

	public GameScreen(Game game, int level) {
		this.game = game;
		this.level = level;
		create();
	}

	public void create() {
		// setup input processor
		controller = new Controller();

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.LEFT || keycode == Keys.A) {
					controller.clearLast();
					controller.lastLeft = true;
					controller.left = false;
					return true;
				}
				if (keycode == Keys.RIGHT || keycode == Keys.D) {
					controller.clearLast();
					controller.lastRight = true;
					controller.right = false;
					return true;
				}
				if (keycode == Keys.UP || keycode == Keys.W) {
					controller.clearLast();
					controller.lastUp = true;
					controller.up = false;
					return true;
				}
				if (keycode == Keys.DOWN || keycode == Keys.S) {
					controller.clearLast();
					controller.lastDown = true;
					controller.down = false;
					return true;
				}
				if (keycode == Keys.R) {
					Gdx.gl.glClearColor(133 / 255f, 133 / 255f, 133 / 255f, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					create();
					return true;
				}
				if (keycode == Keys.ESCAPE) {
					Gdx.gl.glClearColor(133 / 255f, 133 / 255f, 133 / 255f, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					game.setScreen(new MenuScreen(game));
					return true;
				}

				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Keys.LEFT || keycode == Keys.A) {
					controller.left = true;
					return true;
				}
				if (keycode == Keys.RIGHT || keycode == Keys.D) {
					controller.right = true;
					return true;
				}
				if (keycode == Keys.UP || keycode == Keys.W) {
					controller.up = true;
					return true;
				}
				if (keycode == Keys.DOWN || keycode == Keys.S) {
					controller.down = true;
					return true;
				}

				return false;
			}
		});
		// load files
		batch = new SpriteBatch();
		gameBackground = new Texture(Gdx.files.internal("menu.png"));
		sheet = new Texture("spritesheet.png");
		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		levelLoadingMessage = new LevelLoadingMessage(1.5f);
		soundCollect = Gdx.audio.newSound(Gdx.files.internal("sound/pickup.wav"));
		soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sound/explosion.wav"));
		soundHeal = Gdx.audio.newSound(Gdx.files.internal("sound/heal.wav"));
		soundTeleport = Gdx.audio.newSound(Gdx.files.internal("sound/teleport.wav"));
		soundError = Gdx.audio.newSound(Gdx.files.internal("sound/error.wav"));
		soundDoorClosed = Gdx.audio.newSound(Gdx.files.internal("sound/doorClosed.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/Ambient-piano-loop-105-bpm.wav"));
		// Light system
		FLight.initLights();
		FLight.isLightRendering = false;
		// set the "light" sprite
		FLight.lightSprite = new Texture(Gdx.files.internal("lights.png"), true);
		lightBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		lightBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		lightBufferRegion.flip(false, false);

		// setup entities
		entities = new ArrayList<Entity>();
		// load level
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 25, sheet.getHeight() / 25);
		TiledMap map = new TmxMapLoader().load("level" + level + ".tmx");
		loadLevel(map, tmp);
		// setup gui
		healtBarFull = tmp[0][5];
		healtBarMedium = tmp[0][6];
		healtBarCritical = tmp[0][7];
		collectBarEmpy = tmp[0][8];
		collectBarOne = tmp[0][9];
		collectBarTwo = tmp[0][10];
		collectBarThree = tmp[0][11];

	}

	private void loadLevel(TiledMap map, TextureRegion[][] tmp) {
		// load background
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		for (int i = 0; i < layer.getWidth(); i++) {
			for (int j = 0; j < layer.getHeight(); j++) {
				if (layer.getCell(i, j) != null) {
					TiledMapTile t = layer.getCell(i, j).getTile();
					Sprite s = new Sprite(t.getTextureRegion());
					s.setX(i * scale * tileSize);
					s.setY(j * scale * tileSize);
					Tile tile = new Tile(s, scale, tileSize, t.getProperties().get("block", false, Boolean.class));
					entities.add(tile);
				}
			}
		}
		// load walls
		layer = (TiledMapTileLayer) map.getLayers().get(1);
		for (int i = 0; i < layer.getWidth(); i++) {
			for (int j = 0; j < layer.getHeight(); j++) {
				if (layer.getCell(i, j) != null) {
					TiledMapTile t = layer.getCell(i, j).getTile();
					Sprite s = new Sprite(t.getTextureRegion());
					s.setX(i * scale * tileSize);
					s.setY(j * scale * tileSize);
					Tile tile = new Tile(s, scale, tileSize, true);
					entities.add(tile);
				}
			}
		}
		// load entities
		int playerTeleportNumber = 1;
		layer = (TiledMapTileLayer) map.getLayers().get(2);
		for (int i = 0; i < layer.getWidth(); i++) {
			for (int j = 0; j < layer.getHeight(); j++) {
				if (layer.getCell(i, j) != null) {
					TiledMapTile t = layer.getCell(i, j).getTile();
					if (t.getProperties().get("player") != null) {
						Boolean isPlayer = (Boolean) t.getProperties().get("player");
						if (isPlayer) {
							Sprite s = new Sprite(t.getTextureRegion());
							int px = i * scale * tileSize;
							int py = j * scale * tileSize;
							s.setX(px);
							s.setY(py);
							player = new Player(s, scale, tileSize);
							player.setController(controller);
							player.addAnimation(Entity.STAND_RIGHT,
									new Animation<TextureRegion>(0.5f, tmp[0][0], tmp[0][1]));
							player.addAnimation(Entity.STAND_LEFT,
									new Animation<TextureRegion>(0.5f, tmp[1][0], tmp[1][1]));
							player.addAnimation(Entity.STAND_UP,
									new Animation<TextureRegion>(0.5f, tmp[2][0], tmp[2][1]));
							player.addAnimation(Entity.STAND_DOWN,
									new Animation<TextureRegion>(0.5f, tmp[3][0], tmp[3][1]));
							player.addAnimation(Entity.MOVE_RIGHT,
									new Animation<TextureRegion>(0.4f, tmp[0][2], tmp[0][3], tmp[0][4]));
							player.addAnimation(Entity.MOVE_LEFT,
									new Animation<TextureRegion>(0.4f, tmp[1][2], tmp[1][3], tmp[1][4]));
							player.addAnimation(Entity.MOVE_UP,
									new Animation<TextureRegion>(0.4f, tmp[2][2], tmp[2][3], tmp[2][4]));
							player.addAnimation(Entity.MOVE_DOWN,
									new Animation<TextureRegion>(0.4f, tmp[3][2], tmp[3][3], tmp[3][4]));

							player.currentAnimation = Entity.STAND_RIGHT;
							entities.add(player);

							// test light
							FLight.addLight(px, py, 256, FLight.LightType_SphereTense, 180, 180, 180, 255);

						}
					}
					if (t.getProperties().get("collectable") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						Collectable collectable = new Collectable(s, scale, tileSize, true);
						collectable.addAnimation(Entity.STAND_DOWN,
								new Animation<TextureRegion>(0.4f, tmp[4][0], tmp[4][1], tmp[4][2], tmp[4][3]));
						collectable.currentAnimation = Entity.STAND_DOWN;
						entities.add(collectable);
					}
					if (t.getProperties().get("repairStation") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						RepairStation repairStation = new RepairStation(s, scale, tileSize, true);
						repairStation.addAnimation(Entity.STAND_DOWN, new Animation<TextureRegion>(0.25f, tmp[5][0],
								tmp[5][1], tmp[5][2], tmp[5][3], tmp[5][4]));
						repairStation.currentAnimation = Entity.STAND_DOWN;
						entities.add(repairStation);
					}
					if (t.getProperties().get("trap") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						Trap trap = new Trap(s, scale, tileSize, true);
						trap.addAnimation(Entity.STAND_DOWN,
								new Animation<TextureRegion>(0.25f, tmp[6][0], tmp[6][1], tmp[6][2], tmp[6][3]));
						Animation<TextureRegion> expAnim = new Animation<TextureRegion>(0.15f, tmp[1][5], tmp[1][5],
								tmp[1][7], tmp[1][8], tmp[1][9]);
						expAnim.setPlayMode(PlayMode.NORMAL);
						trap.addAnimation(Entity.EXPLOSION_ANIM, expAnim);
						trap.currentAnimation = Entity.STAND_DOWN;
						entities.add(trap);
					}
					if (t.getProperties().get("teleport") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						Teleport teleport = new Teleport(s, scale, tileSize, true);
						teleport.addAnimation(Entity.STAND_DOWN,
								new Animation<TextureRegion>(0.35f, tmp[2][5], tmp[2][6], tmp[2][7], tmp[2][8]));
						teleport.currentAnimation = Entity.STAND_DOWN;
						entities.add(teleport);
					}
					if (t.getProperties().get("door") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						Door door = new Door(s, scale, tileSize, true);
						door.addAnimation(Entity.DOOR_OPEN,
								new Animation<TextureRegion>(0.35f, tmp[6][4], tmp[6][5], tmp[6][6], tmp[8][7]));
						door.hasAnimation = false;
						entities.add(door);
					}
					if (t.getProperties().get("key") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						Key key = new Key(s, scale, tileSize, true);
						key.addAnimation(Entity.STAND_DOWN, new Animation<TextureRegion>(0.35f, tmp[5][5], tmp[5][6]));
						key.currentAnimation = Entity.STAND_DOWN;
						entities.add(key);
					}
					if (t.getProperties().get("movingTrap") != null && t.getProperties().get("horizontal") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						HorizontalMovingTrap trap = new HorizontalMovingTrap(s, scale, tileSize);
						trap.addAnimation(Entity.STAND_DOWN,
								new Animation<TextureRegion>(0.25f, tmp[6][0], tmp[6][1], tmp[6][2], tmp[6][3]));
						Animation<TextureRegion> expAnim = new Animation<TextureRegion>(0.15f, tmp[1][5], tmp[1][5],
								tmp[1][7], tmp[1][8], tmp[1][9]);
						expAnim.setPlayMode(PlayMode.NORMAL);
						trap.addAnimation(Entity.EXPLOSION_ANIM, expAnim);
						trap.currentAnimation = Entity.STAND_DOWN;
						entities.add(trap);
					}
					if (t.getProperties().get("playerTeleport") != null) {
						Sprite s = new Sprite(t.getTextureRegion());
						s.setX(i * scale * tileSize);
						s.setY(j * scale * tileSize);
						PlayerTeleport teleport = new PlayerTeleport(s, scale, tileSize, true, playerTeleportNumber);
						playerTeleportNumber++;
						teleport.addAnimation(Entity.STAND_DOWN,
								new Animation<TextureRegion>(0.35f, tmp[3][5], tmp[3][6], tmp[3][7], tmp[3][8]));
						teleport.currentAnimation = Entity.STAND_DOWN;
						entities.add(teleport);
					}
				}
			}
		}

	}

	@Override
	public void render(float delta) {
		if (player.nextLevel) {
			level++;
			if (level > TOTAL_LEVELS) {
				batch.begin();
				batch.draw(gameBackground, 0, 0);
				font.draw(batch, "You win ?", 240, 240);
				font.draw(batch, "Press ESC to continue", 180, 200);
				batch.end();
				return;
			}
			unlockedLevel++;
			create();
			return;
		}
		if (player.toRemove) {
			batch.begin();
			batch.draw(gameBackground, 0, 0);
			font.draw(batch, "Game Over", 240, 240);
			font.draw(batch, "Press ESC to continue", 180, 200);
			batch.end();
			return;
		}
		// render light

		// renderFlight();

		// render entities
		Collections.sort(entities);
		Gdx.gl.glClearColor(133 / 255f, 133 / 255f, 133 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (Entity entity : entities) {
			entity.draw(batch);
		}
		batch.end();

		// light part 2
		// batch.begin();
		// batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		// batch.draw(lightBufferRegion, 0, 0, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
		// batch.end();
		// batch.setBlendFunction(GL20.GL_SRC_ALPHA,
		// GL20.GL_ONE_MINUS_SRC_ALPHA);

		// render gui
		batch.begin();
		drawHealthBar(batch);
		drawCollectBar(batch);
		// render levelLoad
		levelLoadingMessage.draw(batch, level, font);

		// render end
		batch.end();

		// update
		float dx = 0;
		float dy = 0;
		if (controller.right) {
			dx += 1;
		}
		if (controller.left) {
			dx -= 1;
		}
		if (controller.up) {
			dy += 1;
		}
		if (controller.down) {
			dy -= 1;
		}
		Entity e = collide(player, dx, dy);
		if (e == null) {
			player.move(dx, dy);
		} else {
			e.handleCollision(player);
			player.handleCollision(e);
		}
		// remove entities not more used
		entities.removeIf(en -> en.toRemove);
	}

	private void renderFlight() {
		lightBuffer.begin();
		Gdx.gl.glClearColor(FLight.ambientColor.r, FLight.ambientColor.g, FLight.ambientColor.b, FLight.ambientColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		batch.begin();
		for (int i = 0; i < FLight.myLights.length; i++) {
			if (FLight.myLights[i].isActive()) {
				batch.setColor(FLight.myLights[i].getColor());

				float tx = FLight.myLights[i].getX();
				float ty = FLight.myLights[i].getY();
				float tw = (128 / 100f) * FLight.myLights[i].getDistance();

				// 64,92

				switch (FLight.myLights[i].getLightType()) {
				case FLight.LightType_Up:
					tx -= ((128 / 100f) * FLight.myLights[i].getDistance()) / 2;
					ty -= ((92 / 100f) * FLight.myLights[i].getDistance());
					batch.draw(FLight.lightSprite, tx, ty, tw, tw, 0, 128, 128, 128, false, true);
					break;

				case FLight.LightType_Right:
					tx -= ((128 / 100f) * FLight.myLights[i].getDistance()) / 2;
					ty -= ((128 / 100f) * FLight.myLights[i].getDistance());
					batch.draw(FLight.lightSprite, tx, ty, tw / 2, tw, tw, tw, 1f, 1f, 90, 0, 128, 128, 128, false,
							true);
					break;

				case FLight.LightType_Down:
					tx -= ((128 / 100f) * FLight.myLights[i].getDistance()) / 2;
					ty += ((128 / 100f) * FLight.myLights[i].getDistance());
					batch.draw(FLight.lightSprite, tx, ty, tw / 2, 0, tw, tw, 1f, 1f, 180, 0, 128, 128, 128, false,
							true);
					break;

				case FLight.LightType_Left:
					tx -= ((128 / 100f) * FLight.myLights[i].getDistance()) / 2;
					ty -= ((128 / 100f) * FLight.myLights[i].getDistance());
					batch.draw(FLight.lightSprite, tx, ty, tw / 2, tw, tw, tw, 1f, 1f, 270, 0, 128, 128, 128, false,
							true);
					break;

				case FLight.LightType_SphereTense:
					tx -= (tw / 2);
					ty -= (tw / 2);
					batch.draw(FLight.lightSprite, tx, ty, tw, tw, 256, 0, 128, 128, false, true);
					break;

				case FLight.LightType_FLARE:
					tx -= (tw / 2);
					ty -= (tw / 2);
					batch.draw(FLight.lightSprite, tx, ty, tw, tw, 128, 128, 128, 128, false, true);
					break;

				default:
					tx -= (tw / 2);
					ty -= (tw / 2);
					batch.draw(FLight.lightSprite, tx, ty, tw, tw, 0, 0, 128, 128, false, true);
					break;
				}

			}
			// FLight.myLights[i].setActive(false);
		}

		batch.end();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		lightBuffer.end();

	}

	private void renderLight() {
		// start rendering to the lightBuffer
		lightBuffer.begin();

		// setup the right blending
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		// set the ambient color values, this is the "global" light of your
		// scene
		// imagine it being the sun. Usually the alpha value is just 1, and you
		// change the darkness/brightness with the Red, Green and Blue values
		// for best effect
		// Gdx.gl.glClearColor(133 / 255f, 133 / 255f, 133 / 255f, 1);

		// Gdx.gl.glClearColor(0.5f, 1.0f, 1.0f, 1);
		/// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// start rendering the lights to our spriteBatch
		batch.begin();

		// set the color of your light (red,green,blue,alpha values)
		batch.setColor(0.9f, 0.4f, 0f, 1f);

		// tx and ty contain the center of the light source
		float tx = (Gdx.graphics.getWidth() / 2);
		float ty = (Gdx.graphics.getHeight() / 2);

		// tw will be the size of the light source based on the "distance"
		// (the light image is 128x128)
		// and 96 is the "distance"
		// Experiment with this value between based on your game resolution
		// my lights are 8 up to 128 in distance
		float tw = 200;

		// make sure the center is still the center based on the "distance"
		tx -= (tw / 2);
		ty -= (tw / 2);

		// and render the sprite
		batch.draw(light, tx, ty, tw, tw, 0, 0, 128, 128, false, false);

		batch.end();
		lightBuffer.end();

		// now we render the lightBuffer to the default "frame buffer"
		// with the right blending !

		Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		batch.begin();
		batch.draw(lightBufferRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		// post light-rendering
		// you might want to render your statusbar stuff here

	}

	private void drawCollectBar(SpriteBatch batch2) {
		TextureRegion cb = null;
		switch (player.collectable) {
		case 0:
			cb = collectBarEmpy;
			break;
		case 1:
			cb = collectBarOne;
			break;
		case 2:
			cb = collectBarTwo;
			break;
		case 3:
			cb = collectBarThree;
			break;
		default:
			cb = collectBarEmpy;
			break;
		}
		batch.draw(cb, player.sprite.getX(), player.sprite.getY() - (scale * tileSize), scale * tileSize,
				scale * tileSize);
	}

	private void drawHealthBar(SpriteBatch batch) {
		TextureRegion hb = null;
		switch (player.hp) {
		case 3:
			hb = healtBarFull;
			break;
		case 2:
			hb = healtBarMedium;
			break;
		case 1:
			hb = healtBarCritical;
			break;
		default:
			hb = healtBarFull;
			break;
		}
		batch.draw(hb, player.sprite.getX(), player.sprite.getY() + (scale * tileSize), scale * tileSize,
				scale * tileSize);
	}

	public static Entity collide(Entity entity, float dx, float dy) {
		float tx = entity.sprite.getX() + dx * entity.speed;
		float ty = entity.sprite.getY() + dy * entity.speed;
		Rectangle tr = new Rectangle(entity.collisionBox);
		tr.setPosition(tx, ty);

		for (Entity e : entities) {
			if (!e.equals(entity) && e.collidable) {
				Rectangle er = e.collisionBox;
				if (tr.overlaps(er)) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		batch.dispose();
		sheet.dispose();
	}

	public static PlayerTeleport getNextTeleport(int number) {
		List<PlayerTeleport> pts = new ArrayList<PlayerTeleport>();
		for (Entity entity : entities) {
			if (entity instanceof PlayerTeleport) {
				PlayerTeleport pt = (PlayerTeleport) entity;
				if (pt.number == number) {
					pts.add(pt);
				}
			}
		}
		if (pts == null || pts.isEmpty()) {
			for (Entity entity : entities) {
				if (entity instanceof PlayerTeleport) {
					PlayerTeleport pt = (PlayerTeleport) entity;
					if (pt.number == 1) {
						return pt;
					}
				}
			}
			return null;
		}
		return pts.get(0);
	}

	@Override
	public void show() {
		if (!music.isPlaying()) {
			music.play();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		if (music.isPlaying()) {
			music.pause();
		}
	}

	@Override
	public void resume() {
		if (!music.isPlaying()) {
			music.play();
		}
	}

	@Override
	public void hide() {
		if (music.isPlaying()) {
			music.pause();
		}
	}
}
