package com.apptogo.runner.vars;

public class Box2DVars {
	
	public static final float PPM = 20; //20 pixeli to jeden metr
	public static final boolean DRAW_DEBUG = true;

	public static final short BIT_PLAYER = 2;
	public static final short BIT_ENEMY = 4;
	public static final short BIT_GROUND = 8;
	public static final short BIT_KILLING = 16;
	public static final short BIT_WALLSENSOR = 32;

	public static enum GameCharacter{
		NAKED_MAN, MASTER_OF_DISASTER
	}
}
