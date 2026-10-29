package com.example.qltgl.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qltgl.adapter.OrderDetailAdapter;
import com.example.qltgl.databinding.ActivityCreateOrderBinding;
import com.example.qltgl.db.DatabaseHelper;
import com.example.qltgl.model.ChiTietDonHang;
import com.example.qltgl.model.DichVu;
import com.example.qltgl.model.DonHang;
import com.example.qltgl.model.KhachHang;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateOrderActivity extends AppCompatActivity {

    private ActivityCreateOrderBinding binding;
    private DatabaseHelper dbHelper;
    private List<DichVu> availableServices = new ArrayList<>();
    private List<ChiTietDonHang> orderItems = new ArrayList<>();
    private OrderDetailAdapter adapter;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private Calendar returnDateCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        returnDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        updateReturnDateText();

        binding.etNgayHenTra.setOnClickListener(v -> showDatePicker());

        binding.etSoDienThoai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString().trim();
                if (phone.length() >= 9) {
                    KhachHang existingKh = dbHelper.searchKhachHangByPhone(phone);
                    if (existingKh != null) {
                        binding.etTenKhachHang.setText(existingKh.getTenKhachHang());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        availableServices = dbHelper.getAllDichVu();
        List<String> serviceNames = new ArrayList<>();
        for (DichVu dv : availableServices) {
            serviceNames.add(dv.getTenDichVu() + " (" + formatter.format(dv.getDonGia()) + "đ/" + dv.getDonViTinh() + ")");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDichVu.setAdapter(spinnerAdapter);

        binding.rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(this, orderItems);
        binding.rvOrderItems.setAdapter(adapter);

        binding.btnAddItem.setOnClickListener(v -> addItemToOrder());
        binding.btnSaveOrder.setOnClickListener(v -> saveOrder());
    }

    private void updateReturnDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        binding.etNgayHenTra.setText(sdf.format(returnDateCalendar.getTime()));
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    returnDateCalendar.set(Calendar.YEAR, year);
                    returnDateCalendar.set(Calendar.MONTH, month);
                    returnDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateReturnDateText();
                },
                returnDateCalendar.get(Calendar.YEAR),
                returnDateCalendar.get(Calendar.MONTH),
                returnDateCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void addItemToOrder() {
        if (availableServices.isEmpty()) {
            Toast.makeText(this, "Chưa có dịch vụ giặt nào trong hệ thống", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = binding.spinnerDichVu.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= availableServices.size()) return;

        DichVu selectedDv = availableServices.get(selectedIndex);
        String qtyStr = binding.etSoLuong.getText() != null ? binding.etSoLuong.getText().toString().trim() : "";
        if (qtyStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (Exception e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (qty <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        ChiTietDonHang item = new ChiTietDonHang(selectedDv.getId(), selectedDv.getTenDichVu(), selectedDv.getDonViTinh(), qty, selectedDv.getDonGia());
        orderItems.add(item);
        adapter.notifyDataSetChanged();
        calculateTotal();
        Toast.makeText(this, "Đã thêm " + selectedDv.getTenDichVu(), Toast.LENGTH_SHORT).show();
    }

    private void calculateTotal() {
        double total = 0;
        for (ChiTietDonHang item : orderItems) {
            total += item.getThanhTien();
        }
        binding.tvTongTien.setText(formatter.format(total) + " đ");
    }

    private void saveOrder() {
        String sdt = binding.etSoDienThoai.getText() != null ? binding.etSoDienThoai.getText().toString().trim() : "";
        String tenKh = binding.etTenKhachHang.getText() != null ? binding.etTenKhachHang.getText().toString().trim() : "";
        String ghiChu = binding.etGhiChu.getText() != null ? binding.etGhiChu.getText().toString().trim() : "";
        String ngayHenTraStr = binding.etNgayHenTra.getText() != null ? binding.etNgayHenTra.getText().toString().trim() : "";

        if (sdt.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tenKh.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Đơn hàng phải chứa ít nhất 1 dịch vụ giặt", Toast.LENGTH_SHORT).show();
            return;
        }

        KhachHang kh = dbHelper.searchKhachHangByPhone(sdt);
        long khId;
        if (kh == null) {
            khId = dbHelper.addKhachHang(new KhachHang(tenKh, sdt, ""));
        } else {
            khId = kh.getId();
        }

        double total = 0;
        for (ChiTietDonHang item : orderItems) {
            total += item.getThanhTien();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String now = sdf.format(new Date());

        String maDon = "HD" + System.currentTimeMillis() % 100000;
        DonHang donHang = new DonHang(0, maDon, (int) khId, tenKh, sdt, now, ngayHenTraStr, total, "MOI_NHAN", "CHUA_THANH_TOAN", ghiChu);

        long res = dbHelper.createDonHang(donHang, orderItems);
        if (res != -1) {
            Toast.makeText(this, "Tạo đơn hàng thành công: " + maDon, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
