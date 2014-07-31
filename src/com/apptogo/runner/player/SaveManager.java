package com.apptogo.runner.player;

import java.util.Date;

import com.apptogo.runner.exception.AnonymousPlayerException;
import com.apptogo.runner.exception.AppWarpConnectionException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** klasa do zapisywania Playera NA DYSKU z opcja synchronizacji z appWarpem */
public class SaveManager 
{
	private static final SaveManager INSTANCE = new SaveManager();
	public static SaveManager getInstance()
	{
		return INSTANCE;
	}
	
	private Preferences save;
	private long lastSynchronization;
	
	/** Oczywiscie trzeba wymyslic jakas bezpieczniejsza metode niz Preferences ale tym sie mozna zajac potem - teraz byle dzialalo 'na zewnatrz' */
	public SaveManager()
	{
		save = Gdx.app.getPreferences("gameSave");
	}
	
	
	public Player loadPlayer()
	{
		Player player = new Player();
		
		String playerName = save.getString("PLAYER_NAME", "");
		String playerPassword = save.getString("PLAYER_PASSWORD", "");
		String unlockedLevels = save.getString("PLAYER_UNLOCKED_LEVELS", "map1.0");
		String currentCharacter = save.getString("PLAYER_CURRENT_CHARACTER", "BANDIT"); //dobrze by tez bylo takie inicjalizacyjne wartosci jakos wyrzucic na zew
		
		player.setName(playerName);
		player.setPassword(playerPassword);
		player.setUnlockedLevels(unlockedLevels);
		player.setCurrentCharacter(currentCharacter);
		
		return player;
	}
	
	public boolean savePlayer(Player player)
	{
		save.putString("PLAYER_NAME", player.getName());
		save.putString("PLAYER_PASSWORD", player.getPassword());
		save.putString("PLAYER_UNLOCKED_LEVELS", player.getUnlockedLevels());
		save.putString("PLAYER_CURRENT_CHARACTER", player.getCurrentCharacter());
		save.flush();
		
		return true; //if(not everything is ok) return false;
	}
	
	/** synchronizuje dane z dysku z danymi na appWarpie */
	public void synchronizeWithAppWarp(Player player) throws AnonymousPlayerException, AppWarpConnectionException
	{
		if( player.isAnonymous() ) throw new AnonymousPlayerException();
		
		//duzo madrych rzeczy tu sie dzieje
		
		//if(everything is ok){
		//lastSynchronization = (new Date()).getTime();	
		//do not throw any exception
		//}
		//else
		throw new AppWarpConnectionException();	
	}
}
