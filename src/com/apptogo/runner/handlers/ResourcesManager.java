package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.vars.Box2DVars.GameCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();
	Runner runner;
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
	public static void prepareManager(Runner runner){
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
		
		public void addTextures(String[] textures) { for(String t: textures) this.textures.add(texturesDirectory+t+texturesExtension); }
		public void addTexture(String texture) { this.textures.add(texturesDirectory+texture+texturesExtension); }
		public void addTextureAtlases(String[] textureAtlases) { for(String a: textureAtlases) this.textureAtlases.add(textureAtlasesDirectory+a+textureAtlasesExtension); }
		public void addTextureAtlas(String textureAtlas) { Logger.log(this, textureAtlasesDirectory+textureAtlas+textureAtlasesExtension); this.textureAtlases.add(textureAtlasesDirectory+textureAtlas+textureAtlasesExtension); }
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
	}
	//---
	
	TextureAtlas stickmanAtlas;
	ArrayList<ScreenMeta> screenMetaArray;
		
	public ResourcesManager() 
	{
		screenMetaArray = new ArrayList<ScreenMeta>();
		
		//ADDING SCREENS:
		//-----------------------------------------------------------------------------
		//|1. SPLASH SCREEN
		ScreenMeta splashMeta = new ScreenMeta(ScreenType.SCREEN_SPLASH);
		
		splashMeta.addTexture("gfx/splash/splash.png");
		
		screenMetaArray.add( splashMeta ); 
		
		//|2. MAIN MENU SCREEN
		ScreenMeta mainMenuMeta = new ScreenMeta(ScreenType.SCREEN_MAIN_MENU);
		
		mainMenuMeta.addTexture("ui/menuBackgrounds/mainMenuScreenBackground.png");
		
		screenMetaArray.add( mainMenuMeta );
		
		//|3. GAME SCREEN
		ScreenMeta gameMeta = new ScreenMeta(ScreenType.SCREEN_GAME);
		
		gameMeta.setTexturesDirectory("gfx/game/levels/");
		gameMeta.setTexturesExtension(".png");
		gameMeta.addTextures( new String[]{"mountains","rocks","skyBlue","sand", "barrelSmall", "barrelBig"} );
				
		gameMeta.addTextureAtlas("gfx/game/characters/bandit.pack");
		gameMeta.addTextureAtlas("gfx/game/characters/bomb.pack");
		
		gameMeta.addMusic("mfx/game/gameMusic.ogg");
		
		gameMeta.addSound("mfx/game/gameClick.ogg");
		
		screenMetaArray.add( gameMeta );
		
		//|4. UPGRADE SCREEN
		ScreenMeta upgradeMeta = new ScreenMeta(ScreenType.SCREEN_UPGRADE);
		
		upgradeMeta.addTexture("ui/menuBackgrounds/upgradeScreenBackground.png");
		
		screenMetaArray.add( upgradeMeta );
		
		//|5. LOADING SCREEN
		ScreenMeta loadingMeta = new ScreenMeta(ScreenType.SCREEN_LOADING);
		
		loadingMeta.addTexture("ui/menuBackgrounds/loadingScreenBackground.png");
		
		screenMetaArray.add( loadingMeta );
		
		//|6. CAMPAIGN SCREEN
		ScreenMeta campaignMeta = new ScreenMeta(ScreenType.SCREEN_CAMPAIGN);
		
		campaignMeta.addTexture("ui/menuBackgrounds/campaignScreenBackground.png");
		
		screenMetaArray.add( campaignMeta );
		
		//|7. MULTIPLAYER SCREEN
		ScreenMeta multiplayerMeta = new ScreenMeta(ScreenType.SCREEN_MULTIPLAYER);
		
		multiplayerMeta.addTexture("ui/menuBackgrounds/multiplayerScreenBackground.png");
		
		screenMetaArray.add( multiplayerMeta );

		//|8. CREATE ROOM SCREEN
		ScreenMeta createRoomMeta = new ScreenMeta(ScreenType.SCREEN_CREATE_ROOM);
		
		createRoomMeta.addTexture("ui/menuBackgrounds/createRoomScreenBackground.png");
		
		screenMetaArray.add( createRoomMeta );
		
		//|9. FIND ROOM SCREEN
		ScreenMeta findRoomMeta = new ScreenMeta(ScreenType.SCREEN_FIND_ROOM);
		
		findRoomMeta.addTexture("ui/menuBackgrounds/findRoomScreenBackground.png");
		
		screenMetaArray.add( findRoomMeta );
	}
	
	//--------- LOADING RESOURCES
	public void loadResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadResources(screenType);
	}	
	public void loadResources(ScreenType screenType)
	{	
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
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		for(String texture: (ArrayList<String>)screenMetaArray.get(index).textures) 
		{
			manager.load(texture, Texture.class); 
		}
	}
	
	public void loadTextureAtlases(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadTextureAtlases(screenType);
	}
	public void loadTextureAtlases(ScreenType screenType)
	{Logger.log(this,  "LOADED");
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		for(String textureAtlas: (ArrayList<String>)screenMetaArray.get(index).textureAtlases) 
		{
			manager.load(textureAtlas, TextureAtlas.class); 
		}
	}
	
	public void loadMusics(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadMusics(screenType);
	}
	public void loadMusics(ScreenType screenType)
	{	
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		for(String music: (ArrayList<String>)screenMetaArray.get(index).musics) 
		{
			manager.load(music, Music.class); 
		}
	}
	
	public void loadSounds(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		loadSounds(screenType);
	}
	public void loadSounds(ScreenType screenType)
	{
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		for(String sound: (ArrayList<String>)screenMetaArray.get(index).sounds) 
		{
			manager.load(sound, Sound.class); 
		}
	}
	//---------
	
	//--------- UNLOADING RESOURCES
	public void unloadAllResources(BaseScreen screen)
	{
		ScreenType screenType = screen.getSceneType();
		unloadAllResources(screenType);
	}
	public void unloadAllResources(ScreenType screenType)
	{
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
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		manager.unload(filename);
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
		int index = getScreenIndex(screenType);
		AssetManager manager = (AssetManager)screenMetaArray.get(index).manager;
		
		return manager.get(filename);
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
		
		for(int i=0; i<screenMetaArray.size(); i++)
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
		return new Skin(Gdx.files.internal("ui/uiskin.json"));
	}
	
	public Skin getGuiSkin()
	{
		return new Skin(Gdx.files.internal("gui/guiskin.json"));
	}
}
