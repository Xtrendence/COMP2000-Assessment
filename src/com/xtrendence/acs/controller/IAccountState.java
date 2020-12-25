package com.xtrendence.acs.controller;

import com.xtrendence.acs.model.Account;

// Account state interface used as part of the State design pattern.
public interface IAccountState {
    // Each state has a different action. LoggedInState tries to log the user in, whereas LoggedOutState logs them out.
    public void doAction(Account account);
    public boolean loggedIn();
}
