/**
 * 
 */
package com.example.h3.job;

import android.content.Context;

import com.example.h3.Config;
import com.example.h3.QiangHongBaoService;
/**
 * @author byc
 *
 */
public abstract class BaseAccessbilityJob implements AccessbilityJob {
	
	//≥Ã–Ú±Í ∂
	public static String TAG = "byc001";	
	public QiangHongBaoService service;
	public Context context;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        this.service = service;
        TAG=Config.TAG;
        context=service.getApplicationContext();
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public QiangHongBaoService getService() {
        return service;
    }

}
