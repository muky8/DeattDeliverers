package com.example.mukhtaradepoju.deattdeliverers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class LoginActivity extends AppCompatActivity {

    EditText phonenumber, password;
    String sphoneno, spassword;
    Button loginButton;
    ProgressDialog dialog;
    public static String token, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phonenumber = findViewById(R.id.phoneedittext);
        password = findViewById(R.id.passwordedittext);
        loginButton = findViewById(R.id.loginbutton);
        dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sphoneno = phonenumber.getText().toString();
                spassword = password.getText().toString();

                if(sphoneno.isEmpty()||spassword.isEmpty()){

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Enter Login Details", Toast.LENGTH_SHORT).show();

                        }

                    });
                }else{
                    Login(sphoneno, spassword);
                }

            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        final String tokenclient = preferences.getString("Token", "");
        username = preferences.getString("user", "");


        if (tokenclient.isEmpty()) {


        } else {
            Intent newintent = new Intent(LoginActivity.this, MainActivity.class);
            newintent.putExtra("username", username);
            startActivity(newintent);
            finish();
        }
//        if(tokenclient.contains("")){
//            Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this,"present",Toast.LENGTH_SHORT).show();
//            Intent newintent=new Intent(LoginActivity.this,MainActivity.class);
//            newintent.putExtra("username",username);
//            startActivity(newintent);
//        }


    }

    public void Login(String phonenumber, String password) {
        dialog.setMessage("Login User...");
        dialog.setCancelable(true);
        dialog.show();


        MyApolloClient.getApolloClient().mutate(LoginDelivererMutation.builder().phonenumber(phonenumber).password(password)
                .build()).enqueue(new ApolloCall.Callback<LoginDelivererMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<LoginDelivererMutation.Data> response) {
                dialog.cancel();
                dialog.dismiss();
                if (response.data().loginDeliverer() != null) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();


                    token = response.data().loginDeliverer().token;
                    Intent newintent = new Intent(LoginActivity.this, MainActivity.class);
                    newintent.putExtra("username", response.data().loginDeliverer().deliverer().firstname());
                    username = response.data().loginDeliverer().deliverer().firstname;
                    editor.putString("user", username);
                    editor.putString("Token", token);
                    editor.apply();
                    startActivity(newintent);

                    Log.i("response", response.toString());
                    finish();

                } else {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Check Login Details", Toast.LENGTH_SHORT).show();

                        }

                    });
                }


            }

            @Override
            public void onFailure(@Nonnull final ApolloException e) {
                dialog.cancel();
                dialog.dismiss();

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Please Check Login Details" + e.toString(), Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    }

}
