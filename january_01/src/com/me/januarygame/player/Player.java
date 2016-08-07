package com.me.januarygame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.me.januarygame.rendering.GameScreen;

public class Player {
	
	public int id = -1;
	
	GameScreen screen;
	
	Sprite player_ship;
	Sprite player_thrust;
	Sprite explosion[];
	int explo_anim = 0;
	int step = 256;
	int life = 100;
	boolean dead = false;
	boolean alive = true;
	boolean move = false;
	
	Sound explosion_fx;
	boolean explo_fx = false;
	
	
	public Player(GameScreen screen) {
		this.screen = screen;
		player_ship = new Sprite(new Texture(Gdx.files.internal("data/player_ship/player_01.png")));
		player_ship.setSize(64, 64);
		player_ship.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		player_ship.setOrigin((player_ship.getWidth()/2), (player_ship.getHeight()/2));
		player_ship.rotate(180);
		
		player_thrust = new Sprite(new Texture(Gdx.files.internal("data/player_ship/player_01_thrust.png")));
		player_thrust.setSize(64, 64);
		player_thrust.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		player_thrust.setOrigin((player_thrust.getWidth()/2), (player_thrust.getHeight()/2));
		player_thrust.rotate(180);
		
		explosion = new Sprite[3];
		explosion[0] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_01.png")));
		explosion[0].setSize(64, 64);
		explosion[1] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_02.png")));
		explosion[1].setSize(64, 64);
		explosion[2] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_03.png")));
		explosion[2].setSize(64, 64);
		
		explosion_fx = Gdx.audio.newSound(Gdx.files.internal("data/sounds/explosion.wav"));
		
	}
	
	public void render(float delta, SpriteBatch batch){
		if(life <= 0){
			life = 0;
			alive = false;
			for(int i = 0; i < 3; i++){
				explosion[i].setPosition(player_ship.getX(), player_ship.getY());
			}
		}
		if(alive){
			this.update(delta);
			if(!move){
				player_ship.draw(batch);
			}else{
				player_thrust.setPosition(player_ship.getX(), player_ship.getY());
				player_thrust.setRotation(player_ship.getRotation());
				player_thrust.draw(batch);
			}
		}else{
			if(!explo_fx){
				explosion_fx.play();
				explo_fx = true;
			}
			if(explo_anim < 5){
				explosion[0].draw(batch);
			}else if(explo_anim >= 5 && explo_anim < 10){
				explosion[1].draw(batch);
			}else if(explo_anim >= 10 && explo_anim < 15){
				explosion[2].draw(batch);
			}
			explo_anim++;
			if(explo_anim >= 15){
				dead = true;
			}
		}
		
	}
	
	private void update(float delta){
		move = false;
		// movement
		if(Gdx.input.isKeyPressed(Keys.UP)){
			float angle = player_ship.getRotation();
			player_ship.setPosition((float)(player_ship.getX()+(Math.sin(Math.toRadians(angle))*step*delta)), (float)(player_ship.getY()-(Math.cos(Math.toRadians(angle))*step*delta)));
			move = true;
		}
		else if(Gdx.input.isKeyPressed(Keys.DOWN)){
			float angle = player_ship.getRotation();
			player_ship.setPosition((float)(player_ship.getX()-(Math.sin(Math.toRadians(angle))*step*delta)), (float)(player_ship.getY()+(Math.cos(Math.toRadians(angle))*step*delta)));
			move = true;	
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			player_ship.rotate(-2);
					
		}
		else if(Gdx.input.isKeyPressed(Keys.LEFT)){
			player_ship.rotate(2);
					
		}
		// firing
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			if(System.currentTimeMillis() % 6 == 0){
				screen.addShot(player_ship.getX()+player_ship.getWidth()/2, player_ship.getY()+player_ship.getHeight()/2, player_ship.getRotation(), "01", 10, id);
			}
		}
				
				
		if(player_ship.getX() < 0){
			float overlappingPixels = 0-player_ship.getX();
			player_ship.setPosition(player_ship.getX()+overlappingPixels, player_ship.getY());
		} 
		else if(player_ship.getX()+player_ship.getWidth() > Gdx.graphics.getWidth()){
			float overlappingPixels = player_ship.getX()+player_ship.getWidth()-Gdx.graphics.getWidth();
			player_ship.setPosition(player_ship.getX()-overlappingPixels, player_ship.getY());
		}
				
		if(player_ship.getY() < 128){
			float overlappingPixels = 128-player_ship.getY();
			player_ship.setPosition(player_ship.getX(), player_ship.getY()+overlappingPixels);
		}
		else if(player_ship.getY()+player_ship.getHeight() > Gdx.graphics.getHeight()){
			float overlappingPixels = player_ship.getY()+player_ship.getHeight()-Gdx.graphics.getHeight();
			player_ship.setPosition(player_ship.getX(), player_ship.getY()-overlappingPixels);
		}
		
	}
	
	public void subLife(int life){
		this.life -= life;
		if(life <= 0){
			life = 0;
			alive = false;
		}
	}
	
	public void addLife(int life){
		this.life += life;
		if(life > 100){
			life = 100;
		}
	}
	
	public int getLife(){
		return life;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public float getX(){
		return player_ship.getX();
	}
	
	public float getY(){
		return player_ship.getY();
	}
	
	public Rectangle getBounds(){
		return player_ship.getBoundingRectangle();
	}
	
	public void dispose(){
		player_ship.getTexture().dispose();
		player_thrust.getTexture().dispose();
		explosion[0].getTexture().dispose();
		explosion[1].getTexture().dispose();
		explosion[2].getTexture().dispose();
		explosion_fx.dispose();
	}

}
