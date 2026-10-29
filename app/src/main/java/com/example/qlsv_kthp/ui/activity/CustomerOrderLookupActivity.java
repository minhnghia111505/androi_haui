package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.OrderAdapter;
import com.example.qlsv_kthp.databinding.ActivityCustomerOrderLookupBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DonHang;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderLookupActivity extends AppCompatActivity {

    private ActivityCustomerOrderLookupBinding binding;
    private DatabaseHelper dbHelper;
    private OrderAdapter adapter;
    private List<DonHang> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerOrderLookupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, resultList, new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(DonHang donHang) {
                Intent intent = new Intent(CustomerOrderLookupActivity.this, OrderDetailActivity.class);
                intent.putExtra("DON_HANG_ID", donHang.getId());
                startActivity(intent);
            }

            @Override
            public void onStatusChangeClick(DonHang donHang) {
                Intent intent = new Intent(CustomerOrderLookupActivity.this, OrderDetailActivity.class);
                intent.putExtra("DON_HANG_ID", donHang.getId());
                startActivity(intent);
            }
        });
        binding.rvOrders.setAdapter(adapter);

        binding.btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = binding.etSearchQuery.getText() != null ? binding.etSearchQuery.getText().toString().trim() : "";

        if (query.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập SĐT hoặc Mã đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        resultList.clear();
        List<DonHang> orders = dbHelper.searchDonHang(query);
        if (orders != null && !orders.isEmpty()) {
            resultList.addAll(orders);
            binding.tvEmptyMessage.setVisibility(View.GONE);
            binding.tvResultTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvEmptyMessage.setText("Không tìm thấy đơn giặt nào với từ khóa '" + query + "'");
            binding.tvEmptyMessage.setVisibility(View.VISIBLE);
            binding.tvResultTitle.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
