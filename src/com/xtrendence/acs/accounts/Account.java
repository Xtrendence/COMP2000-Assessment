package com.xtrendence.acs.accounts;

public class Account {
    private IAccountState state;
    private String username;
    private String password;

    public Account(String username, String password) {
        state = null;
        this.username = username;
        this.password = password;
        login();
    }

    public void login() {
        LoggedInState login = new LoggedInState();
        login.doAction(this);
    }

    public void logout() {
        LoggedOutState logout = new LoggedOutState();
        logout.doAction(this);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(IAccountState state) {
        this.state = state;
    }

    public IAccountState getState() {
        return this.state;
    }
}
