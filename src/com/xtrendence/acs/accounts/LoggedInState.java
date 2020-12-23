package com.xtrendence.acs.accounts;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtrendence.acs.Repository;

import java.lang.reflect.Type;
import java.util.Map;

public class LoggedInState implements IAccountState {
    public void doAction(Account account) {
        boolean valid = false;
        String content = Repository.read(Repository.accountsFile);
        if(content != null) {
            try {
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> map = gson.fromJson(content, mapType);
                if(map.containsKey(account.getUsername())) {
                    String hashed = map.get(account.getUsername());
                    BCrypt.Result result = BCrypt.verifyer().verify(account.getPassword().toCharArray(), hashed);
                    valid = result.verified;
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(valid) {
            account.setState(this);
        } else {
            account.setState(new LoggedOutState());
        }
    }

    public boolean loggedIn() {
        return true;
    }
}
