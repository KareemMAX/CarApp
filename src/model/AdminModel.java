package model;

import controller.Admin;
import controller.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminModel extends AccountModel<Admin>{
    private static AdminModel singletonInstance;
    private final Database db = Database.getInstance();

    private AdminModel() {}

    /**
     * Gets a singleton instance
     *
     * @return A {@link CustomerModel} instance
     */
    public static AdminModel getInstance() {
        if (singletonInstance == null) singletonInstance = new AdminModel();
        return singletonInstance;
    }

    @Override
    List<Admin> getAllAccounts() {
        ArrayList<Admin> result = new ArrayList<Admin>();
        ResultSet table;
        table = db.query("SELECT * FROM [admin] INNER JOIN [account] ON [admin].[username] = [account].[username]");

        try {
            while (table.next()) {
                Admin current = new Admin(
                        table.getString("username"),
                        table.getString("password")
                );
                result.add(current);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("No Drivers");
        }

        return result;
    }

    @Override
    Admin getAccount(String username) {
        ResultSet table = db.query("SELECT * FROM [admin] INNER JOIN [account] ON [admin].[username] = [account].[username] WHERE [admin].[username] = '" + username + "'");
        try {
            if(!table.next()) return null;

            Admin target = new Admin(table.getString("username"),
                    table.getString("password"));
            target.setSuspended(false);

            return target;
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    void updateAccount(Admin account) {
        // Nothing to update
    }
}
