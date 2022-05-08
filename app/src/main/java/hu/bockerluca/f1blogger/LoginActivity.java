package hu.bockerluca.f1blogger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firebaseAuth = FirebaseAuth.getInstance();

        TextView linkToRegistration = findViewById(R.id.linkToRegistration);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> singIn());

        linkToRegistration.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

 public void singIn() {
        if (emailEditText.getText().toString().length() != 0 && passwordEditText.getText().toString().length() != 0){
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            firebaseAuth.signInWithEmailAndPassword("luca.boci@gmail.com", "qweqwe")
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Sikeres belépés!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sikertelen belépés!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Minden mező kitöltése kötelező!!",
                    Toast.LENGTH_SHORT).show();
        }

 }
}