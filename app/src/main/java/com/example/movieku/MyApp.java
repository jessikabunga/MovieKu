package com.example.movieku;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map config = new HashMap();
        config.put("cloud_name", "duglsjpuy");
        config.put("api_key", "486889534195826");
        config.put("api_secret", "API_SECRET_KAMU"); // atau hapus kalau pakai unsigned
        MediaManager.init(this, config);
    }
}
