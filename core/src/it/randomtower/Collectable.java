package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Collectable extends Entity {

	public Collectable(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.COLLECTABLE;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		toRemove = true;
		GameScreen.soundCollect.play();
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
