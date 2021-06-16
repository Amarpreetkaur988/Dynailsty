package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.GetServiceListModel;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeViewHolder> {
    Context context;
    private List<GetServiceListModel.Detail> list;
    ClickCallBack clickCallBack;


    public HomeRecyclerViewAdapter(Context context, List<GetServiceListModel.Detail> list, ClickCallBack clickCallBack) {
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
                inflate(R.layout.item_home_recycler_view, viewGroup, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {

        Glide.with(context).load(list.get(i).getImage()).into(homeViewHolder.serviceImage);
        if (i % 2 == 1) {
            homeViewHolder.serviceName.setTextColor(context.getResources().getColor(R.color.lightYellow));
            homeViewHolder.serviceName.setText(list.get(i).getTitle());
            homeViewHolder.serviceName.setBackgroundResource(R.drawable.item_text_back_gradient_white);
        } else {
            homeViewHolder.serviceName.setTextColor(context.getResources().getColor(R.color.white));
            homeViewHolder.serviceName.setText(list.get(i).getTitle());
            homeViewHolder.serviceName.setBackgroundResource(R.drawable.item_text_back_gradient_yellow);
        }
//        if (count % 2 == 1) {
//            homeViewHolder.serviceName.setTextColor(context.getResources().getColor(R.color.lightYellow));
//            homeViewHolder.serviceName.setBackgroundResource(R.drawable.item_text_back_gradient_white);
//        } else {
//            homeViewHolder.serviceRelativeLayout.setBackgroundResource(R.drawable.fullsets);
//            homeViewHolder.serviceName.setTextColor(context.getResources().getColor(R.color.white));
//            homeViewHolder.serviceName.setText("FULLSETS WITHOUT NAIL EXTENSIONS (NATURAL NAILS)");
//            homeViewHolder.serviceName.setBackgroundResource(R.drawable.item_text_back_gradient_yellow);
//        }
//        count++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceName;

        private RelativeLayout serviceRelativeLayout;

        private ImageView serviceImage;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceRelativeLayout = itemView.findViewById(R.id.serviceRelativeLayout);
            serviceImage = itemView.findViewById(R.id.serviceImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCallBack.getClickPosition(getLayoutPosition());
                }
            });

        }
    }
}
