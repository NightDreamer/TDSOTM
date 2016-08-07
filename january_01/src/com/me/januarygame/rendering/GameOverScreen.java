package com.me.januarygame.rendering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {
	
	private Game game;
	
	SpriteBatch batch;
	
	Sprite bg;
	Sprite text;
	Sprite numbers[];
	Sprite secs[];
	Sprite mins[];
	int text_show = 0;
	
	Music bg_music;
	float volume = 0.0f;
	
	boolean end = false;
	boolean esc = false;
	boolean restart = false;
	
	public GameOverScreen(Game game, int p, long start_time){
		int elapsed_time = Math.round((System.nanoTime()-start_time)/1000000000);
		int s = 0, m = 0;
		for(int i = 0; i < elapsed_time; i++){
			s++;
			if(s % 60 == 0){
				m++;
				s = 0;
			}
		}
		String s_secs = String.valueOf(s).trim();
		secs = new Sprite[s_secs.length()];
		for(int i = 0; i < s_secs.length(); i++){
			secs[i] = new Sprite(new Texture(Gdx.files.internal("data/numbers/"+s_secs.charAt(i)+".png")));
			secs[i].setSize(112, 112);
			secs[i].setPosition(685+(i*57), 250-56);
		}
		String s_mins = String.valueOf(m).trim();
		mins = new Sprite[s_mins.length()];
		for(int i = 0; i < s_mins.length(); i++){
			mins[i] = new Sprite(new Texture(Gdx.files.internal("data/numbers/"+s_mins.charAt(i)+".png")));
			mins[i].setSize(112, 112);
			mins[i].setPosition(320+(i*57), 250-56);
		}
		
		this.game = game;
		
		String points = String.valueOf(p).trim();
		numbers = new Sprite[points.length()];
		for(int i = 0; i < points.length(); i++){
			numbers[i] = new Sprite(new Texture(Gdx.files.internal("data/numbers/"+points.charAt(i)+".png")));
			numbers[i].setSize(128, 128);
			numbers[i].setPosition(710+(i*65), 415-55);
		}
		
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
		
		text_show++;
		
		if(Gdx.input.isKeyPressed(Keys.R)){
			end = true;
			restart = true;
		}
		if(Gdx.input.isKeyPressed(Keys.F12)){
			if(Gdx.graphics.isFullscreen())
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			else
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		}
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			end = true;
			esc = true;
		}
		
		batch.begin();
		bg.draw(batch);
		for(int i = 0; i < numbers.length; i++){
			numbers[i].draw(batch);
		}
		for(int i = 0; i < secs.length; i++){
			secs[i].draw(batch);
		}
		for(int i = 0; i < mins.length; i++){
			mins[i].draw(batch);
		}
		if(text_show > 60){
			text.draw(batch);
			if(text_show >= 120){
				text_show = 0;
			}
		}
		batch.end();
		
		if(restart){
			if(volume <= 0.0f) game.setScreen(new GameScreen(game));
		}
		if(esc){
			if(volume <= 0.0f) Gdx.app.exit();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		bg = new Sprite(new Texture(Gdx.files.internal("data/game_over_screen.png")));
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bg.setPosition(0, 0);
		
		text = new Sprite(new Texture(Gdx.files.internal("data/game_over_screen_text.png")));
		text.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		text.setPosition(0, 0);
		
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
		for(int i = 0; i < numbers.length; i++){
			numbers[i].getTexture().dispose();
		}
		bg.getTexture().dispose();
		text.getTexture().dispose();
		batch.dispose();
		
	}

}
