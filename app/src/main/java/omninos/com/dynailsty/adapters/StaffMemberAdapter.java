package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.MyAppointmentModel;
import omninos.com.dynailsty.model.StaffDetailModel;
import omninos.com.dynailsty.util.App;

public class StaffMemberAdapter extends RecyclerView.Adapter<StaffMemberAdapter.MyViewHolder> {

    private TImeSlotAdapter adapter;
    private List<String> timeSlots = new ArrayList<>();

    Context context;
    private StaffDetailModel.Details list;
    private List<String> checkMember = new ArrayList<>();

    public StaffMemberAdapter(Context context, StaffDetailModel.Details list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_staff_name_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.timeSlotRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));

        if (timeSlots != null) {
            timeSlots.clear();
        }
        displayTimeSlots(list.getTimeSlotDetails().getStartTime(), list.getTimeSlotDetails().getEndTime(), App.getSinltonPojo().getServiceTime().substring(0, 5));

        adapter = new TImeSlotAdapter(context, timeSlots, list.getBarberDeatils().get(i).getBookingTime());
        myViewHolder.timeSlotRecyclerView.setAdapter(adapter);
        myViewHolder.timeSlotRecyclerView.setNestedScrollingEnabled(false);

        myViewHolder.ServiceProviderName.setText(list.getBarberDeatils().get(i).getName());

        myViewHolder.ServiceProviderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSinltonPojo().setBarbarId(list.getBarberDeatils().get(i).getId());
                if (!checkMember.contains(list.getBarberDeatils().get(i).getId())) {
                    myViewHolder.timeSlotRecyclerView.setVisibility(View.VISIBLE);
                    checkMember.add(list.getBarberDeatils().get(i).getId());
                    App.getSinltonPojo().setBarbarId(list.getBarberDeatils().get(i).getId());
                } else {
                    myViewHolder.timeSlotRecyclerView.setVisibility(View.GONE);
                    checkMember.remove(list.getBarberDeatils().get(i).getId());
                }
            }
        });
    }

    private void displayTimeSlots(String startTime, String lastTime, String timeSlot) {

        Date today = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateToStr = format1.format(today);
        System.out.println("Today Date " + dateToStr);

        String timeValue = "2015-10-28T18:37:04.899+05:30";
        StringTokenizer stringTokenizer = new StringTokenizer(timeValue, "T");
        String dateValue = stringTokenizer.nextElement().toString();
        String endDateValue = "2015-10-28";
        String restString = stringTokenizer.nextElement().toString();
        StringTokenizer secondTokeniser = new StringTokenizer(startTime, ":");

        StringTokenizer slotToken = new StringTokenizer(timeSlot, ":");


        StringTokenizer secondTokeniser1 = new StringTokenizer(lastTime, ":");

        String hours = secondTokeniser.nextElement().toString();
        String minutes = secondTokeniser.nextElement().toString();


        String slothour = slotToken.nextElement().toString();

        System.out.println("SlotHour:" + slothour);

        String slotmin = slotToken.nextElement().toString();


        String hours1 = secondTokeniser1.nextElement().toString();
        String minutes1 = secondTokeniser1.nextElement().toString();


        hours = String.valueOf(Integer.parseInt(hours));


        hours1 = String.valueOf(Integer.parseInt(hours1));
        String time1 = hours + ":" + minutes;
        String time2 = hours1 + ":" + minutes1;

        String format = "yyyy-MM-dd HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        int timeslot = Integer.parseInt(slotmin);
        int timeHour = Integer.parseInt(slothour);

        int value = (timeslot * 60 * 1000) + (timeHour * 60 * 60 * 1000);
        try {
            Date dateObj1 = sdf.parse(dateValue + " " + time1);
            Date dateObj2 = sdf.parse(endDateValue + " " + time2);
            Log.d("TAG", "Date Start: " + dateObj1);
            Log.d("TAG", "Date End: " + dateObj2);
            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot1 = new Date(dif);
                dif += value;
//                Date slot2 = new Date(dif);
//                dif += 2400000;
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a, dd/MM/yy");
                Log.d("TAG", "New Hour slot = " + sdf1.format(slot1));

                timeSlots.add(sdf1.format(slot1));
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.getBarberDeatils().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView ServiceProviderName;
        private RecyclerView timeSlotRecyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ServiceProviderName = itemView.findViewById(R.id.ServiceProviderName);
            timeSlotRecyclerView = itemView.findViewById(R.id.timeSlotRecyclerView);
        }
    }
}
