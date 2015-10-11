package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Mushroom.MushroomAnimationState;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SmallBomb extends Obstacle implements Poolable {

	public boolean alive;
	private boolean shouldExplode;
	// parameters
	private int level = 3;
	private Character characterOwner;
	private ParticleEffectActor explosion;
	private Body explodeSensor;
	private CustomAction explodeAction;
	private boolean exploding;
	private Vector2 explodePos;

	public ParticleEffectActor currentExplosionParticle;

	public SmallBomb(World world, GameWorld gameWorld) {
		super(new EllipseMapObject(0, 0, 10, 10), world, "bomblvl12", "gfx/game/characters/charactersAtlas.pack");
		this.gameWorld = gameWorld;
		createBody(BodyType.DynamicBody, Materials.obstacleBody, "smallBomb");
		gameWorld.getWorldStage().addActor(this);

		explosion = new ParticleEffectActor("explosion_lvl1.p", 1, 4, 1, 1 / PPM, (TextureAtlas) ResourcesManager.getInstance().getResource(
				ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		FixtureDef fixtureDef = Materials.obstacleSensor;
		CircleShape shape = new CircleShape();
		shape.setPosition(new Vector2(0, 0));
		shape.setRadius(1);
		fixtureDef.shape = shape;

		explodeSensor = world.createBody(bodyDef);
		explodeSensor.createFixture(fixtureDef);
		explodeSensor.setTransform(-100, 0, 0);

		explodeAction = new CustomAction(0.5f) {
			@Override
			public void perform() {
				exploding = false;
			}
		};
	}

	public void init(Character characterOwner, int level) {
		this.level = level;
		alive = true;
		getBody().getFixtureList().get(0).setUserData(new UserData("smallBomb", characterOwner.playerName));
		explodeSensor.getFixtureList().get(0).setUserData(new UserData("bombExplosion", characterOwner.playerName));
		this.characterOwner = characterOwner;
		((UserData) getBody().getUserData()).active = true;
		body.setTransform(characterOwner.getX() + 0.8f, characterOwner.getY() + 1.2f, 0);
		gameWorld.getWorldStage().addActor(explosion);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!((UserData) getBody().getUserData()).active && alive) {
			explosion.obtainAndStart(getBody().getPosition().x, getBody().getPosition().y, 0);
			explodePos = new Vector2(body.getPosition().x, body.getPosition().y);
			exploding = true;
			CustomActionManager.getInstance().registerAction(explodeAction);
			reset();
		}
		if(exploding)
			explodeSensor.setTransform(explodePos, body.getAngle());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 0.5f, 0.5f, getRotation());
	}

	@Override
	public void reset() {
		position.set(-100, 0);
		body.setTransform(position, 0);
		alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isShouldExplode() {
		return shouldExplode;
	}

	public void setShouldExplode(boolean shouldExplode) {
		this.shouldExplode = shouldExplode;
	}
}
