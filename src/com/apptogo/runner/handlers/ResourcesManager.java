package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.logger.Logger.LogLevel;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ResourcesManager 
{
	private static ResourcesManager INSTANCE;
	Runner runner;
	
	public static void create()
	{		
		INSTANCE = new ResourcesManager();
		
		Logger.log(INSTANCE, "Manager has been created", LogLevel.LOW);
	}
	public static void destroy()
	{
		Logger.log(INSTANCE, "Manager has been destroyed", LogLevel.LOW);
		
		INSTANCE = null;
	}
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
	public static void prepareManager(Runner runner)
	{
    	getInstance().runner = runner;
    }
	
	//do zaimplementowania bedzie ladowanie tekstur do atlasów	
		
	//---
	private class ScreenMeta
	{
		public ScreenType screenType;
		public AssetManager manager;
		public ArrayList<String> textures;
		public ArrayList<String> textureAtlases;
		public ArrayList<String> musics;
		public ArrayList<String> sounds;
		public String texturesDirectory;
		public String texturesExtension;
		public String textureAtlasesDirectory;
		public String textureAtlasesExtension;
		public String musicsDirectory;
		public String musicsExtension;
		public String soundsDirectory;
		public String soundsExtension;
		
		public ScreenMeta(ScreenType screenType)
		{
			this.screenType = screenType;
			manager = new AssetManager();
			textures = new ArrayList<String>();
			textureAtlases = new ArrayList<String>();
			musics = new ArrayList<String>();
			sounds = new ArrayList<String>();
			
			texturesDirectory = "";
			texturesExtension = "";
			textureAtlasesDirectory = "";
			textureAtlasesExtension = "";
			musicsDirectory = "";
			musicsExtension = "";
			soundsDirectory = "";
			soundsExtension = "";
		}
		
		public void addTextures(String[] textures) { if( textures != null) for(String t: textures) this.textures.add(texturesDirectory+t+texturesExtension); }
		public void addTexture(String texture) { this.textures.add(texturesDirectory+texture+texturesExtension); }
		public void addTextureAtlases(String[] textureAtlases) { if( textureAtlases != null) for(String a: textureAtlases) this.textureAtlases.add(textureAtlasesDirectory+a+textureAtlasesExtension); }
		public void addTextureAtlas(String textureAtlas) { this.textureAtlases.add(textureAtlasesDirectory+textureAtlas+textureAtlasesExtension); }
		public void addMusics(String[] musics) { for(String m: musics) this.musics.add(musicsDirectory+m+musicsExtension); }
		public void addMusic(String music) { this.musics.add(musicsDirectory+music+musicsExtension); }
		public void addSounds(String[] sounds) { for(String s: sounds) this.sounds.add(soundsDirectory+s+soundsExtension); }
		public void addSound(String sound) { this.sounds.add(soundsDirectory+sound+soundsExtension); }

		public void setTexturesDirectory(String texturesDirectory) {
			this.texturesDirectory = texturesDirectory;
		}
		public void setTexturesExtension(String texturesExtension) {
			this.texturesExtension = texturesExtension;
		}
		public void setTextureAtlasesDirectory(String textureAtlasesDirectory) {
			this.textureAtlasesDirectory = textureAtlasesDirectory;
		}
		public void setTextureAtlasesExtension(String textureAtlasesExtension) {
			this.textureAtlasesExtension = textureAtlasesExtension;
		}
		public void setMusicsDirectory(String musicsDirectory) {
			this.musicsDirectory = musicsDirectory;
		}
		public void setMusicsExtension(String musicsExtension) {
			this.musicsExtension = musicsExtension;
		}
		public void setSoundsDirectory(String soundsDirectory) {
			this.soundsDirectory = soundsDirectory;
		}
		public void setSoundsExtension(String soundsExtension) {
			this.soundsExtension = soundsExtension;
		}	
		
		public void loadTextures()
		{
			Logger.log(this, "Loading " + this.textures.size() + " textures for " + ( (screenType == null)?"SPECIAL_SCREENMETA":screenType.toString() ), LogLevel.MEDIUM );
			
			for(String texture: this.textures)
			{
				this.manager.load(texture, Texture.class);
			}
		}
		public void loadTextureAtlases()
		{
			Logger.log(this, "Loading " + this.textureAtlases.size() + " textureAtlases for " + ( (screenType == null)?"SPECIAL_SCREENMETA":screenType.toString() ), LogLevel.MEDIUM );
			
			for(String textureAtlas: this.textureAtlases)
			{
				this.manager.load(textureAtlas, TextureAtlas.class);
			}
		}
		public void loadMusics()
		{
			Logger.log(this, "Loading " + this.musics.size() + " musics for " + ( (screenType == null)?"SPECIAL_SCREENMETA":screenType.toString() ), LogLevel.MEDIUM );
			
			for(String music: this.musics) //tu chyba nie powinien byc string? wyglada mi na niedorobke moja ale bd sie tym martwic jak dojdziemy do dzwiekow
			{
				this.manager.load(music, Music.class);
			}
		}
		public void loadSounds()
		{
			Logger.log(this, "Loading " + this.sounds.size() + " sounds for " + ( (screenType == null)?"SPECIAL_SCREENMETA":screenType.toString() ), LogLevel.MEDIUM );
			
			for(String sound: this.sounds) //i tu jak wyzej
			{
				this.manager.load(sound, Sound.class);
			}
		}
	}
	//---

	private Array<ScreenMeta> screenMetaArray;
	ScreenMeta menuSpecialMeta;
	ScreenMeta gameSpecialMeta;
	ScreenMeta stillSpecialMeta;
	
	private Skin uiskin;
	private Skin guiskin;
		
	public ResourcesManager() 
	{
		screenMetaArray = new Array<ScreenMeta>();
		
		//ADDING SCREENS:
		//-----------------------------------------------------------------------------
		//|1. SPLASH SCREEN
		ScreenMeta splashMeta = new ScreenMeta(ScreenType.SCREEN_SPLASH);
		
		splashMeta.addTexture("gfx/splash/splash.png");
		splashMeta.addTexture("gfx/menu/menuBackgrounds/splashScreenBackground.png");
		
		splashMeta.addTextureAtlas("gfx/splash/logo.pack");
		splashMeta.addTextureAtlas("gfx/splash/dust.pack");
		
		screenMetaArray.add( splashMeta ); 
		
		//|2. MAIN MENU SCREEN
		ScreenMeta mainMenuMeta = new ScreenMeta(ScreenType.SCREEN_MAIN_MENU);		
		screenMetaArray.add( mainMenuMeta );
		
		//|3. GAME SCREEN SINGLE
		ScreenMeta singleGameMeta = new ScreenMeta(ScreenType.SCREEN_GAME_SINGLE);		
		screenMetaArray.add( singleGameMeta );
		
		//|4. GAME SCREEN MULTI
		ScreenMeta multiplayerGameMeta = new ScreenMeta(ScreenType.SCREEN_GAME_MULTI);
		screenMetaArray.add( multiplayerGameMeta );
				
		//|5. LOADING SCREEN
		ScreenMeta loadingMeta = new ScreenMeta(ScreenType.SCREEN_LOADING);
		screenMetaArray.add( loadingMeta );
		
		//|6. CAMPAIGN SCREEN
		ScreenMeta campaignMeta = new ScreenMeta(ScreenType.SCREEN_CAMPAIGN);		
		screenMetaArray.add( campaignMeta );
		
		//|7. MULTIPLAYER SCREEN
		ScreenMeta multiplayerMeta = new ScreenMeta(ScreenType.SCREEN_MULTIPLAYER);		
		screenMetaArray.add( multiplayerMeta );
		
		//|8. WAITING ROOM SCREEN
		ScreenMeta waitingRoomMeta = new ScreenMeta(ScreenType.SCREEN_WAITING_ROOM);
		screenMetaArray.add( waitingRoomMeta );
		
		//ADDING SPECIALS:
		//-----------------------------------------------------------------------------
		//|0. MENU RESOURCES
		menuSpecialMeta = new ScreenMeta(ScreenType.SCREEN_NONE);
		
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
		
		menuSpecialMeta.addTexture("gfx/menu/chainsDecoration.png");
				
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundWildWest.png");
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundForrest.png");
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundSpace.png");
		
		menuSpecialMeta.addTexture("gfx/menu/sliderMask.png");
		
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundWildWest.png");
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundForrest.png");
		menuSpecialMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundSpace.png");

		menuSpecialMeta.addTexture("gfx/menu/starSmallEmpty.png");
		menuSpecialMeta.addTexture("gfx/menu/starSmallFull.png");
		
		//|1. GAME RESOURCES
		gameSpecialMeta = new ScreenMeta(ScreenType.SCREEN_NONE);	
		
		gameSpecialMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.WILDWEST ) );
		gameSpecialMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.FOREST ) );
		gameSpecialMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.SPACE ) );
		
		gameSpecialMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.WILDWEST ) );
		gameSpecialMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.FOREST ) );
		gameSpecialMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.SPACE ) );

		gameSpecialMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.BANDIT ) );
		gameSpecialMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.ARCHER ) );
		gameSpecialMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.ALIEN ) );
		
		//|2. STILL RESOURCES [CONTINUOSLY BEING USED IN MENU AND GAME]
		stillSpecialMeta = new ScreenMeta(ScreenType.SCREEN_NONE);	
		
		stillSpecialMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.BANDIT ) );
		stillSpecialMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.ARCHER ) );
		stillSpecialMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.ALIEN ) );
		
		//|... INITIALIZING SKINS
		this.uiskin = new Skin(Gdx.files.internal("ui/ui/uiskin.json"));
	    this.guiskin = new Skin(Gdx.files.internal("ui/gui/guiskin.json"));
	}
	
	public void adjustResources(GameWorldType worldType, Array<CharacterType> characterTypes, boolean isCampaign)
	{
		Logger.log(this, "Adjusting resources for " + worldType.toString() + ( isCampaign?"[CAMPAIGN]":"" ) + ", with character types like: " + characterTypes, LogLevel.MEDIUM );
		
		/*
		ScreenType desiredScreenType = ScreenType.SCREEN_GAME_SINGLE;
		
		if( !isCampaign ) desiredScreenType = ScreenType.SCREEN_GAME_MULTI;
		
		for(int i = 0; i < screenMetaArray.size; i++)
		{		
			screenMetaArray.get(i).textureAtlases = new ArrayList<String>();
			if( screenMetaArray.get(i).screenType == desiredScreenType )
			{
				screenMetaArray.get(i).textures = new ArrayList<String>();
				
				screenMetaArray.get(i).addTextures( GameWorldType.convertToTexturesList( worldType ) );
				screenMetaArray.get(i).addTextureAtlases( GameWorldType.convertToTextureAtlases( worldType ) );

				if( characterTypes != null)
				{
					for(int j = 0; j < characterTypes.size; j++)
					{			
						//screenMetaArray.get(i).addTextureAtlases( CharacterType.convertToTextureAtlases( characterTypes.get(j) ) );
						screenMetaArray.get(i).addTextures( CharacterType.convertToTexturesList( characterTypes.get(j) ) );
					}
				}
				
				break;
			}
			else continue;
		}*/
	}
	
	//--------- LOADING RESOURCES
	public void loadMenuResources()
	{
		if( menuSpecialMeta.manager.getLoadedAssets() == 0 )
		{
			Logger.log(this, "Loading menu resources", LogLevel.LOW);
			
			loadSpecialResources(menuSpecialMeta);
		}
	}
	
	public void loadGameResources()
	{		
		if( gameSpecialMeta.manager.getLoadedAssets() == 0 )
		{
			Logger.log(this, "Loading game resources", LogLevel.LOW);
			
			loadSpecialResources(gameSpecialMeta);
		}
	}
	
	//still chyba nie jest zbyt szczesliwe ale chodzi mi o resourcy ktore powinny byc caly czas w pamieci - i w menu i w game
	public void loadStillResources()
	{		
		if( stillSpecialMeta.manager.getLoadedAssets() == 0 )
		{
			Logger.log(this, "Loading still resources", LogLevel.LOW);
			
			loadSpecialResources(stillSpecialMeta);
		}
	}
	
	private void loadSpecialResources(ScreenMeta specialMeta)
	{
		specialMeta.loadTextures();
		specialMeta.loadTextureAtlases();
		specialMeta.loadMusics();
		specialMeta.loadSounds();
	}
	
	public void loadResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadResources(screenType);
	}	
	public void loadResources(ScreenType screenType)
	{	
		Logger.log(this, "Loading resources for " + screenType.toString(), LogLevel.LOW);
		
		this.loadTextureAtlases(screenType);
		this.loadTextures(screenType);
		this.loadMusics(screenType);
		this.loadSounds(screenType);
	}
	
	public void loadTextures(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadTextures(screenType);
	}
	public void loadTextures(ScreenType screenType)
	{
		int index = getScreenIndex(screenType);
		screenMetaArray.get(index).loadTextures();
	}
	
	public void loadTextureAtlases(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadTextureAtlases(screenType);
	}
	public void loadTextureAtlases(ScreenType screenType)
	{
		int index = getScreenIndex(screenType);
		screenMetaArray.get(index).loadTextureAtlases();
	}
	
	public void loadMusics(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadMusics(screenType);
	}
	public void loadMusics(ScreenType screenType)
	{	
		int index = getScreenIndex(screenType);
		screenMetaArray.get(index).loadMusics();
	}
	
	public void loadSounds(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadSounds(screenType);
	}
	public void loadSounds(ScreenType screenType)
	{
		int index = getScreenIndex(screenType);
		screenMetaArray.get(index).loadSounds();
	}
	//---------
	
	//--------- UNLOADING RESOURCES
	public void unloadMenuResources()
	{		
		if( menuSpecialMeta.manager.getLoadedAssets() > 0 )
		{
			Logger.log(this, "Unloading menu resources", LogLevel.LOW);
			
			menuSpecialMeta.manager.clear();
		}
	}
	
	public void unloadGameResources()
	{		
		if( gameSpecialMeta.manager.getLoadedAssets() > 0 )
		{
			Logger.log(this, "Unloading game resources", LogLevel.LOW);
			
			gameSpecialMeta.manager.clear();
		}
	}
	
	public void unloadStillResources()
	{		
		if( stillSpecialMeta.manager.getLoadedAssets() > 0 )
		{
			Logger.log(this, "Unloading still resources", LogLevel.LOW);
			
			stillSpecialMeta.manager.clear();
		}
	}
	
	public void unloadAllResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		unloadAllResources(screenType);
	}
	public void unloadAllResources(ScreenType screenType)
	{		
		Logger.log(this, "Unloading all resources for " + screenType.toString(), LogLevel.LOW);
		
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		manager.clear();
		//manager.dispose();
	}
	
	public void unloadResource(BaseScreen screen, String filename)
	{
		ScreenType screenType = screen.getSceneType();
		unloadResource(screenType, filename);
	}
	public void unloadResource(ScreenType screenType, String filename)
	{
		Logger.log(this, "Unloading resource " + filename + ", for " + screenType.toString(), LogLevel.LOW);
		
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		manager.unload(filename);
	}
	
	public void unloadAllApplicationResources()
	{
		//po dispose() cos jest nie tak na tel - jak wyl i wlaczam jeszcze raz to mi leci blad 
		//     09-16 11:41:33.829: E/AndroidRuntime(6746): FATAL EXCEPTION: GLThread 17
		//     09-16 11:41:33.829: E/AndroidRuntime(6746): com.badlogic.gdx.utils.GdxRuntimeException: com.badlogic.gdx.utils.GdxRuntimeException: Cannot run tasks on an executor that has been shutdown (disposed)
		//do sprawdzenia w wolnej chwili
		
		int unloadedAssets = 0;
		
		Logger.log(this, "Unloading all application resources", LogLevel.MEDIUM);
		
		for(ScreenMeta screenMeta: screenMetaArray)
		{
			unloadedAssets += screenMeta.manager.getLoadedAssets();
			
			screenMeta.manager.clear();
			//screenMeta.manager.dispose();
		}
		
		unloadedAssets += menuSpecialMeta.manager.getLoadedAssets();
		unloadedAssets += gameSpecialMeta.manager.getLoadedAssets();
		unloadedAssets += stillSpecialMeta.manager.getLoadedAssets();
		
		menuSpecialMeta.manager.clear();
		//menuSpecialMeta.manager.dispose();
		
		gameSpecialMeta.manager.clear();
		//gameSpecialMeta.manager.dispose();
		
		stillSpecialMeta.manager.clear();
		//stillSpecialMeta.manager.dispose();
		
		screenMetaArray.clear();
		
		Logger.log(this, String.valueOf(unloadedAssets) + " resources has been unloaded.", LogLevel.MEDIUM);
	}
	//---------
	
	//--------- ACCESSING RESOURCES
	public <T> T getResource(BaseScreen screen, String filename)
	{
		ScreenType screenType = screen.getSceneType();
		return getResource(screenType, filename);
	}
	public <T> T getResource(ScreenType screenType, String filename)
	{
		Logger.log(this, "Accessing resource " + filename + ", for " + screenType.toString(), LogLevel.LOW);
		
		try
		{
			int index = getScreenIndex(screenType);
			AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
			
			Logger.log(this, "    - searching in " + screenType.toString() + " manager.", LogLevel.LOW);
			
			return manager.get(filename);
		}
		catch(Exception e)
		{
			try
			{
				Logger.log(this, "    - searching in menu special manager.", LogLevel.LOW);
				
				return menuSpecialMeta.manager.get(filename);
			}
			catch(Exception f)
			{
				try
				{
					Logger.log(this, "    - searching in game special manager.", LogLevel.LOW);
					
					return gameSpecialMeta.manager.get(filename);
				}
				catch(Exception g)
				{
					try
					{
						Logger.log(this, "    - searching in still special manager.", LogLevel.LOW);
						
						return stillSpecialMeta.manager.get(filename);
					}
					catch(Exception h)
					{
						Logger.log(this, "Haven't found " + filename + " resource enywhere!", LogLevel.HIGH);
						
						return null;
					}
				}
			}
		}
	}
	
	public AssetManager getMenuAssetManager()
	{
		return menuSpecialMeta.manager;
	}
	
	public AssetManager getGameAssetManager()
	{
		return gameSpecialMeta.manager;
	}
	
	public AssetManager getStillAssetManager()
	{
		return stillSpecialMeta.manager;
	}
	
	public AssetManager getAssetManager(BaseScreen screen)
	{		
		ScreenType screenType = screen.getSceneType();
		return getAssetManager(screenType);
	}
	public AssetManager getAssetManager(ScreenType screenType)
	{		
		int index = getScreenIndex(screenType);
		return (AssetManager)screenMetaArray.get(index).manager;
	}
	//---------
	
	private int getScreenIndex(ScreenType screenType)
	{
		int index = -1;
		
		for(int i=0; i<screenMetaArray.size; i++)
		{
			if( screenMetaArray.get(i).screenType == screenType )
			{
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public Skin getUiSkin()
	{
		return this.uiskin;//new Skin(Gdx.files.internal("ui/uiskin.json"));
	}
	
	public Skin getGuiSkin()
	{
		return this.guiskin;//new Skin(Gdx.files.internal("gui/guiskin.json"));
	}
}
