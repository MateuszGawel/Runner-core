package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Bomb extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body bombBody;
	private Player player;
	private World world;
	
	private float timeToExplode = 2;
	
	public Bomb(Player player, World world){
		this.position = new Vector2();
        this.alive = false;
        this.player = player;
        this.world = world;
        
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef;
		
		bombBody = world.createBody(bodyDef);
		bombBody.setUserData("bomb");
		
		//main fixture
		shape.setRadius(10/PPM);
		fixtureDef = Materials.bombBody;
		fixtureDef.shape = shape;
		bombBody.createFixture(fixtureDef).setUserData("player");
	}

    public void init() {
    	position.set(player.getX(), player.getY());
        bombBody.setTransform(position, 0);
        bombBody.setLinearVelocity(player.getPlayerSpeed()/2, 0);
        alive = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				alive = false;
			}
		}, timeToExplode);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		bombBody.setTransform(position, 0);
        alive = false;
	}
}
