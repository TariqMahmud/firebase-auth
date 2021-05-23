package tariq.mahmood.firebaseauth;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;


public class RegisterActivity extends AppCompatActivity {

    CircularProgressButton btRegister;
    EditText etName, etEmail, etMobile, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etMobile = findViewById(R.id.editTextMobile);
        etPassword = findViewById(R.id.editTextPassword);

        btRegister = findViewById(R.id.cirRegisterButton);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Required");
                    return;
                }
                if (email.isEmpty()) {
                    etEmail.setError("Required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter valid email");
                    return;
                }
                if (mobile.isEmpty()) {
                    etMobile.setError("Required");
                    return;
                }
                if (password.isEmpty()) {
                    etPassword.setError("Required");
                    return;
                }
                if (password.length() < 6) {
                    etPassword.setError("Paswwrod must me grater than 5 char");
                    return;
                }

                btRegister.startAnimation();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email, mobile);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            btRegister.revertAnimation();
                                            if(task.isSuccessful()){
                                                Toasty.success(RegisterActivity.this, "User Register Successfully",
                                                        Toasty.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toasty.error(RegisterActivity.this, "Failed",
                                                        Toasty.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            btRegister.revertAnimation();
                            Toasty.error(RegisterActivity.this, task.getException().toString(), Toasty.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
