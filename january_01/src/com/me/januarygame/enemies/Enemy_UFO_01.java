package com.me.januarygame.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me.januarygame.player.Player;
import com.me.januarygame.rendering.GameScreen;

public class Enemy_UFO_01 extends EnemyEntity {
	
	int step = 64;
	int shoot = 0;
	float angle_dir;
	float circle;

	public Enemy_UFO_01(float life, Player player, GameScreen screen, int id, int value) {
		super(life, player, screen, id, value);
		
		double angle = (float) ((Math.random()+Math.random())*Math.PI);
		x = (float) ((Gdx.graphics.getWidth()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.cos(angle)));
		y = (float) ((Gdx.graphics.getHeight()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.sin(angle)));
		
		Vector2 direction = new Vector2(player.getX()-x, player.getY()-y);
		angle_dir = direction.angle();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("data/enemies/enemy_01.png")));
		sprite.setSize(64, 64);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(x, y);
		
		circle = (float) (Math.random()*360);
		
	}

	@Override
	protected void update(float delta) {
		sprite.rotate(1);
		shoot++;
		
		Vector2 direction = new Vector2(player.getX()-x, player.getY()-y);
		float shot_angle = direction.angle();
		if(shoot >= 380+id*100){
			if(!screen.player.isDead()) screen.addShot(sprite.getX()+sprite.getWidth()/2, sprite.getY()+sprite.getHeight()/2, shot_angle+90, "02", 15, id);
			shoot = 0;
		}
		
		x += Math.cos(Math.toRadians(angle_dir))*step*delta + Math.cos(Math.toRadians(circle))*step*delta;
		y += Math.sin(Math.toRadians(angle_dir))*step*delta + Math.sin(Math.toRadians(circle))*step*delta;
		sprite.setPosition(x, y);
		
		circle++;
		if(circle >= 360) circle = 0;
	}

}
