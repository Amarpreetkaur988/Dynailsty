package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;
import omninos.com.dynailsty.MyViewModel.ServicesViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.adapters.HomeRecyclerViewAdapter;
import omninos.com.dynailsty.model.GetServiceListModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView servicesRecyclerView;

    private HomeRecyclerViewAdapter recyclerViewAdapter;

    private DrawerLayout drawerLayout;

    private TextView logOut, termsAndConditions, helpNavItem, myAppointmentNavItem, homeNavItem, settingsNavItem, navi_name, navi_email;

    private LinearLayout openNavigationView;
    private ServicesViewModel viewModel;
    private CircleImageView navi_userImage, userImage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopService(new Intent(MainActivity.this, MySerives.class));
        Intent intent = new Intent(MainActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

        viewModel = ViewModelProviders.of(this).get(ServicesViewModel.class);
        initViews();
        setClicks();
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
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        drawerLayout = findViewById(R.id.drawerLayout);
        openNavigationView = findViewById(R.id.openNavigationView);
        logOut = findViewById(R.id.logOutNavItem);
        termsAndConditions = findViewById(R.id.termsAndConditionsNavItem);
        helpNavItem = findViewById(R.id.helpNavItem);
        myAppointmentNavItem = findViewById(R.id.myAppointmentNavItem);
        homeNavItem = findViewById(R.id.homeNavItem);
        settingsNavItem = findViewById(R.id.settingsNavItem);

        navi_name = findViewById(R.id.navi_name);
        navi_email = findViewById(R.id.navi_email);
        navi_userImage = findViewById(R.id.navi_userImage);

        userImage = findViewById(R.id.userImage);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!App.getAppPreference().getLoginDetail().getDetails().getImage().isEmpty()) {
            Glide.with(MainActivity.this).load(App.getAppPreference().getLoginDetail().getDetails().getImage()).into(navi_userImage);
            Glide.with(MainActivity.this).load(App.getAppPreference().getLoginDetail().getDetails().getImage()).into(userImage);
        }
        navi_name.setText(App.getAppPreference().getLoginDetail().getDetails().getName());
        navi_email.setText(App.getAppPreference().getLoginDetail().getDetails().getEmail());

    }

    private void setClicks() {
        openNavigationView.setOnClickListener(this);
        logOut.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
        helpNavItem.setOnClickListener(this);
        myAppointmentNavItem.setOnClickListener(this);
        homeNavItem.setOnClickListener(this);
        settingsNavItem.setOnClickListener(this);
        userImage.setOnClickListener(this);

    }

    private void makeRecyclerView() {

        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        viewModel.getServiceListModelLiveData(MainActivity.this).observe(MainActivity.this, new Observer<GetServiceListModel>() {
            @Override
            public void onChanged(@Nullable final GetServiceListModel getServiceListModel) {
                if (getServiceListModel.getSuccess().equalsIgnoreCase("1")) {

                    recyclerViewAdapter = new HomeRecyclerViewAdapter(MainActivity.this, getServiceListModel.getDetails(), new HomeRecyclerViewAdapter.ClickCallBack() {
                        @Override
                        public void getClickPosition(int pos) {
                            if (getServiceListModel.getDetails().get(pos).getCategory() != null || getServiceListModel.getDetails().get(pos).getCategory().size() != 0) {
                                App.getSinltonPojo().setCategories(getServiceListModel.getDetails().get(pos).getCategory());
                                App.getSinltonPojo().setMainServiceName(getServiceListModel.getDetails().get(pos).getTitle());
                                startActivity(new Intent(MainActivity.this, BookServiceActivity.class));
                            }
                        }
                    });
                    servicesRecyclerView.setAdapter(recyclerViewAdapter);
                } else {
                    CommonUtils.showSnackbarAlert(servicesRecyclerView, getServiceListModel.getMessage());
                }
            }
        });
//        recyclerViewAdapter = new HomeRecyclerViewAdapter(this, new HomeRecyclerViewAdapter.ClickCallBack() {
//            @Override
//            public void getClickPosition(int pos) {
//                startActivity(new Intent(MainActivity.this, BookServiceActivity.class));
//                Toast.makeText(MainActivity.this, "" + pos, Toast.LENGTH_SHORT).show();
//            }
//        });
        servicesRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openNavigationView:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.logOutNavItem:
//                drawerLayout.closeDrawer(Gravity.START);
                OpenConfirmBox();
                break;
            case R.id.termsAndConditionsNavItem:
                drawerLayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainActivity.this, TermsAndConditionsActivity.class));
                break;
            case R.id.helpNavItem:
                drawerLayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            case R.id.myAppointmentNavItem:
                drawerLayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainActivity.this, MyAppointmentActivity.class));
                break;
            case R.id.homeNavItem:
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.settingsNavItem:
                drawerLayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

            case R.id.userImage:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
        }
    }

    private void OpenConfirmBox() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
//                        startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
//                        finishAffinity();

                        App.getAppPreference().Logout();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you Sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
