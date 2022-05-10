package com.authentication.activity;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.HandlerThread;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.code.microlog4android.config.PropertyConfigurator;

public class MyApplication extends MultiDexApplication {
	private String rootPath;


	private HandlerThread handlerThread;
	public String getRootPath() {
		return rootPath;
	}

	public HandlerThread getHandlerThread() {
		return handlerThread;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		PropertyConfigurator.getConfigurator(this).configure();

		handlerThread = new HandlerThread("handlerThread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
		handlerThread.start();
		setRootPath();

		Stetho.initializeWithDefaults(this);
	}

	private void setRootPath() {
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			rootPath = info.applicationInfo.dataDir;
			Log.i("rootPath", "################rootPath=" + rootPath);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

}
