package it.randomtower;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import it.randomtower.util.FLight;

public abstract class Entity implements Comparable<Entity> {

	public static final String STAND_RIGHT = "standRight";
	public static final String STAND_LEFT = "standLeft";
	public static final String STAND_UP = "standUp";
	public static final String STAND_DOWN = "standDown";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_UP = "moveUp";
	public static final String MOVE_DOWN = "moveDown";
	public static final String COLLECTABLE = "collectable";
	public static final String PLAYER = "player";
	public static final String REPAIR_STATION = "repairStation";
	public static final String TRAP = "trap";
	public static final String EXPLOSION_ANIM = "explosionAnim";
	public static final String TELEPORT = "teleport";
	public static final String DOOR = "door";
	public static final String DOOR_OPEN = "door_open";
	public static final String KEY = "key";
	public static final String HORIZONTAL_MOVING_TRAP = "horizontal_moving_trap";
	public static final String PLAYER_TELEPORT = "playerTeleport";
	public Sprite sprite;
	public float speed;
	public boolean collidable;
	public int layer; // 0= background, 1= entities, 2=foreground
	public int scale;
	public Rectangle collisionBox;
	private Map<String, Animation<TextureRegion>> animations;
	protected float stateTime;
	public boolean hasAnimation;
	public int tileSize;
	public String currentAnimation;
	protected String type;
	public boolean toRemove;

	public Entity(Sprite sprite, int scale, int tileSize, boolean collidable) {
		this.sprite = sprite;
		this.scale = scale;
		this.tileSize = tileSize;
		this.speed = 0;
		this.collidable = collidable;
		this.collisionBox = new Rectangle(sprite.getX() + 1, sprite.getY() + 1, (sprite.getWidth() - 1) * scale,
				(sprite.getHeight() - 1) * scale);
		this.animations = new HashMap<String, Animation<TextureRegion>>();
		this.stateTime = 0f;
	}

	public void move(float dx, float dy) {
		if (dx != 0) {
			float tx = sprite.getX() + dx * speed;
			if (tx >= 0 && tx < Gdx.graphics.getWidth() - sprite.getWidth() * scale) {
				sprite.setX(tx);
				collisionBox.setX(tx);
				FLight.myLights[0].setX(tx);
			}
		}
		if (dy != 0) {
			float ty = sprite.getY() + dy * speed;
			if (ty >= 0 && ty < Gdx.graphics.getHeight() - sprite.getHeight() * scale) {
				sprite.setY(ty);
				collisionBox.setY(ty);
				FLight.myLights[0].setY(ty);
			}
		}
	}

	@Override
	public int compareTo(Entity e) {
		if (this.layer > e.layer) {
			return 1;
		}
		if (this.layer < e.layer) {
			return -1;
		}
		return 0;
	}

	public void addAnimation(String name, Animation<TextureRegion> animation) {
		if (animations.containsKey(name)) {
			throw new IllegalArgumentException("Cannot add two animations with the same name " + name);
		}
		animations.put(name, animation);
		hasAnimation = true;
	}

	public void draw(SpriteBatch batch) {
		if (!hasAnimation) {
			batch.draw(sprite, sprite.getX(), sprite.getY(), scale * tileSize, scale * tileSize);
			return;
		}
		stateTime += Gdx.graphics.getDeltaTime();
		setCurrentAnimation();
		Animation<TextureRegion> animation = animations.get(currentAnimation);
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, sprite.getX(), sprite.getY(), scale * tileSize, scale * tileSize);
		if (animation.isAnimationFinished(stateTime)) {
			animationFinished(currentAnimation, animation);
		}
	}

	public abstract void animationFinished(String name, Animation<TextureRegion> animation);

	public abstract void setCurrentAnimation();

	public abstract void handleCollision(Entity other);

}
