package com.me.januarygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.me.januarygame.rendering.StartScreen;

public class GameStarter extends Game {

	@Override
	public void create() {
		Gdx.gl10.glClearColor(.1f, .1f, .1f, 1f);
		
		this.setScreen(new StartScreen(this));
	}

}
