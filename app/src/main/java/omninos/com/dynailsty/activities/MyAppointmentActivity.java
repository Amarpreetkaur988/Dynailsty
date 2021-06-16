package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import omninos.com.dynailsty.MyViewModel.AppointmentViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.adapters.BookServiceRecyclerViewAdapter;
import omninos.com.dynailsty.adapters.MyAppointmentsRecyclerViewAdapter;
import omninos.com.dynailsty.model.MyAppointmentModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;

public class MyAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView myAppointmentsRecyclerView;

    private MyAppointmentsRecyclerViewAdapter recyclerViewAdapter;

    private LinearLayout backLinearLayout;
    private AppointmentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);


        stopService(new Intent(MyAppointmentActivity.this, MySerives.class));
        Intent intent = new Intent(MyAppointmentActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        initViews();
        serClicks();
        makeRecyclerView();
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
        myAppointmentsRecyclerView = findViewById(R.id.myAppointmentsRecyclerView);
        myAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(MyAppointmentActivity.this));
    }

    private void serClicks() {
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

    private void makeRecyclerView() {

        viewModel.getAppointment(MyAppointmentActivity.this, App.getAppPreference().getLoginDetail().getDetails().getId()).observe(MyAppointmentActivity.this, new Observer<MyAppointmentModel>() {
            @Override
            public void onChanged(@Nullable MyAppointmentModel myAppointmentModel) {
                if (myAppointmentModel.getSuccess().equalsIgnoreCase("1")) {
                    recyclerViewAdapter = new MyAppointmentsRecyclerViewAdapter(MyAppointmentActivity.this, myAppointmentModel.getDetails(), new MyAppointmentsRecyclerViewAdapter.ClickCallBack() {
                        @Override
                        public void getClickPosition(int pos) {
//                            Toast.makeText(MyAppointmentActivity.this, "" + pos, Toast.LENGTH_SHORT).show();
                        }
                    });

                    myAppointmentsRecyclerView.setAdapter(recyclerViewAdapter);
                } else {
                    CommonUtils.showSnackbarAlert(myAppointmentsRecyclerView, myAppointmentModel.getMessage());
                }
            }
        });


    }

}
