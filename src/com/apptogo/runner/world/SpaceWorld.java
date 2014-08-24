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

public class SpaceWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -10f);

	public Image space4;
	public ParallaxBackground space3;
	public Image space2;
	public RepeatingConstantParallaxBackground space1;
	
	public SpaceWorld(String mapPath, Player player){
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		createBackground();
	}
	
	private void createBackground(){

		space4 = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/space4.png"));
		space4.setPosition(0, 0);
		space4.setWidth(space4.getWidth()/PPM);
		space4.setHeight(space4.getHeight()/PPM);
		background.addActor(space4);
		
		space3 = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/space3.png"), mapSize, 0, character, 0, 0);
		background.addActor(space3);

		space2 = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/space2.png"));
		space2.setPosition(0, 0);
		background.addActor(space2);
		
		space1 = new RepeatingConstantParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/space1.png"), 2, 0, mapSize, 0, 0);
		background.addActor(space1);
		
	}
}
