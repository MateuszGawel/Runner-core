package com.apptogo.runner.handlers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.exception.AnonymousPlayerException;
import com.apptogo.runner.exception.AppWarpConnectionException;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.settings.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.googlecode.gwt.crypto.client.TripleDesKeyGenerator;

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
	
	private Preferences preferences;
	private Preferences save;
	private TripleDesCipher encryptor; 	
	
	public SaveManager()
	{
		
		preferences = Gdx.app.getPreferences("gamePreferences");
		save = Gdx.app.getPreferences("save");
		
		initializeEncryptor();
	}
		
	public Settings loadSettings()
	{
		Settings settings = new Settings();

		Json json = new Json();

		if( preferences.getString("PREFERENCES", "").equals("") )
		{
			save( settings );
		}
		
		String serializedSettings = preferences.getString("PREFERENCES");
		serializedSettings = decryptString(serializedSettings);

		return json.fromJson(Settings.class, serializedSettings);
	}
	
	public Player loadPlayer()
	{			
		//Player player = new Player("cycek", CharacterType.BANDIT); //tutaj ustawiam sobie imie zebym mogl testowac na 1 kompie
		//player.save();
		Json json = new Json();

		if( save.getString("PLAYER", "").equals("") )
		{
			save( new Player() );
		}
		
		String serializedPlayer = save.getString("PLAYER");
		serializedPlayer = decryptString(serializedPlayer);

		return json.fromJson(Player.class, serializedPlayer);
	}
	
	public void save(Settings settings)
	{
		Json json = new Json();
		
		String settingsToSerialize = json.prettyPrint(settings);
		settingsToSerialize = encryptString(settingsToSerialize);
		
		preferences.putString("PREFERENCES", settingsToSerialize );
		preferences.putString("CHECKSUM", getMD5CheckSum(settingsToSerialize));		
		preferences.putString("DATE", (new Date()).toString() );
		preferences.flush();
	}
	
	public void save(Player player)
	{
		Json json = new Json();

		String playerToSerialize = json.prettyPrint(player);
		playerToSerialize = encryptString(playerToSerialize);
		
		save.putString("PLAYER", playerToSerialize );
		save.putString("CHECKSUM", getMD5CheckSum(playerToSerialize));
		save.putString("DATE", (new Date()).toString() );
		save.flush();
	}
	
	public String getChecksum()
	{
		return save.getString("CHECKSUM");
	}
	
	public Date getDate()
	{
		String dateString = save.getString("DATE");
				
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		
		Date date = null;
		
		try 
		{
			date = df.parse( dateString );
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		} 
		
		return date;
	}
	
	/** synchronizuje dane z dysku z danymi na appWarpie */
	public void synchronizeWithAppWarp(Player player) throws AnonymousPlayerException, AppWarpConnectionException
	{
		if( player.isAnonymous() ) throw new AnonymousPlayerException();
		
		//pobranie jsona z appWarp, deszyfrowanie, parsowanie do jSona, porownanie daty i checksumy, lub w druga strone (w zaleznosci od daty)
		throw new AppWarpConnectionException();	
	}
	
	private void initializeEncryptor()
	{
		TripleDesKeyGenerator generator = new TripleDesKeyGenerator();
		byte[] key = generator.decodeKey("04578a8f0be3a7109d9e5e86839e3bc41654927034df92ec");
		
		encryptor = new TripleDesCipher();
		encryptor.setKey(key);
	}
	
	private String encryptString(String string)
	{
		try 
		{
			string = encryptor.encrypt( string );
		} 
		catch (DataLengthException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IllegalStateException e1) 
		{
			e1.printStackTrace();
		} 
		catch (InvalidCipherTextException e1) 
		{
			e1.printStackTrace();
		}
		
		return string;
	}
	
	private String decryptString(String string)
	{
		try 
		{
			string = encryptor.decrypt(string);
		} 
		catch (DataLengthException e) 
		{
			e.printStackTrace();
		} catch (IllegalStateException e) 
		{
			e.printStackTrace();
		} catch (InvalidCipherTextException e)
		{
			e.printStackTrace();
		}
		
		return string;
	}

	private String getMD5CheckSum(String string)
	{
		MessageDigest msg = null;
		
		try 
		{
			msg = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		msg.update(string.getBytes(), 0, string.length());
		
		String md5 = new BigInteger(1, msg.digest()).toString(16);
		
		while(md5.length() < 32)
		{
			md5 += "0" + md5;
		}
		
		return md5;
	}
}
