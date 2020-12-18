package com.xtrendence.acs;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class DataAccess {
    public String accountsFile = System.getProperty("user.dir") + "\\resources\\accounts.json";
    public String stockFile = System.getProperty("user.dir") + "\\resources\\stock.json";

    static boolean writeFile(String filePath, String data) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(data);
            writer.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static String readFile(String filePath) {
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(filePath);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            return builder.toString();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
