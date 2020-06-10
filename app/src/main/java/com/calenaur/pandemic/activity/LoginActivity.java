package com.calenaur.pandemic.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
        findViewById(R.id.submitLogin).setOnClickListener(this::onSubmit);
    }

    public void onSubmit(View v) {
        inputUsername.setError(null);
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        api.getUserStore().login(username, password, new PromiseHandler<LocalUser>() {
            public void onDone(LocalUser object) {
                System.out.println(object);
            }

            public void onError(ErrorCode errorCode) {
                inputUsername.setError(getResources().getString(errorCode.getResource()));
            }
        });
    }
}
