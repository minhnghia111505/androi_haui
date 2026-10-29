package com.example.qltgl.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qltgl.databinding.ActivityProfileBinding;
import com.example.qltgl.databinding.DialogChangePasswordBinding;
import com.example.qltgl.db.DatabaseHelper;
import com.example.qltgl.model.TaiKhoan;
import com.example.qltgl.util.SecurityUtils;
import com.example.qltgl.util.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager session;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        bindProfile();

        binding.btnAccountInfo.setOnClickListener(v -> showEditProfileDialog());
        binding.btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        binding.btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void showChangePasswordDialog() {
        DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Đổi mật khẩu")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Cập nhật", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String oldPass = dialogBinding.etOldPassword.getText().toString().trim();
                String newPass = dialogBinding.etNewPassword.getText().toString().trim();
                String confirmPass = dialogBinding.etConfirmPassword.getText().toString().trim();

                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass.length() < 6) {
                    Toast.makeText(this, "Mật khẩu phải ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                String newHashed = SecurityUtils.sha256(newPass);

                boolean success = dbHelper.changePassword(session.getUserId(), oldPass, newHashed);
                if (success) {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void bindProfile() {
        String name = session.getFullName();
        binding.tvFullName.setText(name);
        binding.tvUserRole.setText("Chủ cửa hàng Giặt là");
        binding.tvUserMeta.setText("Quản trị hệ thống (ADMIN)");

        if (!TextUtils.isEmpty(name)) {
            String[] parts = name.trim().split("\\s+");
            binding.tvAvatarInitials.setText(parts[parts.length - 1].substring(0, 1).toUpperCase());
        }
    }

    private void showEditProfileDialog() {
        TaiKhoan taiKhoan = dbHelper.getTaiKhoanById(session.getUserId());
        if (taiKhoan == null) return;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding / 2, padding, 0);

        EditText etName = new EditText(this);
        etName.setHint("Họ và tên");
        etName.setText(taiKhoan.getHoTen());

        EditText etEmail = new EditText(this);
        etEmail.setHint("Email");
        etEmail.setText(taiKhoan.getEmail());

        layout.addView(etName);
        layout.addView(etEmail);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Cập nhật thông tin cá nhân")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.updateUserProfile(session.getUserId(), name, email)) {
                        session.createLoginSession(new TaiKhoan(session.getUserId(), session.getUsername(), "", name, email, "ADMIN"));
                        bindProfile();
                        Toast.makeText(this, "Đã cập nhật thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    session.logoutUser();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
