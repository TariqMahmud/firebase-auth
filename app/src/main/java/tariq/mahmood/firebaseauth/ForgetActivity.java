package tariq.mahmood.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;

public class ForgetActivity extends AppCompatActivity {

    EditText etEmail;
    CircularProgressButton btForget;

    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etEmail = findViewById(R.id.editTextEmail);
        btForget = findViewById(R.id.cirResetButton);

        mauth= FirebaseAuth.getInstance();

        btForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    etEmail.setError("Required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please provide valid email");
                    return;
                }

                btForget.startAnimation();

                mauth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btForget.revertAnimation();
                                if (task.isSuccessful()){
                                    Toasty.success(ForgetActivity.this,
                                            "Password reset link has been sent to your email",
                                            Toasty.LENGTH_SHORT).show();
                                }else {
                                    Toasty.error(ForgetActivity.this,
                                            "Email not found",
                                            Toasty.LENGTH_SHORT).show();
                                }

                            }
                        });


            }
        });
    }
}