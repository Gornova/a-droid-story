package it.randomtower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Launcher extends Game {

	@Override
	public void create() {
		Screen menu = new MenuScreen(this);
		this.setScreen(menu);
	}

}
