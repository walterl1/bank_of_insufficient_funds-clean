package org.boif;


import java.sql.*;

public class BankingRepository {


    public void signupUser(Account signupAccount) {


            String query = "INSERT INTO account(accountId, pin, balance) " +
                    "Values(?, ?, ?)";

            String url = "jdbc:sqlite:BankAccounts.db";

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

        String url = "jdbc:sqlite:BankAccounts.db";


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

    public Account depositFunds(Account account) {

            String query2 = "UPDATE account set balance = ? WHERE userId = ?";

            String url = "jdbc:sqlite:BankAccounts.db";


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
}

