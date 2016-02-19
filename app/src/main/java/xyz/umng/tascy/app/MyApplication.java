package xyz.umng.tascy.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import xyz.umng.tascy.helper.ParseUtils;

/**
 * Created by Umang on 2/16/2016.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // register with parse
        ParseUtils.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}