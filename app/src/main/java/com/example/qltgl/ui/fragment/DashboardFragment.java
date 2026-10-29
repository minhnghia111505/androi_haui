package com.example.qltgl.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qltgl.adapter.OrderAdapter;
import com.example.qltgl.databinding.FragmentDashboardBinding;
import com.example.qltgl.db.DatabaseHelper;
import com.example.qltgl.model.DonHang;
import com.example.qltgl.ui.activity.CreateOrderActivity;
import com.example.qltgl.ui.activity.CustomerManagementActivity;
import com.example.qltgl.ui.activity.OrderDetailActivity;
import com.example.qltgl.ui.activity.ServiceManagementActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private OrderAdapter orderAdapter;
    private List<DonHang> orderList = new ArrayList<>();
    private DecimalFormat formatter = new DecimalFormat("#,###");

    private final String[] statusFilters = {"Tất cả trạng thái", "Mới nhận", "Đang giặt", "Sẵn sàng trả", "Đã giao"};
    private final String[] statusCodes = {"ALL", "MOI_NHAN", "DANG_GIAT", "DA_XONG", "DA_GIAO"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusFilters);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilterStatus.setAdapter(spinnerAdapter);

        binding.spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterOrders();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.etSearchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter(requireContext(), orderList, new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(DonHang donHang) {
                Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
                intent.putExtra("DON_HANG_ID", donHang.getId());
                startActivity(intent);
            }

            @Override
            public void onStatusChangeClick(DonHang donHang) {
                Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
                intent.putExtra("DON_HANG_ID", donHang.getId());
                startActivity(intent);
            }
        });
        binding.rvOrders.setAdapter(orderAdapter);

        binding.btnCreateOrder.setOnClickListener(v -> startActivity(new Intent(requireContext(), CreateOrderActivity.class)));
        binding.btnManageServices.setOnClickListener(v -> startActivity(new Intent(requireContext(), ServiceManagementActivity.class)));
        binding.btnManageCustomers.setOnClickListener(v -> startActivity(new Intent(requireContext(), CustomerManagementActivity.class)));

        loadDashboardData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        int todayOrders = dbHelper.getTodayOrderCount();
        int pendingOrders = dbHelper.getPendingOrderCount();
        int completedOrders = dbHelper.getCompletedOrderCount();
        double todayRevenue = dbHelper.getTodayRevenue();

        binding.tvTodayOrders.setText(String.valueOf(todayOrders));
        binding.tvPendingOrders.setText(String.valueOf(pendingOrders));
        binding.tvCompletedOrders.setText(String.valueOf(completedOrders));
        binding.tvTodayRevenue.setText(formatter.format(todayRevenue) + " đ");

        filterOrders();
    }

    private void filterOrders() {
        if (binding == null) return;

        String query = binding.etSearchOrder.getText() != null ? binding.etSearchOrder.getText().toString().trim() : "";
        int selectedStatusIndex = binding.spinnerFilterStatus.getSelectedItemPosition();
        String selectedStatusCode = (selectedStatusIndex >= 0 && selectedStatusIndex < statusCodes.length) ? statusCodes[selectedStatusIndex] : "ALL";

        orderList.clear();
        List<DonHang> rawList;
        if (query.isEmpty()) {
            rawList = dbHelper.getDonHangByStatus(selectedStatusCode);
        } else {
            List<DonHang> searchResults = dbHelper.searchDonHang(query);
            if ("ALL".equalsIgnoreCase(selectedStatusCode)) {
                rawList = searchResults;
            } else {
                rawList = new ArrayList<>();
                for (DonHang dh : searchResults) {
                    if (selectedStatusCode.equalsIgnoreCase(dh.getTrangThai())) {
                        rawList.add(dh);
                    }
                }
            }
        }

        orderList.addAll(rawList);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
