package com.example.qltgl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qltgl.model.ChiTietDonHang;
import com.example.qltgl.model.DichVu;
import com.example.qltgl.model.DonHang;
import com.example.qltgl.model.KhachHang;
import com.example.qltgl.model.TaiKhoan;
import com.example.qltgl.util.SecurityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LaundryManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TAI_KHOAN = "TAIKHOAN";
    public static final String TABLE_DICH_VU = "DICHVU";
    public static final String TABLE_KHACH_HANG = "KHACHHANG";
    public static final String TABLE_DON_HANG = "DONHANG";
    public static final String TABLE_CHI_TIET_DON_HANG = "CHITIETDONHANG";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TAI_KHOAN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "hoTen TEXT NOT NULL, " +
                "email TEXT, " +
                "role TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_DICH_VU + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenDichVu TEXT NOT NULL, " +
                "donViTinh TEXT NOT NULL, " +
                "donGia REAL NOT NULL, " +
                "moTa TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_KHACH_HANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenKhachHang TEXT NOT NULL, " +
                "soDienThoai TEXT UNIQUE NOT NULL, " +
                "diaChi TEXT, " +
                "diemTichLuy INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_DON_HANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maDonHang TEXT UNIQUE NOT NULL, " +
                "khachHangId INTEGER, " +
                "tenKhachHang TEXT NOT NULL, " +
                "soDienThoai TEXT NOT NULL, " +
                "ngayNhan TEXT NOT NULL, " +
                "ngayHenTra TEXT, " +
                "tongTien REAL NOT NULL, " +
                "trangThai TEXT NOT NULL, " +
                "trangThaiThanhToan TEXT NOT NULL, " +
                "ghiChu TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CHI_TIET_DON_HANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "donHangId INTEGER NOT NULL, " +
                "dichVuId INTEGER NOT NULL, " +
                "tenDichVu TEXT NOT NULL, " +
                "donViTinh TEXT NOT NULL, " +
                "soLuong REAL NOT NULL, " +
                "donGia REAL NOT NULL, " +
                "thanhTien REAL NOT NULL, " +
                "FOREIGN KEY(donHangId) REFERENCES DONHANG(id) ON DELETE CASCADE)");

        seedSampleData(db);
    }

    private void seedSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_TAI_KHOAN + " (username, password, hoTen, email, role) VALUES ('admin', '123456', 'Chủ cửa hàng Giặt là', 'admin@laundry.com', 'ADMIN')");

        db.execSQL("INSERT INTO " + TABLE_DICH_VU + " (tenDichVu, donViTinh, donGia, moTa) VALUES ('Giặt sấy quần áo', 'kg', 15000, 'Giặt sấy thơm tho, gấp gọn')");
        db.execSQL("INSERT INTO " + TABLE_DICH_VU + " (tenDichVu, donViTinh, donGia, moTa) VALUES ('Giặt hấp Comle / Váy', 'bộ', 50000, 'Giặt hấp cao cấp bảo vệ chất vải')");
        db.execSQL("INSERT INTO " + TABLE_DICH_VU + " (tenDichVu, donViTinh, donGia, moTa) VALUES ('Giặt chăn mền / Drap', 'cái', 35000, 'Giặt sấy diệt khuẩn mền gối')");
        db.execSQL("INSERT INTO " + TABLE_DICH_VU + " (tenDichVu, donViTinh, donGia, moTa) VALUES ('Ủi quần áo', 'cái', 10000, 'Ủi phẳng theo yêu cầu')");
        db.execSQL("INSERT INTO " + TABLE_DICH_VU + " (tenDichVu, donViTinh, donGia, moTa) VALUES ('Giặt giày thể thao', 'đôi', 40000, 'Vệ sinh giặt sấy giày')");

        db.execSQL("INSERT INTO " + TABLE_KHACH_HANG + " (tenKhachHang, soDienThoai, diaChi, diemTichLuy) VALUES ('Nguyễn Văn An', '0912345678', 'Số 12 Cầu Giấy, Hà Nội', 50)");
        db.execSQL("INSERT INTO " + TABLE_KHACH_HANG + " (tenKhachHang, soDienThoai, diaChi, diemTichLuy) VALUES ('Trần Thị Bích', '0987654321', 'Số 45 Nhổn, Bắc Từ Liêm', 30)");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String now = sdf.format(new Date());

        db.execSQL("INSERT INTO " + TABLE_DON_HANG + " (maDonHang, khachHangId, tenKhachHang, soDienThoai, ngayNhan, ngayHenTra, tongTien, trangThai, trangThaiThanhToan, ghiChu) VALUES " +
                "('HD" + System.currentTimeMillis() % 100000 + "', 1, 'Nguyễn Văn An', '0912345678', '" + now + "', '" + now + "', 75000, 'MOI_NHAN', 'CHUA_THANH_TOAN', 'Giặt gấp trong ngày')");

        db.execSQL("INSERT INTO " + TABLE_CHI_TIET_DON_HANG + " (donHangId, dichVuId, tenDichVu, donViTinh, soLuong, donGia, thanhTien) VALUES " +
                "(1, 1, 'Giặt sấy quần áo', 'kg', 5, 15000, 75000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHI_TIET_DON_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DON_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHACH_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICH_VU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
        onCreate(db);
    }

    // ==========================================
    // TÀI KHOẢN (USER / LOGIN)
    // ==========================================
    public TaiKhoan checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TAI_KHOAN + " WHERE username = ? AND (password = ? OR password = ?)",
                new String[]{username, password, SecurityUtils.sha256(password)});

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = new TaiKhoan();
            tk.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            tk.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            tk.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            tk.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow("hoTen")));
            tk.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            tk.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            cursor.close();
            return tk;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean registerUser(TaiKhoan tk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", tk.getUsername());
        values.put("password", tk.getPassword());
        values.put("hoTen", tk.getHoTen());
        values.put("email", tk.getEmail());
        values.put("role", tk.getRole() != null ? tk.getRole() : "STAFF");

        long result = db.insert(TABLE_TAI_KHOAN, null, values);
        return result != -1;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_TAI_KHOAN + " WHERE username = ?", new String[]{username});
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public TaiKhoan getTaiKhoanById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TAI_KHOAN + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = new TaiKhoan();
            tk.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            tk.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            tk.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            tk.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow("hoTen")));
            tk.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            tk.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            cursor.close();
            return tk;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean changePassword(int userId, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPass);
        return db.update(TABLE_TAI_KHOAN, values, "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean updateUserProfile(int userId, String hoTen, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoTen", hoTen);
        values.put("email", email);
        return db.update(TABLE_TAI_KHOAN, values, "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean resetPassword(String username, String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update(TABLE_TAI_KHOAN, values, "username = ? AND email = ?", new String[]{username, email});
        return rows > 0;
    }

    // ==========================================
    // DỊCH VỤ GIẶT (SERVICES)
    // ==========================================
    public List<DichVu> getAllDichVu() {
        List<DichVu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DICH_VU + " ORDER BY id DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DichVu dv = new DichVu(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenDichVu")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donViTinh")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("donGia")),
                        cursor.getString(cursor.getColumnIndexOrThrow("moTa"))
                );
                list.add(dv);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<DichVu> searchDichVu(String query) {
        List<DichVu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DICH_VU + " WHERE tenDichVu LIKE ? ORDER BY id DESC",
                new String[]{"%" + query + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DichVu dv = new DichVu(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenDichVu")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donViTinh")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("donGia")),
                        cursor.getString(cursor.getColumnIndexOrThrow("moTa"))
                );
                list.add(dv);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean addDichVu(DichVu dv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenDichVu", dv.getTenDichVu());
        values.put("donViTinh", dv.getDonViTinh());
        values.put("donGia", dv.getDonGia());
        values.put("moTa", dv.getMoTa());
        return db.insert(TABLE_DICH_VU, null, values) != -1;
    }

    public boolean updateDichVu(DichVu dv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenDichVu", dv.getTenDichVu());
        values.put("donViTinh", dv.getDonViTinh());
        values.put("donGia", dv.getDonGia());
        values.put("moTa", dv.getMoTa());
        return db.update(TABLE_DICH_VU, values, "id = ?", new String[]{String.valueOf(dv.getId())}) > 0;
    }

    public boolean deleteDichVu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DICH_VU, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==========================================
    // KHÁCH HÀNG (CUSTOMERS)
    // ==========================================
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KHACH_HANG + " ORDER BY id DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                KhachHang kh = new KhachHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenKhachHang")),
                        cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diaChi")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("diemTichLuy"))
                );
                list.add(kh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<KhachHang> searchKhachHang(String query) {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KHACH_HANG + " WHERE tenKhachHang LIKE ? OR soDienThoai LIKE ? ORDER BY id DESC",
                new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                KhachHang kh = new KhachHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenKhachHang")),
                        cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diaChi")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("diemTichLuy"))
                );
                list.add(kh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean updateKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenKhachHang", kh.getTenKhachHang());
        values.put("soDienThoai", kh.getSoDienThoai());
        values.put("diaChi", kh.getDiaChi());
        return db.update(TABLE_KHACH_HANG, values, "id = ?", new String[]{String.valueOf(kh.getId())}) > 0;
    }

    public boolean deleteKhachHang(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_KHACH_HANG, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<DonHang> getDonHangByKhachHangId(int khachHangId) {
        return getDonHangByQuery("SELECT * FROM " + TABLE_DON_HANG + " WHERE khachHangId = ? ORDER BY id DESC", new String[]{String.valueOf(khachHangId)});
    }

    public KhachHang searchKhachHangByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KHACH_HANG + " WHERE soDienThoai = ?", new String[]{phone});

        if (cursor != null && cursor.moveToFirst()) {
            KhachHang kh = new KhachHang(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("tenKhachHang")),
                    cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")),
                    cursor.getString(cursor.getColumnIndexOrThrow("diaChi")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("diemTichLuy"))
            );
            cursor.close();
            return kh;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public long addKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenKhachHang", kh.getTenKhachHang());
        values.put("soDienThoai", kh.getSoDienThoai());
        values.put("diaChi", kh.getDiaChi());
        values.put("diemTichLuy", kh.getDiemTichLuy());
        return db.insert(TABLE_KHACH_HANG, null, values);
    }

    // ==========================================
    // ĐƠN HÀNG (LAUNDRY ORDERS)
    // ==========================================
    public long createDonHang(DonHang donHang, List<ChiTietDonHang> chiTietList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("maDonHang", donHang.getMaDonHang());
            values.put("khachHangId", donHang.getKhachHangId());
            values.put("tenKhachHang", donHang.getTenKhachHang());
            values.put("soDienThoai", donHang.getSoDienThoai());
            values.put("ngayNhan", donHang.getNgayNhan());
            values.put("ngayHenTra", donHang.getNgayHenTra());
            values.put("tongTien", donHang.getTongTien());
            values.put("trangThai", donHang.getTrangThai() != null ? donHang.getTrangThai() : "MOI_NHAN");
            values.put("trangThaiThanhToan", donHang.getTrangThaiThanhToan() != null ? donHang.getTrangThaiThanhToan() : "CHUA_THANH_TOAN");
            values.put("ghiChu", donHang.getGhiChu());

            long donHangId = db.insert(TABLE_DON_HANG, null, values);

            if (donHangId != -1) {
                for (ChiTietDonHang item : chiTietList) {
                    ContentValues detailValues = new ContentValues();
                    detailValues.put("donHangId", donHangId);
                    detailValues.put("dichVuId", item.getDichVuId());
                    detailValues.put("tenDichVu", item.getTenDichVu());
                    detailValues.put("donViTinh", item.getDonViTinh());
                    detailValues.put("soLuong", item.getSoLuong());
                    detailValues.put("donGia", item.getDonGia());
                    detailValues.put("thanhTien", item.getThanhTien());
                    db.insert(TABLE_CHI_TIET_DON_HANG, null, detailValues);
                }
                db.setTransactionSuccessful();
            }
            return donHangId;
        } finally {
            db.endTransaction();
        }
    }

    public List<DonHang> getAllDonHang() {
        return getDonHangByQuery("SELECT * FROM " + TABLE_DON_HANG + " ORDER BY id DESC", null);
    }

    public List<DonHang> searchDonHang(String query) {
        String sql = "SELECT * FROM " + TABLE_DON_HANG + " WHERE maDonHang LIKE ? OR tenKhachHang LIKE ? OR soDienThoai LIKE ? ORDER BY id DESC";
        String pattern = "%" + query + "%";
        return getDonHangByQuery(sql, new String[]{pattern, pattern, pattern});
    }

    public List<DonHang> getDonHangByStatus(String status) {
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("ALL")) {
            return getAllDonHang();
        }
        return getDonHangByQuery("SELECT * FROM " + TABLE_DON_HANG + " WHERE trangThai = ? ORDER BY id DESC", new String[]{status});
    }

    private List<DonHang> getDonHangByQuery(String sql, String[] args) {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DonHang dh = new DonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("maDonHang")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("khachHangId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenKhachHang")),
                        cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ngayNhan")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ngayHenTra")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("tongTien")),
                        cursor.getString(cursor.getColumnIndexOrThrow("trangThai")),
                        cursor.getString(cursor.getColumnIndexOrThrow("trangThaiThanhToan")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ghiChu"))
                );
                list.add(dh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public DonHang getDonHangById(int id) {
        List<DonHang> list = getDonHangByQuery("SELECT * FROM " + TABLE_DON_HANG + " WHERE id = ?", new String[]{String.valueOf(id)});
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ChiTietDonHang> getChiTietByDonHangId(int donHangId) {
        List<ChiTietDonHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHI_TIET_DON_HANG + " WHERE donHangId = ?", new String[]{String.valueOf(donHangId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietDonHang item = new ChiTietDonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("donHangId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("dichVuId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenDichVu")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donViTinh")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("soLuong")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("donGia")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("thanhTien"))
                );
                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean updateDonHangStatus(int donHangId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangThai", newStatus);
        return db.update(TABLE_DON_HANG, values, "id = ?", new String[]{String.valueOf(donHangId)}) > 0;
    }

    public boolean updatePaymentStatus(int donHangId, String newPaymentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangThaiThanhToan", newPaymentStatus);
        return db.update(TABLE_DON_HANG, values, "id = ?", new String[]{String.valueOf(donHangId)}) > 0;
    }

    // ==========================================
    // THỐNG KÊ (DASHBOARD ANALYTICS)
    // ==========================================
    public int getTodayOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DON_HANG + " WHERE ngayNhan LIKE ?", new String[]{today + "%"});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int getPendingOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DON_HANG + " WHERE trangThai IN ('MOI_NHAN', 'DANG_GIAT')", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int getCompletedOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DON_HANG + " WHERE trangThai = 'DA_XONG'", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public double getTodayRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        Cursor cursor = db.rawQuery("SELECT SUM(tongTien) FROM " + TABLE_DON_HANG + " WHERE ngayNhan LIKE ? AND trangThaiThanhToan = 'DA_THANH_TOAN'", new String[]{today + "%"});
        double sum = 0;
        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
            cursor.close();
        }
        return sum;
    }
}
