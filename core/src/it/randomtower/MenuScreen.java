package it.randomtower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {

	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private BitmapFont fontBig;
	private Texture menuBackground;

	public MenuScreen(Game game) {
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		fontBig = new BitmapFont(Gdx.files.internal("orbitron_big_yellow.fnt"),
				Gdx.files.internal("orbitron_big_yellow.png"), false);
		batch = new SpriteBatch();
		menuBackground = new Texture(Gdx.files.internal("menu.png"));
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(menuBackground, 0, 0);
		fontBig.draw(batch, "A droid story", 190, 420);
		font.draw(batch, "Move using arrows or WASD, Press SPACE to start", 30, 200);
		font.draw(batch, "Random tower of games - 2017", 150, 50);
		batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
