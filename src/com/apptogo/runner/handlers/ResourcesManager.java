package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenClass;
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
	public class ScreenMeta
	{
		public ScreenClass screenClass;
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
		
		public boolean isLoaded;
		
		public ScreenMeta(ScreenClass screenClass)
		{
			this.screenClass = screenClass;
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
			
			isLoaded = false;
		}
		
		public void addTextures(Array<String> textures) { if( textures != null) for(String t: textures) this.textures.add(texturesDirectory+t+texturesExtension); }
		public void addTextures(String[] textures) { if( textures != null) for(String t: textures) this.textures.add(texturesDirectory+t+texturesExtension); }
		public void addTexture(String texture) { this.textures.add(texturesDirectory+texture+texturesExtension); }
		public void addTextureAtlases(String[] textureAtlases) { if( textureAtlases != null) for(String a: textureAtlases) this.textureAtlases.add(textureAtlasesDirectory+a+textureAtlasesExtension); }
		public void addTextureAtlas(String textureAtlas) { this.textureAtlases.add(textureAtlasesDirectory+textureAtlas+textureAtlasesExtension); }
		public void addMusics(String[] musics) { for(String m: musics) this.musics.add(musicsDirectory+m+musicsExtension); }
		public void addMusic(String music) { this.musics.add(musicsDirectory+music+musicsExtension); }
		public void addSounds(String... sounds) { for(String s: sounds) this.sounds.add(soundsDirectory+s+soundsExtension); }
		public void addSound(String sound) { this.sounds.add(soundsDirectory+sound+soundsExtension); }
		
		public void loadTextures()
		{
	
			for(String texture: this.textures)
			{
				this.manager.load(texture, Texture.class);
			}
		}
		public void loadTextureAtlases()
		{
			
			for(String textureAtlas: this.textureAtlases)
			{
				this.manager.load(textureAtlas, TextureAtlas.class);
			}
		}
		public void loadMusics()
		{
			
			for(String music: this.musics) //tu chyba nie powinien byc string? wyglada mi na niedorobke moja ale bd sie tym martwic jak dojdziemy do dzwiekow
			{
				this.manager.load(music, Music.class);
			}
		}
		public void loadSounds()
		{
			
			for(String sound: this.sounds) //i tu jak wyzej
			{
				this.manager.load(sound, Sound.class);
			}
		}
	}
	//---

	private Array<ScreenMeta> screenMetaArray;
	
	private Skin uiskin;
	private Skin guiskin;
		
	public ResourcesManager() 
	{
		screenMetaArray = new Array<ScreenMeta>();
		
		//ASSETS FOR SPLASH
		ScreenMeta splashMeta = new ScreenMeta(ScreenClass.SPLASH);
		
		splashMeta.addTexture("gfx/splash/splash.png");
		splashMeta.addTexture("gfx/menu/menuBackgrounds/splashScreenBackground.png");
		
		splashMeta.addTexture("gfx/splash/logoSplash.png");
		
		splashMeta.addTextureAtlas("gfx/splash/logoSplashLetterD.pack");
		splashMeta.addTextureAtlas("gfx/splash/dust.pack");
		splashMeta.addTextureAtlas("gfx/splash/loading.pack");
		
		screenMetaArray.add(splashMeta);
		
		//ASSETS FOR STILL [ALWAYS LOADED]
		ScreenMeta stillMeta = new ScreenMeta(ScreenClass.STILL);
		
		stillMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.BANDIT ) );
		stillMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.ARCHER ) );
		stillMeta.addTextureAtlases( CharacterType.convertToTextureAtlases( CharacterType.ALIEN ) );
		
		stillMeta.addTextureAtlas("gfx/splash/loading.pack");
		
		stillMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundWildWest.png");
		stillMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundForrest.png");
		stillMeta.addTexture("gfx/menu/menuBackgrounds/loadingScreenBackgroundSpace.png");
		
		screenMetaArray.add(stillMeta);
		
		//ASSETS FOR MENU
		ScreenMeta menuMeta = new ScreenMeta(ScreenClass.MENU);
		
		menuMeta.addTexture("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
		
		menuMeta.addTexture("gfx/menu/newIcon.png");
		menuMeta.addTexture("gfx/menu/newsfeed.png");
		menuMeta.addTexture("gfx/menu/chainsDecoration.png");
		
		menuMeta.addTexture("gfx/menu/logoMenu.png");
		
		menuMeta.addTexture("gfx/languageFlags/de_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/de.png");
		menuMeta.addTexture("gfx/languageFlags/es_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/es.png");
		menuMeta.addTexture("gfx/languageFlags/in_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/in.png");
		menuMeta.addTexture("gfx/languageFlags/pl_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/pl.png");
		menuMeta.addTexture("gfx/languageFlags/ru_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/ru.png");
		menuMeta.addTexture("gfx/languageFlags/uk_chosen.png");
		menuMeta.addTexture("gfx/languageFlags/uk.png");
		
		menuMeta.addTexture("gfx/game/characters/banditGround.png");
		
		menuMeta.addTextureAtlas("gfx/game/characters/characters.pack");
				
		menuMeta.addTextures( ShopManager.getInstance().getTextures() );
		
		//tempy totalnie do wyjebania
		menuMeta.addTexture("temp/exampleFlag.png");
		menuMeta.addTexture("temp/online.png");
		menuMeta.addTexture("temp/offline.png");
		menuMeta.addTexture("temp/find.png");
		menuMeta.addTexture("temp/coin.png");
		menuMeta.addTexture("temp/diamond.png");
		menuMeta.addTexture("temp/settingsButton.png");
		menuMeta.addTexture("temp/achievementsButton.png");
		menuMeta.addTexture("temp/shopSign.png");
		menuMeta.addTexture("gfx/menu/comingSoon.png");
		//
		
		menuMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundWildWest.png");
		menuMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundForrest.png");
		menuMeta.addTexture("gfx/menu/menuBackgrounds/campaignScreenBackgroundSpace.png");
		
		menuMeta.addTexture("gfx/menu/sliderMask.png");
		
		menuMeta.addTexture("gfx/menu/paperSmall.png");
		menuMeta.addTexture("gfx/menu/blackBoardMedium.png");
		menuMeta.addTexture("gfx/menu/paperBig.png");
		
		menuMeta.addTexture("gfx/menu/shareButtonFacebook.png");
		menuMeta.addTexture("gfx/menu/shareButtonGoogle.png");
		menuMeta.addTexture("gfx/menu/shareButtonTwitter.png");
		
		menuMeta.addTexture("gfx/menu/starSmallEmpty.png");
		menuMeta.addTexture("gfx/menu/starSmallFull.png");
		
		screenMetaArray.add(menuMeta);
		
		//ASSETS FOR GAME
		ScreenMeta gameMeta = new ScreenMeta(ScreenClass.GAME);
		
		gameMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.WILDWEST ) );
		gameMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.FOREST ) );
		gameMeta.addTextures( GameWorldType.convertToTexturesList( GameWorldType.SPACE ) );
		
		gameMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.WILDWEST ) );
		gameMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.FOREST ) );
		gameMeta.addTextureAtlases( GameWorldType.convertToTextureAtlases( GameWorldType.SPACE ) );
		
		gameMeta.addTextureAtlas( "gfx/game/levels/powerup.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/countdown.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/gameGuiAtlas.pack" );

		gameMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.BANDIT ) );
		gameMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.ARCHER ) );
		gameMeta.addTextures( CharacterType.convertToTexturesList( CharacterType.ALIEN ) );
		
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.BANDIT ) );
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.ARCHER ) );
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.ALIEN ) );
		
		gameMeta.addSounds("mfx/game/levels/countdown1.ogg", "mfx/game/levels/countdown2.ogg", "mfx/game/levels/countdown3.ogg", "mfx/game/levels/countdownGo.ogg", "mfx/game/levels/coin.ogg");
		gameMeta.addSounds("mfx/game/characters/steps.ogg", "mfx/game/characters/land.ogg", "mfx/game/characters/slide.ogg");
		
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.WILDWEST ) );
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.FOREST ) );
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.SPACE ) );
		
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.WILDWEST ) );
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.FOREST ) );
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.SPACE ) );
		
		screenMetaArray.add(gameMeta);
		
		//|... INITIALIZING SKINS
		this.uiskin = new Skin(Gdx.files.internal("ui/ui/uiskin.json"));
	    this.guiskin = new Skin(Gdx.files.internal("ui/gui/guiskin.json"));
	}
	
	public void adjustResources(GameWorldType worldType, Array<CharacterType> characterTypes, boolean isCampaign)
	{
		
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
	public void loadResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadResources(screenType);
	}	
	public void loadResources(ScreenType screenType)
	{		
		ScreenClass screenClass = ScreenType.convertToScreenClass(screenType);
		loadResources(screenClass);
	}
	public void loadResources(ScreenClass screenClass)
	{
		int index = getScreenIndex(screenClass);
		
		if( !this.screenMetaArray.get( index ).isLoaded )
		{
			this.screenMetaArray.get( index ).isLoaded = true;
			
			this.loadTextureAtlases(index);
			this.loadTextures(index);
			this.loadMusics(index);
			this.loadSounds(index);
			
			unloadUnnecessary(screenClass);
		}
	}
	
	public void loadTextures(int index)
	{
		screenMetaArray.get(index).loadTextures();
	}
	
	public void loadTextureAtlases(int index)
	{
		screenMetaArray.get(index).loadTextureAtlases();
	}
	
	public void loadMusics(int index)
	{
		screenMetaArray.get(index).loadMusics();
	}
	
	public void loadSounds(int index)
	{
		screenMetaArray.get(index).loadSounds();
	}
	//---------
	
	//--------- UNLOADING RESOURCES
	private void unloadUnnecessary(ScreenClass loadingScreenClass) //tu sa praktycznie reguly co kiedy unloadowac
	{
		if( loadingScreenClass == ScreenClass.MENU )
		{
			int gameIndex = getScreenIndex( ScreenClass.GAME );
			
			if( this.screenMetaArray.get(gameIndex).isLoaded )
			{
				this.unloadAllResources(ScreenClass.GAME);
			}
		}
		else if( loadingScreenClass == ScreenClass.GAME )
		{
			int menuIndex = getScreenIndex( ScreenClass.MENU );
			
			if( this.screenMetaArray.get(menuIndex).isLoaded )
			{
				this.unloadAllResources(ScreenClass.MENU);
			}
		}
	}
	
	/* to jest do wywalenia ale widze ze tu sie dzieja jakies czary i magia wiec zostawiam zakomentowane	
	public void unloadGameResources()
	{		
		if( gameSpecialMeta.manager.getLoadedAssets() > 0 )
		{
			Array<Music> tempMusicsListToStop = new Array<Music>();
			gameSpecialMeta.manager.getAll(Music.class, tempMusicsListToStop);
			
			for(Music music : tempMusicsListToStop){
				music.stop();
				music.dispose();
			}
			tempMusicsListToStop = null;
			
			Array<Sound> tempSoundsListToStop = new Array<Sound>();
			gameSpecialMeta.manager.getAll(Sound.class, tempSoundsListToStop);
			for(Sound sound : tempSoundsListToStop){
				sound.stop();
				sound.dispose();
			}
			tempSoundsListToStop = null;
			
			gameSpecialMeta.manager.clear();
		}
	}*/
		
	public void unloadAllResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		unloadAllResources(screenType);
	}
	public void unloadAllResources(ScreenType screenType)
	{		
		ScreenClass screenClass = ScreenType.convertToScreenClass(screenType);
		unloadAllResources(screenClass);
	}
	public void unloadAllResources(ScreenClass screenClass)
	{		
		int index = getScreenIndex(screenClass);
		
		this.screenMetaArray.get( index ).isLoaded = false;
		
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		manager.clear();
	}
	
	public void unloadResource(BaseScreen screen, String filename)
	{
		ScreenType screenType = screen.getSceneType();
		unloadResource(screenType, filename);
	}
	public void unloadResource(ScreenType screenType, String filename)
	{
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
		
		for(ScreenMeta screenMeta: screenMetaArray)
		{
			unloadedAssets += screenMeta.manager.getLoadedAssets();
			
			screenMeta.manager.clear();
			//screenMeta.manager.dispose();
		}
				
		screenMetaArray.clear();
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
		ScreenClass screenClass = ScreenType.convertToScreenClass(screenType);
		return getResource(screenClass, filename);
	}
	public <T> T getResource(ScreenClass screenClass, String filename)
	{
		try
		{
			int index = getScreenIndex(screenClass);
			AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
			
			return manager.get(filename);
		}
		catch(Exception e)
		{
			//na wszelki wypadek poszukaj jeszcze w assetManagerze Game - jesli ladujesz wlasnie gre to bd potrzebne
			try
			{
				int index = getScreenIndex( ScreenClass.GAME );
				AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
				
				return manager.get(filename);
			}
			catch(Exception f)
			{
				try{
					int index = getScreenIndex( ScreenClass.STILL );
					AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
					
					return manager.get(filename);
				}
				catch(Exception ex){
					Logger.log(this, "can't load asset");
					return null;
				}
			}
		}
	}
	
	public AssetManager getAssetManager(BaseScreen screen)
	{		
		ScreenType screenType = screen.getSceneType();
		return getAssetManager(screenType);
	}
	public AssetManager getAssetManager(ScreenType screenType)
	{		
		ScreenClass screenClass = ScreenType.convertToScreenClass( screenType );
		return getAssetManager(screenClass);
	}
	public AssetManager getAssetManager(ScreenClass screenClass)
	{
		int index = getScreenIndex(screenClass);
		return (AssetManager)screenMetaArray.get(index).manager;
	}
	//---------
	private int getScreenIndex(ScreenType screenType)
	{
		return getScreenIndex( ScreenType.convertToScreenClass(screenType) );
	}
	private int getScreenIndex(ScreenClass screenClass)
	{
		int index = -1;
		
		for(int i=0; i<screenMetaArray.size; i++)
		{
			if( screenMetaArray.get(i).screenClass == screenClass )
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
