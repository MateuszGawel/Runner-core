package com.apptogo.runner.actors;

import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Coin extends Obstacle
{	
	public enum CoinAnimationState
	{
		NORMAL
	}
	
	public Coin(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/coin.pack", "coin", 1, 0.03f, CoinAnimationState.NORMAL);
		super.animate = true;
		
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleGhostBody, "active");

		createFixture(Materials.obstacleSensor, "coin");
	}
	
	private void gainCoinResult()
	{
		
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(((UserData)getBody().getUserData()).key.equals("inactive") )
		{
			gainCoinResult();
			
			//przenosze body poza mape(moge takze cos innego ale to dziala OK) i usuwam aktora z jego parenta.
			getBody().setTransform(new Vector2(-100f, 0), 0);
			getBody().setAwake(true);
			getBody().setActive(false);
			this.remove();
		}
	}
}
