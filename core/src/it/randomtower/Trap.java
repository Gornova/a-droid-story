package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Trap extends Entity {

	public boolean collided;

	public Trap(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		int offset = 2;
		this.layer = 2;
		this.collisionBox = new Rectangle(sprite.getX() + offset, sprite.getY() + offset,
				(sprite.getWidth() - 1 - offset) * scale, (sprite.getHeight() - 1 - offset) * scale);
		type = Entity.TRAP;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Player && collidable) {
			currentAnimation = Entity.EXPLOSION_ANIM;
			collidable = false;
			stateTime = 0;
			GameScreen.soundExplosion.play();
		}
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
		if (name.equals(Entity.EXPLOSION_ANIM) && animation.getPlayMode().compareTo(PlayMode.NORMAL) == 0) {
			toRemove = true;
		}
	}

}
