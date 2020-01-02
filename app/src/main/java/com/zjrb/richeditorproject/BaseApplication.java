package com.zjrb.richeditorproject;

import android.support.multidex.MultiDexApplication;

import com.zjrb.core.utils.UIUtils;
import com.zjrb.me.bizcore.network.NetworkManager;

/**
 *
 * Created by yyp on 2019.4.15
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        UIUtils.init(this);
        NetworkManager.init(this);
        NetworkManager.login();
    }
}
