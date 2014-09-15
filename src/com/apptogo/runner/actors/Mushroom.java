package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Mushroom extends Obstacle{

	public enum MushroomAnimationState{
		STATIC, WORKING
	}
	
	private Fixture mushroomFixture;
	
	public Mushroom(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/mushroom.pack", "mushroom", 1, 0.05f, MushroomAnimationState.STATIC);
		super.animate = false;
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.groundBody, "nonKilling");
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50/PPM, 5/PPM, new Vector2(70/PPM, -5/PPM), 0);
		
		mushroomFixture = createFixture(Materials.mushroomBody, shape, "mushroom");
		setOffset(-10/PPM, -35/PPM);

		animationManager.createAnimation(new MyAnimation(0.05f, MushroomAnimationState.WORKING, animationManager.createFrames(8, "mushroom"), false){
			@Override
			public void onAnimationFinished(){
				animationManager.setCurrentAnimationState(MushroomAnimationState.STATIC);
				animate = false;
			}
		});
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		if(mushroomFixture.getUserData().equals("mushroomWorking")){
			animate = true;
			animationManager.setCurrentAnimationState(MushroomAnimationState.WORKING);
			mushroomFixture.setUserData("mushroom");
		}
	}
}
