package com.example.mukhtaradepoju.deattdeliverers;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.deliverorderAdapter;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.restaurantAdapter;
import com.example.mukhtaradepoju.deattdeliverers.POJO.detailPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import type.OrderStatus;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    restaurantAdapter adapter;
    deliverorderAdapter adapter2;
    List<DeliveryOrdersQuery.DelivererOrder> restaurants;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SwitchCompat switchbtn;
    List<DeliveryOrdersQuery.DelivererOrder> delivererOrderList;
    String username;
    Boolean store = false;
    TextView name, pick, select;
    ImageView logout;
    CardView relativeLayout;

    List<DeliveryOrdersQuery.Order> restaurantList = new ArrayList<DeliveryOrdersQuery.Order>();
    public static List<DeliveryOrdersQuery.Order> arraylist = new ArrayList<DeliveryOrdersQuery.Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview2);

        name = findViewById(R.id.welcome);
        pick = findViewById(R.id.picktxt);
        select = findViewById(R.id.select_rest);
        logout = findViewById(R.id.logout);
        relativeLayout = findViewById(R.id.card1);



        Intent detailintent = getIntent();
        username = detailintent.getStringExtra("username");
        name.setText("Welcome " + username);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        switchbtn = findViewById(R.id.switch2);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

//                mSwipeRefreshLayout.setRefreshing(true);
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }

                // Fetching data from server
                if (isNetworkAvailable()) {
                    getRestaurants();
                } else {
                    Toast.makeText(MainActivity.this, "Make sure you're connected to the internet", Toast.LENGTH_LONG).show();
                }

            }
        });


        switchbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    store = isChecked;
                    pick.setText("Deliver");
                    select.setText("Deliver Orders");
                    mSwipeRefreshLayout.post(new Runnable() {

                        @Override
                        public void run() {

//                mSwipeRefreshLayout.setRefreshing(true);
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(true);
                            }

                            // Fetching data from server
                            if (isNetworkAvailable()) {
                                deliverorder();
                            } else {
                                Toast.makeText(MainActivity.this, "Make sure you're connected to the internet", Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                } else {
                    pick.setText("Pick up");
                    select.setText("Select Restaurant");

                    mSwipeRefreshLayout.post(new Runnable() {

                        @Override
                        public void run() {

//                mSwipeRefreshLayout.setRefreshing(true);
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(true);
                            }

                            // Fetching data from server
                            if (isNetworkAvailable()) {
                                getRestaurants();
                            } else {
                                Toast.makeText(MainActivity.this, "Make sure you're connected to the internet", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Token","");//RESET TOKEN TO EMPTY
                editor.apply();
                finish();
            }
        });


    }

    @Override
    protected void onRestart() {
        if (adapter.hasObservers()) {
            getRestaurants();
        } else {
            deliverorder();
        }
        super.onRestart();
    }

    @Override
    public void onRefresh() {
        if (adapter.hasObservers()) {
            getRestaurants();
        } else {
            deliverorder();
        }
        // Fetching data from server

    }


    public void getRestaurants() {
        // Showing refresh animation before making http call
//        mSwipeRefreshLayout.setRefreshing(true);
        MyApolloClient.getApolloClient2(MainActivity.this).query(
                DeliveryOrdersQuery.builder().status(OrderStatus.READY).build()
        ).enqueue(new ApolloCall.Callback<DeliveryOrdersQuery.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<DeliveryOrdersQuery.Data> response) {


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new restaurantAdapter(MainActivity.this, response.data().delivererOrders());
                        Log.i("nth", response.data().toString());
                        recyclerView.setAdapter(adapter);
                        restaurants = response.data().delivererOrders();
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        adapter.setOnItemClick(new restaurantAdapter.OnItemClick() {
                            @Override
                            public void getPosition(int pos, DeliveryOrdersQuery.DelivererOrder r) {
                                Intent intent = new Intent(MainActivity.this, DetailActivity.class);




                                arraylist = response.data().delivererOrders.get(pos).orders();
//                                detailPojo pojo=new detailPojo();
//                                pojo.setId(r.id);
//                                pojo.setName(r.name());
//                                pojo.setItemList(r.orders());
                              intent.putExtra("detail",response.data().delivererOrders.get(pos).name);
                              intent.putExtra("pending",String.valueOf(r.orders.size()));
                                startActivity(intent);

                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void deliverorder() {

        restaurantList.clear();
        MyApolloClient.getApolloClient2(MainActivity.this).query(
                DeliveryOrdersQuery.builder().status(OrderStatus.DELIVERING).build()
        ).enqueue(new ApolloCall.Callback<DeliveryOrdersQuery.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<DeliveryOrdersQuery.Data> response) {
                Log.i("ordersquery", response.data().toString());


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.data().delivererOrders!=null){
                            for (int i = 0; i <= response.data().delivererOrders.size() - 1; i++) {

                                restaurantList.addAll(response.data().delivererOrders.get(i).orders);
                            }
                        }


                        Log.i("size", restaurantList.toString());

                        mSwipeRefreshLayout.setRefreshing(false);

                        adapter2 = new deliverorderAdapter(MainActivity.this, restaurantList);


                        recyclerView.setAdapter(adapter2);
                        delivererOrderList = response.data().delivererOrders;

                        adapter2.notifyDataSetChanged();


//                        if(response.data().delivererOrders().size()==0){
//                            Toast.makeText(MainActivity.this,"Empty",Toast.LENGTH_SHORT).show();
//                        }

                    }
                });


            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
