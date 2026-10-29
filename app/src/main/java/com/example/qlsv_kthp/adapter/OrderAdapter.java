package com.example.qlsv_kthp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(DonHang donHang);
        void onStatusChangeClick(DonHang donHang);
    }

    private Context context;
    private List<DonHang> list;
    private OnOrderClickListener listener;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public OrderAdapter(Context context, List<DonHang> list, OnOrderClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DonHang dh = list.get(position);
        holder.tvMaDonHang.setText(dh.getMaDonHang());
        holder.tvTenKhachHang.setText(dh.getTenKhachHang() + " (" + dh.getSoDienThoai() + ")");
        holder.tvNgayNhan.setText("Nhận: " + dh.getNgayNhan());
        holder.tvTongTien.setText(formatter.format(dh.getTongTien()) + " đ");

        switch (dh.getTrangThai()) {
            case "MOI_NHAN":
                holder.tvTrangThai.setText("Mới nhận");
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "DANG_GIAT":
                holder.tvTrangThai.setText("Đang giặt");
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case "DA_XONG":
                holder.tvTrangThai.setText("Sẵn sàng trả");
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case "DA_GIAO":
                holder.tvTrangThai.setText("Đã giao");
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#9E9E9E"));
                break;
            default:
                holder.tvTrangThai.setText(dh.getTrangThai());
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOrderClick(dh);
        });

        holder.tvTrangThai.setOnClickListener(v -> {
            if (listener != null) listener.onStatusChangeClick(dh);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDonHang, tvTenKhachHang, tvNgayNhan, tvTongTien, tvTrangThai;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang = itemView.findViewById(R.id.tvMaDonHang);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvNgayNhan = itemView.findViewById(R.id.tvNgayNhan);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
