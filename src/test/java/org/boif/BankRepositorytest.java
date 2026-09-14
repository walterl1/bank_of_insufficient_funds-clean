package org.boif;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;



public class BankRepositorytest {

    private static final String URL = "jdbc:sqlite:BankAccountsTest.db";
    private BankingRepository bankRepository;


    


    @BeforeAll // this method sets up the database schema before any tests are run
    public static void globalSetup() throws SQLException{
        new java.io.File("BankAccountsTest.db").delete();
        try (Connection connection = DriverManager.getConnection(URL); // establish a connection to the database
             Statement statement = connection.createStatement()) { // create a statement to execute SQL commands
                statement.execute("CREATE TABLE IF NOT EXISTS account (" +
                                    "userid INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "accountId TEXT NOT NULL," +
                                    "pin TEXT NOT NULL," +
                                    "balance REAL NOT NULL" +
                                ")"
                );

                statement.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "accountId TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"
            );

        }
    }

    @AfterAll
    public static void globalTeardown() {
        new java.io.File("BankAccountsTest.db").delete();
    }

    @BeforeEach
    public void setup() throws SQLException { // this method sets up the database state before each test is run
        bankRepository = new BankingRepository(URL);
        try (Connection connection = DriverManager.getConnection(URL); //estabilish a connect with the database
             Statement statement = connection.createStatement()) { // create a statement to execute SQL commands
            statement.execute("DELETE FROM account");
            statement.execute("DELETE FROM transactions"); //delete all records from the Transaction table before each test
        }

    }
    // the method below is supposed to insert a new account into the database
    private void insertAccount(String accountId, String pin, double balance) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)){
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO account (accountId, pin, balance) VALUES(?, ?, ?)");
            preparedStatement.setString(1, accountId);
            preparedStatement.setString(2, pin);
            preparedStatement.setDouble(3, balance);
            preparedStatement.executeUpdate();
        }


    }

    private int fetchUserId(String accountId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)){
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT userid FROM account WHERE accountId = ? ");
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            assertTrue (resultSet.next(), "Expected a result for account ID: " + accountId);
            return resultSet.getInt("userid");
        }
    }

    // Sign up user tests 
    @Test 
    public void signupUserPositive() throws SQLException {
        Account account = new Account("testAccountId", "234567", 100.0);
        boolean result = bankRepository.signupUser(account);
        assertTrue(result); 
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM account WHERE accountId = ?"
            );
            preparedStatement.setString(1, "testAccountId");
            ResultSet resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("testAccountId", resultSet.getString("accountId"));
            assertEquals("234567", resultSet.getString("pin"));
            assertEquals(100.0, resultSet.getDouble("balance"));
        }

    }

    @Test
    public void signupUserNegative() throws SQLException {
        insertAccount("dupAccountId", "234567", 100.0);
        Account duplicateAccount = new Account("dupAccountId", "234567", 100.0);
        boolean result = bankRepository.signupUser(duplicateAccount);
        assertFalse(result);
        
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM account WHERE accountId = ?"
            );
            preparedStatement.setString(1, "dupAccountId");
            ResultSet resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("dupAccountId", resultSet.getString("accountId"));
            assertEquals("234567", resultSet.getString("pin"));
            assertEquals(100.0, resultSet.getDouble("balance"));
            assertFalse(resultSet.next(), "Expected only one result for account ID: dupAccountId");

        }
    }


    // login user tests  
    @Test
    public void loginUserPositive() throws SQLException {
        insertAccount("testAccountId2", "567234", 100.0);
        Account result = bankRepository.loginUser("testAccountId2", "567234");
        assertNotNull(result, "Expected a non-null result for account ID: testAccountId2");
        assertEquals("testAccountId2", result.getAccountId(), "Expected account ID to match");
        assertEquals("567234", result.getPin(), "Expected PIN to match");
        assertEquals(100.0, result.getBalance(), "Expected balance to match");
    }

    @Test 
    public void loginUserNegative() throws SQLException {
        insertAccount("testAccountId2", "567234", 100.0);
        Account result = bankRepository.loginUser("testAccountId2", "998989");
        assertNull(result, "Expected null result for incorrect PIN");

    }

    @Test
    public void loginUserPositiveAccountExists() throws SQLException {
        insertAccount("testAccountId2", "567234", 100.0);
        Account result = bankRepository.loginUser("testAccountId2", "567234");
        assertNotNull(result, "Expected a non-null result for account ID: testAccountId2");
        assertEquals("testAccountId2", result.getAccountId(), "Expected account ID to match");
        assertEquals("567234", result.getPin(), "Expected PIN to match");
        assertEquals(100.0, result.getBalance(), "Expected balance to match");
    }

    @Test 
    public void loginUserNegativeAccountDoesNotExist() throws SQLException {
        Account result = bankRepository.loginUser("nonExistentAccountId", "567234");
        assertNull(result, "Expected null result for non-existent account");
    }

    // deposit funds tests

    @Test
    public void depositfundsPositive() throws SQLException {
        insertAccount("testAccountId3", "567890", 100.0);
        int userId = fetchUserId("testAccountId3");
        Account account = new Account("testAccountId3", "567890", 150.0);
        account.setUserId(userId);

        bankRepository.depositFunds(account);
        Account updatedAccount = bankRepository.loginUser("testAccountId3", "567890");
        assertEquals(150.0, updatedAccount.getBalance(), "Expected balance to be updated after deposit");
    }

    // the test below checks deposit attempt on a non-existent (ghost) account
    @Test
    public void depositfundsNegative() throws SQLException {
    Account account = new Account("ghostAccountId", "123456", 100.0);
    account.setUserId(999999);

    Account result = bankRepository.depositFunds(account);
    assertNotNull(result, "Expected a non-null result for deposit attempt on ghost account");
    assertEquals(100.0, result.getBalance(), "Expected balance to remain unchanged for ghost account");
    assertNull(bankRepository.loginUser("ghostAccountId"), "Expected ghost account to not actually exist");
    }

    // make transfer tests

    @Test
    public void transferFundsPositive() throws SQLException {
        insertAccount("senderAccountId", "111111", 200.0);
        insertAccount("receiverAccountId", "222222", 50.0);

        Account senderAccount = new Account("senderAccountId", "111111", 150.0);
        Account receiverAccount = new Account("receiverAccountId", "222222", 100.0);

        bankRepository.makeTransfer(senderAccount, receiverAccount);
        assertEquals(150.0, bankRepository.loginUser("senderAccountId", "111111").getBalance(), "Expected sender's balance to be updated after transfer");
        assertEquals(100.0, bankRepository.loginUser("receiverAccountId", "222222").getBalance(), "Expected receiver's balance to be updated after transfer");
    }

    @Test
    public void transferFundsNegative() throws SQLException {
        insertAccount("senderAccountId", "111111", 200);

        Account senderAccount = new Account("senderAccountId", "111111", 150);
        Account ghostAccount = new Account("ghostAccountId", "222222", 100);

        bankRepository.makeTransfer(senderAccount, ghostAccount);
        assertEquals(200, bankRepository.loginUser("senderAccountId", "111111").getBalance(), "Expected sender's balance to remain unchanged after failed transfer");
        assertNull(bankRepository.loginUser("ghostAccountId", "222222"), "Expected ghost account to not exist");
    }

    // recordTransaction tests
     @Test
    public void recordTransactionPositive() throws SQLException {
        Account account = new Account("txAccountId", "111222", 100.0);

        bankRepository.recordTransaction(account, "DEPOSIT", 50.0);

        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE accountId = ?");
            ps.setString(1, "txAccountId");
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            assertEquals("DEPOSIT", rs.getString("type"));
            assertEquals(50.0, rs.getDouble("amount"));
        }
    }

    @Test
    public void recordTransactionNegative() throws SQLException {
        Account neverSignedUp = new Account("neverSignedUpId", "000000", 0.0);

        bankRepository.recordTransaction(neverSignedUp, "DEPOSIT", 25.0);

        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(*) AS cnt FROM transactions WHERE accountId = ?");
            ps.setString(1, "neverSignedUpId");
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt("cnt"));
        }
    }

    // ----- getTransactions tests -----

    @Test
    public void getTransactionsPositive() throws SQLException {
        Account account = new Account("historyAccountId", "333444", 100.0);
        bankRepository.recordTransaction(account, "DEPOSIT", 50.0);
        bankRepository.recordTransaction(account, "WITHDRAWAL", 20.0);

        List<Transaction> transactions = bankRepository.getTransactions(account);

        assertEquals(2, transactions.size(), "Expected both recorded transactions to come back");
    }

    @Test
    public void getTransactionsNegative() {
        Account accountWithNoHistory = new Account("noHistoryAccountId", "555666", 0.0);

        List<Transaction> transactions = bankRepository.getTransactions(accountWithNoHistory);

        assertNotNull(transactions, "Expected an empty list, not null, when there's no history");
        assertTrue(transactions.isEmpty());
    }


}







    

    
