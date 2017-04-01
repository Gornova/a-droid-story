package it.randomtower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {

	private Controller controller;
	public int hp;
	public int collectable;
	public boolean nextLevel;
	public boolean key;

	public Player(Sprite sprite, int scale, int tileSize) {
		super(sprite, scale, tileSize, true);
		this.speed = 3.2f;
		this.layer = 1;
		type = PLAYER;
		this.hp = 3;
		this.collectable = 0;
		this.nextLevel = false;
	}

	@Override
	public void setCurrentAnimation() {
		if (!controller.right && !controller.left && !controller.up && !controller.down) {
			if (controller.lastRight) {
				currentAnimation = STAND_RIGHT;
			}
			if (controller.lastLeft) {
				currentAnimation = STAND_LEFT;
			}
			if (controller.lastUp) {
				currentAnimation = STAND_UP;
			}
			if (controller.lastDown) {
				currentAnimation = STAND_DOWN;
			}
		} else {
			if (controller.right) {
				currentAnimation = MOVE_RIGHT;
			}
			if (controller.left) {
				currentAnimation = MOVE_LEFT;
			}
			if (controller.up) {
				currentAnimation = MOVE_UP;
			}
			if (controller.down) {
				currentAnimation = MOVE_DOWN;
			}
		}
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void handleCollision(Entity other) {
		if (other instanceof Trap || other instanceof HorizontalMovingTrap) {
			Trap t = (Trap) other;
			if (!t.collided) {
				t.collided = true;
				hp -= 1;
				if (hp <= 0) {
					toRemove = true;
				}
			}
		}
		if (other instanceof Collectable) {
			collectable++;
		}
		if (other instanceof RepairStation) {
			RepairStation rs = (RepairStation) other;
			if (collectable - 1 >= 0 && hp < 3) {
				collectable--;
				hp++;
				GameScreen.soundHeal.stop();
				GameScreen.soundHeal.play();
			}
		}
	}

	@Override
	public void animationFinished(String name, Animation<TextureRegion> animation) {
	}

	public void nextLevel() {
		this.nextLevel = true;
	}

}
