package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Asteroid;
import com.apptogo.runner.actors.Bomb;
import com.apptogo.runner.actors.Bomb.BombAnimationState;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class SpaceWorld extends GameWorld{
	public static final Vector2 GRAVITY = new Vector2(0f, -10f);

	public Image space, planet2, planet3, asteroid1, asteroid2, asteroid3, asteroid4, asteroid5;
	public ConstantParallaxBackground planet1;
	private Actor asteroidHandler;
	
	private final Array<Asteroid> activeAsteroids = new Array<Asteroid>();
    private Pool<Asteroid> asteroidsPool = new Pool<Asteroid>() {
	    @Override
	    protected Asteroid newObject() {
	    	Asteroid asteroid = new Asteroid();
	    	background.addActor(asteroid);
	    	return asteroid;
	    }
    };
    
    
	public SpaceWorld(String mapPath, Player player){
		super(mapPath, player);
		super.world.setGravity(GRAVITY);
		createBackground();

		Timer.schedule(new Task() {
			@Override
			public void run() {
				Asteroid asteroid = asteroidsPool.obtain();
				asteroid.init();
				activeAsteroids.add(asteroid);
				freePools();
			}
		}, 0.5f, 0.5f);
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
		background.addActor(image);
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

}
