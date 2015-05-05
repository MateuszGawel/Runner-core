package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GravityField extends Obstacle
{
	private Vector2[] worldVertices;
	private GameWorld gameWorld;
	private Shape shape;
	private ParticleEffectActor pooledEffectActor;
	private Array<Vector2> positions, activePositions;
	
	public GravityField(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "gravityField");
		this.gameWorld = gameWorld;
		pooledEffectActor = new ParticleEffectActor("gravityField.p", 120, 120, 120, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		gameWorld.getWorldStage().addActor(this);
		gameWorld.getWorldStage().addActor(pooledEffectActor);
		
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
		positions = new Array<Vector2>();
		activePositions = new Array<Vector2>();
		Array<Vector2> worldVert = new Array(worldVertices);
		
		for(int i=0; i<effectsInRow; i++){
			for(int j=0; j<effectsInColumn; j++){
				Vector2 currentPos = new Vector2(bottomLeft.x+i, bottomLeft.y+j);
				if ( Intersector.isPointInPolygon( worldVert, currentPos ) ){	
					positions.add(currentPos);
				}
			}
		}
	}
	@Override
	public void act(float delta){
		super.act(delta);	
		if(pooledEffectActor.getCharacter()==null)
			pooledEffectActor.setCharacter(gameWorld.player.character);
		Vector2 position = gameWorld.player.character.getBody().getPosition();
		for(int i=positions.size-1; i>=0; i--){
			Vector2 particlePos = positions.get(i);
			if(position.x + 15 > particlePos.x && position.x - 10 < particlePos.x){
				pooledEffectActor.obtainAndStart(particlePos.x, particlePos.y, Runner.SCREEN_WIDTH/2/PPM);
				activePositions.add(particlePos);
				positions.removeIndex(i);
				
			}
		}
		for(int i=activePositions.size-1; i>=0; i--){
		    if(position.x -10 > activePositions.get(i).x || position.x + 15 < activePositions.get(i).x){
		    	positions.add(activePositions.get(i));
				activePositions.removeIndex(i);
		    }
		}
		//pooledEffectActor.toFront();
	}
}
