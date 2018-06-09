package fq.fuqueue;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginUser extends AppCompatActivity {
    boolean isLogin;
    EditText nameLogin ;
    EditText passwordLogin ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        isLogin = false;
        nameLogin = findViewById(R.id.nameToLogin);
        passwordLogin = findViewById(R.id.passwordToLogin);

    }

    public void ClickLogin(View v)
    {
        if(nameLogin.getText().toString().equals("Basia") && passwordLogin.getText().toString().equals("qwerty"))
        {
            Intent intent = new Intent(this, Chosen.class);
            startActivity(intent);
        }else {
            AlertDialog.Builder dialogBuldier = new AlertDialog.Builder(this);
            dialogBuldier.setMessage("Login or password is wrong!!!");
            dialogBuldier.create();
            dialogBuldier.show();
        }

    }


}
