package com.example.qlsv_kthp.ui.activity;

import android.app.AlertDialog;
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
import com.example.qlsv_kthp.adapter.ServiceAdapter;
import com.example.qlsv_kthp.databinding.ActivityServiceManagementBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DichVu;

import java.util.ArrayList;
import java.util.List;

public class ServiceManagementActivity extends AppCompatActivity {

    private ActivityServiceManagementBinding binding;
    private DatabaseHelper dbHelper;
    private ServiceAdapter adapter;
    private List<DichVu> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.rvServices.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(this, serviceList, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onEdit(DichVu dichVu) {
                showAddEditDialog(dichVu);
            }

            @Override
            public void onDelete(DichVu dichVu) {
                new AlertDialog.Builder(ServiceManagementActivity.this)
                        .setTitle("Xóa dịch vụ")
                        .setMessage("Bạn có chắc chắn muốn xóa dịch vụ: " + dichVu.getTenDichVu() + "?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            if (dbHelper.deleteDichVu(dichVu.getId())) {
                                Toast.makeText(ServiceManagementActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                                loadServices();
                            } else {
                                Toast.makeText(ServiceManagementActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        binding.rvServices.setAdapter(adapter);

        binding.etSearchService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterServices(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.fabAddService.setOnClickListener(v -> showAddEditDialog(null));

        loadServices();
    }

    private void loadServices() {
        serviceList.clear();
        serviceList.addAll(dbHelper.getAllDichVu());
        adapter.notifyDataSetChanged();
    }

    private void filterServices(String query) {
        serviceList.clear();
        if (query.isEmpty()) {
            serviceList.addAll(dbHelper.getAllDichVu());
        } else {
            serviceList.addAll(dbHelper.searchDichVu(query));
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddEditDialog(DichVu serviceToEdit) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_service, null);
        EditText etTen = view.findViewById(R.id.etTenDichVu);
        EditText etDonVi = view.findViewById(R.id.etDonViTinh);
        EditText etDonGia = view.findViewById(R.id.etDonGia);
        EditText etMoTa = view.findViewById(R.id.etMoTa);

        if (serviceToEdit != null) {
            etTen.setText(serviceToEdit.getTenDichVu());
            etDonVi.setText(serviceToEdit.getDonViTinh());
            etDonGia.setText(String.valueOf((long) serviceToEdit.getDonGia()));
            etMoTa.setText(serviceToEdit.getMoTa());
        }

        new AlertDialog.Builder(this)
                .setTitle(serviceToEdit == null ? "Thêm Dịch vụ Giặt" : "Chỉnh sửa Dịch vụ")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String ten = etTen.getText().toString().trim();
                    String donVi = etDonVi.getText().toString().trim();
                    String donGiaStr = etDonGia.getText().toString().trim();
                    String moTa = etMoTa.getText().toString().trim();

                    if (ten.isEmpty()) {
                        Toast.makeText(this, "Tên dịch vụ không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (donVi.isEmpty()) {
                        Toast.makeText(this, "Đơn vị tính không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (donGiaStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đơn giá", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double donGia;
                    try {
                        donGia = Double.parseDouble(donGiaStr);
                    } catch (Exception e) {
                        Toast.makeText(this, "Đơn giá không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (donGia < 0) {
                        Toast.makeText(this, "Đơn giá không được âm", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (serviceToEdit == null) {
                        DichVu newDv = new DichVu(ten, donVi, donGia, moTa);
                        if (dbHelper.addDichVu(newDv)) {
                            Toast.makeText(this, "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Thêm dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        serviceToEdit.setTenDichVu(ten);
                        serviceToEdit.setDonViTinh(donVi);
                        serviceToEdit.setDonGia(donGia);
                        serviceToEdit.setMoTa(moTa);
                        if (dbHelper.updateDichVu(serviceToEdit)) {
                            Toast.makeText(this, "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cập nhật dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    loadServices();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
