package com.apptogo.runner.vars;

public class Box2DVars {
	
	public static final float PPM = 64; //64 pixeli to jeden metr
	public static final boolean DRAW_DEBUG = true;
	public static double ZERO_GROUND_POSITION = 655.0348/PPM;
	
	public static final short BIT_PLAYER = 2;
	public static final short BIT_ENEMY = 4;
	public static final short BIT_GROUND = 8;
	public static final short BIT_KILLING = 16;
	public static final short BIT_WALLSENSOR = 32;
	public static final short BIT_FOOTSENSOR = 64;
	public static final short BIT_BOMB = 128;
	public static final short BIT_BARREL = 256;
	
	public static enum GameCharacter{
		ALIEN, BANDIT
	}
}
