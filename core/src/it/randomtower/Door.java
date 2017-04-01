package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door extends Entity {

	public Door(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.DOOR;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Player) {
			Player player = (Player) other;
			if (player.key) {
				hasAnimation = true;
				currentAnimation = Entity.DOOR_OPEN;
				collidable = false;
				player.key = false;
			} else {
				GameScreen.soundDoorClosed.play();
			}
		}
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
