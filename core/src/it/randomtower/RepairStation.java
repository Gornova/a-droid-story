package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RepairStation extends Entity {

	public RepairStation(Sprite sprite, int scale, int tileSize, boolean collidable) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.REPAIR_STATION;
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
