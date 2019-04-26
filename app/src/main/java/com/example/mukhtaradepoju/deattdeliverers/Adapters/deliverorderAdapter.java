package com.example.mukhtaradepoju.deattdeliverers.Adapters;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.mukhtaradepoju.deattdeliverers.DeliveryOrdersQuery;

import com.example.mukhtaradepoju.deattdeliverers.LoginActivity;
import com.example.mukhtaradepoju.deattdeliverers.MainActivity;
import com.example.mukhtaradepoju.deattdeliverers.MyApolloClient;
import com.example.mukhtaradepoju.deattdeliverers.R;
import com.example.mukhtaradepoju.deattdeliverers.RestaurantOrdersQuery;
import com.example.mukhtaradepoju.deattdeliverers.VerifyOrderMutation;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.recyclerview.widget.RecyclerView;

public class deliverorderAdapter extends RecyclerView.Adapter<deliverorderAdapter.RestaurantViewHolder> {

    private List<DeliveryOrdersQuery.Order> delivererOrderList;
    private MainActivity mContext;
    OnItemClick onItemClick;
    ProgressDialog dialog;

    private Animation animationDown, animationUp;
    String id, emailEdittext;


    public deliverorderAdapter(MainActivity context, List<DeliveryOrdersQuery.Order> restaurantList) {
        this.mContext = context;
        this.delivererOrderList = restaurantList;
        dialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.deliverorder_item, null);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, final int position) {
        //getting the product of the specified position
        final DeliveryOrdersQuery.Order r = delivererOrderList.get(position);

        //binding the data with the viewholder views


        animationDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        animationUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        Log.i("MMM", String.valueOf(r.cart()));

        holder.receiverName.setText(r.cart().name());
        holder.address.setText(r.cart().address());
        id = r.id();

        holder.verifytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.verifytext.isShown()) {
                    holder.verifytext.startAnimation(animationUp);
                    holder.relativeLayout.setVisibility(View.VISIBLE);

                    holder.donebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            emailEdittext = holder.email.getText().toString();
                            if (emailEdittext.isEmpty()) {
                                mContext.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "Enter Email Address", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                dialog.setMessage("Verifying...");
                                dialog.setCancelable(true);
                                dialog.show();
                                Log.i("verifyid", id);

                                MyApolloClient.getApolloClient2(mContext).mutate(VerifyOrderMutation.builder().email(emailEdittext)
                                        .id(id).build()
                                ).enqueue(new ApolloCall.Callback<VerifyOrderMutation.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull final Response<VerifyOrderMutation.Data> response) {
                                        dialog.cancel();
                                        if(response.data().toString().equals("success")){

                                            mContext.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(mContext, "Verified Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else if(response.data().toString().equals("")){

                                            mContext.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(mContext, "Order has being verified", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }





                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {
                                        Log.i("error",e.getMessage());
                                        dialog.cancel();
                                        mContext.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(mContext, "Error try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                            }


                        }
                    });


                } else {


                }


            }
        });

        holder.phonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + r.cart().phonenumber();
                i.setData(Uri.parse(p));
                mContext.startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != delivererOrderList ? delivererOrderList.size() : 0);
    }


    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView receiverName, address, verifytext;
        EditText email;
        RelativeLayout relativeLayout;
        Button donebtn;
        ImageView phonecall;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            receiverName = itemView.findViewById(R.id.receiver_name);
            address = itemView.findViewById(R.id.addressdeliver);
            email = itemView.findViewById(R.id.edittextemail);
            relativeLayout = itemView.findViewById(R.id.relaytive3);
            verifytext = itemView.findViewById(R.id.verifyorder);
            donebtn = itemView.findViewById(R.id.buttondone);
            phonecall = itemView.findViewById(R.id.phoneimg);


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


