package hu.bockerluca.f1blogger;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private TextView usernameEditText;
    private TextView emailEditText;
    private TextView password1EditText;
    private TextView password2EditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        firebaseAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        password1EditText = findViewById(R.id.password1EditText);
        password2EditText = findViewById(R.id.password2EditText);
        Button registrationBtn = findViewById(R.id.registrationBtn);

        registrationBtn.setOnClickListener(view -> createAccount());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void createAccount() {
        String email = emailEditText.getText().toString();
        String password = password1EditText.getText().toString();
        if (this.password1EditText.getText().toString().equals(this.password2EditText.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sikeres regisztráció!",
                                    Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sikertelen regisztráció!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
        } else {
            Toast.makeText(getApplicationContext(), "Nem egyezik a két jelszó!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}