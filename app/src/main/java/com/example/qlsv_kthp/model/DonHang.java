package com.example.qlsv_kthp.model;

public class DonHang {
    private int id;
    private String maDonHang;
    private int khachHangId;
    private String tenKhachHang;
    private String soDienThoai;
    private String ngayNhan;
    private String ngayHenTra;
    private double tongTien;
    private String trangThai; // "MOI_NHAN", "DANG_GIAT", "DA_XONG", "DA_GIAO"
    private String trangThaiThanhToan; // "CHUA_THANH_TOAN", "DA_THANH_TOAN"
    private String ghiChu;

    public DonHang() {}

    public DonHang(int id, String maDonHang, int khachHangId, String tenKhachHang, String soDienThoai,
                   String ngayNhan, String ngayHenTra, double tongTien, String trangThai,
                   String trangThaiThanhToan, String ghiChu) {
        this.id = id;
        this.maDonHang = maDonHang;
        this.khachHangId = khachHangId;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.ngayNhan = ngayNhan;
        this.ngayHenTra = ngayHenTra;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public int getKhachHangId() { return khachHangId; }
    public void setKhachHangId(int khachHangId) { this.khachHangId = khachHangId; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(String ngayNhan) { this.ngayNhan = ngayNhan; }

    public String getNgayHenTra() { return ngayHenTra; }
    public void setNgayHenTra(String ngayHenTra) { this.ngayHenTra = ngayHenTra; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTrangThaiThanhToan() { return trangThaiThanhToan; }
    public void setTrangThaiThanhToan(String trangThaiThanhToan) { this.trangThaiThanhToan = trangThaiThanhToan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
