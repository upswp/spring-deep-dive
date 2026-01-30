package com.example.withaop.util;

public class SecurityContext {
    private static boolean isAdmin = true; // 테스트용으로 기본값 true

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
