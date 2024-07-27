package hoanglv.fpoly.assignment.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import hoanglv.fpoly.assignment.ModelHelper.ProductDataHelper;
import hoanglv.fpoly.assignment.Models.Product;

public class ProductDAO {

    private SQLiteDatabase db;

    public ProductDAO(Context context) {
        ProductDataHelper dbHelper = new ProductDataHelper(context);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();
    }

    public long addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("tensp", product.getTensp());
        values.put("soLuong", product.getSoLuong());
        values.put("gia", product.getGia());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        product.getImg().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        values.put("anh", encodedImage);
        return db.insert("PRODUCT", null, values);
    }

    public List<Product> getData() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Product product = new Product();
                product.setMasp(cursor.getInt(0));
                product.setTensp(cursor.getString(1));
                product.setSoLuong(cursor.getInt(2));
                product.setGia(cursor.getInt(3));
                String img = cursor.getString(4);
                byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                product.setImg(bitmap);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("masp", product.getMasp());
        values.put("tensp", product.getTensp());
        values.put("soLuong", product.getSoLuong());
        values.put("gia", product.getGia());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        product.getImg().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        values.put("anh", encodedImage);
        db.update("PRODUCT", values, "masp = ?", new String[]{String.valueOf(product.getMasp())});
    }

    public void deleteProduct(String masp) {
        db.delete("PRODUCT", "masp = ?", new String[]{String.valueOf(masp)});
    }
}
