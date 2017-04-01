package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile extends Entity {

	public Tile(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
