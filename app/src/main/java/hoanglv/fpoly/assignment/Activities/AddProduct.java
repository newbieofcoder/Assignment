package hoanglv.fpoly.assignment.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import hoanglv.fpoly.assignment.DAO.ProductDAO;
import hoanglv.fpoly.assignment.ModelHelper.ProductDataHelper;
import hoanglv.fpoly.assignment.Models.Product;
import hoanglv.fpoly.assignment.R;
import hoanglv.fpoly.assignment.Services.MyApplication;

public class AddProduct extends AppCompatActivity {
    private ImageView img_back, img_add;
    private Button btn_select_image;
    private EditText tv_new_product, tv_new_price, tv_total;
    private TextView tv_add;
    private ProductDataHelper db;
    private ProductDAO dao;
    private Bitmap bitmap;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mappingComponents();
        setEvents();
    }

    private void setEvents() {
        img_back.setOnClickListener(v -> finish());
        tv_add.setOnClickListener(v -> {
            String name = tv_new_product.getText().toString();
            int price = Integer.parseInt(tv_new_price.getText().toString());
            int total = Integer.parseInt(tv_total.getText().toString());
            if (name.isEmpty() || (tv_new_price.getText().toString().isEmpty()) || (tv_total.getText().toString().isEmpty())) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!isNumeric(tv_new_price.getText().toString())
                    || !isNumeric(tv_total.getText().toString())
                    || price <= 0 || total <= 0) {
                Toast.makeText(this, "Giá và số lượng phải là số > 0", Toast.LENGTH_SHORT).show();
            } else {
                saveToDatabase(name, price, total, bitmap);
                sendNotification("Đã thêm " + name);
                finish();
            }
        });
        btn_select_image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });
    }

    private void saveToDatabase(String name, int price, int total, Bitmap bitmap) {
        dao.addProduct(new Product(name, total, price, bitmap));
    }

    private void mappingComponents() {
        img_back = findViewById(R.id.img_back);
        img_add = findViewById(R.id.img_add);
        tv_new_product = findViewById(R.id.tv_new_product);
        tv_new_price = findViewById(R.id.tv_new_price);
        tv_total = findViewById(R.id.tv_total);
        tv_add = findViewById(R.id.tv_add);
        btn_select_image = findViewById(R.id.btn_select_image);
        db = new ProductDataHelper(this);
        dao = new ProductDAO(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                img_add.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void sendNotification(String notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Thông báo")
                .setContentText(notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(1, builder.build());
    }
}