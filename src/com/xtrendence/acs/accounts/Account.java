package com.xtrendence.acs.accounts;

// Account object used to log admins into the system.
public class Account {
    // A State design pattern is used to specify whether the admin is in a logged in state, or logged out.
    private IAccountState state;
    private String username;
    private String password;

    /* The constructor creates an Account object with username and password as class members, and calls the login() method.
    *  @param username The admin's username.
    *  @param password The admin's password.
    */
    public Account(String username, String password) {
        state = null;
        this.username = username;
        this.password = password;
        login();
    }

    /* The LoggedInState object contains a method that actually verifies the username and password. If they match what's saved in the JSON file, the user's state is set to LoggedIn.
    *  @return Nothing.
    */
    public void login() {
        LoggedInState login = new LoggedInState();
        login.doAction(this);
    }

    /* When the user logs out, their Account state is set to LoggedOut.
    *  @return Nothing.
    */
    public void logout() {
        LoggedOutState logout = new LoggedOutState();
        logout.doAction(this);
    }

    /* Returns the admin's username.
    *  @return String Admin's username.
    */
    public String getUsername() {
        return this.username;
    }

    /* Returns the admin's password.
    *  @return String Admin's password.
    */
    public String getPassword() {
        return password;
    }

    /* Sets the admin's username.
    *  @param username Admin's username.
    *  @return Nothing.
    */
    public void setUsername(String username) {
        this.username = username;
    }

    /* Sets the admin's password.
    *  @param password Admin's password.
    *  @return Nothing.
    */
    public void setPassword(String password) {
        this.password = password;
    }

    /* Sets the account state.
    *  @param state The account state.
    *  @return Nothing.
    */
    public void setState(IAccountState state) {
        this.state = state;
    }

    /* Returns the account state.
    *  @return IAccountState The account state.
    */
    public IAccountState getState() {
        return this.state;
    }
}
