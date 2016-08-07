package com.me.januarygame.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me.januarygame.player.Player;
import com.me.januarygame.rendering.GameScreen;

public class Enemy_Ship01_03 extends EnemyEntity {
	
	int step = 24;
	int shot = 0;

	public Enemy_Ship01_03(float life, Player player, GameScreen screen, int id, int value) {
		super(life, player, screen, id, value);
		
		double angle = (float) ((Math.random()+Math.random())*Math.PI);
		x = (float) ((Gdx.graphics.getWidth()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.cos(angle)));
		y = (float) ((Gdx.graphics.getHeight()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.sin(angle)));
		
		Vector2 direction = new Vector2(player.getX()-x, player.getY()-y);
		float angle_dir = direction.angle();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("data/enemies/enemy_03.png")));
		sprite.setSize(96, 96);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(x, y);
		sprite.setRotation(angle_dir-90);
		
		explosion[0].setSize(96, 96);
		explosion[1].setSize(96, 96);
		explosion[2].setSize(96, 96);
		
	}

	@Override
	protected void update(float delta) {
		Vector2 direction = new Vector2(player.getX()-x, player.getY()-y);
		float angle_dir = direction.angle();
		
		sprite.setRotation(angle_dir-90);
		x += Math.cos(Math.toRadians(angle_dir))*step*delta;
		y += Math.sin(Math.toRadians(angle_dir))*step*delta;
		sprite.setPosition(x, y);
		
		if(shot > 400 && shot % 4 == 0){
			if(!screen.player.isDead()) screen.addShot(sprite.getX()+sprite.getWidth()/2, sprite.getY()+sprite.getHeight()/2, angle_dir+90, "01", 1, id);
		}
		
		shot++;
		if(shot >= 450) shot = 0;
	}

}
