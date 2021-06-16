package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import omninos.com.dynailsty.R;

public class SelectBookingMinuteAdapter extends RecyclerView.Adapter<SelectBookingMinuteAdapter.HourViewHolder> {

    MinuteClickCallBack minuteClickCallBack;
    List<String> minutes;
    Context context;

    int selectedMinutee = -1;


    public SelectBookingMinuteAdapter(Context context, List<String> minutes, MinuteClickCallBack minuteClickCallBack) {
        this.context = context;
        this.minutes = minutes;
        this.minuteClickCallBack = minuteClickCallBack;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_select_booking_time, viewGroup, false);

        return new HourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder hourViewHolder, int i) {

        hourViewHolder.hourTv.setText(minutes.get(i));

        if (hourViewHolder.getAdapterPosition() == selectedMinutee) {
            hourViewHolder.hourTv.setTextColor(context.getResources().getColor(R.color.white));
            hourViewHolder.hourTv.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
        } else if (hourViewHolder.getAdapterPosition() != selectedMinutee) {
            hourViewHolder.hourTv.setTextColor(context.getResources().getColor(R.color.black));
            hourViewHolder.hourTv.setBackgroundResource(R.drawable.stroke_button_grey);

        }

    }

    @Override
    public int getItemCount() {
        return minutes.size();
    }

    class HourViewHolder extends RecyclerView.ViewHolder {

        TextView hourTv;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);

            hourTv = itemView.findViewById(R.id.hourTv);

            hourTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minuteClickCallBack.clickCallBack(getAdapterPosition(), hourTv.getText().toString());
                }
            });
        }
    }

    public interface MinuteClickCallBack {
        void clickCallBack(int pos, String hourSelected);
    }

    public void selectMinute(int selectedMinute) {
        selectedMinutee = selectedMinute;
        notifyDataSetChanged();
    }

}
