package com.xtrendence.acs.controller;

import com.xtrendence.acs.model.Account;

public class LoggedOutState implements IAccountState {
    /* Logs the admin out of their account.
    *  @param account An Account object.
    *  @return Nothing.
    */
    public void doAction(Account account) {
        account.setUsername(null);
        account.setPassword(null);
        account.setState(this);
    }

    public boolean loggedIn() {
        return false;
    }
}