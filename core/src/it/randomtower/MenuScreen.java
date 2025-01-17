package it.randomtower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private BitmapFont fontBig;
	private Texture menuBackground;
	private Stage stage;
	private Skin skin;

	public MenuScreen(Game game) {
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		fontBig = new BitmapFont(Gdx.files.internal("orbitron_big_yellow.fnt"),
				Gdx.files.internal("orbitron_big_yellow.png"), false);
		batch = new SpriteBatch();
		menuBackground = new Texture(Gdx.files.internal("menu.png"));

		// ui
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("defaultSkin/uiskin.json"));

		final TextButton lb = new TextButton("Start", skin, "default");
		lb.setWidth(150);
		lb.setHeight(30);
		lb.setPosition(250, 230);
		lb.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				game.setScreen(new LevelSelectScreen(game));
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
		fontBig.draw(batch, "A droid story", 190, 420);
		font.draw(batch, "Move using arrows or WASD", 160, 160);
		font.draw(batch, "Random tower of games - 2017", 150, 50);
		batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

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
