package com.example.safeme.Onboarding_Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.safeme.Dashboard.Dashboard;
import com.example.safeme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    TextInputLayout UserName, Password;
    Button LoginBtn;
    ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        //Hide Notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);

        //Hooks
        UserName = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        LoginBtn = findViewById(R.id.LoginBtn);
        progressBar = findViewById(R.id.progressBar);

        //change colour in progressbar
        progressBar.getIndeterminateDrawable().setColorFilter(0xff000000,
                android.graphics.PorterDuff.Mode.MULTIPLY);
        //Validations
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = UserName.getEditText().getText().toString();
                String password = Password.getEditText().getText().toString();

                if (isConnected()) {

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection, Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please enter your NIC", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG).show();
                }
                if (password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {

                                        } else {
                                            Toast.makeText(getApplicationContext(), "ERROR! Please check your credentials", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        finish();
                                    }
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });
//
                }
            }
        });
    }

    //Check Internet Connection Method
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void gotoUserRegister(View view) {
        Intent intent = new Intent(UserLogin.this, UserRegister.class);
        startActivity(intent);
    }

    public void gotoResetPassword(View view) {
        Intent intent = new Intent(UserLogin.this, ForgetPassword.class);
        startActivity(intent);
    }
}