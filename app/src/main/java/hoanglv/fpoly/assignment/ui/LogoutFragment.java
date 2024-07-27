package hoanglv.fpoly.assignment.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import hoanglv.fpoly.assignment.Activities.Login;
import hoanglv.fpoly.assignment.Activities.MainActivity;
import hoanglv.fpoly.assignment.R;

public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startActivity(new Intent(getContext(), Login.class));
        Toast.makeText(getContext(), "Đăng xuất thành công",
                Toast.LENGTH_SHORT).show();
        requireActivity().finish();
        return null;
    }
}