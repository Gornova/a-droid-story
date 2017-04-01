package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Key extends Entity {

	public Key(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.KEY;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Player) {
			toRemove = true;
			((Player) other).key = true;
			GameScreen.soundCollect.play();
		}
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
