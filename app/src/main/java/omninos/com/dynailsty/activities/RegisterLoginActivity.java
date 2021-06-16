package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.fragments.LoginFragment;
import omninos.com.dynailsty.fragments.RegisterFragment;
import omninos.com.dynailsty.services.MySerives;

public class RegisterLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView loginRegisterText, accountStatusText;

    private int countClicks = 0;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        stopService(new Intent(RegisterLoginActivity.this, MySerives.class));
        Intent intent = new Intent(RegisterLoginActivity.this, MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            startService(intent);
        }

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        initViews();
        loadFragment(new LoginFragment());
        setOnClickListeners();
    }

    private boolean isMyServiceRunning1(Class<? extends Intent> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void setOnClickListeners() {
        loginRegisterText.setOnClickListener(this);
    }

    private void initViews() {
        loginRegisterText = findViewById(R.id.loginRegisterText);
        accountStatusText = findViewById(R.id.accountStatusText);

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerLoginRegister, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginRegisterText: {
                countClicks++;
                if (countClicks % 2 == 1) {
                    loadFragment(new RegisterFragment());
                    loginRegisterText.setText(getString(R.string.login));
                    accountStatusText.setText(getString(R.string.loginPreText));
                } else {
                    loadFragment(new LoginFragment());
                    loginRegisterText.setText(getString(R.string.register));
                    accountStatusText.setText(getString(R.string.registerPreTxt));
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = getFragmentManager().findFragmentById(R.id.your_host_fragment_in_activity);
//        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
