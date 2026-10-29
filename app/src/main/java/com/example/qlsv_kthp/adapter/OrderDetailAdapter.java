package com.example.qlsv_kthp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.model.ChiTietDonHang;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private List<ChiTietDonHang> list;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public OrderDetailAdapter(Context context, List<ChiTietDonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        ChiTietDonHang item = list.get(position);
        holder.tvTenDichVu.setText(item.getTenDichVu());
        holder.tvSoLuong.setText(item.getSoLuong() + " " + item.getDonViTinh());
        holder.tvDonGia.setText(formatter.format(item.getDonGia()) + " đ");
        holder.tvThanhTien.setText(formatter.format(item.getThanhTien()) + " đ");
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDichVu, tvSoLuong, tvDonGia, tvThanhTien;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDichVu = itemView.findViewById(R.id.tvTenDichVu);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
        }
    }
}
