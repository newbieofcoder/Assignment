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

import java.util.Objects;

import hoanglv.fpoly.assignment.R;

public class Register extends AppCompatActivity {
    private EditText edtUsername, edtPassword, edtConfirmPassword;
    private TextView txtOK, txtCancel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        mappingComponents();
        setEvents();
    }
    private void mappingComponents() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        txtOK = findViewById(R.id.txtOK);
        txtCancel = findViewById(R.id.txtCancel);
    }

    private void setEvents() {
        txtCancel.setOnClickListener(v -> {
            finish();
        });
        txtOK.setOnClickListener(v -> {
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "Bạn chưa nhập đủ thông tin",
                        Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(Register.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6){
                Toast.makeText(Register.this, "Mật khẩu ít nhất có 6 ký tự",
                        Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Mật khẩu nhập lại không khớp",
                        Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(Register.this, "Đăng ký thành công, vui lòng kiểm tra email", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, Objects.requireNonNull(task1.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(Register.this, Objects.requireNonNull(task.getException()).getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$";
        return email.matches(emailPattern);
    }
}