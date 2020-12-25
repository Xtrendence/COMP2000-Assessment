package com.xtrendence.acs.controller;

// Part of the Repository design pattern. Ensures that classes that implement the IRepository interface have methods used for CRUD functionality.
public interface IRepository {
    static void create() { }
    static String read(String filePath) { return null; }
    static boolean update(String filePath, String data) { return true; }
    static void delete(String filePath) { }
}
