package model;

import controller.Account;

import java.util.List;

/**
 * Model class to work with accounts
 * @param <T> The account type we are working on
 */
public abstract class AccountModel <T extends Account>{
    /**
     * Gets all the accounts available of a specific class
     *
     * @return A list of objects representing the account class
     */
    abstract List<T> getAllAccounts();

    /**
     * Updates account in the database
     *
     * @param account Account to be updated
     */
    abstract void updateAccount(T account);

    /**
     * Gets a single account object from a username
     *
     * @param username The username of the desired account
     * @return The account object of the specific username
     */
    abstract T getAccount(String username);
}
