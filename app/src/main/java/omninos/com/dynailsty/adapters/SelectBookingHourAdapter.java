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

public class SelectBookingHourAdapter extends RecyclerView.Adapter<SelectBookingHourAdapter.HourViewHolder> {

    HourClickCallBack hourClickCallBack;
    List<String> hours;
    Context context;

    int selectedHourr = -1;


    public SelectBookingHourAdapter(Context context, List<String> hours, HourClickCallBack hourClickCallBack) {
        this.context = context;
        this.hours = hours;
        this.hourClickCallBack = hourClickCallBack;
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

        if (hourViewHolder.getAdapterPosition() == selectedHourr) {
            hourViewHolder.hourTv.setText(hours.get(i));
            hourViewHolder.hourTv.setTextColor(context.getResources().getColor(R.color.white));
            hourViewHolder.hourTv.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
        } else if (hourViewHolder.getAdapterPosition() != selectedHourr) {
            hourViewHolder.hourTv.setText(hours.get(i));
            hourViewHolder.hourTv.setTextColor(context.getResources().getColor(R.color.black));
            hourViewHolder.hourTv.setBackgroundResource(R.drawable.stroke_button_grey);
        }

    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    class HourViewHolder extends RecyclerView.ViewHolder {

        TextView hourTv;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);

            hourTv = itemView.findViewById(R.id.hourTv);

            hourTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hourClickCallBack.clickCallBack(getAdapterPosition(), hourTv.getText().toString());
                }
            });
        }
    }

    public interface HourClickCallBack {
        void clickCallBack(int pos, String hourSelected);
    }

    public void selectHour(int selectedHour) {
        selectedHourr = selectedHour;
        notifyDataSetChanged();
    }

}
