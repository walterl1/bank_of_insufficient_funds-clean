package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class BankingRepository {
    private final Logger logger = LoggerFactory.getLogger(BankService.class);
    private static final String url = "jdbc:sqlite:BankAccounts.db";

    public void signupUser(Account signupAccount) {


            String query = "INSERT INTO account(accountId, pin, balance) " +
                    "Values(?, ?, ?)";

            try (Connection con = DriverManager.getConnection(url)) {


                PreparedStatement p = con.prepareStatement(query);
                p.setString(1, signupAccount.getAccountId());
                p.setString(2, signupAccount.getPin());
                p.setDouble(3, signupAccount.getBalance());



                int rowsUdated = p.executeUpdate();

                if (rowsUdated == 1) {

                    System.out.println("Signup Successful");

                } else {

                    System.out.println("Signup attempt Unsuccessful. Please Try Again");

                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    public Account loginUser(String accountId, String pin){


        String query = "SELECT * FROM account where accountId = ? AND pin = ?";


        try(Connection con = DriverManager.getConnection(url)) {


            PreparedStatement  p = con.prepareStatement(query);
            p.setString(1, accountId);
            p.setString(2, pin);

            ResultSet rs = p.executeQuery();

            while(rs.next()){

                Account account = new Account(rs.getString("accountId"), rs.getString("pin"), rs.getDouble("balance"));


                account.setUserId(rs.getInt("userId"));


                return account;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    public Account loginUser(String accountId){


        String query = "SELECT * FROM account where accountId = ?";


        try(Connection con = DriverManager.getConnection(url)) {


            PreparedStatement  p = con.prepareStatement(query);
            p.setString(1, accountId);

            ResultSet rs = p.executeQuery();

            while(rs.next()){

                Account account = new Account(rs.getString("accountId"), rs.getString("pin"), rs.getDouble("balance"));


                account.setUserId(rs.getInt("userId"));


                return account;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    public Account depositFunds(Account account) {

            String query2 = "UPDATE account set balance = ? WHERE userId = ?";


            try (Connection con = DriverManager.getConnection(url)){

                PreparedStatement ps = con.prepareStatement(query2);
                ps.setDouble(1, account.getBalance());
                ps.setLong(2, account.getUserId());

                int rowsUpdated = ps.executeUpdate();

                if(rowsUpdated == 1){

                    System.out.println("Transaction successful");
                    return account;
                }
                else {

                    System.out.println("Transaction failed. Please try again later.");
                    return account;
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }

    public void makeTransfer(Account account1, Account account2) {
        String query2 = "UPDATE account set balance = ? WHERE accountId = ?";
        String query = "UPDATE account set balance = ? WHERE accountId = ?";




        try{
            Connection con = DriverManager.getConnection(url);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setDouble(1, account1.getBalance());
            ps.setString(2, account1.getAccountId());

            PreparedStatement p = con.prepareStatement(query);
            p.setDouble(1, account2.getBalance());
            p.setString(2, account2.getAccountId());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated != 0){

                System.out.println("Transfer for account ID: " + account1.getAccountId() + " complete. New Balance: $" + account1.getBalance());

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }
            int rowUpdated = p.executeUpdate();

            if(rowUpdated != 0){

                System.out.println("Transfer for account ID: " + account2.getAccountId() + " complete. New Balance: $" + account2.getBalance());

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void recordTransaction(Account account, String type, double amount) {
        String query = "INSERT INTO transactions (accountId, type, amount) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getAccountId());
            ps.setString(2, type);
            ps.setDouble(3, amount);

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException thrown while recording transaction: " + e.getMessage());
        }
    }
}

