package hoanglv.fpoly.assignment.Models;

import android.graphics.Bitmap;

public class Product {
    private int masp;
    private String tensp;
    private int soLuong;
    private int gia;
    private Bitmap img;

    public Product(String tensp, int soLuong, int gia, Bitmap img) {
        this.tensp = tensp;
        this.soLuong = soLuong;
        this.gia = gia;
        this.img = img;
    }

    public Product() {
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
