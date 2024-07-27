package hoanglv.fpoly.assignment.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import hoanglv.fpoly.assignment.R;

public class ResetPassword extends AppCompatActivity {
    private EditText edtUsername;
    private TextView txtOK, txtCancel;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mappingComponents();
        setEvents();
    }

    private void mappingComponents() {
        edtUsername = findViewById(R.id.edtUsername);
        txtOK = findViewById(R.id.txtOK);
        txtCancel = findViewById(R.id.txtCancel);
    }

    private void setEvents() {
        txtCancel.setOnClickListener(v -> {
            finish();
        });
        txtOK.setOnClickListener(v -> {
            String email = edtUsername.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(ResetPassword.this, "Bạn chưa nhập đủ thông tin",
                        Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassword.this, "Đã gửi email cho bạn",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}