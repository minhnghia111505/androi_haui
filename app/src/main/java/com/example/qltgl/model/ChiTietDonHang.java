package com.example.qltgl.model;

public class ChiTietDonHang {
    private int id;
    private int donHangId;
    private int dichVuId;
    private String tenDichVu;
    private String donViTinh;
    private double soLuong;
    private double donGia;
    private double thanhTien;

    public ChiTietDonHang() {}

    public ChiTietDonHang(int id, int donHangId, int dichVuId, String tenDichVu, String donViTinh, double soLuong, double donGia, double thanhTien) {
        this.id = id;
        this.donHangId = donHangId;
        this.dichVuId = dichVuId;
        this.tenDichVu = tenDichVu;
        this.donViTinh = donViTinh;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    public ChiTietDonHang(int dichVuId, String tenDichVu, String donViTinh, double soLuong, double donGia) {
        this.dichVuId = dichVuId;
        this.tenDichVu = tenDichVu;
        this.donViTinh = donViTinh;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDonHangId() { return donHangId; }
    public void setDonHangId(int donHangId) { this.donHangId = donHangId; }

    public int getDichVuId() { return dichVuId; }
    public void setDichVuId(int dichVuId) { this.dichVuId = dichVuId; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public double getSoLuong() { return soLuong; }
    public void setSoLuong(double soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
}
