package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.StaffDetailModel;
import omninos.com.dynailsty.util.App;

import static android.support.constraint.Constraints.TAG;

public class TImeSlotAdapter extends RecyclerView.Adapter<TImeSlotAdapter.MyViewHolder> {

    Context context;
    List<String> list;
    List<StaffDetailModel.BookingTime> deatilList;
    List<String> setUpTimenew = new ArrayList<>();


    String currentDate, currentTime, selectedTime;
    DateFormat dateFormat, timeFormat;

    public TImeSlotAdapter(Context context, List<String> list, List<StaffDetailModel.BookingTime> deatilList) {
        this.context = context;
        this.list = list;
        this.deatilList = deatilList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_slot_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");

        final Date date = new Date();
        currentDate = dateFormat.format(date);
        currentTime = timeFormat.format(date);

        try {
            Date appointMentTime = timeFormat.parse(list.get(i));
            Date timeData = timeFormat.parse(currentTime);

            Date appointMentDate = dateFormat.parse(App.getSinltonPojo().getSelectedDate());
            Date Current = dateFormat.parse(currentDate);

            if (appointMentDate.getTime() == Current.getTime()) {
                if (timeData.getTime() > appointMentTime.getTime()) {
                    myViewHolder.layoutSelect.setBackgroundColor(Color.parseColor("#000000"));
                    myViewHolder.timesData.setTextColor(Color.parseColor("#ffffff"));
                    myViewHolder.timesData.setClickable(false);
                } else {
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < deatilList.size(); j++) {
            System.out.println("Data: " + deatilList.get(j).getBookingTime());
            Log.d(TAG, "onBindViewHolder: " + deatilList.get(j).getBookingTime());
            if (deatilList.get(j).getBookingTime().substring(0, 5).equalsIgnoreCase(list.get(i))) {
                myViewHolder.layoutSelect.setBackgroundColor(Color.parseColor("#E41717"));
                myViewHolder.timesData.setTextColor(Color.parseColor("#ffffff"));
                myViewHolder.timesData.setClickable(false);
            }
        }
//        }
        myViewHolder.timesData.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView timesData;
        RelativeLayout layoutSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timesData = itemView.findViewById(R.id.timesData);
            layoutSelect = itemView.findViewById(R.id.layoutSelect);

            timesData.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!setUpTimenew.contains(list.get(getLayoutPosition()))) {
                if (setUpTimenew.size() < 1) {
                    setUpTimenew.add(list.get(getLayoutPosition()));
                    selectedTime = list.get(getLayoutPosition());
                    layoutSelect.setBackgroundColor(Color.parseColor("#47ec10"));
                } else {
                    Toast.makeText(context, "You can choose maximum 1 slot", Toast.LENGTH_SHORT).show();
                }
            } else {
                setUpTimenew.remove(list.get(getLayoutPosition()));
//                layoutSelect.setBackgroundColor(Color.parseColor("#f2efef"));
                layoutSelect.setBackground(context.getDrawable(R.drawable.time_back));
            }
            App.getSinltonPojo().setBookingTime(selectedTime);
//            chooseTime.selectTime(getLayoutPosition());
        }
    }
}
