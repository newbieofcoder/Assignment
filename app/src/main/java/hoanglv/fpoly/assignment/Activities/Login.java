package hoanglv.fpoly.assignment.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import hoanglv.fpoly.assignment.R;

public class Login extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private CheckBox chkRememberPassword;
    private TextView txtForgotPassword, txtLogin, txtRegister;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mappingComponents();
        setEvents();
        checkRemember();
    }

    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload();
        }
        super.onStart();
    }

    private void checkRemember() {
        SharedPreferences sharedPreferences = getSharedPreferences("remembered", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        boolean remember = sharedPreferences.getBoolean("remember", false);
        chkRememberPassword.setChecked(remember);
        if (chkRememberPassword.isChecked()) {
            edtUsername.setText(email);
            edtPassword.setText(password);
        }
    }

    private void setEvents() {
        txtRegister.setOnClickListener(v -> {
            txtRegister.setPaintFlags(txtRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            startActivity(new Intent(Login.this, Register.class));
        });
        txtForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ResetPassword.class)));
        txtLogin.setOnClickListener(v -> {
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Bạn chưa nhập đủ thông tin",
                        Toast.LENGTH_SHORT).show();
            } else {
                getLogin(email, password);
            }
        });
    }

    private void mappingComponents() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        chkRememberPassword = findViewById(R.id.chkRememberPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtLogin = findViewById(R.id.txtLogin);
        txtRegister = findViewById(R.id.txtRegister);
    }

    public void getLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                    rememberMe(email, password, chkRememberPassword.isChecked());
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Đăng nhập thành công",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Login.this, "Vui lòng xác thực email",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Đăng nhập thất bại",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rememberMe(String email, String password, boolean remember) {
        SharedPreferences sharedPreferences = getSharedPreferences("remembered", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("remember", remember);
        editor.apply();
    }
}