package hoanglv.fpoly.assignment.ui;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hoanglv.fpoly.assignment.Activities.MainActivity;
import hoanglv.fpoly.assignment.Adapters.ProductAdapter;
import hoanglv.fpoly.assignment.Adapters.SpacingItemDecoration;
import hoanglv.fpoly.assignment.DAO.ProductDAO;
import hoanglv.fpoly.assignment.Interface.ProductInterface;
import hoanglv.fpoly.assignment.Models.Product;
import hoanglv.fpoly.assignment.Services.MyApplication;
import hoanglv.fpoly.assignment.databinding.FragmentHomeBinding;
import hoanglv.fpoly.assignment.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ProductDAO dao;
    private ProductAdapter adapter;
    private List<Product> list;
    private Product currentProduct;
    private AlertDialog.Builder builder;
    private ImageView img_edit;
    private Bitmap bitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.productView;
        dao = new ProductDAO(getContext());
        list = dao.getData();
        if (list == null) {
            list = new ArrayList<>();
        }
        adapter = new ProductAdapter(list);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20);
        recyclerView.addItemDecoration(spacingItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDeleteClick(int position) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận xoá");
                builder.setIcon(R.drawable.baseline_warning_24);
                builder.setMessage("Bạn có chắc chắn muốn xóa?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    Product product = list.get(position);
                    dao.deleteProduct(String.valueOf(product.getMasp()));
                    list.remove(position);
                    sendPopupNotification("Đã xoá " + product.getTensp());
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                });
                builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEditClick(int position) {
                currentProduct = list.get(position);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_edit_dialog, null);
                TextView tv_edit_product = view.findViewById(R.id.tv_edit_product);
                TextView tv_edit_price = view.findViewById(R.id.tv_edit_price);
                TextView tv_edit_total = view.findViewById(R.id.tv_edit_total);
                Product product = list.get(position);
                tv_edit_product.setText(product.getTensp());
                tv_edit_price.setText(String.valueOf(product.getGia()));
                tv_edit_total.setText(String.valueOf(product.getSoLuong()));
                img_edit.setImageBitmap(product.getImg());
                builder = new AlertDialog.Builder(getContext());
                builder.setView(view);
                builder.setTitle("Chỉnh sửa sản phẩm");
                builder.setPositiveButton("Lưu", (dialog, which) -> {
                    String name = tv_edit_product.getText().toString();
                    int price = Integer.parseInt(tv_edit_price.getText().toString());
                    int total = Integer.parseInt(tv_edit_total.getText().toString());
                    if (name.isEmpty() || (tv_edit_price.getText().toString().isEmpty()) || (tv_edit_total.getText().toString().isEmpty())) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    } else if (!isNumeric(tv_edit_price.getText().toString())
                            || !isNumeric(tv_edit_total.getText().toString())
                            || price <= 0 || total <= 0) {
                        Toast.makeText(getContext(), "Giá và số lượng phải là số > 0", Toast.LENGTH_SHORT).show();
                    } else {
                        currentProduct.setTensp(name);
                        currentProduct.setGia(price);
                        currentProduct.setSoLuong(total);
                        saveToDatabase(currentProduct);
                        sendPopupNotification("Đã chỉnh sửa " + currentProduct.getTensp());
                        adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return root;
    }

    private void sendPopupNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Thông báo")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), builder.build());
        }
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private void saveToDatabase(Product product) {
        dao.updateProduct(product);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        list = dao.getData();
        if (list == null) {
            list = new ArrayList<>();
        }
        adapter = new ProductAdapter(list);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20);
        recyclerView.addItemDecoration(spacingItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}