package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HorizontalMovingTrap extends Trap {

	public boolean collided;
	private boolean moveRight = true;

	public HorizontalMovingTrap(Sprite sprite, int scale, int tileSize) {
		super(sprite, scale, tileSize, true);
		int offset = 2;
		this.layer = 2;
		this.collisionBox = new Rectangle(sprite.getX() + offset, sprite.getY() + offset,
				(sprite.getWidth() - 1 - offset) * scale, (sprite.getHeight() - 1 - offset) * scale);
		type = Entity.HORIZONTAL_MOVING_TRAP;
		this.speed = 3;
	}

	@Override
	public void handleCollision(Entity other) {
		super.handleCollision(other);
		if (other instanceof Tile && other.collidable) {
			moveRight = !moveRight;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		// move horizontally
		if (currentAnimation == Entity.EXPLOSION_ANIM) {
			return;
		}
		float dx = 0;
		if (moveRight) {
			dx = 1;
		} else {
			dx = -1;
		}
		Entity e = GameScreen.collide(this, dx, 0);
		if (e == null) {
			move(dx, 0);
		} else {
			moveRight = !moveRight;
		}
	}

}
