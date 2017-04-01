package it.randomtower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelLoadingMessage {

	private float time;
	private Texture levelLoad;
	private float timer;

	public LevelLoadingMessage(float time) {
		this.time = time;
		this.levelLoad = new Texture("levelLoad.png");
		this.timer = 0;
	}

	public void draw(SpriteBatch batch, int level, BitmapFont font) {
		timer += Gdx.graphics.getDeltaTime();
		if (timer > time) {
			return;
		}
		batch.draw(levelLoad, 0, 0);
		font.draw(batch, "LEVEL " + level, 50, 150);
	}

}
