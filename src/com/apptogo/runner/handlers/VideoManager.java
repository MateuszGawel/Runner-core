package com.apptogo.runner.handlers;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;


public class VideoManager 
{
	private static VideoManager INSTANCE;
	public static void create()
	{
		INSTANCE = new VideoManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static VideoManager getInstance()
	{
		return INSTANCE;
	}
	
	private int counter = 1;
	private String timestamp;
	
	private HashMap <String, Pixmap> frames;
	
	public VideoManager()
	{
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
		timestamp = df.format( new Date() );
		
		frames = new HashMap<String, Pixmap>();
	}
		
	public void makeScreenshot()
	{
		Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		frames.put("screenshot" + counter++ + ".png", pixmap);
	}
	
    public void saveScreenshots()
    {
    	FileHandle totalFile = new FileHandle("videoFrames" + timestamp + "/total_" + String.valueOf(frames.keySet().size()) + ".txt");
    	totalFile.writeString( "Czego tu szukasz? :)", false);
    	
    	for(String key : frames.keySet())
    	{
    		try
            {
                FileHandle fh;
                do
                {
                   fh = new FileHandle("videoFrames" + timestamp + "/" + key);
                }
                while (fh.exists());
                
                PixmapIO.writePNG(fh, frames.get(key));
                frames.get(key).dispose();
            }
            catch (Exception e)
            {
            	//pass
            }
    	}
    }

    private Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
            pixels.clear();
        }

        return pixmap;
    }
	
}
