package com.CS3152.FoodChain;

import java.util.HashMap;	
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.assets.*;

/** 
 *  Static controller class for managing sound.
 */
public class SoundController {
	// Static names to the sounds 
		/** Weapon fire sound */
		public static final String TRAP_SOUND = "trap";
		/** Owl sound */
		public static final String OWL_SOUND = "owl_sound";
		
		public static final String WOLF_ANGRY_SOUND = "wolf_angry_sound";

		public static final String PIG_SCARED_SOUND = "pig_scared_sound";
		
		public static final String HUNTER_DEAD_SOUND = "hunter_dead_sound";
		
		/** Hash map storing references to sound assets (after they are loaded) */
		private static HashMap<String, Sound> soundBank; 

		// Files storing the sound references
		/** File to weapon fire */
		private static final String TRAP_FILE = "sounds/trap_dropblop.mp3";
		private static final String OWL_FILE = "sounds/owl.mp3";
		private static final String WOLF_A_FILE = "sounds/growl.mp3";
		private static final String PIG_S_FILE = "sounds/pig_sound.mp3";
		private static final String HUNTER_DEAD_FILE = "sounds/death_sound.mp3";

		/** 
		 * Preloads the assets for this Sound controller.
		 * 
		 * The asset manager for LibGDX is asynchronous.  That means that you
		 * tell it what to load and then wait while it loads them.  This is 
		 * the first step: telling it what to load.
		 * 
		 * @param manager Reference to global asset manager.
		 */
		public static void PreLoadContent(AssetManager manager) {
			manager.load(TRAP_FILE,Sound.class);
			manager.load(OWL_FILE,Sound.class);
			manager.load(WOLF_A_FILE,Sound.class);
			manager.load(PIG_S_FILE,Sound.class);
			manager.load(HUNTER_DEAD_FILE,Sound.class);
		}

		/** 
		 * Loads the assets for this Sound controller.
		 * 
		 * The asset manager for LibGDX is asynchronous.  That means that you
		 * tell it what to load and then wait while it loads them.  This is 
		 * the second step: extracting assets from the manager after it has
		 * finished loading them.
		 * 
		 * @param manager Reference to global asset manager.
		 */
		public static void LoadContent(AssetManager manager) {
			soundBank = new HashMap<String, Sound>();
			if (manager.isLoaded(TRAP_FILE)) {
				soundBank.put(TRAP_SOUND,manager.get(TRAP_FILE,Sound.class));
			}
			if (manager.isLoaded(OWL_FILE)) {
				soundBank.put(OWL_SOUND,manager.get(OWL_FILE,Sound.class));
			}
			if (manager.isLoaded(WOLF_A_FILE)) {
				soundBank.put(WOLF_ANGRY_SOUND,manager.get(WOLF_A_FILE,Sound.class));
			}
			if (manager.isLoaded(PIG_S_FILE)) {
				soundBank.put(PIG_SCARED_SOUND,manager.get(PIG_S_FILE,Sound.class));
			}
			if (manager.isLoaded(HUNTER_DEAD_FILE)) {
				soundBank.put(HUNTER_DEAD_SOUND,manager.get(HUNTER_DEAD_FILE,Sound.class));
			}

		}

		/** 
		 * Unloads the assets for this GameCanvas
		 * 
		 * This method erases the static variables.  It also deletes the
		 * associated textures from the assert manager.
		 * 
		 * @param manager Reference to global asset manager.
		 */
		public static void UnloadContent(AssetManager manager) {
			if (soundBank != null) {
				soundBank.clear();
				soundBank = null;
				manager.unload(TRAP_FILE);
				manager.unload(OWL_FILE);
				manager.unload(WOLF_A_FILE);
				manager.unload(PIG_S_FILE);
				manager.unload(HUNTER_DEAD_FILE);
			}
		}
		
		/**
		 * Returns the sound for the given name
		 *
		 * @return the sound for the given name
		 */
		public static Sound get(String key) {
	        return soundBank.get(key);
	    }
	}
