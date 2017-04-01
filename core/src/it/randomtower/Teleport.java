package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Teleport extends Entity {

	public Teleport(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.TELEPORT;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Player) {
			GameScreen.soundTeleport.play();
			((Player) other).nextLevel();
			toRemove = true;
		}
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
