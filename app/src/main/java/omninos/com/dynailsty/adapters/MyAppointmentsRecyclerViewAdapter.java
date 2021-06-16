package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.MyAppointmentModel;

public class MyAppointmentsRecyclerViewAdapter extends RecyclerView.Adapter<MyAppointmentsRecyclerViewAdapter.HomeViewHolder> {

    Context context;
    private List<MyAppointmentModel.Detail> list;
    ClickCallBack clickCallBack;


    public MyAppointmentsRecyclerViewAdapter(Context context, List<MyAppointmentModel.Detail> list, ClickCallBack clickCallBack) {
        this.context = context;
        this.list = list;
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void getClickPosition(int pos);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_my_appointment, viewGroup, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {
        Glide.with(context).load(list.get(i).getImage()).into(homeViewHolder.serviceImage);
        homeViewHolder.serviceType.setText(list.get(i).getTitle());
        homeViewHolder.time.setText(list.get(i).getBookingDateTime());
        if (list.get(i).getPaymentStatus().equalsIgnoreCase("0")) {
            homeViewHolder.paymentMethod.setText("Cash");
        } else if (list.get(i).getPaymentStatus().equalsIgnoreCase("1")) {
            homeViewHolder.paymentMethod.setText("Card");
        } else if (list.get(i).getPaymentStatus().equalsIgnoreCase("2")) {
            homeViewHolder.paymentMethod.setText("Net Banking");
        }


        if (list.get(i).getBookingStatus().equalsIgnoreCase("0")) {
            homeViewHolder.cancel.setText("Waiting");
        } else if (list.get(i).getBookingStatus().equalsIgnoreCase("1")) {
            homeViewHolder.cancel.setText("Accept");
        } else if (list.get(i).getBookingStatus().equalsIgnoreCase("2")) {
            homeViewHolder.cancel.setText("Cancel");
        } else if (list.get(i).getBookingStatus().equalsIgnoreCase("3")) {
            homeViewHolder.cancel.setText("Done");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView serviceImage;
        private TextView serviceType, time, paymentMethod, cancel;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceType = itemView.findViewById(R.id.serviceType);
            time = itemView.findViewById(R.id.time);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            cancel = itemView.findViewById(R.id.cancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCallBack.getClickPosition(getAdapterPosition());
                }
            });

        }
    }
}
