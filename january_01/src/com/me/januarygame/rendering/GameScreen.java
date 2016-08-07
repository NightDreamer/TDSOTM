package com.me.januarygame.rendering;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.*;
import com.me.januarygame.enemies.EnemyEntity;
import com.me.januarygame.enemies.Enemy_Rocket_02;
import com.me.januarygame.enemies.Enemy_Ship01_03;
import com.me.januarygame.enemies.Enemy_UFO_01;
import com.me.januarygame.player.Player;
import com.me.januarygame.player.Shot;

public class GameScreen implements Screen{
	
	private Game game;
	
	public Player player;
	private Array<EnemyEntity> enemies = new Array<EnemyEntity>();
	private Array<Shot> fired_shots = new Array<Shot>();
	
	private SpriteBatch batch;
	private Sprite hud;
	private Sprite bg;
	
	private int time = 0;
	
	private int points = 0;
	private Sprite numbers[];
	
	private long start_time;
	
	private Music bg_music;
	private float volume = 0.0f;
	
	private Sound laser01_fx;
	private Sound laser02_fx;
	
	private boolean end = false;
	private boolean pause = false;
	private int resume = 0;
	
	private Sprite pause_screen;
	private Sprite resume_screen;
	private Sprite pause_bg_sprite = null;
	
	private Sprite heal_orb;
	private boolean heal_orb_spawned = false;
	
	private Sound healing;
	
	boolean esc = false;
	
	Sprite help_screen;
	boolean help = true;
	int helping = 0;
	
	
	public GameScreen(Game game){
		start_time = System.nanoTime();
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
		
		if(help && helping == 2){
			if(pause_bg_sprite == null){
				pause_bg_sprite = new Sprite(ScreenUtils.getFrameBufferTexture());
			}
			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				help = false;
				pause_bg_sprite.getTexture().dispose();
				pause_bg_sprite = null;
				resume = 0;
				return;
			}
			
			batch.begin();
			pause_bg_sprite.draw(batch);
			help_screen.draw(batch);
			if(resume < 60) resume_screen.draw(batch);
			batch.end();
			
			resume++;
			if(resume > 120) resume = 0;
			
			return;
		}
		
		if(pause){
			if(pause_bg_sprite == null){
				pause_bg_sprite = new Sprite(ScreenUtils.getFrameBufferTexture());
			}
			batch.begin();
			pause_bg_sprite.draw(batch);
			pause_screen.draw(batch);
			if(resume < 60) resume_screen.draw(batch);
			batch.end();
			
			if(Gdx.input.isKeyPressed(Keys.E)){
				end = true;
			}
			if(Gdx.input.isKeyPressed(Keys.R)){
				pause_bg_sprite.getTexture().dispose();
				pause_bg_sprite = null;
				game.setScreen(new GameScreen(game));
			}
			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				pause_bg_sprite.getTexture().dispose();
				pause_bg_sprite = null;
				resume = 0;
				pause = false;
				return;
			}
			
			resume++;
			if(resume > 120) resume = 0;
			
			if(end && volume <= 0.0f){
				Gdx.app.exit();
			}
			
