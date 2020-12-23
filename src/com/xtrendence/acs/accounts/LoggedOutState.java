package com.xtrendence.acs.accounts;

public class LoggedOutState implements IAccountState {
    public void doAction(Account account) {
        account.setUsername(null);
        account.setPassword(null);
        account.setState(this);
    }

    public boolean loggedIn() {
        return false;
    }
}