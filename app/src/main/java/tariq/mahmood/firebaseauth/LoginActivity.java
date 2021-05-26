package tariq.mahmood.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {

    EditText etEmail,etPasswrod;
    CircularProgressButton btLogin;
    TextView tvForget;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        etEmail=findViewById(R.id.editTextEmail);
        etPasswrod=findViewById(R.id.editTextPassword);
        btLogin=findViewById(R.id.cirLoginButton);
        tvForget= findViewById(R.id.tvforget);

        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(i);
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= etEmail.getText().toString().trim();
                String password= etPasswrod.getText().toString().trim();

                if(email.isEmpty()){
                    etEmail.setError("Required");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Please provide valid email");
                    return;
                }

                if(password.isEmpty()){
                    etPasswrod.setError("Required");
                    return;
                }
                if(password.length()<6){
                    etPasswrod.setError("minimum lenght is 5");
                    return;
                }

                btLogin.startAnimation();

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                btLogin.revertAnimation();
                                if(task.isSuccessful()){

                                    Intent i= new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(i);
                                }
                                else {
                                    Toasty.error(LoginActivity.this,"Filaed to login",Toasty.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });

    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
}
