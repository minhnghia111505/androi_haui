package com.example.qltgl.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.qltgl.databinding.ActivityProfileBinding;
import com.example.qltgl.db.DatabaseHelper;
import com.example.qltgl.model.TaiKhoan;
import com.example.qltgl.ui.activity.LoginActivity;
import com.example.qltgl.util.SecurityUtils;
import com.example.qltgl.util.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class MoreFragment extends Fragment {

    private ActivityProfileBinding binding;
    private SessionManager session;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityProfileBinding.inflate(inflater, container, false);
        session = new SessionManager(requireContext());
        dbHelper = new DatabaseHelper(requireContext());

        bindProfile();
        setupActions();
        return binding.getRoot();
    }

    private void bindProfile() {
        String fullName = session.getFullName();
        binding.tvFullName.setText(fullName);
        binding.tvUserRole.setText("Chủ cửa hàng Giặt là");
        binding.tvUserMeta.setText("Quản trị hệ thống (ADMIN)");

        if (!TextUtils.isEmpty(fullName)) {
            String[] parts = fullName.trim().split("\\s+");
            binding.tvAvatarInitials.setText(parts[parts.length - 1].substring(0, 1).toUpperCase());
        }
    }

    private void setupActions() {
        binding.btnAccountInfo.setOnClickListener(v -> showEditProfileDialog());
        binding.btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        binding.btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void showEditProfileDialog() {
        TaiKhoan taiKhoan = dbHelper.getTaiKhoanById(session.getUserId());
        if (taiKhoan == null) return;

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (20 * requireContext().getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding / 2, padding, 0);

        EditText etName = new EditText(requireContext());
        etName.setHint("Họ và tên");
        etName.setText(taiKhoan.getHoTen());

        EditText etEmail = new EditText(requireContext());
        etEmail.setHint("Email");
        etEmail.setText(taiKhoan.getEmail());

        layout.addView(etName);
        layout.addView(etEmail);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cập nhật thông tin cá nhân")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(getContext(), "Tên không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.updateUserProfile(session.getUserId(), name, email)) {
                        session.createLoginSession(new TaiKhoan(session.getUserId(), session.getUsername(), "", name, email, "ADMIN"));
                        bindProfile();
                        Toast.makeText(getContext(), "Đã cập nhật thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showChangePasswordDialog() {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (20 * requireContext().getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding / 2, padding, 0);

        TextInputEditText oldPass = new TextInputEditText(requireContext());
        oldPass.setHint("Mật khẩu cũ");
        TextInputEditText newPass = new TextInputEditText(requireContext());
        newPass.setHint("Mật khẩu mới");
        TextInputEditText confirmPass = new TextInputEditText(requireContext());
        confirmPass.setHint("Nhập lại mật khẩu mới");

        layout.addView(oldPass);
        layout.addView(newPass);
        layout.addView(confirmPass);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Đổi mật khẩu")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String oldPassword = textOf(oldPass);
                    String newPassword = textOf(newPass);
                    String confirmPassword = textOf(confirmPass);

                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPassword.length() < 6) {
                        Toast.makeText(getContext(), "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(getContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean success = dbHelper.changePassword(
                            session.getUserId(),
                            oldPassword,
                            SecurityUtils.sha256(newPassword)
                    );

                    Toast.makeText(getContext(),
                            success ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    session.logoutUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private String textOf(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
