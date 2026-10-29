package com.example.qlsv_kthp.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.adapter.CustomerAdapter;
import com.example.qlsv_kthp.databinding.ActivityCustomerManagementBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DonHang;
import com.example.qlsv_kthp.model.KhachHang;

import java.util.ArrayList;
import java.util.List;

public class CustomerManagementActivity extends AppCompatActivity {

    private ActivityCustomerManagementBinding binding;
    private DatabaseHelper dbHelper;
    private CustomerAdapter adapter;
    private List<KhachHang> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(this, customerList, new CustomerAdapter.OnCustomerClickListener() {
            @Override
            public void onCustomerClick(KhachHang khachHang) {
                showCustomerOrderHistory(khachHang);
            }

            @Override
            public void onEdit(KhachHang khachHang) {
                showAddEditCustomerDialog(khachHang);
            }

            @Override
            public void onDelete(KhachHang khachHang) {
                new AlertDialog.Builder(CustomerManagementActivity.this)
                        .setTitle("Xóa khách hàng")
                        .setMessage("Bạn có chắc chắn muốn xóa khách hàng: " + khachHang.getTenKhachHang() + "?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            if (dbHelper.deleteKhachHang(khachHang.getId())) {
                                Toast.makeText(CustomerManagementActivity.this, "Đã xóa khách hàng", Toast.LENGTH_SHORT).show();
                                loadCustomers();
                            } else {
                                Toast.makeText(CustomerManagementActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        binding.rvCustomers.setAdapter(adapter);

        binding.etSearchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCustomers(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.fabAddCustomer.setOnClickListener(v -> showAddEditCustomerDialog(null));

        loadCustomers();
    }

    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(dbHelper.getAllKhachHang());
        adapter.notifyDataSetChanged();
    }

    private void filterCustomers(String query) {
        customerList.clear();
        if (query.isEmpty()) {
            customerList.addAll(dbHelper.getAllKhachHang());
        } else {
            customerList.addAll(dbHelper.searchKhachHang(query));
        }
        adapter.notifyDataSetChanged();
    }

    private void showCustomerOrderHistory(KhachHang kh) {
        List<DonHang> orders = dbHelper.getDonHangByKhachHangId(kh.getId());
        if (orders.isEmpty()) {
            Toast.makeText(this, "Khách hàng " + kh.getTenKhachHang() + " chưa có đơn hàng nào.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] orderItems = new String[orders.size()];
        for (int i = 0; i < orders.size(); i++) {
            DonHang dh = orders.get(i);
            orderItems[i] = dh.getMaDonHang() + " - " + (long) dh.getTongTien() + "đ (" + dh.getNgayNhan() + ")";
        }

        new AlertDialog.Builder(this)
                .setTitle("Lịch sử đơn giặt: " + kh.getTenKhachHang())
                .setItems(orderItems, (dialog, which) -> {
                    Intent intent = new Intent(CustomerManagementActivity.this, OrderDetailActivity.class);
                    intent.putExtra("DON_HANG_ID", orders.get(which).getId());
                    startActivity(intent);
                })
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showAddEditCustomerDialog(KhachHang khToEdit) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null);
        EditText etTen = view.findViewById(R.id.etTenKhachHang);
        EditText etSdt = view.findViewById(R.id.etSoDienThoai);
        EditText etDiaChi = view.findViewById(R.id.etDiaChi);

        if (khToEdit != null) {
            etTen.setText(khToEdit.getTenKhachHang());
            etSdt.setText(khToEdit.getSoDienThoai());
            etDiaChi.setText(khToEdit.getDiaChi());
        }

        new AlertDialog.Builder(this)
                .setTitle(khToEdit == null ? "Thêm Khách Hàng Mới" : "Chỉnh Sửa Khách Hàng")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String ten = etTen.getText().toString().trim();
                    String sdt = etSdt.getText().toString().trim();
                    String diaChi = etDiaChi.getText().toString().trim();

                    if (ten.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (sdt.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (khToEdit == null) {
                        KhachHang existing = dbHelper.searchKhachHangByPhone(sdt);
                        if (existing != null) {
                            Toast.makeText(this, "Số điện thoại đã tồn tại cho khách hàng khác", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        KhachHang kh = new KhachHang(ten, sdt, diaChi);
                        if (dbHelper.addKhachHang(kh) != -1) {
                            Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                            loadCustomers();
                        } else {
                            Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        khToEdit.setTenKhachHang(ten);
                        khToEdit.setSoDienThoai(sdt);
                        khToEdit.setDiaChi(diaChi);

                        if (dbHelper.updateKhachHang(khToEdit)) {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            loadCustomers();
                        } else {
                            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
