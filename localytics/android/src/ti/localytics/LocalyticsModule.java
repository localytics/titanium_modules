/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.localytics;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollDict;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import com.localytics.android.*;
import android.app.Activity;
import android.os.Bundle;
import android.content.res.*;
import android.content.*;
import java.io.*;
import java.util.*;

@Kroll.module(name="Localytics", id="ti.localytics")
public class LocalyticsModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "LocalyticsModule";
	private static final boolean DBG = TiConfig.LOGD;
	private static LocalyticsSession localyticsSession;
	private static List<String> customDimensions;
	
	public LocalyticsModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{		
		LocalyticsModule.customDimensions = LocalyticsModule.unarchiveCustomDimensions();
		TiApplication appContext = TiApplication.getInstance();
		Resources resources = appContext.getResources();
		AssetManager assetManager = resources.getAssets();
		Properties properties = new Properties();

		try
		{
			InputStream inputStream = assetManager.open("localytics.properties");
			properties.load(inputStream);
		}
		catch (IOException e) {}
		
		String appKey = properties.getProperty("appkey");
		LocalyticsModule.localyticsSession = new LocalyticsSession(appContext, appKey);
		LocalyticsModule.localyticsSession.open(LocalyticsModule.customDimensions);
		LocalyticsModule.localyticsSession.upload();
	}
	
	public void onCreate(Activity activity) {
		super.onStart(activity);
				
        this.localyticsSession.open(this.customDimensions);
	}
	
	public void onStart(Activity activity) {
		super.onStart(activity);
		
		this.localyticsSession.open(this.customDimensions);
	}
	
	public void onPause(Activity activity) {
		
		this.localyticsSession.close(this.customDimensions);
	    this.localyticsSession.upload();
	    
		super.onPause(activity);
	}
		    
	@Kroll.method
    public void close()
    {
    	this.localyticsSession.close(this.customDimensions);
    }
	
	@Kroll.method
    public void open()
    {		
    	this.localyticsSession.open(this.customDimensions);
    }
	
	@Kroll.method
	public void upload()
    {
    	this.localyticsSession.upload();
    }
	
	@Kroll.method @Kroll.setProperty
    public void setOptOut(boolean isOptedOut)
	{
		this.localyticsSession.setOptOut(isOptedOut);
	}
	
	@Kroll.method
	public void tagEvent(@Kroll.argument(optional=false) String event, @Kroll.argument(optional=true) KrollDict attributes)
    {				
    	this.localyticsSession.tagEvent(event, (Map) attributes, this.customDimensions);
    }
	
	@Kroll.method
	public void setCustomDimensions(@Kroll.argument(optional=false) String[] customDimensions)
	{
		if (null == customDimensions)
			this.customDimensions = null;
		else
			this.customDimensions = Arrays.asList(customDimensions);
		
		TiApplication context = TiApplication.getInstance();
				
	    try
	    {
			File file = new File(context.getDir("data", context.MODE_PRIVATE), "localytics_map");    
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.reset();
			if (null != this.customDimensions)
			{
				outputStream.writeObject(LocalyticsModule.customDimensions);
			}
			outputStream.flush();
			outputStream.close();
	    }
	    catch(FileNotFoundException e){}
	    catch(IOException e){}
	}
	
	@Kroll.method
    public void tagScreen(String screen)
	{
		this.localyticsSession.tagScreen(screen);
	}
	
	@Kroll.method
	public String createRangedAttribute(int actualValue, int[] steps)
	{
		return this.localyticsSession.createRangedAttribute(actualValue, steps);
	}
	
	@Kroll.method @Kroll.setProperty
    public void setCustomerEmail(String email)
	{
		this.localyticsSession.setCustomerEmail(email);
	}
	
	@Kroll.method @Kroll.setProperty
    public void setCustomerName(String name)
	{
		this.localyticsSession.setCustomerName(name);
	}
	
	@Kroll.method @Kroll.setProperty
    public void setCustomerId(String id)
	{
		this.localyticsSession.setCustomerId(id);
	}
	
	@Kroll.method
    public void setCustomerData(String key, String value)
	{
		this.localyticsSession.setCustomerData(key, value);
	}
	
	// Private methods
	private static List<String> unarchiveCustomDimensions()
	{
		List<String> dimensions = null;
		TiApplication context = TiApplication.getInstance();
		
		try
		{
			File file = new File(context.getDir("data", context.MODE_PRIVATE), "localytics_map"); 
			FileInputStream fileInput = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fileInput);
			dimensions = (List) ois.readObject();
		}
	    catch(FileNotFoundException e){}
	    catch(IOException e){}
		catch(ClassNotFoundException e){}
		
		return dimensions;
	}
}

