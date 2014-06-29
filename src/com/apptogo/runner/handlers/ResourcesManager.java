package com.apptogo.runner.handlers;

import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars.GameCharacter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ResourcesManager {

	//do zaimplementowania bedzie ladowanie tekstur do atlasów
	
	Runner runner;
	AssetManager menuManager;
	AssetManager gameManager;
	AssetManager splashManager;
	
	TextureAtlas stickmanAtlas;
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public ResourcesManager() {

	}
	
	/*---SPLASH---*/
	
	public void loadSplashTextures(){
		splashManager.load("gfx/splash/splash.png", Texture.class);
		splashManager.load("gfx/splash/background.png", Texture.class);
		splashManager.load("gfx/splash/frame-background.png", Texture.class);
		splashManager.load("gfx/splash/frame.png", Texture.class);
		splashManager.load("gfx/splash/bar.png", Texture.class);
		splashManager.load("gfx/splash/hidden.png", Texture.class);
	}
	
	public <T> T getSplashResource(String key) {
		return splashManager.get(key);
	}
	
	public void unloadSplashResource(String key) {
		if(splashManager.get(key) != null)
			splashManager.unload(key);
	}
	
	public void unloadAllSplashResources() {
		splashManager.clear();
		 //splashManager.dispose();
	}
	
	
	/*---- MENU----*/
	
	public void loadMenuTextures(){
		menuManager.load("gfx/menu/singlePlayer.png", Texture.class);
		menuManager.load("gfx/menu/multiPlayer.png", Texture.class);
		menuManager.load("gfx/menu/player1.jpg", Texture.class);
		menuManager.load("gfx/menu/player2.jpg", Texture.class);
		menuManager.load("gfx/menu/level1.png", Texture.class);
		menuManager.load("gfx/menu/level2.png", Texture.class);
		menuManager.load("gfx/menu/back.png", Texture.class);
		menuManager.load("gfx/menu/start.png", Texture.class);
		/*
		menuManager.load("gfx/menu/big1.png", Texture.class);
		menuManager.load("gfx/menu/big2.png", Texture.class);
		menuManager.load("gfx/menu/big3.png", Texture.class);
		menuManager.load("gfx/menu/big4.png", Texture.class);
		menuManager.load("gfx/menu/big5.png", Texture.class);
		menuManager.load("gfx/menu/big6.png", Texture.class);
		menuManager.load("gfx/menu/big7.png", Texture.class);
		*/
	}
	
	public void loadMenuMusic(){
		menuManager.load("mfx/menu/menuMusic.ogg", Music.class);
	}
	
	public void loadMenuSounds() {
		menuManager.load("mfx/menu/menuClick.ogg", Sound.class);
	}
	
	public <T> T getMenuResource(String key) {
		return menuManager.get(key);
	}
	
	public <T> T getMenuResource(GameCharacter character) {
		if(character == GameCharacter.BANDIT)
			return menuManager.get("gfx/menu/player1.jpg");
		else if(character == GameCharacter.ALIEN)
			return menuManager.get("gfx/menu/player2.jpg");
		else
			return null;
	}
	
	public void unloadMenuResource(String key) {
		if(menuManager.get(key) != null)
			menuManager.unload(key);
	}
	
	public void unloadAllMenuResources() {
		 menuManager.clear();
		 //menuManager.dispose();
	}
	
	
	
	/*---GAME---*/
	
	public void loadGameTextures(){
		gameManager.load("gfx/game/characters/bandit.pack", TextureAtlas.class);
		gameManager.load("gfx/game/levels/mountains.png", Texture.class);
		gameManager.load("gfx/game/levels/rocks.png", Texture.class);
		gameManager.load("gfx/game/levels/skyBlue.png", Texture.class);
		gameManager.load("gfx/game/levels/sand.png", Texture.class);
	}
	
	public void loadGameMusic(){
		gameManager.load("mfx/game/gameMusic.ogg", Music.class);
	}
	
	public void loadGameSounds() {
		gameManager.load("mfx/game/gameClick.ogg", Sound.class);
	}
	
	public <T> T getGameResource(String key) {
		return gameManager.get(key);
	}
	
	public void unloadGameResource(String key) {
		if(gameManager.get(key) != null)
			gameManager.unload(key);
	}
	
	public void unloadAllGameResources() {
		gameManager.clear();
		 //gameManager.dispose();
	}
	
	
	/*---OTHER---*/
	public static ResourcesManager getInstance(){ return INSTANCE; }
	
    public static void prepareManager(Runner runner){
    	getInstance().runner = runner;
    	getInstance().menuManager = new AssetManager();
    	getInstance().gameManager = new AssetManager();
    	getInstance().splashManager = new AssetManager();
    }
    
    public AssetManager getMenuManager(){ return this.menuManager; }
    public AssetManager getGameManager(){ return this.gameManager; }
    public AssetManager getSplashManager(){ return this.splashManager; }
}
