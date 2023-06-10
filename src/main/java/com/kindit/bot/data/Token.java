package com.kindit.bot.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Token {
    public static final String TOKEN;

    static {
        try {
            TOKEN = getTokenFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getTokenFromFile() throws IOException {
        File file = new File("TOKEN.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            file.createNewFile();
        }
        String token = scanner.nextLine();
        scanner.close();
        return token;
    }
}
