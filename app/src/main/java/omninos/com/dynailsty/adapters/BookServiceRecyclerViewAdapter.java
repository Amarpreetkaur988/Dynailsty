package omninos.com.dynailsty.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.GetServiceListModel;

public class BookServiceRecyclerViewAdapter extends RecyclerView.Adapter<BookServiceRecyclerViewAdapter.HomeViewHolder> {


    Context context;
    private List<GetServiceListModel.Category> categories;
    ClickCallBack clickCallBack;

    public BookServiceRecyclerViewAdapter(Context context, List<GetServiceListModel.Category> categories, ClickCallBack clickCallBack) {
        this.context = context;
        this.categories = categories;
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void getClickPosition(int pos);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_book_service, viewGroup, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {
        Glide.with(context).load(categories.get(i).getImage()).into(homeViewHolder.categoryImage);
        homeViewHolder.categoryTitle.setText(categories.get(i).getTitle());
        homeViewHolder.timeTextView.setText(categories.get(i).getServiceTime());
        homeViewHolder.amountTextView.setText("$" + categories.get(i).getPrice());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        Button bookServiceBtn;
        private ImageView categoryImage;
        private TextView categoryTitle, timeTextView, amountTextView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);

            bookServiceBtn = itemView.findViewById(R.id.bookServiceBtn);
            bookServiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCallBack.getClickPosition(getAdapterPosition());
                }
            });

        }
    }
}
