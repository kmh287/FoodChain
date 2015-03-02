package com.CS3152.FoodChain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.CS3152.FoodChain.GDXRoot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.resizable  = false;
        config.fullscreen = false; // RETINA MACS DO NOT SUPPORT FULLSCREEN (LWJGL Bug)
        config.foregroundFPS = 60;
		new LwjglApplication(new GDXRoot(), config);
	}
}
		