package hoanglv.fpoly.assignment.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hoanglv.fpoly.assignment.Activities.Login;
import hoanglv.fpoly.assignment.databinding.FragmentChangepasswordBinding;

public class ChangPasswordFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AuthCredential authCredential;
    private SharedPreferences sharedPreferences;
    private FragmentChangepasswordBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangepasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText edtUsername = binding.edtUsername;
        EditText edtPassword = binding.edtPassword;
        EditText edtNewPassword = binding.edtNewPassword;
        EditText edtConfirmNewPassword = binding.edtConfirmNewPassword;
        TextView tvOK = binding.txtOK;
        Intent intent = requireActivity().getIntent();
        String oldPassword = intent.getStringExtra("password");
        mAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        tvOK.setOnClickListener(v -> {
            mUser = mAuth.getCurrentUser();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String confirmNewPassword = edtConfirmNewPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!username.equals(mUser.getEmail())) {
                Toast.makeText(getContext(), "Email không đúng", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(oldPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.show();
                            startActivity(new Intent(getContext(), Login.class));
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }
                });

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}