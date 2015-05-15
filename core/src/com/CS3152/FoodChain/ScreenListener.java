package com.CS3152.FoodChain;

import com.badlogic.gdx.Screen;

public interface ScreenListener {

		/**
		 * The given screen has made a request to exit its player mode.
		 *
		 * The value exitCode can be used to implement menu options.
		 *
		 * @param screen   The screen requesting to exit
		 * @param exitCode The state of the screen upon exit
		 */
	public void exitScreen(Screen screen, int exitCode, int level);
	

public void exitScreen(Screen screen, int exitCode);
}

