package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Powerup extends Obstacle{

	private float stateTime = 0;
	private float timeElapsed = 0;
	private float interval = 1f;
	private Vector2 velocity = new Vector2(0, 0.2f);
	
	public enum PowerupAnimationState
	{
		NORMAL
	}
	
	public Powerup(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/powerup.pack", "powerup", 36, 0.03f, PowerupAnimationState.NORMAL);
		super.animate = true;
		
		String userData = "powerup";
		
		//losowanie powerupa - na razie na abilities to robie [zeby bylo byle co] ale trzeba bedzie dodac nowy enum
		//info o powerupie dodaje w userdata bo musze jakos w contactListenerze miec do tego dostep
		PowerupType powerup = PowerupType.getRandom();
		userData += "|" + powerup.toString();
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.KinematicBody, Materials.obstacleGhostBody, "active");
		createFixture(Materials.obstacleSensor, userData);
		setOffset(-40f/PPM, -35f/PPM);
	}
	
	private void tutajNalezyZaimplementowacToCoSieDziejeKiedyGraczPodniesieSkrzynkeZwanaPrzezNasTakzePowerupem(){
		
	}
	
	@Override
	public void act(float delta){
		super.act(delta);	
		stateTime += delta;
		if((stateTime - timeElapsed)/interval >= 1){
			timeElapsed+=interval;
			velocity.y *= -1;
		}
		getBody().setLinearVelocity(velocity);
		
		if(getBody().getUserData().equals("inactive")){
			//tutaj mozna odpalic animacjê zbierania )np jakies gwiazdki albo zmniejszenie sie skrzynki do 0 albo cokolwiek innego.
			tutajNalezyZaimplementowacToCoSieDziejeKiedyGraczPodniesieSkrzynkeZwanaPrzezNasTakzePowerupem();
			
			//przenosze body poza mape(moge takze cos innego ale to dziala OK) i usuwam aktora z jego parenta.
			getBody().setTransform(new Vector2(-100f, 0), 0);
			this.remove();
		}
	}
}
