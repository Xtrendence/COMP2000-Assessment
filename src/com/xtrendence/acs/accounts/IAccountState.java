package com.xtrendence.acs.accounts;

public interface IAccountState {
    public void doAction(Account account);
    public boolean loggedIn();
}
