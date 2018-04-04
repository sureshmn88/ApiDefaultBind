package mine.manik.com.listsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by manikkam on 9/2/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Product> mList;
    OnClickListener onClickListener;

    public interface OnClickListener {
        void onLayoutClick(int position);
    }

    public ProductAdapter(Context context, ArrayList<Product> list) {
        mContext = context;
        mList = list;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setProductList(ArrayList<Product> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_details, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product item=mList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvCategory.setText(item.getCategory());
        holder.tvPrice.setText(item.getPrice());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLayout;
        TextView tvName,tvCategory,tvPrice;

        public MyViewHolder(View view) {
            super(view);

            mLayout= (LinearLayout) view.findViewById(R.id.list_product_layout);
            tvName= (TextView) view.findViewById(R.id.list_text_1);
            tvCategory= (TextView) view.findViewById(R.id.list_text_2);
            tvPrice= (TextView) view.findViewById(R.id.list_text_3);


            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null)
                        onClickListener.onLayoutClick(getAdapterPosition());
                }
            });


        }
    }
}

