package omninos.com.dynailsty.util;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private Context context;
    private static AppPreference appPreference;
    private static SingltonPojo sinltonPojo;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appPreference = AppPreference.init(context);
        sinltonPojo = new SingltonPojo();
    }

    public static SingltonPojo getSinltonPojo() {
        return sinltonPojo;
    }


    public static AppPreference getAppPreference() {
        return appPreference;
    }


}
