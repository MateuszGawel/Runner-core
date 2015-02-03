package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.actors.Asteroid;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SpaceWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -60f);

	public Image space, planet2, planet3, asteroid1, asteroid2, asteroid3, asteroid4, asteroid5;
	public ConstantParallaxBackground planet1;
	private CustomAction asteroidSpawnAction;
	private final Array<Asteroid> activeAsteroids = new Array<Asteroid>();
    private Pool<Asteroid> asteroidsPool = new Pool<Asteroid>() {
	    @Override
	    protected Asteroid newObject() {
	    	Asteroid asteroid = new Asteroid();
	    	backgroundStage.addActor(asteroid);
	    	return asteroid;
	    }
    };
    
    
    private void createTestBody(float x){
    	BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef;
		
		Body testBody = world.createBody(bodyDef);
		testBody.setUserData( new UserData("testBody") );

		shape.setRadius(14/PPM);
		fixtureDef = Materials.obstacleBody;
		fixtureDef.shape = shape;
		testBody.createFixture(fixtureDef).setUserData( new UserData("testBody") );
		testBody.setTransform(x, 10f,  0);
    }
    
	public SpaceWorld(String mapPath, Player player){
		super(mapPath, player, GameWorldType.SPACE);
		super.world.setGravity(GRAVITY);
		createBackground();
		music = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/spaceMusic.ogg");
		music.setVolume(0.2f);
		
		asteroidSpawnAction = new CustomAction(0.5f, 0) {
			@Override
			public void perform() {
				Asteroid asteroid = asteroidsPool.obtain();
				asteroid.init();
				activeAsteroids.add(asteroid);
				freePools();
			}
		};
		CustomActionManager.getInstance().registerAction(asteroidSpawnAction);
		
		Random random = new Random();
		
		for(int i=0; i<=50; i++)
			createTestBody(random.nextFloat()*1000);
	}
	
	private void createBackground(){

		space = createImage("gfx/game/levels/space.jpg", 0, 0);
		
		//chujowa interpolacja
		//planet1 = new ConstantParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/planet1.png"), 0.05f, -0.04f, 800, 400);
		//background.addActor(planet1);
		
	}
	
	private Image createImage(String path, int posX, int posY){
		Image image = new Image((Texture)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), path));
		image.setPosition(posX/PPM, posY/PPM);
		image.setWidth(image.getWidth()/PPM);
		image.setHeight(image.getHeight()/PPM);
		backgroundStage.addActor(image);
		return image;
	}
	
	private void freePools(){
		Asteroid item;
        int len = activeAsteroids.size;
        for (int i = len; --i >= 0;) {
            item = activeAsteroids.get(i);
            if (item.alive == false) {
            	activeAsteroids.removeIndex(i);
                asteroidsPool.free(item);
            }
        }
	}
	
	@Override
	public void dispose(){
		super.dispose();
		asteroidSpawnAction.setFinished(true);
	}

}
