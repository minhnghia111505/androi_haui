package com.example.qltgl.model;

public class DichVu {
    private int id;
    private String tenDichVu;
    private String donViTinh;
    private double donGia;
    private String moTa;

    public DichVu() {}

    public DichVu(int id, String tenDichVu, String donViTinh, double donGia, String moTa) {
        this.id = id;
        this.tenDichVu = tenDichVu;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.moTa = moTa;
    }

    public DichVu(String tenDichVu, String donViTinh, double donGia, String moTa) {
        this.tenDichVu = tenDichVu;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.moTa = moTa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
