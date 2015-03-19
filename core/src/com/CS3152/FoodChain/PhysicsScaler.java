package com.CS3152.FoodChain;

public final class PhysicsScaler {
	
	//Number of pixels per meter
	static float SCALE = 100f;
	
	public static float pixelsToMeters(float x){
		return x/SCALE;
	}
	
	public static float metersToPixels(float x){
		return x*SCALE;
	}
}
