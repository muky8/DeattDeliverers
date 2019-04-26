package com.example.mukhtaradepoju.deattdeliverers.Adapters;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.mukhtaradepoju.deattdeliverers.DeliveryOrdersQuery;
import com.example.mukhtaradepoju.deattdeliverers.DetailActivity;
import com.example.mukhtaradepoju.deattdeliverers.MainActivity;
import com.example.mukhtaradepoju.deattdeliverers.MyApolloClient;
import com.example.mukhtaradepoju.deattdeliverers.R;
import com.example.mukhtaradepoju.deattdeliverers.RestaurantOrdersQuery;
import com.example.mukhtaradepoju.deattdeliverers.UpdateOrderMutation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class detailAdapter extends RecyclerView.Adapter<detailAdapter.RestaurantViewHolder> {

    private List<DeliveryOrdersQuery.Order> restaurantList;
    private DetailActivity mContext;
    OnItemClick onItemClick;

    private Animation animationDown, animationUp;


    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> listsize = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    int pos;
    ProgressDialog dialog;
    AlertDialog.Builder alertDialogBuilder;


    public detailAdapter(DetailActivity context, List<DeliveryOrdersQuery.Order> restaurantList) {
        this.mContext = context;
        this.restaurantList = restaurantList;

        dialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        alertDialogBuilder = new AlertDialog.Builder(mContext,R.style.AlertDialog);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.detail_item, null);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, final int position) {
        //getting the product of the specified position
        final DeliveryOrdersQuery.Order r = restaurantList.get(position);
        pos = position;

        //binding the data with the viewholder views


        animationDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        animationUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);


        holder.Name.setText(r.cart().name());
        holder.address.setText(r.cart().address());
        holder.vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();

                for (int i = 0; i <= r.foods().size() - 1; i++) {

                    list.add(r.foods().get(i).name() + " - " +"N"+Math.round(r.foods().get(i).price()));


                    holder.qty.setText(Long.toString(r.qty()));


                }


                Log.i("array2", list.toString());


                for (String s : list) {

                    sb.append(s);
                    sb.append("\n");


                    Log.i("string", s);


                }

                holder.food.setText(sb.toString());
                sb.delete(0, sb.length());
                // holder.food.setText(list.toString());
                if (holder.vieworder.isShown()) {
                    holder.vieworder.startAnimation(animationUp);
                    holder.food.setVisibility(View.VISIBLE);
                    holder.qty.setVisibility(View.VISIBLE);
                    holder.relativeLayout.setVisibility(View.VISIBLE);


                    holder.button2.setVisibility(View.VISIBLE);
                    holder.button1.setVisibility(View.VISIBLE);
                    holder.ordertxt.setVisibility(View.VISIBLE);
                    holder.divider.setVisibility(View.VISIBLE);


                } else {


                }

            }

        });


        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.vieworder.startAnimation(animationDown);
                holder.ordertxt.setVisibility(View.INVISIBLE);

                holder.food.setVisibility(View.INVISIBLE);
                holder.qty.setVisibility(View.INVISIBLE);
                holder.relativeLayout.setVisibility(View.GONE);
                holder.button2.setVisibility(View.INVISIBLE);
                holder.button1.setVisibility(View.INVISIBLE);
                holder.divider.setVisibility(View.INVISIBLE);


            }
        });
        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setMessage("Are you sure you want to pick up?");
                alertDialogBuilder.setTitle("PLEASE CONFIRM");
                alertDialogBuilder.setCancelable(true);


                alertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int id) {
                                dialog2.cancel();


                                dialog.setMessage("Picking Up...");
                                dialog.setCancelable(true);
                                dialog.show();

                                Log.i("id", r.id());

                                MyApolloClient.getApolloClient2(mContext).mutate(UpdateOrderMutation.builder().id(r.id())
                                        .build()).enqueue(new ApolloCall.Callback<UpdateOrderMutation.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull Response<UpdateOrderMutation.Data> response) {
                                        dialog.dismiss();
                                        mContext.finish();
                                        Log.i("response", response.data().toString());


                                        mContext.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {


                                                dialog.dismiss();
                                                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // dialog.cancel();
                                    }

                                    @Override
                                    public void onFailure(@Nonnull final ApolloException e) {

                                        mContext.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "Error " + e, Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                        // dialog.cancel();
                                    }
                                });


                            }
                        });


                alertDialogBuilder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialogBuilder.create();

                alert11.show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != restaurantList ? restaurantList.size() : 0);
    }


    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView Name, ordertxt, address;
        TextView vieworder, food, qty;
        RelativeLayout relativeLayout, rel1;
        Button button1, button2;
        View divider;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.newname);
            address = itemView.findViewById(R.id.addresstxt);
            vieworder = itemView.findViewById(R.id.vieworder);
            food = itemView.findViewById(R.id.food);
            qty = itemView.findViewById(R.id.counter);
            relativeLayout = itemView.findViewById(R.id.relativeitem3);
            rel1 = itemView.findViewById(R.id.rel1);
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2);
            ordertxt = itemView.findViewById(R.id.orderdetailtxt);
            divider = itemView.findViewById(R.id.divider);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(RestaurantOrdersQuery item);

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void getPosition(int pos, String slug); //pass any things
    }

}


