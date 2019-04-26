package com.example.mukhtaradepoju.deattdeliverers.Adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mukhtaradepoju.deattdeliverers.DeliveryOrdersQuery;
import com.example.mukhtaradepoju.deattdeliverers.MainActivity;
import com.example.mukhtaradepoju.deattdeliverers.R;
import com.example.mukhtaradepoju.deattdeliverers.RestaurantOrdersQuery;

import java.io.Serializable;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class restaurantAdapter extends RecyclerView.Adapter<restaurantAdapter.RestaurantViewHolder> {

    private List<DeliveryOrdersQuery.DelivererOrder> restaurantList;
    private MainActivity mContext;
    OnItemClick onItemClick;
    public String pending;

    private int row_index;


    public restaurantAdapter(MainActivity context, List<DeliveryOrdersQuery.DelivererOrder> restaurantList) {
        this.mContext = context;
        this.restaurantList = restaurantList;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.restaurant_card, null);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, final int position) {
        //getting the product of the specified position
        final DeliveryOrdersQuery.DelivererOrder r = restaurantList.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                row_index = position;

                onItemClick.getPosition(position, r);
                if (row_index == position) {
                    holder.relativeLayout.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    holder.restaurantName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    holder.counter.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background));
                    holder.restaurantName.setTextColor(ContextCompat.getColor(mContext, R.color.textcolor));
                    holder.counter.setTextColor(ContextCompat.getColor(mContext, R.color.textcolor));
                }

            }
        });
        //binding the data with the viewholder views


        holder.restaurantName.setText(r.name());
        holder.counter.setText(String.valueOf(r.orders().size()) + " Order(s)");


    }


    @Override
    public int getItemCount() {
        return (null != restaurantList ? restaurantList.size() : 0);
    }


    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName, counter, name;
        CardView relativeLayout;


        public RestaurantViewHolder(View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.vendorname);
            counter = itemView.findViewById(R.id.counter);
            relativeLayout = itemView.findViewById(R.id.card1);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(RestaurantOrdersQuery item);

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void getPosition(int pos, DeliveryOrdersQuery.DelivererOrder r); //pass any things
    }

}


