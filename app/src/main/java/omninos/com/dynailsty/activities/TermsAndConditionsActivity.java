package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.services.MySerives;

public class TermsAndConditionsActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout backLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        stopService(new Intent(TermsAndConditionsActivity.this, MySerives.class));
        Intent intent = new Intent(TermsAndConditionsActivity.this, MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            startService(intent);
        }

        initViews();
        setClickListeners();
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

    private void initViews() {
        backLinearLayout = findViewById(R.id.backLinearLayout);
    }

    private void setClickListeners() {
        backLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLinearLayout:
                this.finish();
                break;
        }
    }
}
