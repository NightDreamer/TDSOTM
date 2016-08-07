package com.me.january;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.januarygame.GameStarter;

public class Main {
	public static void main(String[] args) {
		
		int width = 0, height = 0;
		
		for(DisplayMode dm : LwjglApplicationConfiguration.getDisplayModes()){
			if(dm.height == dm.width/16*9){
				width = dm.width;
				height = dm.height;
			}
		}
		// 16:9 screen resolution
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "The Dark Side Of The Moon v1.1";
		cfg.useGL20 = false;
		cfg.width = width;
		cfg.height = height;
		cfg.fullscreen = false;
		cfg.addIcon("data/icon.png", FileType.Internal);
		
		new LwjglApplication(new GameStarter(), cfg);
	}
}
