package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GravityField extends Obstacle
{
	private Vector2[] worldVertices;
	private GameWorld gameWorld;
	private Shape shape;
	
	
	public GravityField(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "gravityField");
		this.gameWorld = gameWorld;

		createGravityEffects(object);
	}
	
	private void createGravityEffects(MapObject mapObject)
	{
		
		PolygonShape shape = (PolygonShape)body.getFixtureList().get(0).getShape();
		worldVertices = new Vector2[shape.getVertexCount()];
		
		for(int i=0; i<shape.getVertexCount(); i++){
			worldVertices[i] = new Vector2();
			shape.getVertex(i, worldVertices[i]);
			
			worldVertices[i].x = (worldVertices[i].x + body.getPosition().x -0.5f);
			worldVertices[i].y = (worldVertices[i].y + body.getPosition().y -1.5f);
		}
		
		//getting polygon bounding box
		Vector2 bottomLeft = new Vector2( worldVertices[0].x, worldVertices[0].y );
		Vector2 topRight = new Vector2( worldVertices[0].x, worldVertices[0].y );
		
		for(Vector2 v : worldVertices)
		{   
			if( v.x < bottomLeft.x ) bottomLeft.x = v.x;			
			if( v.x > topRight.x   ) topRight.x = v.x;		
			if( v.y < bottomLeft.y ) bottomLeft.y = v.y;			
			if( v.y > topRight.y   ) topRight.y = v.y;
		}
		
		//calculating effects amount
		int effectsInRow = Math.abs( (int) (topRight.x - bottomLeft.x) );
		int effectsInColumn = Math.abs( (int) (topRight.y - bottomLeft.y) );
		
		
		for(int i=0; i<effectsInRow; i++){
			for(int j=0; j<effectsInColumn; j++){
				if ( Intersector.isPointInPolygon( new Array(worldVertices), new Vector2(bottomLeft.x+i, bottomLeft.y+j) ) ){
					ParticleEffectActor effectActor = new ParticleEffectActor("gravityField.p");
					effectActor.scaleBy(1/PPM);
					effectActor.setPosition(bottomLeft.x+i, bottomLeft.y+j);
					gameWorld.worldBackgroundGroup.addActor(effectActor);
					effectActor.start();
				}
			}
		}
	}
	@Override
	public void act(float delta){
		super.act(delta);	
	}
}
