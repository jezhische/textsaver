package com.jezh.textsaver.jpaTestUtils;

public class JpaTestUtils {
    public static String newTextPartBody() {
        StringBuilder textBodyBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++)
            textBodyBuilder.append(generateRandomChar());
        return textBodyBuilder.toString();
    }

    private static char generateRandomChar() {
        char randomChar = (char)((Math.random() * 200 - Math.random() * 100));
        return randomChar >= 65 && randomChar <= 90 || randomChar >= 97 && randomChar <= 122 ?
                randomChar : generateRandomChar();
    }
}
