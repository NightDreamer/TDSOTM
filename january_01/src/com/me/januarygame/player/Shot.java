package com.me.januarygame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Shot {
	
	public int id;
	
	Sprite shot;
	Sprite explo;
	int explo_time = 0;
	int step = 512;
	int power;
	boolean hit = false;
	boolean alive = true;
	
	
	public Shot(float x, float y, float rotation, String type, int power, int id){
		this.id = id;
		this.power = power;
		
		shot = new Sprite(new Texture(Gdx.files.internal("data/weapons/shots/shot_"+type+".png")));
		shot.setSize(16, 16);
		shot.setPosition(x-8, y-8);
		shot.setOrigin(shot.getWidth()/2, shot.getHeight()/2);
		shot.setRotation(rotation);
		
		explo = new Sprite(new Texture(Gdx.files.internal("data/animation/explosion/explosion_animation_01.png")));
		explo.setSize(32, 32);
		explo.setOrigin(explo.getWidth()/2, explo.getHeight()/2);
		
	}
	
	public void update(float delta, SpriteBatch batch){
		float step_x = (float) Math.sin(Math.toRadians(shot.getRotation()));
		float step_y = (float) Math.cos(Math.toRadians(shot.getRotation()));
		if(alive) shot.setPosition(shot.getX()+(step*step_x*delta), shot.getY()-(step*step_y*delta));
		
		if(!hit) shot.draw(batch);
		if(hit){
			explo.draw(batch);
			explo_time++;
			if(explo_time > 15){
				alive = false;
			}
		}
	}
	
	public float getX(){
		return shot.getX();
	}
	public float getY(){
		return shot.getY();
	}
	
	public Rectangle getBounds(){
		return shot.getBoundingRectangle();
	}
	
	public int power(){
		return power;
	}
	
	public float getRotation(){
		return shot.getRotation();
	}
	
	public void dispose(){
		shot.getTexture().dispose();
		explo.getTexture().dispose();
	}
	
	public void setHit(boolean hit, float delta){
		this.hit = hit;
		explo.setPosition((float) (shot.getX() + Math.sin(Math.toRadians(shot.getRotation())*4*delta)), (float) (shot.getY() - Math.cos(Math.toRadians(shot.getRotation())*4*delta)));
		explo.setRotation(shot.getRotation());
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public boolean hit(){
		return hit;
	}

}
