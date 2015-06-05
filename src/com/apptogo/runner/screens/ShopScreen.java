package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.actors.SlotMachine;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.shop.ShopItem;
import com.apptogo.runner.shop.ShopManager;
import com.apptogo.runner.widget.ShopWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ShopScreen extends BaseScreen
{		
	Button backButton;
	
	private Widget shopWidget;
	private ShopWidget descriptionWidget;
	
	private Label coinLabel;
	
	private Table table;
	
	private Animation coinAnimation;
	
	ParticleEffectActor coinCounterEffectActor;
	ParticleEffectActor starExplodeEffectActor;
	
	TextButton signboard;
	
	ScrollPane container;
	
	Group group;
		
	public ShopScreen(Runner runner)
	{
		super(runner);
	}
	
	public void prepare() 
	{	
		setBackground("mainMenuScreenBackground");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
         });
               
        descriptionWidget = new ShopWidget(); //Widget(Align.center, Align.center, 0, WidgetType.SMALL, WidgetFadingType.NONE, true);
        
        createShopWidget();
        
        coinAnimation = new Animation("coin", 16, 0.03f, CharacterAnimationState.IDLE, true, true); // CharacterType.convertToCharacterAnimation(player.getCharacterType(), -340.0f, -220.0f, true);
        coinAnimation.setPosition(315, 333);
        
        coinLabel = new Label(String.valueOf(player.coins), skin, "coinLabel");
        coinLabel.setPosition(350, 320);
        
        
        coinCounterEffectActor = new ParticleEffectActor("losecoins.p", (TextureAtlas)ResourcesManager.getInstance().getResource(this, "gfx/menu/menuAtlas.pack"));
		coinCounterEffectActor.setPosition(coinLabel.getX() -25, 334);
		
		starExplodeEffectActor = new ParticleEffectActor("starGained.p", 1, 4, 1, 1, (TextureAtlas)ResourcesManager.getInstance().getResource(this, "gfx/menu/menuAtlas.pack"));
		       
		signboard = new TextButton("SHOP", skin, "signboard"); // createImage("signboard", 0, 0);
		setCenterPosition(signboard, 175);
		
		
		Image backMachine = createImage("blackNonTransparent", 0, 0);
		backMachine.setSize(700, 600);
		setCenterPosition(backMachine, -350);
		
		backMachine.setX( backMachine.getX() + Runner.SCREEN_WIDTH );
				
		final SlotMachine machine = new SlotMachine("slot", 6);
		machine.setPosition(Runner.SCREEN_WIDTH-195, -120);
		
		final SlotMachine machine2 = new SlotMachine("slot", 6);
		machine2.setPosition(machine.getX()+128, -120);
		
		final SlotMachine machine3 = new SlotMachine("slot", 6);
		machine3.setPosition(machine2.getX()+128, -120);
		
		Image machineS = createImage("machineSlot", 0, 0);
		
		setCenterPosition(machineS, -350);
		machineS.setX( machineS.getX() + Runner.SCREEN_WIDTH );
		
		machineS.addListener(
				
				new ClickListener() {
		            public void clicked(InputEvent event, float x, float y) 
		            {
		            	machine.start(0);
		            	machine2.start(0.1f);
		            	machine3.start(0.25f);
		            }
		         }
				
				);
		
		group = new Group();
		
		final Button moveToLeftButton = new Button(skin, "campaignArrowLeft");
		final Button moveToRightButton = new Button(skin, "campaignArrowRight");
		
		ClickListener moveToLeftListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(0, 0.0f) );
			moveToLeftButton.setVisible(false);
			moveToRightButton.setVisible(true);
		} };
		
		ClickListener moveToRightListener = new ClickListener() { public void clicked(InputEvent event, float x, float y){ 
			group.addAction( getMoveAction(-Runner.SCREEN_WIDTH, 0.0f) );
			moveToLeftButton.setVisible(true);
			moveToRightButton.setVisible(false);
		} };
		
		moveToLeftButton.setPosition(-570.0f + Runner.SCREEN_WIDTH, -100.0f);
		moveToLeftButton.addListener(moveToLeftListener);

		moveToRightButton.setPosition(470.0f, -100.0f);
		moveToRightButton.addListener(moveToRightListener);
		
		
		group.addActor(moveToLeftButton);
		group.addActor(moveToRightButton);
		
		group.addActor(backMachine);
		group.addActor(machine);
		group.addActor(machine2);
		group.addActor(machine3);
		group.addActor(machineS);
		
        group.addActor( shopWidget.actor() );
        
        group.addActor(signboard);
                
        group.addActor( starExplodeEffectActor );
		
        addToScreen( descriptionWidget.actor() );
        addToScreen(backButton);
        
        addToScreen( coinCounterEffectActor );
        
        addToScreen(coinLabel);
        addToScreen(coinAnimation);
        
        addToScreen(group);
	}
	
	private MoveToAction getMoveAction(float x, float y)
	{
		MoveToAction moveToAction = new MoveToAction();
    	moveToAction.setPosition(x, y);
    	moveToAction.setDuration(0.2f);
    	
    	return moveToAction;
	}
	
	private void createShopWidget()
	{
		shopWidget = new Widget(Align.center, -360.0f, 0.0f, WidgetType.BLACKBIG, WidgetFadingType.NONE, false);
	
		shopWidget.setCurrentTab(1);
		
        table = new Table();
        //table.debug();
                
        fillTable();
                
        container = createScroll(table, 690.0f, 520.0f, true);
        //container.debug();
        container.setPosition(-340.0f, -310.0f);
                
        shopWidget.addActorToTab(container, 1);
        
        shopWidget.toggleWidget();
	}
	
	boolean backgr = true;
	
	private void fillTable()
	{
		for(final ShopItem item : ShopManager.getInstance().getShopItems( player ))
        {
        	table.row().pad(10, 0, 0, 0);
        	
        	Table t = new Table();
        	
        	if( backgr ) 
    		{
        		t.setBackground( new TextureRegionDrawable( ResourcesManager.getInstance().getAtlasRegion("widgetBlack") ) );
        		backgr = false;
    		}
        	else 
        	{
        		backgr = true;
        	}
        	
        	Image image = createImage(item.thumbnailName, 0, 0);
        	image.setScaling(Scaling.none);
        	
        	t.add(image).width(120).height(120).pad(30, 30, 30, 10);
        	
        	//description + stars
        	Array<Image> stars = new Array<Image>();
        	Group descriptionGroup = new Group();
        	
        	Label title = new Label(item.title,  skin, "coinLabel");
        	title.setPosition(10, 90);
        	title.setWrap(true);
        	
        	descriptionGroup.addActor( title );
        	
        	for(int i = 0; i < item.maxLevel; i++)
        	{
        		Image star = null;
        		
        		if(i < item.currentLevel)
        		{        			
        			star = this.createImage("starSmallFull", (i*50)+((i+1) * 10), 10);
        		}
        		else
        		{
        			star = this.createImage("starSmallEmpty", (i*50)+((i+1) * 10), 10);	
        		}
        		
    			stars.add(star);
    			
        		descriptionGroup.addActor( star );
        	}
        	
        	t.add(descriptionGroup).width(250).height(150).pad(15, 15, 15, 15);
        	
        	Group buyGroup = new Group();
        	
        	Button buyButton = new Button(this.skin, "buyButton");
        	buyButton.setPosition(35, 45);
        	
        	buyButton.addListener(getBuyListener(item, stars));
        	
        	buyGroup.addActor(buyButton);
        	        	
        	Label cost = new Label(item.getCostLabel(),  skin, "default");
        	cost.setPosition(95 - cost.getWidth()/2, 10);
        	
        	buyGroup.addActor(cost);
        	
        	t.add(buyGroup).width(190).height(150).pad(15, 15, 15, 45);
        	
        	table.add(t);
        }
	}
	
	private SequenceAction getSequence()
	{
		ScaleToAction scaleAction = new ScaleToAction();
		scaleAction.setScale(1.5f);
		scaleAction.setDuration(0.7f);
		scaleAction.setInterpolation(Interpolation.bounceOut);
		
		ScaleToAction scaleDownAction = new ScaleToAction();
		scaleDownAction.setScale(1);
		scaleDownAction.setDuration(0.3f);
		scaleDownAction.setInterpolation(Interpolation.circleIn);
		
		SequenceAction sequence = new SequenceAction(scaleAction, scaleDownAction);
		
		return sequence;
	}
	
	private ClickListener getBuyListener(final ShopItem item, final Array<Image> stars)
	{
		ClickListener listener = new ClickListener() 
		{
            public void clicked(InputEvent event, float x, float y) 
            {            	
            	container.setScrollY( table.getHeight() - event.getTarget().localToAscendantCoordinates(table, new Vector2(0,event.getTarget().getHeight()) ).y - 150 );
            	
            	descriptionWidget.setItem( item );
            	descriptionWidget.toggleWidget();
            	
            	descriptionWidget.setListener(new ClickListener() 
            	{
			        public void clicked(InputEvent event, float x, float y) 
			        {
			        	if( !ShopManager.getInstance().buyShopItem(descriptionWidget.item, player) )
			        	{
			        		descriptionWidget.shakePrice();
			        	}
			        	else
			        	{
			        		int starIndex = item.currentLevel - 1;
			        		
			        		Group parentGroup = stars.get( starIndex ).getParent();
			        		
			        		stars.get( starIndex ).setVisible(false);
			        		
			        		Image newStar = createImage("starSmallFull", stars.get( starIndex ).getX(), stars.get( starIndex ).getY());
			        		newStar.setOrigin(Align.center);
			        		newStar.addAction( getSequence() );
			        		parentGroup.addActor(newStar);
			        				        		
			        		Vector2 newStarPosition = newStar.localToStageCoordinates( new Vector2( newStar.getOriginX(), newStar.getOriginY() ) );
			        		
			        		starExplodeEffectActor.obtainAndStart( newStarPosition.x, newStarPosition.y, 0);
			        		
			        		descriptionWidget.toggleWidget();
			        	}
			        }
			    });
            }
		};
	    
	    return listener;
	}
		
	//nieoptymalne i chujowe to trzeba zrobic madrzej ale na razie chce zeby byl odpowiedni efekt
	private void handleCountdown()
	{
		int value = Integer.parseInt( coinLabel.getText().toString() );
		if( value > player.coins )
		{
			if(coinCounterEffectActor.isComplete()){
				coinCounterEffectActor.getEffect().disallowCompletion();
				coinCounterEffectActor.reset();
				coinCounterEffectActor.start();
			}
			
			if( value - 1 <= player.coins )
			{
				coinLabel.setText(String.valueOf(player.coins));
				coinCounterEffectActor.getEffect().allowCompletion();
			}
			else
			{
				coinLabel.setText(String.valueOf(value - 1));
			}
		}
	}

	public void step()
	{
		handleInput();
		handleCountdown();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			WarpController.getInstance().stopApp();
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
	}
	
	@Override
	public void hide() 
	{
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_SHOP;
	}
}
