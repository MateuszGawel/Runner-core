package com.apptogo.runner.enums;

public enum WidgetType
{
	SMALL,
	MEDIUM,
	BIG;
	
	public static float getWidth(WidgetType widgetType)
	{
		if(widgetType == WidgetType.SMALL)
		{
			return 800.0f;
		}
		else if(widgetType == WidgetType.MEDIUM)
		{
			return 800.0f;
		}
		else if(widgetType == WidgetType.BIG)
		{
			return 1180.0f;
		}
		else
		{
			return 0.0f;
		}
	}
	
	public static float getHeight(WidgetType widgetType)
	{
		if(widgetType == WidgetType.SMALL)
		{
			return 320.0f;
		}
		else if(widgetType == WidgetType.MEDIUM)
		{
			return 420.0f;
		}
		else if(widgetType == WidgetType.BIG)
		{
			return 700.0f;
		}
		else
		{
			return 0.0f;
		}
	}

	public static String getWidgetBackgroundPath(WidgetType widgetType) 
	{
		if(widgetType == WidgetType.SMALL)
		{
			return "gfx/menu/paperSmall.png";
		}
		else if(widgetType == WidgetType.MEDIUM)
		{
			return "gfx/menu/blackBoardMedium.png";
		}
		else if(widgetType == WidgetType.BIG)
		{
			return "gfx/menu/paperBig.png";
		}
		else
		{
			return null;
		}
	}

	public static boolean showCloseWidgetButton(WidgetType widgetType) 
	{
		if(widgetType == WidgetType.SMALL)
		{
			return false;
		}
		else if(widgetType == WidgetType.MEDIUM)
		{
			return false;
		}
		else if(widgetType == WidgetType.BIG)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}