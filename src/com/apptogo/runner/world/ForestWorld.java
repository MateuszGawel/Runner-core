package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class ForestWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -60f);


	public ParallaxBackground tree4;
	public Actor tree3;
	public Actor tree2;
	public Actor tree1;
	
	
	public ForestWorld(String mapPath, Player player){
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		createBackground();
	}
	
	private void createBackground(){


		tree4 = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree4.png"), mapSize, -0.05f, character, 0, 0);
		background.addActor(tree4);
		
		tree3 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree3.png"), -0.4f, -0.03f, mapSize, character, 0, 190/PPM);
		background.addActor(tree3);
		
		tree2 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree2.png"), -0.5f, -0.07f, mapSize, character, 0, 120/PPM);
		background.addActor(tree2);
		
		tree1 = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/tree1.png"), -0.8f, -0.15f, mapSize, character, 0, 80/PPM);
		background.addActor(tree1);
	}
}
