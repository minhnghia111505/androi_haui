package com.example.qltgl.model;

public class KhachHang {
    private int id;
    private String tenKhachHang;
    private String soDienThoai;
    private String diaChi;
    private int diemTichLuy;

    public KhachHang() {}

    public KhachHang(int id, String tenKhachHang, String soDienThoai, String diaChi, int diemTichLuy) {
        this.id = id;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.diemTichLuy = diemTichLuy;
    }

    public KhachHang(String tenKhachHang, String soDienThoai, String diaChi) {
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.diemTichLuy = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
}
