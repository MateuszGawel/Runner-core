package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class GravityField extends Obstacle{
	public ParticleEffectActor effectActor;
	
	public GravityField(MapObject object, World world, GameWorld gameWorld){
		super(object, world);
		//setAnimate(false);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "gravityField");
		
		effectActor = new ParticleEffectActor("gravityField.p");
		effectActor.scaleBy(1/PPM);
		effectActor.setPosition(20, 20);
		//ta pozycja musi byc ustawiana tak jak coiny. Przy okazji co do coinów powinien byæ nowy aktor CoinField zamiast robiæ to na pa³ê w tiledmaploaderze
		//efekt jest 64x64, te kulki beda sie wzajemnie przenikac i tak ma byc. Jak bedzie gotowe to bede poprawia³.
		gameWorld.getWorldStage().addActor(effectActor);
		gameWorld.getWorldStage().addActor(this);
		effectActor.start();
	}
}
