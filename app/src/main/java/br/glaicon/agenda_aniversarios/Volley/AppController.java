package br.glaicon.agenda_aniversarios.Volley;

import android.app.Application;


public class AppController extends Application {

    private BitmapCache bitmapCache;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public BitmapCache getBitmapCache() {
        if (bitmapCache == null)
            bitmapCache = new BitmapCache();

        return bitmapCache;
    }
}