			return;
		}
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		this.update(delta);
		
		batch.begin();
		bg.draw(batch);
		
		for(Shot shot : fired_shots){
			if(player.getBounds().contains(shot.getBounds()) && player.id != shot.id && shot.isAlive() && !shot.hit()){
				if(!shot.hit()) player.subLife(shot.power());
				shot.setHit(true, delta);
				continue;
			}
			shot.update(delta, batch);
		}
		
		for(EnemyEntity enemy : enemies){
			if(!enemy.isAlive() && enemy.isDead()){
				enemy.dispose();
				enemies.removeIndex(enemies.indexOf(enemy, true));
				continue;
			}
			
			enemy.render(delta, batch);
			
			for(Shot shot : fired_shots){
				if(enemy.getBounds().contains(shot.getBounds()) && enemy.id != shot.id){
								
					if(!shot.hit()){
						enemy.subLife(shot.power());
						shot.setHit(true, delta);
					}
					if(!enemy.isAlive() && (shot.id == -1)){
						points += enemy.getValue();
						enemy.setValue(0);
						break;
					}
					break;
				}
			}
			
			if(enemy.getClass() == Enemy_Rocket_02.class){
				if(enemy.isAlive() && enemy.getBounds().overlaps(player.getBounds())){
					player.subLife(enemy.getDmg());
					enemy.subLife(enemy.getLife());
					continue;
				}
			}
			
			Rectangle bounds = enemy.getBounds();
			if(bounds.x+bounds.width < 0 || bounds.x > Gdx.graphics.getWidth() || bounds.y+bounds.height < 128 || bounds.y > Gdx.graphics.getHeight()){
				if(enemy.isVisible()){
					enemy.dispose();
					enemies.removeIndex(enemies.indexOf(enemy, true));
					continue;
				}
			}
		}
		
		if(heal_orb_spawned){
			heal_orb.draw(batch);
		}
		
		player.render(delta, batch);
		hud.draw(batch);
		
		String s = String.valueOf(points).trim();
		for(int i = 0; i < s.length(); i++){
			numbers[Integer.parseInt(s.substring(i, i+1))].setPosition(160+(i*16), 75);
			numbers[Integer.parseInt(s.substring(i, i+1))].draw(batch);
		}
		
		String life = String.valueOf(player.getLife()).trim();
		for(int i = 0; i < life.length(); i++){
			numbers[Integer.parseInt(life.substring(i, i+1))].setPosition(135+(i*16), 30);
			numbers[Integer.parseInt(life.substring(i, i+1))].draw(batch);
		}
		
		if(player.getLife() < 10){
			if(!heal_orb_spawned){
				heal_orb.setPosition((float)(Math.random()*Gdx.graphics.getWidth()), (float)(Math.random()*Gdx.graphics.getHeight()));
				heal_orb_spawned = true;
			}
			heal_orb.rotate(1);
			if(player.getBounds().contains(heal_orb.getBoundingRectangle())){
				healing.play(1.0f);
				player.addLife(25);
				heal_orb_spawned = false;
			}
		}
		
		batch.end();
		
		for(Shot shot : fired_shots){
			if(!shot.isAlive()){
				shot.dispose();
				fired_shots.removeIndex(fired_shots.indexOf(shot, true));
				continue;
			}
			else if(shot.getX() < 0-shot.getBounds().width || shot.getX() > Gdx.graphics.getWidth() || shot.getY() < 128-shot.getBounds().height || shot.getY() > Gdx.graphics.getHeight()){
				shot.dispose();
				fired_shots.removeIndex(fired_shots.indexOf(shot, true));
				continue;
			}
		}
		
		if(help){
			helping++;
		}
		
	}
	
	private void update(float delta){
		// exit
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			pause = true;
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.F12)){
			if(Gdx.graphics.isFullscreen())
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			else
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		}
		
		if(enemies.size == 1 || time == 1000){
			enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));
			enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));
			enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));

			if(time % 2 == 0){
				enemies.add(new Enemy_Rocket_02(50, player, this, enemies.size, 15));
			}
			
			if(time % 10 == 0){
				enemies.add(new Enemy_Ship01_03(350, player, this, enemies.size, 50));
			}
		}
		
		if(!player.isAlive() && player.isDead()){
			end = true;
			if(volume <= 0.0f) game.setScreen(new GameOverScreen(game, points, start_time));
		}
		
		time++;
		if(time > 1000) time = 0;
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		player = new Player(this);
		
		numbers = new Sprite[10];
		for(int i = 0; i < numbers.length; i++){
			numbers[i] = new Sprite(new Texture(Gdx.files.internal("data/numbers/"+i+".png")));
			numbers[i].setSize(32, 32);
		}
		
		bg = new Sprite(new Texture(Gdx.files.internal("data/bg.png")));
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bg.setPosition(0, 0);
		bg.setOrigin(bg.getWidth()/2, bg.getHeight()/2);
		
		hud = new Sprite(new Texture(Gdx.files.internal("data/hud.png")));
		hud.setSize(Gdx.graphics.getWidth(), 128);
		hud.setPosition(0f, 0f);
		hud.setOrigin(hud.getWidth()/2, hud.getHeight()/2);
		
		enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));
		enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));
		enemies.add(new Enemy_UFO_01(100, player, this, enemies.size, 25));
		
		bg_music = Gdx.audio.newMusic(Gdx.files.internal("data/music/ingame.mp3"));
		bg_music.setLooping(true);
		bg_music.setVolume(volume);
		
		laser01_fx = Gdx.audio.newSound(Gdx.files.internal("data/sounds/laser_shot.wav"));
		laser02_fx = Gdx.audio.newSound(Gdx.files.internal("data/sounds/circle_shot.wav"));
		
		pause_screen = new Sprite(new Texture(Gdx.files.internal("data/pause.png")));
		pause_screen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pause_screen.setPosition(0, 0);
		resume_screen = new Sprite(new Texture(Gdx.files.internal("data/resume.png")));
		resume_screen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		resume_screen.setPosition(0, 0);
		
		heal_orb = new Sprite(new Texture(Gdx.files.internal("data/misc/heal_orb.png")));
		heal_orb.setSize(32, 32);
		heal_orb.setOrigin(heal_orb.getWidth()/2, heal_orb.getHeight()/2);
		
		healing = Gdx.audio.newSound(Gdx.files.internal("data/sounds/powerup.wav"));
		
		help_screen = new Sprite(new Texture(Gdx.files.internal("data/help.png")));
		help_screen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		help_screen.setPosition(0, 0);

	}

	@Override
	public void hide() {
		this.dispose();
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		for(EnemyEntity enemy : enemies){
			enemy.dispose();
		}
		for(Shot shot : fired_shots){
			shot.dispose();
		}
		for(int i = 0; i < numbers.length; i++){
			numbers[i].getTexture().dispose();
		}
		player.dispose();
		hud.getTexture().dispose();
		bg.getTexture().dispose();
		batch.dispose();
		
		bg_music.dispose();
		laser01_fx.dispose();
		laser02_fx.dispose();
		healing.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	public void addShot(float x, float y, float rotation, String type, int power, int id){
		if(type == "01"){
			laser01_fx.play(0.2f);
		}else if(type == "02"){
			laser02_fx.play(0.3f);
		}
		fired_shots.add(new Shot(x, y, rotation, type, power, id));
	}

}
