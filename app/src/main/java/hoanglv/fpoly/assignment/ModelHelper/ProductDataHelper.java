package hoanglv.fpoly.assignment.ModelHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ProductDataHelper extends SQLiteOpenHelper {
    public ProductDataHelper(Context context) {
        super(context, "PRODUCT.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE PRODUCT (" +
                "masp INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tensp TEXT, " +
                "soLuong INTEGER, " +
                "gia INTEGER, " +
                "anh TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        onCreate(db);
    }
}
