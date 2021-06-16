package omninos.com.dynailsty.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omninos.com.dynailsty.MyViewModel.AppointmentViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.adapters.SelectBookingHourAdapter;
import omninos.com.dynailsty.adapters.SelectBookingMinuteAdapter;
import omninos.com.dynailsty.adapters.StaffMemberAdapter;
import omninos.com.dynailsty.model.MyAppointmentModel;
import omninos.com.dynailsty.model.StaffDetailModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;

public class SelectAppointmentSlotActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout backLinearLayout;

    private RecyclerView timeRecyclerView;

    private ImageView hourArrow, minuteArrow;

    private RelativeLayout hourRelativeLayout, minuteRelativeLayout;

    private int hourSelectedPosition = -1, minuteSelectedPosition = -1;

    private SelectBookingHourAdapter recyclerAdapter;

    private SelectBookingMinuteAdapter recyclerAdapterMinute;

    private TextView hourSelectedTv, minuteSelectedTv, minuteTextTv, hourTextTv, timeAndCharges, categoryTitle, pageTitle, todayName, todayDate, todayYearAndMonth;

    private Button bookAppointmentBtn;

    private MaterialCalendarView materialCalendarView;
    private String SelectedDate = "", SelectedTime = "";
    private List<String> HoursList = new ArrayList<>();
    private List<String> MinuteList = new ArrayList<>();

    private RecyclerView stafNames;

    private StaffMemberAdapter adapter;
    private AppointmentViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_slot);

        viewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        stopService(new Intent(SelectAppointmentSlotActivity.this, MySerives.class));
        Intent intent = new Intent(SelectAppointmentSlotActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }


        initViews();
        serClicks();
        setCalendar();

        makeHourRecyclerView();
    }

    private void getStaffList(String data) {
        viewModel.staffDetailModelLiveData(SelectAppointmentSlotActivity.this, data).observe(SelectAppointmentSlotActivity.this, new Observer<StaffDetailModel>() {
            @Override
            public void onChanged(@Nullable StaffDetailModel myAppointmentModel) {
                if (myAppointmentModel.getSuccess().equalsIgnoreCase("1")) {
                    adapter = new StaffMemberAdapter(SelectAppointmentSlotActivity.this, myAppointmentModel.getDetails());
                    stafNames.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    CommonUtils.showSnackbarAlert(timeRecyclerView, myAppointmentModel.getMessage());
                }
            }
        });
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
        timeRecyclerView = findViewById(R.id.timeRecyclerView);
        backLinearLayout = findViewById(R.id.backLinearLayout);
        hourSelectedTv = findViewById(R.id.hourSelectedTv);
        hourRelativeLayout = findViewById(R.id.hourRelativeLayout);
        minuteRelativeLayout = findViewById(R.id.minuteRelativeLayout);
        minuteSelectedTv = findViewById(R.id.minuteSelectedTv);
        minuteTextTv = findViewById(R.id.minuteTextTv);
        hourTextTv = findViewById(R.id.hourTextTv);
        bookAppointmentBtn = findViewById(R.id.bookAppointmentBtn);
        hourArrow = findViewById(R.id.hourArrow);
        minuteArrow = findViewById(R.id.minuteArrow);
        minuteArrow.setVisibility(View.GONE);

        materialCalendarView = findViewById(R.id.calendarView);

        timeAndCharges = findViewById(R.id.timeAndCharges);
        categoryTitle = findViewById(R.id.categoryTitle);
        pageTitle = findViewById(R.id.pageTitle);

        todayName = findViewById(R.id.todayName);
        todayDate = findViewById(R.id.todayDate);
        todayYearAndMonth = findViewById(R.id.todayYearAndMonth);


        stafNames = findViewById(R.id.stafNames);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stafNames.setLayoutManager(linearLayoutManager);


    }

    private void serClicks() {

        timeAndCharges.setText(App.getSinltonPojo().getServiceTime() + "| $" + App.getSinltonPojo().getServicePrice());
        pageTitle.setText(App.getSinltonPojo().getMainServiceName());
        categoryTitle.setText(App.getSinltonPojo().getServiceTitle());
        backLinearLayout.setOnClickListener(this);
        hourRelativeLayout.setOnClickListener(this);
        minuteRelativeLayout.setOnClickListener(this);
        bookAppointmentBtn.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mont = calendar.get(Calendar.MONTH);
        int yr = calendar.get(Calendar.YEAR);

        SimpleDateFormat year = new SimpleDateFormat("MMM,yyyy");
        todayYearAndMonth.setText(year.format(calendar.getTime()));


        SimpleDateFormat dateData = new SimpleDateFormat("dd");
        todayDate.setText(dateData.format(calendar.getTime()));


        SimpleDateFormat nameData = new SimpleDateFormat("EEEE");
        todayName.setText(nameData.format(calendar.getTime()));

        final SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "Current Time : " + mdformat.format(calendar.getTime());
        SelectedDate = mdformat.format(calendar.getTime());

        getStaffList(SelectedDate);
        App.getSinltonPojo().setSelectedDate(SelectedDate);

        materialCalendarView.state().edit().setMinimumDate(CalendarDay.from(yr, mont, day)).commit();

        materialCalendarView.setDateSelected(Calendar.getInstance().getTime(), true);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String str = new SimpleDateFormat("yyyy-MM-dd").format(date.getDate()).toString();
                SelectedDate = str;
                getStaffList(SelectedDate);

                App.getSinltonPojo().setSelectedDate(SelectedDate);
                Toast.makeText(SelectAppointmentSlotActivity.this, " " + SelectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLinearLayout:
                this.finish();
                break;

            case R.id.bookAppointmentBtn:
                String hours = App.getSinltonPojo().getBookingTime();
                if (hours == null) {
                    CommonUtils.showSnackbarAlert(hourSelectedTv, "select time");
                } else if (SelectedDate.isEmpty()) {
                    CommonUtils.showSnackbarAlert(hourTextTv, "select date");
                } else {
                    App.getSinltonPojo().setBookingDate(SelectedDate);
                    startActivity(new Intent(this, SelectPaymentMethodActivity.class));
                }

                break;

            case R.id.hourRelativeLayout:
                makeHourRecyclerView();
                //changing text color
                hourSelectedTv.setTextColor(this.getResources().getColor(R.color.white));
                minuteSelectedTv.setTextColor(this.getResources().getColor(R.color.darkYellow));
                hourTextTv.setTextColor(this.getResources().getColor(R.color.white));
                minuteTextTv.setTextColor(this.getResources().getColor(R.color.darkYellow));
                //changing background
                minuteRelativeLayout.setBackgroundResource(R.drawable.btn_background_yellow_stroke);
                hourRelativeLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
                hourArrow.setVisibility(View.VISIBLE);
                minuteArrow.setVisibility(View.GONE);
                break;
            case R.id.minuteRelativeLayout:
                makeMinuteRecyclerView();
                //changing Text color
                minuteSelectedTv.setTextColor(this.getResources().getColor(R.color.white));
                hourSelectedTv.setTextColor(this.getResources().getColor(R.color.darkYellow));
                minuteTextTv.setTextColor(this.getResources().getColor(R.color.white));
                hourTextTv.setTextColor(this.getResources().getColor(R.color.darkYellow));
                //changing background
                minuteRelativeLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
                hourRelativeLayout.setBackgroundResource(R.drawable.btn_background_yellow_stroke);
                minuteArrow.setVisibility(View.VISIBLE);
                hourArrow.setVisibility(View.GONE);

                break;
        }
    }

    private void makeHourRecyclerView() {

        if (HoursList != null) {
            HoursList.clear();
        }

        HoursList.add("09");

        HoursList.add("10");

        HoursList.add("11");

        HoursList.add("12");

        HoursList.add("13");

        HoursList.add("14");

        HoursList.add("15");

        HoursList.add("16");

        HoursList.add("17");

        HoursList.add("18");

        recyclerAdapter = new SelectBookingHourAdapter(SelectAppointmentSlotActivity.this, HoursList, new SelectBookingHourAdapter.HourClickCallBack() {
            @Override
            public void clickCallBack(int pos, String hourSelected) {
//                Toast.makeText(SelectAppointmentSlotActivity.this, pos + " " + hourSelected, Toast.LENGTH_SHORT).show();
                hourSelectedTv.setText(String.valueOf(hourSelected.charAt(0)) + String.valueOf(hourSelected.charAt(1)) + "");
                recyclerAdapter.selectHour(pos);
                hourSelectedPosition = pos;
            }
        });

        timeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

        timeRecyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.selectHour(hourSelectedPosition);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void makeMinuteRecyclerView() {

        if (MinuteList != null) {
            MinuteList.clear();
        }

        MinuteList.add("00");

        MinuteList.add("10");

        MinuteList.add("20");

        MinuteList.add("30");

        MinuteList.add("40");

        MinuteList.add("50");


        recyclerAdapterMinute = new SelectBookingMinuteAdapter(this, MinuteList, new SelectBookingMinuteAdapter.MinuteClickCallBack() {
            @Override
            public void clickCallBack(int pos, String minuteSelected) {
//                Toast.makeText(SelectAppointmentSlotActivity.this, pos + " " + minuteSelected, Toast.LENGTH_SHORT).show();
                minuteSelectedTv.setText(String.valueOf(minuteSelected.charAt(0)) + String.valueOf(minuteSelected.charAt(1)) + "");
                recyclerAdapterMinute.selectMinute(pos);
                minuteSelectedPosition = pos;
            }
        });

        timeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

        timeRecyclerView.setAdapter(recyclerAdapterMinute);

        recyclerAdapterMinute.selectMinute(minuteSelectedPosition);
        recyclerAdapterMinute.notifyDataSetChanged();
    }

}
