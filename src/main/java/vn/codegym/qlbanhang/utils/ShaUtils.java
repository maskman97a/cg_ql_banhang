package vn.codegym.qlbanhang.utils;

public class ShaUtils {
    public static String encryptSHA1(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
