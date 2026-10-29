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
import com.example.qltgl.model.DichVu;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onEdit(DichVu dichVu);
        void onDelete(DichVu dichVu);
    }

    private Context context;
    private List<DichVu> list;
    private OnServiceClickListener listener;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public ServiceAdapter(Context context, List<DichVu> list, OnServiceClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_laundry_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        DichVu dv = list.get(position);
        holder.tvTenDichVu.setText(dv.getTenDichVu());
        holder.tvDonGia.setText(formatter.format(dv.getDonGia()) + " đ / " + dv.getDonViTinh());
        holder.tvMoTa.setText(dv.getMoTa() != null ? dv.getMoTa() : "");

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(dv);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(dv);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDichVu, tvDonGia, tvMoTa;
        ImageButton btnEdit, btnDelete;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDichVu = itemView.findViewById(R.id.tvTenDichVu);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
