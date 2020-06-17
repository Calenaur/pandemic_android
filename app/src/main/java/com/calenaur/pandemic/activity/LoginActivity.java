package com.calenaur.pandemic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.calenaur.pandemic.R;
import com.calenaur.pandemic.api.API;
import com.calenaur.pandemic.api.model.user.LocalUser;
import com.calenaur.pandemic.api.net.response.ErrorCode;
import com.calenaur.pandemic.api.store.PromiseHandler;
import com.calenaur.pandemic.app.PandemicApplication;

public class LoginActivity extends AppCompatActivity {

    private API api;
    private EditText inputUsername;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = ((PandemicApplication)getApplication()).getAPI();
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        findViewById(R.id.submitLogin).setOnClickListener(this::onLogin);
        findViewById(R.id.submitRegister).setOnClickListener(this::onRegister);

        if (getIntent().hasExtra("username") && getIntent().hasExtra("password")) {
            inputUsername.setText(getIntent().getStringExtra("username"));
            inputPassword.setText(getIntent().getStringExtra("password"));
            onLogin(null);
        }
    }

    public void onSubmit() {
        inputUsername.setError(null);
        inputUsername.getText().clear();
        inputPassword.getText().clear();
    }

    public void onLogin(View v) {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        onSubmit();
        api.getUserStore().login(username, password, new PromiseHandler<LocalUser>() {
            public void onDone(LocalUser object) {
                System.out.println(object);
                startGameActivity(object);
            }

            public void onError(ErrorCode errorCode) {
                inputUsername.setError(getResources().getString(errorCode.getResource()));
            }
        });
    }

    public void onRegister(View v) {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        onSubmit();
        api.getUserStore().register(username, password, new PromiseHandler<Object>() {
            public void onDone(Object object) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup), Toast.LENGTH_SHORT).show();
            }

            public void onError(ErrorCode errorCode) {
                inputUsername.setError(getResources().getString(errorCode.getResource()));
            }
        });
    }

    public void startGameActivity(LocalUser localUser) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
