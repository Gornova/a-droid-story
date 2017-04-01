package it.randomtower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerTeleport extends Entity {

	public int number;
	private float cooldown;
	private boolean enabled = true;

	public PlayerTeleport(Sprite sprite, int scale, int tileSize, boolean collidable, int playerTeleportNumber) {
		super(sprite, scale, tileSize, collidable);
		type = Entity.PLAYER_TELEPORT;
		this.number = playerTeleportNumber;
		this.layer = 3;
	}

	@Override
	public void setCurrentAnimation() {
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Player && enabled) {
			Player p = (Player) other;
			GameScreen.soundTeleport.play();
			PlayerTeleport pt = GameScreen.getNextTeleport(number + 1);
			pt.enabled = false;
			p.sprite.setX(pt.sprite.getX());
			p.sprite.setY(pt.sprite.getY() + scale * tileSize);
			enabled = false;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if (!enabled) {
			cooldown += Gdx.graphics.getDeltaTime();
			if (cooldown >= 1) {
				enabled = true;
				cooldown = 0;
			}
		}

	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

}
