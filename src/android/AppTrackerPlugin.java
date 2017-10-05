package com.apptracker.android.phonegap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.apptracker.android.listener.AppModuleListener;
import com.apptracker.android.track.AppTracker;

public class AppTrackerPlugin extends CordovaPlugin {
	private boolean ret = false;

	@Override
	public boolean execute(final String action, final JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		final Activity act = cordova.getActivity();
		final Context ctx = act.getApplicationContext();  
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (action.equals("startSession")) {
					try {
						AppTracker.setModuleListener(

						new AppModuleListener() {

							@Override
							public void onModuleLoaded(final String placement) { 
								act.runOnUiThread(new Runnable() {  
									@Override
									public void run() { 
										webView.loadUrl("javascript:cordova.fireDocumentEvent('onModuleLoaded',{placement:'" + placement + "'});"); 										
									}
								});
							}

							@Override
							public void onModuleFailed(final String placement,
									final String error, final boolean cached) {
								act.runOnUiThread(new Runnable() {  
									@Override
									public void run() {   
										webView.loadUrl("javascript:cordova.fireDocumentEvent('onModuleFailed',{placement:'" + placement + "', error:'" + error + "', cached:"+ (cached?"true":"false") + "});"); 
									}
								});

							}

							@Override
							public void onModuleClosed(final String placement,final boolean reward) { 
								act.runOnUiThread(new Runnable() {  
									@Override
									public void run() {   
										webView.loadUrl("javascript:cordova.fireDocumentEvent('onModuleClosed',{placement:'" + placement + "',reward:" + (reward?"true":"false") + "});"); 
									}
								}); 
							}

							@Override
							public void onModuleClicked(final String placement) { 
								act.runOnUiThread(new Runnable() {  
									@Override
									public void run() {   
										webView.loadUrl("javascript:cordova.fireDocumentEvent('onModuleClicked',{placement:'" + placement + "'});"); 
									}
								}); 
							}

							@Override
							public void onModuleCached(final String placement) {
								act.runOnUiThread(new Runnable() {  
									@Override
									public void run() {   
										webView.loadUrl("javascript:cordova.fireDocumentEvent('onModuleCached',{placement:'" + placement + "'});"); 
									}
								});
							}
						});
						AppTracker.setFramework("phonegap");
						AppTracker.startSession(ctx, args.getString(0), args.getBoolean(1));
						ret = true;
					} catch (Exception e) {
						ret = false;
					}
				}
				if (action.equals("closeSession")) {
					AppTracker.closeSession(ctx, true);
					ret = true;
				}
				if (action.equals("pause")) {
					AppTracker.pause(ctx);
					ret = true;
				}
				if (action.equals("resume")) {
					AppTracker.resume(ctx);
					ret = true;
				}
				if (action.equals("event")) {
					try {
						AppTracker.event(ctx, args.getString(0),
								args.getLong(1));
						ret = true;
					} catch (Exception e) {
						try {
							AppTracker.event(ctx, args.getString(0));
							ret = true;
						} catch (Exception e1) {
							ret = false;
						}
					}
				}
				if (action.equals("transaction")) {
					try {
						AppTracker.transaction(ctx, args.getString(0),
								args.getLong(1), args.getString(2));
						ret = true;
					} catch (Exception e) {
						ret = false;
					}
				}
				if (action.equals("loadModule")) {
					try {
						String userData = null;
						if(args.getString(1) != null && !"null".equalsIgnoreCase(args.getString(1)))
						{
							userData = args.getString(1);
						}
						AppTracker.loadModule(ctx, args.getString(0), userData);
						
						ret = true;
					} catch (Exception e) {
						ret = false;
					}
				}
				if (action.equals("loadModuleToCache")) {
					try {
						String userData = null;
						if(args.getString(1) != null && !"null".equalsIgnoreCase(args.getString(1)))
						{
							userData = args.getString(1);
						}
						AppTracker.loadModuleToCache(ctx, args.getString(0), userData);
						ret = true;
					} catch (Exception e) {
						ret = false;
					}
				}
				if (action.equals("destroyModule")) {
					AppTracker.destroyModule();
					ret = true;
				}
				if(action.equals("fixAdOrientation")){
					try {
 						AppTracker.fixAdOrientation(args.getInt(0));
						ret = true;
					} catch (JSONException e) { 
						ret = false;
					}
				}
				if(action.equals("setCrashHandlerStatus")){
					try {
 						AppTracker.setCrashHandlerStatus(args.getBoolean(0));
						ret = true;
					} catch (JSONException e) { 
						ret = false;
					}
				}
				if(action.equals("setAgeRange")){
					try{
						AppTracker.setAgeRange(args.getString(0));
						ret = true;
					}catch(JSONException e){
						ret = false;
					}
				}
				if(action.equals("setGender")){
					try{
						AppTracker.setGender(args.getString(0));
						ret = true;
					}catch(JSONException e){
						ret = false;
					}
				}
			}
		});
		return ret;
	}
}
