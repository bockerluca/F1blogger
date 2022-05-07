package hu.bockerluca.f1blogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Sikeres kijelentkezés!",
                Toast.LENGTH_SHORT).show();
        finish();

    }
}