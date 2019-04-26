package com.example.mukhtaradepoju.deattdeliverers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.detailAdapter;
import com.example.mukhtaradepoju.deattdeliverers.Adapters.restaurantAdapter;
import com.example.mukhtaradepoju.deattdeliverers.POJO.detailPojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import type.OrderStatus;

import static com.example.mukhtaradepoju.deattdeliverers.MainActivity.arraylist;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    detailAdapter adapter;
    List<RestaurantOrdersQuery.Order> restaurants;
    ImageView back;
    LottieAnimationView lottieAnimationView;
    DeliveryOrdersQuery.Order r;
    ArrayList<? extends DeliveryOrdersQuery.Order> itemList;
    TextView titletxt,pendingtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        recyclerView = findViewById(R.id.recyclerview3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back = findViewById(R.id.backbtn);
        titletxt=findViewById(R.id.title);
        pendingtext=findViewById(R.id.pending);
//        lottieAnimationView = findViewById(R.id.lottieview);
//        lottieAnimationView.setVisibility(View.VISIBLE);
//        lottieAnimationView.playAnimation();

        Log.i("kk", arraylist.toString());
        Intent intent=getIntent();
        String title=intent.getStringExtra("detail");
        String pending=intent.getStringExtra("pending");

        if(title!=null){
            titletxt.setText(title);
            pendingtext.setText(pending+" pending Order(s)");
        }else{
            titletxt.setText("Empty");
        }

//        detailPojo detail= intent.getParcelableExtra("detail");
//        String name =detail.getName();
//        itemList=detail.getItemList();

//        if(name!=null){
//            Log.i("namedetail",name);
//            Log.i("namedetail",itemList.get(0).id);
//        }else{
//            Log.i("null","null");
//        }


//        r=getIntent().getSerializableExtra("data");
//        Log.i("name",r.name);


//        Bundle extras = getIntent().getExtras();
//        if (extras != null)
//        {
//            r = (DeliveryOrdersQuery.DelivererOrder)extras.getSerializable("data");
//            Log.i("R",r.name);
//            // do something with the customer
//        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRestaurants();


    }


    public void getRestaurants() {
        // Showing refresh animation before making http call
//        mSwipeRefreshLayout.setRefreshing(true);

        DetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                adapter = new detailAdapter(DetailActivity.this, arraylist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        });


    }
}
