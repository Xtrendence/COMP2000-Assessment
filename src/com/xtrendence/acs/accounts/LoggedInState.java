package com.xtrendence.acs.accounts;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtrendence.acs.data.Repository;

import java.lang.reflect.Type;
import java.util.Map;

public class LoggedInState implements IAccountState {
    /* Verifies the username and password of the admin, and sets the account state to LoggedIn if they're valid, and LoggedOut if they're not.
    *  @param account An Account object containing a username and password.
    *  @return Nothing.
    */
    public void doAction(Account account) {
        boolean valid = false;

        // Read the content of the accounts.json file.
        String content = Repository.read(Repository.accountsFile);
        if(content != null) {
            try {
                Gson gson = new Gson();

                // Account information is saved as username/password, with the username acting as the key, and the password as the value, both of which are strings.
                Type mapType = new TypeToken<Map<String, String>>() {}.getType();

                // Turns a JSON string into a map that pairs keys with values.
                Map<String, String> map = gson.fromJson(content, mapType);

                // If the username exists, its value (the corresponding password) is fetched. At this point, it would be a bcrypt hash, so it is compared with the password in the Account object to ensure they match. Since bcrypt hashes store the salt and cost with the hash, no additional data is necessary.
                if(map.containsKey(account.getUsername())) {
                    String hashed = map.get(account.getUsername());
                    BCrypt.Result result = BCrypt.verifyer().verify(account.getPassword().toCharArray(), hashed);
                    valid = result.verified;
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        // If the username/password pair are valid, then the state of the account is set to "this", which refers to the LoggedInState. Otherwise, it's set to LoggedOutState.
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
