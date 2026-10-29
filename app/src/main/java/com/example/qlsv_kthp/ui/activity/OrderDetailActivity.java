package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.OrderDetailAdapter;
import com.example.qlsv_kthp.databinding.ActivityOrderDetailBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.ChiTietDonHang;
import com.example.qlsv_kthp.model.DonHang;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private DatabaseHelper dbHelper;
    private int donHangId = -1;
    private DonHang donHang;
    private List<ChiTietDonHang> detailList = new ArrayList<>();
    private OrderDetailAdapter adapter;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        donHangId = getIntent().getIntExtra("DON_HANG_ID", -1);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(this, detailList);
        binding.rvOrderDetails.setAdapter(adapter);

        binding.btnNextStatus.setOnClickListener(v -> changeStatusNext());
        binding.btnTogglePayment.setOnClickListener(v -> togglePaymentStatus());

        loadOrderDetail();
    }

    private void loadOrderDetail() {
        if (donHangId == -1) finish();

        donHang = dbHelper.getDonHangById(donHangId);
        if (donHang == null) {
            finish();
            return;
        }

        binding.tvMaDonHang.setText("Mã đơn: " + donHang.getMaDonHang());
        binding.tvTenKhachHang.setText("Khách: " + donHang.getTenKhachHang() + " (" + donHang.getSoDienThoai() + ")");
        binding.tvNgayNhan.setText("Ngày nhận: " + donHang.getNgayNhan());
        binding.tvTongTien.setText(formatter.format(donHang.getTongTien()) + " đ");

        updateStatusUI();

        detailList.clear();
        detailList.addAll(dbHelper.getChiTietByDonHangId(donHangId));
        adapter.notifyDataSetChanged();
    }

    private void updateStatusUI() {
        switch (donHang.getTrangThai()) {
            case "MOI_NHAN":
                binding.tvTrangThai.setText("Mới nhận");
                binding.btnNextStatus.setText("CHUYỂN SANG: ĐANG GIẶT");
                break;
            case "DANG_GIAT":
                binding.tvTrangThai.setText("Đang giặt");
                binding.btnNextStatus.setText("CHUYỂN SANG: SẴN SÀNG TRẢ");
                break;
            case "DA_XONG":
                binding.tvTrangThai.setText("Sẵn sàng trả");
                binding.btnNextStatus.setText("CHUYỂN SANG: ĐÃ GIAO KHÁCH");
                break;
            case "DA_GIAO":
                binding.tvTrangThai.setText("Đã giao");
                binding.btnNextStatus.setText("ĐƠN HÀNG HOÀN THÀNH");
                binding.btnNextStatus.setEnabled(false);
                break;
        }

        if ("DA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan())) {
            binding.btnTogglePayment.setText("ĐÃ THANH TOÁN (BẤM ĐỂ HỦY)");
        } else {
            binding.btnTogglePayment.setText("XÁC NHẬN ĐÃ THANH TOÁN");
        }
    }

    private void changeStatusNext() {
        String nextStatus = "DANG_GIAT";
        switch (donHang.getTrangThai()) {
            case "MOI_NHAN":
                nextStatus = "DANG_GIAT";
                break;
            case "DANG_GIAT":
                nextStatus = "DA_XONG";
                break;
            case "DA_XONG":
                nextStatus = "DA_GIAO";
                break;
            default:
                return;
        }

        if (dbHelper.updateDonHangStatus(donHangId, nextStatus)) {
            Toast.makeText(this, "Đã cập nhật trạng thái đơn", Toast.LENGTH_SHORT).show();
            loadOrderDetail();
        }
    }

    private void togglePaymentStatus() {
        String newPayment = "DA_THANH_TOAN".equals(donHang.getTrangThaiThanhToan()) ? "CHUA_THANH_TOAN" : "DA_THANH_TOAN";
        if (dbHelper.updatePaymentStatus(donHangId, newPayment)) {
            Toast.makeText(this, "Đã cập nhật trạng thái thanh toán", Toast.LENGTH_SHORT).show();
            loadOrderDetail();
        }
    }
}
