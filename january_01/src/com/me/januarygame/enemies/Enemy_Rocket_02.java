package com.me.januarygame.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me.januarygame.player.Player;
import com.me.januarygame.rendering.GameScreen;

public class Enemy_Rocket_02 extends EnemyEntity {
	
	int step = 512;
	float angle_dir;

	public Enemy_Rocket_02(float life, Player player, GameScreen screen, int id, int value) {
		super(life, player, screen, id, value);
		
		super.dmg = 25;
		
		double angle = (float) ((Math.random()+Math.random())*Math.PI);
		x = (float) ((Gdx.graphics.getWidth()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.cos(angle)));
		y = (float) ((Gdx.graphics.getHeight()/2) + ((Gdx.graphics.getWidth()/1.5)*Math.sin(angle)));
		
		Vector2 direction = new Vector2(player.getX()-x, player.getY()-y);
		angle_dir = direction.angle();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("data/enemies/enemy_02.png")));
		sprite.setSize(32, 32);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(x, y);
		sprite.setRotation(angle_dir-90);
		
	}

	@Override
	protected void update(float delta) {		
		x += Math.cos(Math.toRadians(angle_dir))*step*delta;
		y += Math.sin(Math.toRadians(angle_dir))*step*delta;
		sprite.setPosition(x, y);
		
	}

}
