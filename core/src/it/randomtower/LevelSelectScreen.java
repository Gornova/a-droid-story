package it.randomtower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen {

	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private Texture menuBackground;
	private Skin skin;
	private Stage stage;

	public LevelSelectScreen(Game game) {
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		batch = new SpriteBatch();
		menuBackground = new Texture(Gdx.files.internal("menu.png"));

		// ui
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("defaultSkin/uiskin.json"));

		int dx = 0;
		int dy = 0;
		int i = 0;
		int j = 0;
		while (i < GameScreen.TOTAL_LEVELS) {
			i++;
			if (i > GameScreen.unlockedLevel) {
				break;
			}
			dy = 350 - j * 60;
			addButton(i, 30 + dx, dy);
			if (i % 5 == 0) {
				dx = 0;
				j++;
				dx = 0;
			} else {
				dx += 130;
			}
		}
	}

	private void addButton(int level, int dx, int dy) {
		final TextButton lb = new TextButton("" + level, skin, "default");
		lb.setWidth(50);
		lb.setHeight(30);
		lb.setPosition(dx, dy);
		lb.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				// load level
				game.setScreen(new GameScreen(game, level));
			};
		});
		stage.addActor(lb);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(menuBackground, 0, 0);
		font.draw(batch, "Select level  " + GameScreen.unlockedLevel + " / " + GameScreen.TOTAL_LEVELS, 30, 450);
		batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new MenuScreen(game));
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
