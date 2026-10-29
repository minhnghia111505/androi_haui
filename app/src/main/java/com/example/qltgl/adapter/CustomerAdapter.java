package com.example.qltgl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qltgl.R;
import com.example.qltgl.model.KhachHang;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    public interface OnCustomerClickListener {
        void onCustomerClick(KhachHang khachHang);
        void onEdit(KhachHang khachHang);
        void onDelete(KhachHang khachHang);
    }

    private Context context;
    private List<KhachHang> list;
    private OnCustomerClickListener listener;

    public CustomerAdapter(Context context, List<KhachHang> list, OnCustomerClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        KhachHang kh = list.get(position);
        holder.tvTenKhachHang.setText(kh.getTenKhachHang());
        holder.tvSoDienThoai.setText(kh.getSoDienThoai());
        holder.tvDiaChi.setText(kh.getDiaChi() != null ? kh.getDiaChi() : "");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCustomerClick(kh);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(kh);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(kh);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKhachHang, tvSoDienThoai, tvDiaChi;
        ImageButton btnEdit, btnDelete;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvSoDienThoai = itemView.findViewById(R.id.tvSoDienThoai);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
