package com.xtrendence.acs;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Account {
    public static String username;
    public static boolean loggedIn = false;

    public static boolean login(String username, String password) {
        boolean valid = false;
        String content = Repository.readFile(Repository.accountsFile);
        if(content != null) {
            try {
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> map = gson.fromJson(content, mapType);
                if(map.containsKey(username)) {
                    String hashed = map.get(username);
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashed);
                    valid = result.verified;
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(valid) {
            Account.username = username;
            Account.loggedIn = true;
        }
        return valid;
    }

    public static void logout() {
        Account.username = "";
        Account.loggedIn = false;
    }
}
