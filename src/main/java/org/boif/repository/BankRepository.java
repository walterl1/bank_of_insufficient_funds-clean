package org.boif.repository;

import org.boif.account.BankAccount;
import org.boif.user.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankRepository {


    public void signupUser(UserAccount userAccount) {

        String query = "INSERT INTO userAccount(userId, accountId, pin, username, email, firstName, lastName, phoneNumber, age) " +
                "Values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try {
            Connection con = DriverManager.getConnection(url, dbName, password);


            PreparedStatement p = con.prepareStatement(query);
            p.setInt(1, userAccount.getUserId());
            p.setLong(2, userAccount.getAccountId());
            p.setString(3, userAccount.getPin());
            p.setString(4, userAccount.getUsername());
            p.setString(5, userAccount.getEmail());
            p.setString(6, userAccount.getFirstName());
            p.setString(7, userAccount.getLastName());
            p.setString(8, userAccount.getPhoneNumber());
            p.setInt(9, userAccount.getAge());


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

    public void registerBankAccount(UserAccount userAccount){

        String query2 = "INSERT INTO bankAccount(accountNumber, firstName, lastName,  accountType, accountBalance) " +
                "Values(?, ?, ?, ?, ?)";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, dbName, password);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setLong(1, userAccount.getBankAccount().getAccountNumber());
            ps.setString(2, userAccount.getBankAccount().getFirstName());
            ps.setString(3, userAccount.getBankAccount().getLastName());
            ps.setString(4, userAccount.getBankAccount().getAccountType());
            ps.setDouble(5, userAccount.getBankAccount().getAccountBalance());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){

                System.out.println("Bank account registered");

            }
            else {

                System.out.println("Unable to register bank account at this time. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public UserAccount loginUser(String uname, String pin){


        String query = "SELECT * FROM userAccount where username = ? AND pin = ?";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String username = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, username, password);


            PreparedStatement  p = con.prepareStatement(query);
            p.setString(1, uname);
            p.setString(2, pin);

            ResultSet rs = p.executeQuery();

            while(rs.next()){

                UserAccount userAccount = new UserAccount();
                userAccount.setUserId(rs.getInt("userId"));
                userAccount.setAccountId(rs.getLong("accountId"));
                userAccount.setPin(rs.getString("pin"));
                userAccount.setUsername(rs.getString("username"));
                userAccount.setEmail(rs.getString("email"));
                userAccount.setFirstName(rs.getString("firstName"));
                userAccount.setLastName(rs.getString("lastName"));
                userAccount.setPhoneNumber(rs.getString("phoneNumber"));
                userAccount.setAge(rs.getInt("age"));

                return userAccount;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    public List<BankAccount> loginUser(UserAccount account){

        List<BankAccount> listOfAccounts = new ArrayList<>();

        String query = "SELECT * FROM bankAccount where firstName = ? AND lastName = ?";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String username = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, username, password);


            PreparedStatement  p = con.prepareStatement(query);
            p.setString(1, account.getFirstName());
            p.setString(2, account.getLastName());

            ResultSet rs = p.executeQuery();

            while(rs.next()){

                BankAccount bankAccount = new BankAccount(rs.getInt("accountBalance"), rs.getLong("accountNumber"));
                bankAccount.setAccountOwner(rs.getString("firstName"), rs.getString("lastName"));
                bankAccount.setFirstName(rs.getString("firstName"));
                bankAccount.setLastName(rs.getString("lastName"));
                bankAccount.setAccountType(rs.getString("accountType"));


                listOfAccounts.add(bankAccount);


            }

            return listOfAccounts;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addNewBankAccount(UserAccount userAccount, String accountType) {

        String query2 = "INSERT INTO bankAccount(accountNumber, firstName, lastName,  accountType, accountBalance) " +
                "Values(?, ?, ?, ?, ?)";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, dbName, password);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setLong(1, userAccount.getBankAccount().getAccountNumber());
            ps.setString(2, userAccount.getFirstName());
            ps.setString(3, userAccount.getLastName());
            ps.setString(4, accountType);
            ps.setDouble(5, userAccount.getBankAccount().getAccountBalance());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){

                System.out.println("New bank account registered");

            }
            else {

                System.out.println("Unable to register bank account at this time. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateAccountBalance(BankAccount bankAccount) {

        String query2 = "UPDATE bankAccount set accountBalance = ? WHERE accountNumber = ?";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, dbName, password);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setDouble(1, bankAccount.getAccountBalance());
            ps.setLong(2, bankAccount.getAccountNumber());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){

                System.out.println("Transaction successful");

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addFunds(BankAccount bankAccount) {

        String query2 = "UPDATE bankAccount set accountBalance = ? WHERE accountNumber = ?";

        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, dbName, password);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setDouble(1, bankAccount.getAccountBalance());
            ps.setLong(2, bankAccount.getAccountNumber());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){

                System.out.println("Transaction successful");

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTransfer(BankAccount sendingBankAccount, BankAccount recipientBankAccount) {

        String query2 = "UPDATE bankAccount set accountBalance = ? WHERE accountNumber = ?";
        String query = "UPDATE bankAccount set accountBalance = ? WHERE accountNumber = ?";


        String url = "jdbc:mysql://localhost:3306/bankDb";
        String dbName = "root";
        String password = "Tarploe1";

        try{
            Connection con = DriverManager.getConnection(url, dbName, password);

            PreparedStatement ps = con.prepareStatement(query2);
            ps.setDouble(1, sendingBankAccount.getAccountBalance());
            ps.setLong(2, sendingBankAccount.getAccountNumber());

            PreparedStatement p = con.prepareStatement(query);
            p.setDouble(1, recipientBankAccount.getAccountBalance());
            p.setLong(2, recipientBankAccount.getAccountNumber());

            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){

                System.out.println("Transaction successful");

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }
            int rowUpdated = ps.executeUpdate();

            if(rowUpdated == 1){

                System.out.println("Transaction successful");

            }
            else {

                System.out.println("Transaction failed. Please try again later.");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
