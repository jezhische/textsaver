package com.jezh.textsaver.util;

public class JpaUtils {
    public static String newAssemblyId() {
        StringBuilder ssoBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++)
            ssoBuilder.append(generateRandomChar());
        return ssoBuilder.toString();
    }

    private static char generateRandomChar() {
        char randomChar = (char)((Math.random() * 200 - Math.random() * 100));
        return randomChar >= 65 && randomChar <= 90 || randomChar >= 97 && randomChar <= 122 ?
                randomChar : generateRandomChar();
    }
}
