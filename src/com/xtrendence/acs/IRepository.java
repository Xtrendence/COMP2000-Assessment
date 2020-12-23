package com.xtrendence.acs;

public interface IRepository {
    static boolean writeFile(String filePath, String data) {
        return true;
    }
    static String readFile(String filePath) {
        return null;
    }
    static void generateFiles() { }
}
