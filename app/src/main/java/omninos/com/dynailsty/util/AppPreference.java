package omninos.com.dynailsty.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import omninos.com.dynailsty.model.LoginRegisterModel;

public class AppPreference {

    private static AppPreference appPreference;
    private SharedPreferences sharedPreferences;


    private AppPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantData.DYNAILSTY, Context.MODE_PRIVATE);

    }

    public static AppPreference init(Context context) {
        if (appPreference == null) {
            appPreference = new AppPreference(context);
        }
        return appPreference;
    }


    public void SaveString(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public String GetString(String key) {

        return sharedPreferences.getString(key, "");
    }

    public void Logout() {
        sharedPreferences.edit().clear().apply();
    }

    public void saveLoginDetail(LoginRegisterModel userLoginModel) {

        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantData.LOG_IN_DATA, gson.toJson(userLoginModel));
        editor.apply();
    }

    //
    public LoginRegisterModel getLoginDetail() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(ConstantData.LOG_IN_DATA, ""), LoginRegisterModel.class);
    }


}
