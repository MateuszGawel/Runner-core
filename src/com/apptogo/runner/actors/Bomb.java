package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Bomb extends Obstacle implements Poolable{

	public boolean alive;
	private Bandit player;
	private Fixture explodeSensor;
	private float timeToExplode = 2;
	private float explosionRange = 2;
	public enum BombAnimationState{
		NORMAL, EXPLODING
	}
	
	public Bomb(Bandit player, World world, GameWorld gameWorld){
		super(new EllipseMapObject(0,0,32,32), world, "coin", 5, 0.03f, BombAnimationState.NORMAL, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		this.player = player;
		animationManager.createAnimation(new MyAnimation(0.03f, BombAnimationState.EXPLODING, animationManager.createFrames(6, "coin"), false){
			@Override
			public void onAnimationFinished(){
				alive = false;
		    	animationManager.setCurrentAnimationState(BombAnimationState.NORMAL);
		    	((UserData)explodeSensor.getUserData()).ignoreContact = true;
			}
		});
		
		createBody(BodyType.DynamicBody, Materials.bombBody, "bomb");
		
		CircleShape shape = new CircleShape();
		shape.setRadius(explosionRange);
		shape.setPosition(new Vector2(0.25f, 0.25f));
		explodeSensor = createFixture(Materials.obstacleSensor, shape, "bombExplosion");
		((UserData)explodeSensor.getUserData()).ignoreContact = true;
		
		gameWorld.getWorldStage().addActor(this);

		animationManager.setCurrentAnimationState(BombAnimationState.NORMAL);
		currentFrame = animationManager.animate(0f);
	}

    public void init(String ownerPlayer) {

        body.setTransform(player.getX()-20/PPM, player.getY(), 0);
        body.setLinearVelocity(player.getSpeed()/3, 0);
        body.setUserData(new UserData("bomb", ownerPlayer));
        alive = true;

		CustomActionManager.getInstance().registerAction(new CustomAction(timeToExplode) {	
			@Override
			public void perform() {
				((UserData)explodeSensor.getUserData()).ignoreContact = false;
				animationManager.setCurrentAnimationState(BombAnimationState.EXPLODING);
			}
		});	
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		body.setTransform(position, 0);
        alive = false;
	}
}
