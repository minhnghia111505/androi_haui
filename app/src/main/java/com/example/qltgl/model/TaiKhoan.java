package com.example.qltgl.model;

/**
 * Lớp đại diện cho đối tượng Tài khoản người dùng (ADMIN hoặc STAFF)
 */
public class TaiKhoan {
    private int id;
    private String username;
    private String password;
    private String hoTen;
    private String email;
    private String role; // "ADMIN" hoặc "STAFF"

    public TaiKhoan() {}

    public TaiKhoan(int id, String username, String password, String hoTen, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
