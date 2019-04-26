package com.example.mukhtaradepoju.deattdeliverers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.deliverorderAdapter;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.detailAdapter;

import java.util.List;

import javax.annotation.Nonnull;

public class DeliverOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    deliverorderAdapter adapter;
    List<DelivererOrdersQuery.DelivererOrder> delivererOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_orders);
        recyclerView = findViewById(R.id.recyclerview3);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//
//        MyApolloClient.getApolloClient().query(
//                DelivererOrdersQuery.builder().build()
//        ).enqueue(new ApolloCall.Callback<DelivererOrdersQuery.Data>() {
//            @Override
//            public void onResponse(@Nonnull final Response<DelivererOrdersQuery.Data> response) {
//                Log.i("ordersquery", response.data().toString());
//
//                DeliverOrders.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter = new deliverorderAdapter(DeliverOrders.this, response.data().delivererOrders);
//                        recyclerView.setAdapter(adapter);
//                        delivererOrderList = response.data().delivererOrders;
//                        adapter.notifyDataSetChanged();
//                        adapter.setOnItemClick(new restaurantAdapter.OnItemClick() {
//                            @Override
//                            public void getPosition(int pos, String slug) {
//                                Toast.makeText(getApplicationContext(),"Click Item Postion::"+slug,Toast.LENGTH_SHORT).show();
//                                Intent intent= new Intent(MainActivity.this,DetailActivity.class);
//                                intent.putExtra("slug",slug);
//                                startActivity(intent);
//
//                            }
//                        });
                    }
//                });
//
//
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//
//
//            }
//        });
//    }

}

