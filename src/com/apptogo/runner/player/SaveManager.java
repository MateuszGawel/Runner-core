package com.apptogo.runner.player;

import java.util.HashMap;
import java.util.Iterator;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.exception.AnonymousPlayerException;
import com.apptogo.runner.exception.AppWarpConnectionException;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

/** klasa do zapisywania Playera NA DYSKU z opcja synchronizacji z appWarpem */
public class SaveManager 
{
	private static SaveManager INSTANCE;
	public static void create()
	{
		INSTANCE = new SaveManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static SaveManager getInstance()
	{
		return INSTANCE;
	}
	
	private Preferences save;
	private long lastSynchronization;
	
	private final String DEFAULT_NAME = "";
	private final String DEFAULT_PASSWORD = "";
	private final String DEFAULT_UNLOCKED_LEVELS = "map1.0,200;map7.0,0;";
	private final String DEFAULT_CURRENT_CHARACTER = CharacterType.BANDIT.toString();
	private final String DEFAULT_STATISTICS = (new Statistics()).serialize();
	
	/** Oczywiscie trzeba wymyslic jakas bezpieczniejsza metode niz Preferences ale tym sie mozna zajac potem - teraz byle dzialalo 'na zewnatrz' */
	public SaveManager()
	{
		save = Gdx.app.getPreferences("gameSave");
		save.remove("PLAYER_CURRENT_CHARACTER");
	}
	
	
	public Player loadPlayer()
	{
		Player player = new Player();

		String playerName = save.getString("PLAYER_NAME", "");
		String playerPassword = save.getString("PLAYER_PASSWORD", "");
		String unlockedLevels = DEFAULT_UNLOCKED_LEVELS;//save.getString("UNLOCKED_LEVELS", DEFAULT_UNLOCKED_LEVELS);
		CharacterType currentCharacter = CharacterType.parseFromString( save.getString("CURRENT_CHARACTER", DEFAULT_CURRENT_CHARACTER ) );
		
		player.setName(playerName);
		player.setPassword(playerPassword);
		player.setUnlockedLevels(unserializeHashMap( unlockedLevels ) );
		player.setCharacterType(currentCharacter);
		
		if( save.contains("STATISTICS") )
		{
			player.getStatistics().unserialize( save.getString("STATISTICS", DEFAULT_STATISTICS) );
		}
						
		return player;
	}
	
	public boolean savePlayer(Player player)
	{
		save.putString("PLAYER_NAME", player.getName());
		save.putString("PLAYER_PASSWORD", player.getPassword());
		save.putString("UNLOCKED_LEVELS", serializeHashMap( player.getUnlockedLevels() ) );
		save.putString("CURRENT_CHARACTER", player.getCharacterType().toString());
		save.putString("STATISTICS", player.getStatistics().serialize());
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
	
	private String serializeHashMap( HashMap<String, Integer> map )
	{
		String serializedMap = "";
		
		Iterator<String> mapIter = map.keySet().iterator();
		
		while( mapIter.hasNext() )
		{
			String key = mapIter.next();
			int value = map.get(key);
			
			serializedMap += key + "," + String.valueOf( value ) + ";";
		}
		
		return serializedMap;
	}
	
	private HashMap<String, Integer> unserializeHashMap( String serializedMap )
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		Array<String> records = new Array<String>( serializedMap.split(";") );
		
		for(String s: records)
		{
			map.put(s.split(",")[0], Integer.parseInt( s.split(",")[1] ) );
		}
		 
		return map;
	}
}
