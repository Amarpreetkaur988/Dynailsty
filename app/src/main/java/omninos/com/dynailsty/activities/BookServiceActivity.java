package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.adapters.BookServiceRecyclerViewAdapter;
import omninos.com.dynailsty.model.GetServiceListModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;

public class BookServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout backLinearLayout;

    private RecyclerView bookServiceRecyclerView;
    private TextView pageTitle;

    private BookServiceRecyclerViewAdapter recyclerViewAdapter;
    private List<GetServiceListModel.Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);

        stopService(new Intent(BookServiceActivity.this, MySerives.class));
        Intent intent = new Intent(BookServiceActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }


        categoryList = App.getSinltonPojo().getCategories();
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
        bookServiceRecyclerView = findViewById(R.id.bookServiceRecyclerView);
        pageTitle = findViewById(R.id.pageTitle);
    }

    private void serClicks() {
        pageTitle.setText(App.getSinltonPojo().getMainServiceName());
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
        recyclerViewAdapter = new BookServiceRecyclerViewAdapter(BookServiceActivity.this, categoryList, new BookServiceRecyclerViewAdapter.ClickCallBack() {
            @Override
            public void getClickPosition(int pos) {
                App.getSinltonPojo().setCategoryId(categoryList.get(pos).getId());
                App.getSinltonPojo().setServicePrice(categoryList.get(pos).getPrice());
                App.getSinltonPojo().setServiceTime(categoryList.get(pos).getServiceTime());
                App.getSinltonPojo().setServiceTitle(categoryList.get(pos).getTitle());
//                Toast.makeText(BookServiceActivity.this, "" + pos, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BookServiceActivity.this, SelectAppointmentSlotActivity.class));
            }
        });
        bookServiceRecyclerView.setLayoutManager(new GridLayoutManager(BookServiceActivity.this, 2, GridLayoutManager.VERTICAL, false));
        bookServiceRecyclerView.setAdapter(recyclerViewAdapter);
    }

}
