package com.me.januarygame.rendering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.*;

public class StartScreen implements Screen {
	
	Game game;
	
	SpriteBatch batch;
	Sprite background;
	
	Music bg_music;
	float volume = 0.0f;
	boolean end = false;
	boolean esc = false;
	boolean start = false;
	
	public StartScreen(Game game){
		this.game = game;
	}

	@Override
	public void render(float delta) {
		if(!bg_music.isPlaying()){
			bg_music.play();
		}
		if(!end && volume < 1.0f){
			volume += 0.01f;
			bg_music.setVolume(volume);
		}else if(end){
			volume -= 0.01f;
			if(volume >= 0.0f) bg_music.setVolume(volume);
		}
		
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			end = true;
			esc = true;
		}
		if(Gdx.input.isKeyPressed(Keys.F12)){
			if(Gdx.graphics.isFullscreen())
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			else
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		}
		if(Gdx.input.isKeyPressed(Keys.R)){
			end = true;
			start = true;
		}
		
		batch.begin();
		background.draw(batch);
		batch.end();
		
		if(esc){
			if(volume <= 0.0f) Gdx.app.exit();
		}
		if(start){
			if(volume <= 0.0f) game.setScreen(new GameScreen(game));
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		background = new Sprite(new Texture(Gdx.files.internal("data/main_menu.png")));
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.setPosition(0, 0);
		background.setOrigin(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		bg_music = Gdx.audio.newMusic(Gdx.files.internal("data/music/main_menu.mp3"));
		bg_music.setLooping(true);
		bg_music.setVolume(volume);
		
	}

	@Override
	public void hide() {
		this.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		background.getTexture().dispose();
		batch.dispose();
		bg_music.dispose();
		
	}

}
