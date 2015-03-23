package com.apptogo.runner.enums;

public enum WidgetType
{
	SMALL,
	MEDIUM,
	BIG,
	BLACKBIG,
	FINISH;
	
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
		else if(widgetType == WidgetType.BLACKBIG)
		{
			return 1280.0f;
		}
		else if(widgetType == WidgetType.FINISH)
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
		else if(widgetType == WidgetType.BLACKBIG)
		{
			return 450.0f;
		}
		else if(widgetType == WidgetType.FINISH)
		{
			return 700.0f;
		}
		else
		{
			return 0.0f;
		}
	}

	public static String getWidgetBackgroundRegionName(WidgetType widgetType) 
	{
		if(widgetType == WidgetType.SMALL)
		{
			return "paperSmall";
		}
		else if(widgetType == WidgetType.MEDIUM)
		{
			return "blackBoardMedium";
		}
		else if(widgetType == WidgetType.BIG)
		{
			return "paperBig";
		}
		else if(widgetType == WidgetType.BLACKBIG)
		{
			return null;
		}
		else if(widgetType == WidgetType.FINISH)
		{
			return null;
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
		else if(widgetType == WidgetType.BLACKBIG)
		{
			return false;
		}
		else if(widgetType == WidgetType.FINISH)
		{
			return false;
		}
		else
		{
			return false;
		}
	}
}