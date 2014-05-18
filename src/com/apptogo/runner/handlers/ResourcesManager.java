package com.apptogo.runner.handlers;

import java.util.HashMap;

import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class ResourcesManager {

	//do zaimplementowania bedzie ladowanie tekstur do atlasów
	
	private HashMap<String, Texture> menuTextures;
	private HashMap<String, Music> menuMusic;
	private HashMap<String, Sound> menuSounds;
	
	private HashMap<String, Texture> gameTextures;
	private HashMap<String, Music> gameMusic;
	private HashMap<String, Sound> gameSounds;
	
	Runner runner;
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public ResourcesManager() {

	}
	
	
	/*---- MENU TEXTURES ----*/
	
	public void loadMenuTextures(){
		menuTextures.put("playButton", new Texture(Gdx.files.internal("gfx/menu/playButton.png")));
	}
	public Texture getMenuTexture(String key) {
		return menuTextures.get(key);
	}
	public void removeMenuTexture(String key) {
		Texture tex = menuTextures.get(key);
		if(tex != null) {
			menuTextures.remove(key);
			tex.dispose();
		}
	}
	public void removeAllMenuTextures() {
		 for(Texture tex : menuTextures.values()){
			 tex.dispose();
		 }
		 menuTextures.clear();
	}
	
	
	/*---- MENU MUSIC ----*/
	
	public void loadMenuMusic(){
		menuMusic.put("menuMusic", Gdx.audio.newMusic(Gdx.files.internal("mfx/menu/menuMusic.ogg")));
	}
	
	public Music getMenuMusic(String key) {
		return menuMusic.get(key);
	}
	public void removeMenuMusic(String key) {
		Music m = menuMusic.get(key);
		if(m != null) {
			menuMusic.remove(key);
			m.dispose();
		}
	}
	public void removeAllMenuMusic() {
		 for(Music m : menuMusic.values()){
			 m.dispose();
		 }
		 menuMusic.clear();
	}
	
	
	/*---- MENU SOUNDS ----*/

	public void loadMenuSounds() {
		menuSounds.put("menuClick", Gdx.audio.newSound(Gdx.files.internal("mfx/menu/menuClick.ogg")));
	}
	public Sound getMenuSound(String key) {
		return menuSounds.get(key);
	}
	public void removeMenuSound(String key) {
		Sound sound = menuSounds.get(key);
		if(sound != null) {
			menuSounds.remove(key);
			sound.dispose();
		}
	}
	public void removeAllMenuSounds() {
		 for(Sound s : menuSounds.values()){
			 s.dispose();
		 }
		 menuSounds.clear();
	}
	
	
	
	/*---- GAME TEXTURES ----*/
	
	public void loadGameTextures(){
		gameTextures.put("gameImage", new Texture(Gdx.files.internal("gfx/game/gameImage.png")));
	}
	public Texture getGameTexture(String key) {
		return gameTextures.get(key);
	}
	public void removeGameTexture(String key) {
		Texture tex = gameTextures.get(key);
		if(tex != null) {
			gameTextures.remove(key);
			tex.dispose();
		}
	}
	public void removeAllGameTextures() {
		 for(Texture tex : gameTextures.values()){
			 tex.dispose();
		 }
		 gameTextures.clear();
	}
	
	
	/*---- MENU MUSIC ----*/
	
	public void loadGameMusic(){
		gameMusic.put("gameMusic", Gdx.audio.newMusic(Gdx.files.internal("mfx/game/gameMusic.ogg")));
	}
	
	public Music getGameMusic(String key) {
		return menuMusic.get(key);
	}
	public void removeGameMusic(String key) {
		Music m = gameMusic.get(key);
		if(m != null) {
			gameMusic.remove(key);
			m.dispose();
		}
	}
	public void removeAllGameMusic() {
		 for(Music m : gameMusic.values()){
			 m.dispose();
		 }
		 gameMusic.clear();
	}
	
	
	/*---- MENU SOUNDS ----*/

	public void loadGameSounds() {
		gameSounds.put("gameClick", Gdx.audio.newSound(Gdx.files.internal("mfx/game/gameClick.ogg")));
	}
	public Sound getGameSound(String key) {
		return gameSounds.get(key);
	}
	public void removeGameSound(String key) {
		Sound sound = gameSounds.get(key);
		if(sound != null) {
			gameSounds.remove(key);
			sound.dispose();
		}
	}
	public void removeAllGameSounds() {
		 for(Sound s : gameSounds.values()){
			 s.dispose();
		 }
		 gameSounds.clear();
	}
	
	/*---OTHER---*/
	public static ResourcesManager getInstance(){ return INSTANCE; }
	
    public static void prepareManager(Runner runner){
    	getInstance().runner = runner;
    	
    	getInstance().menuTextures = new HashMap<String, Texture>();
    	getInstance().menuMusic = new HashMap<String, Music>();
    	getInstance().menuSounds = new HashMap<String, Sound>();
    	
    	getInstance().gameTextures = new HashMap<String, Texture>();
    	getInstance().gameMusic = new HashMap<String, Music>();
    	getInstance().gameSounds = new HashMap<String, Sound>();
    }
}
