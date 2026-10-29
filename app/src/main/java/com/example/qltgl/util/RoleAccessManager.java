package com.example.qltgl.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public final class RoleAccessManager {

    private RoleAccessManager() {
    }

    public static boolean isAdmin(Context context) {
        return new SessionManager(context).isAdmin();
    }

    public static boolean isStaff(Context context) {
        return new SessionManager(context).isStaff();
    }

    public static boolean requireAdmin(Activity activity) {
        if (isAdmin(activity)) {
            return true;
        }
        Toast.makeText(activity, "Bạn không có quyền truy cập chức năng này", Toast.LENGTH_SHORT).show();
        activity.finish();
        return false;
    }

    public static boolean checkAccess(Activity activity, String... allowedRoles) {
        SessionManager session = new SessionManager(activity);
        String userRole = session.getUserRole();
        for (String role : allowedRoles) {
            if (role.equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        Toast.makeText(activity, "Truy cập bị từ chối: Không đủ quyền hạn", Toast.LENGTH_SHORT).show();
        activity.finish();
        return false;
    }
}
