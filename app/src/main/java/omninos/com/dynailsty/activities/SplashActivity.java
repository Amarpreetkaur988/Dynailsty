package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.ConstantData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        stopService(new Intent(SplashActivity.this, MySerives.class));
//        Intent intent = new Intent(SplashActivity.this, MySerives.class);
//        if (!isMyServiceRunning1(intent.getClass())) {
//            startService(intent);
//        }


        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("omninos.com.dynailsty", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (App.getAppPreference().GetString(ConstantData.TOKEN).equalsIgnoreCase("1")){

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }else {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }

//    private boolean isMyServiceRunning1(Class<? extends Intent> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.i("isMyServiceRunning?", true + "");
//                return true;
//            }
//        }
//        Log.i("isMyServiceRunning?", false + "");
//        return false;
//    }
}
