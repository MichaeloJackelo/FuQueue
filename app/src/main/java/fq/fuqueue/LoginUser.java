package fq.fuqueue;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.font.MaterialIcons;

import butterknife.BindView;
import butterknife.OnClick;

import static com.beardedhen.androidbootstrap.font.FontAwesome.FA_ANCHOR;

import static com.beardedhen.androidbootstrap.font.FontAwesome.FA_HEART;
import static com.beardedhen.androidbootstrap.font.FontAwesome.FA_TWITTER;
import static com.beardedhen.androidbootstrap.font.Typicon.TY_CODE;

public class LoginUser extends BaseActivity {
    boolean isLogin;
    EditText nameLogin ;
    EditText passwordLogin ;
    private int contentView;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login_user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        isLogin = false;
        nameLogin = findViewById(R.id.nameToLogin);
        passwordLogin = findViewById(R.id.passwordToLogin);
        TypefaceProvider.registerDefaultIconSets();

    }

    public void ClickLogin(View v)
    {
        if(nameLogin.getText().toString().equals("") && passwordLogin.getText().toString().equals(""))
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
