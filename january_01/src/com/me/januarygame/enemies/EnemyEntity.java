package com.me.januarygame.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.me.januarygame.player.Player;
import com.me.januarygame.rendering.GameScreen;

public abstract class EnemyEntity {
	
	public int id;
	GameScreen screen;
	
	float x, y;
	float life;
	boolean alive = true;
	boolean dead = false;
	Player player;
	Sprite sprite;
	Sprite explosion[];
	int explo_anim = 0;
	int value;
	int dmg = 0;
	
	boolean visible = false;
	
	Sound explosion_fx;
	boolean explo_fx = false;
	
	public EnemyEntity(float life, Player player, GameScreen screen, int id, int value){
		this.id = id;
		this.life = life;
		this.player = player;
		this.screen = screen;
		this.value = value;
		
		explosion = new Sprite[3];
		explosion[0] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_01.png")));
		explosion[0].setSize(64, 64);
		explosion[1] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_02.png")));
		explosion[1].setSize(64, 64);
		explosion[2] = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_03.png")));
		explosion[2].setSize(64, 64);
		
		explosion_fx = Gdx.audio.newSound(Gdx.files.internal("data/sounds/explosion.wav"));
	}
	
	public void addLife(float life){
		this.life += life;
	}
	
	public void subLife(float life){
		this.life -= life;
	}
	
	public int getDmg(){
		return dmg;
	}
	
	protected abstract void update(float delta);
	
	public void render(float delta, SpriteBatch batch){
		if(x > 0 && x < Gdx.graphics.getWidth()	&& y > 128 && y < Gdx.graphics.getHeight()) visible = true;
		
		if(life <= 0){
			alive = false;
			for(int i = 0; i < 3; i++){
				explosion[i].setPosition(sprite.getX(), sprite.getY());
			}
		}
		if(alive){
			this.update(delta);
			sprite.draw(batch);
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
			if(explo_anim >= 15) dead = true;
		}
	}
	
	public Rectangle getBounds(){
		return sprite.getBoundingRectangle();
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public int getValue(){
		return value;
	}
	
	public float getLife(){
		return life;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void dispose(){
		sprite.getTexture().dispose();
		explosion[0].getTexture().dispose();
		explosion[1].getTexture().dispose();
		explosion[2].getTexture().dispose();
		explosion_fx.dispose();
	}

}
