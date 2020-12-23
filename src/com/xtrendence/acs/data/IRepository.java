package com.xtrendence.acs.data;

public interface IRepository {
    static void create() { }
    static String read(String filePath) { return null; }
    static boolean update(String filePath, String data) { return true; }
    static void delete(String filePath) { }
}
