package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ResourcesManager 
{
	private static ResourcesManager INSTANCE;
	Runner runner;
	
	public static void create()
	{		
		INSTANCE = new ResourcesManager();
	}
	public static void destroy()
	{		
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
	
	//---
	public class ScreenMeta
	{
		public ScreenClass screenClass;
		public AssetManager manager;
		public ArrayList<String> textures;
		public ArrayList<String> textureAtlases;
		
		public ArrayList<String> ignored;
		
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
		
		public Skin skin;
		public String skinFile;
		public String skinAtlas;
		
		public boolean isLoaded;
		
		public ScreenMeta(ScreenClass screenClass)
		{
			this.screenClass = screenClass;
			manager = new AssetManager();
			textures = new ArrayList<String>();
			textureAtlases = new ArrayList<String>();
			
			ignored = new ArrayList<String>();
			
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
		public void addTextureAtlas(String textureAtlas, boolean isSkinAtlas) { this.textureAtlases.add(textureAtlasesDirectory+textureAtlas+textureAtlasesExtension); if( isSkinAtlas ) this.skinAtlas = textureAtlasesDirectory+textureAtlas+textureAtlasesExtension; }
		public void addMusics(String[] musics) { for(String m: musics) this.musics.add(musicsDirectory+m+musicsExtension); }
		public void addMusic(String music) { this.musics.add(musicsDirectory+music+musicsExtension); }
		public void addSounds(String... sounds) { for(String s: sounds) this.sounds.add(soundsDirectory+s+soundsExtension); }
		public void addSound(String sound) { this.sounds.add(soundsDirectory+sound+soundsExtension); }
		
		public void setSkinFile(String skinFile) { this.skinFile = skinFile; }
		
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
				if( !this.ignored.contains(textureAtlas) )
				{
					this.manager.load(textureAtlas, TextureAtlas.class);
				}
			}
			
			if(this.screenClass != ScreenClass.GAME)
			{
				this.manager.finishLoading();
				this.createSkin();
			}
		}
		
		public void createSkin()
		{	
			if( this.skinAtlas != null )
			{
				FileHandle skinFileHandle = Gdx.files.internal( this.skinFile );
				
				this.skin = new Skin( skinFileHandle, (TextureAtlas)this.manager.get(skinAtlas) );
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

	public Array<ScreenMeta> screenMetaArray;
		
	public ResourcesManager() 
	{
		screenMetaArray = new Array<ScreenMeta>();

		//ASSETS FOR SPLASH
		ScreenMeta splashMeta = new ScreenMeta(ScreenClass.SPLASH);
		
		splashMeta.addTextureAtlas("gfx/splash/splashAtlas.pack");
		
		screenMetaArray.add(splashMeta);
		
		//ASSETS FOR STILL [ALWAYS LOADED]
		ScreenMeta stillMeta = new ScreenMeta(ScreenClass.STILL);
		
		stillMeta.addTextureAtlas( "gfx/still/stillBackgroundAtlas.pack" );
		stillMeta.addTextureAtlas( "gfx/still/stillAtlas.pack", true );
		stillMeta.setSkinFile("gfx/still/stillAtlas.json");
				
		screenMetaArray.add(stillMeta);
				
		//ASSETS FOR MENU
		ScreenMeta menuMeta = new ScreenMeta(ScreenClass.MENU);
		
		menuMeta.addTextureAtlas( "gfx/menu/backgroundAtlas0.pack" );
		menuMeta.addTextureAtlas( "gfx/menu/backgroundAtlas1.pack" );
		menuMeta.addTextureAtlas( "gfx/menu/widgetAtlas.pack" );
		menuMeta.addTextureAtlas( "gfx/menu/menuAtlas.pack", true );
		menuMeta.setSkinFile("gfx/menu/menuAtlas.json");	
										
		screenMetaArray.add(menuMeta);
		
		//ASSETS FOR GAME
		ScreenMeta gameMeta = new ScreenMeta(ScreenClass.GAME);
		
		gameMeta.addTextureAtlas("gfx/game/characters/guiAtlas.pack", true);
		
		gameMeta.addTextureAtlas("gfx/game/characters/countdownAtlas.pack");
		gameMeta.addTextureAtlas("gfx/game/characters/charactersAtlas.pack");
		gameMeta.setSkinFile("gfx/game/characters/charactersAtlas.json");
		
		gameMeta.addTextureAtlas( "gfx/game/levels/wildWestAtlas.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/forestAtlas.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/spaceAtlas.pack" );
		
		gameMeta.addTextureAtlas( "gfx/game/levels/wildwestBackground.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/forestBackground.pack" );
		gameMeta.addTextureAtlas( "gfx/game/levels/spaceBackground.pack" );
		
				
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.BANDIT ) );
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.ARCHER ) );
		gameMeta.addSounds( CharacterType.convertToSoundsList( CharacterType.ALIEN ) );
		
		gameMeta.addSounds("mfx/game/levels/countdown1.ogg", "mfx/game/levels/countdown2.ogg", "mfx/game/levels/countdown3.ogg", "mfx/game/levels/countdownGo.ogg", "mfx/game/levels/coin.ogg");
		gameMeta.addSounds("mfx/game/characters/steps.ogg", "mfx/game/characters/land.ogg", "mfx/game/characters/slide.ogg", "mfx/game/characters/jumpSound.ogg");
		
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.WILDWEST ) );
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.FOREST ) );
		gameMeta.addMusics( GameWorldType.convertToMusics( GameWorldType.SPACE ) );
		
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.WILDWEST ) );
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.FOREST ) );
		gameMeta.addSounds( GameWorldType.convertToSounds( GameWorldType.SPACE ) );
		
		screenMetaArray.add(gameMeta);
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
	
	public void loadResources(ScreenType screenType, GameWorldType gameWorldType)
	{			
		int index = getScreenIndex(ScreenClass.GAME);
		
		this.screenMetaArray.get(index).ignored = new ArrayList<String>();
		
		if( gameWorldType == GameWorldType.FOREST )
		{
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/wildWestAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/wildwestBackground.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/spaceAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/spaceBackground.pack" );
		}
		else if( gameWorldType == GameWorldType.WILDWEST )
		{
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/forestAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/forestBackground.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/spaceAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/spaceBackground.pack" );
		}
		else if( gameWorldType == GameWorldType.SPACE )
		{
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/wildWestAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/wildwestBackground.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/forestAtlas.pack" );
			this.screenMetaArray.get(index).ignored.add( "gfx/game/levels/forestBackground.pack" );
		}
		
		ScreenClass screenClass = ScreenType.convertToScreenClass(screenType);
		loadResources(screenClass);
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
		
		if( this.screenMetaArray.get( index ).skin != null )
		{
			this.screenMetaArray.get( index ).skin.dispose();
		}
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
				
		for(ScreenMeta screenMeta: screenMetaArray)
		{
			screenMeta.manager.clear();
			//screenMeta.manager.dispose();
			
			if( screenMeta.skin != null )
			{
				screenMeta.skin.dispose();
			}
		}
				
		screenMetaArray.clear();
	}
	//---------
	
	//--------- ACCESSING RESOURCES
	public AtlasRegion getAtlasRegion(BaseScreen screen, String textureName)
	{
		ScreenType screenType = screen.getSceneType();
		return getResource(screenType, textureName);
	}
	public AtlasRegion getAtlasRegion(ScreenType screenType, String textureName)
	{
		ScreenClass screenClass = ScreenType.convertToScreenClass(screenType);
		return getResource(screenClass, textureName);
	}
	public AtlasRegion getAtlasRegion(ScreenClass screenClass, String textureName)
	{
		int index = getScreenIndex(screenClass);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
					
		Array<TextureAtlas> managerAtlases = new Array<TextureAtlas>();
		manager.getAll(TextureAtlas.class, managerAtlases);
		
		for(TextureAtlas atlas : managerAtlases)
		{
			if( atlas.findRegion(textureName) != null )
			{
				AtlasRegion ar = atlas.findRegion(textureName);
				
				ar.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
				return ar; 
			}
		}

		return null;
	}
	
	public AtlasRegion getAtlasRegion(String textureName)
	{
		for(ScreenMeta screenMeta : screenMetaArray)
		{
			if( screenMeta.isLoaded )
			{
				AssetManager manager = screenMeta.manager;
				
				Array<TextureAtlas> managerAtlases = new Array<TextureAtlas>();
				manager.getAll(TextureAtlas.class, managerAtlases);
				
				for(TextureAtlas atlas : managerAtlases)
				{
					if( atlas.findRegion(textureName) != null )
					{
						return atlas.findRegion(textureName);
					}
				}
			}
		}
		
		return null;
	}
	
	public AtlasRegion[] getAtlasRegionArray(String regionArrayName, int framesCount)
	{
		for(ScreenMeta screenMeta : screenMetaArray)
		{
			if( screenMeta.isLoaded )
			{
				AssetManager manager = screenMeta.manager;
				
				Array<TextureAtlas> managerAtlases = new Array<TextureAtlas>();
				manager.getAll(TextureAtlas.class, managerAtlases);
				
				for(TextureAtlas atlas : managerAtlases)
				{
					Array<AtlasRegion> array = atlas.getRegions();
					
					for(AtlasRegion region : array)
					{
						if(region.name.contains(regionArrayName))
						{
							AtlasRegion[] frames = new AtlasRegion[framesCount];
							
							for(int i = 0; i < framesCount; i++)
							{
								frames[i] = atlas.findRegion(regionArrayName + i);
							}
					
							return frames;
						}
					}
				}
			}
		}

		return null;
	}
	
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
					Logger.log(this, "can't load asset " + filename);
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
	public int getScreenIndex(ScreenClass screenClass)
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
	
	public Skin getUiSkin(BaseScreen screen)
	{
		return getUiSkin( screen.getSceneType() );
	}	
	public Skin getUiSkin(ScreenType screenType)
	{		
		return getUiSkin( ScreenType.convertToScreenClass(screenType) );
	}
	public Skin getUiSkin(ScreenClass screenClass)
	{
		int index = getScreenIndex(screenClass);
		
		return screenMetaArray.get( index ).skin;
	}
}
