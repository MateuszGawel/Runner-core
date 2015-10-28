package com.apptogo.runner.vars;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class Box2DVars {
	
	public static final float PPM = 64; //64 pixeli to jeden metr
	public static final boolean DRAW_DEBUG = true;
	public static double ZERO_GROUND_POSITION = 655.0348/PPM;
	
	//niezbyt szczesliwe miejsce ale zoomowac chcemy tylko na tel wiec potrzebujemy
	//flagi ktora ustawiamy w launch androida na true :)
	public static boolean ZOOM_CAMERA_IN_REGISTER_SCREEN = false;
	
	public static final short BIT_CHARACTER = 2;
	public static final short BIT_CHARACTER_SENSOR = 8;
	public static final short BIT_WORLD_OBJECT = 32;
	public static final short BIT_ABILITY = 128;
	public static final short BIT_TERRAIN = 256;
	public static final short BIT_BODY_MEMBER = 512;
	
	public static float getShapeWidth(Shape shape)
	{
		if( shape instanceof PolygonShape )
		{
			PolygonShape polygon = (PolygonShape)shape;
			Vector2 vertex = new Vector2();
			
			polygon.getVertex(0, vertex);
			
			float minX = vertex.x;
			float maxX = vertex.x;
			
			for(int i = 1; i < polygon.getVertexCount(); i++ )
			{
				polygon.getVertex(i, vertex);
				if( vertex.x < minX )
				{
					minX = vertex.x;
				}
				if( vertex.x > maxX )
				{
					maxX = vertex.x;
				}
			}

			return (maxX - minX);
		}
		else if( shape instanceof ChainShape )
		{
			ChainShape chain = (ChainShape)shape;
			Vector2 vertex = new Vector2();
			
			chain.getVertex(0, vertex);
			
			float minX = vertex.x;
			float maxX = vertex.x;
			
			for(int i = 1; i < chain.getVertexCount(); i++ )
			{
				chain.getVertex(i, vertex);
				if( vertex.x < minX )
				{
					minX = vertex.x;
				}
				if( vertex.x > maxX )
				{
					maxX = vertex.x;
				}
			}
			
			return (maxX - minX);
		}
		else if( shape instanceof CircleShape )
		{
			CircleShape circle = (CircleShape)shape;
			
			return (circle.getRadius() * 2 );
		}
		else
			return -1.0f;
	}

	public static float getShapeHeight(Shape shape)
	{
		if( shape instanceof PolygonShape )
		{
			PolygonShape polygon = (PolygonShape)shape;
			Vector2 vertex = new Vector2();
			
			polygon.getVertex(0, vertex);
			
			float minY = vertex.y;
			float maxY = vertex.y;
			
			for(int i = 1; i < polygon.getVertexCount(); i++ )
			{
				polygon.getVertex(i, vertex);
				if( vertex.y < minY )
				{
					minY = vertex.y;
				}
				if( vertex.y > maxY )
				{
					maxY = vertex.y;
				}
			}

			return (maxY - minY);
		}
		else if( shape instanceof ChainShape )
		{
			ChainShape chain = (ChainShape)shape;
			Vector2 vertex = new Vector2();
			
			chain.getVertex(0, vertex);
			
			float minY = vertex.y;
			float maxY = vertex.y;
			
			for(int i = 1; i < chain.getVertexCount(); i++ )
			{
				chain.getVertex(i, vertex);
				if( vertex.y < minY )
				{
					minY = vertex.y;
				}
				if( vertex.y > maxY )
				{
					maxY = vertex.y;
				}
			}
			
			return (maxY - minY);
		}
		else if( shape instanceof CircleShape )
		{
			CircleShape circle = (CircleShape)shape;
			
			return (circle.getRadius() * 2 );
		}
		else
			return -1.0f;
	}
}
