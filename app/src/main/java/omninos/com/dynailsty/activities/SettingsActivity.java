package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.services.MySerives;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout backLinearLayout;

    TextView changePasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        stopService(new Intent(SettingsActivity.this, MySerives.class));
        Intent intent = new Intent(SettingsActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

        initViews();
        setClicks();

    }

    private boolean isMyServiceRunning(Class<? extends Intent> serviceClass) {
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


    private void initViews() {
        backLinearLayout = findViewById(R.id.backLinearLayout);
        changePasswordTextView = findViewById(R.id.changePasswordTextView);
    }

    private void setClicks() {
        backLinearLayout.setOnClickListener(this);
        changePasswordTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLinearLayout:
                this.finish();
                break;

            case R.id.changePasswordTextView:
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
                break;
        }
    }
}
