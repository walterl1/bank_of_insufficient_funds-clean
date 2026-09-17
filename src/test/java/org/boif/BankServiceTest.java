package org.boif;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class BankServiceTest {

    @Mock 
    BankingRepository bankingRepository;
    BankService bankService;

    @BeforeEach
    public void setUp() {
        bankService = new BankService();
        bankService.bankingRepository = bankingRepository;

    }
    //validateAccountId - tests whether the accountId meets the required criteria
    @Test 
    public void testValidateAccountIdPositive() {
        String error = bankService.validateAccountId("validAccountId");
        assertNull(error);

    }

    @Test 
    public void testValidateAccountIdNegative() {
        String error = bankService.validateAccountId("invalidAccountIdBecauseOfTooManyCharacters");
        assertNotNull(error);
        assertEquals("Account ID must be between 4 and 20 characters.", error);
    }

    //validatePin - tests whether the PIN meets the required criteria
    @Test 
    public void testValidatePinPositive() {
        String error = bankService.validatePin("123456");
        assertNull(error);

    }

    @Test 
    public void testValidatePinNegative() {
        String error = bankService.validatePin("123");
        assertNotNull(error);
        assertEquals("PIN must be exactly 6 digits (0-9).", error);
    }


    //validateAccount - tests whether the account exists and credentials are correct

    @Test 
    public void testValidateAccountPositive() {
        Account expected = new Account("validAccountId", "123456", 100.0);
        when(bankingRepository.loginUser("validAccountId", "123456")).thenReturn(expected); // this mocks the repository to return the expected amount for the given account ID and PIN
        Account actual = bankService.validateAccount("validAccountId", "123456");
        assertNotNull(actual);
        assertEquals("validAccountId", actual.getAccountId());

    }

    @Test 
    public void testValidateAccountNegative() {
        when(bankingRepository.loginUser("invalidAccountId", "123456")).thenReturn(null);
        Account actual = bankService.validateAccount("invalidAccountId", "123456");
        assertNull(actual);


    }

    // credentials validation tests

    @Test 
    public void testCredentialsPositive() {
        String error = bankService.validateCredentials("validAccountId", "123456");
        assertNull(error);
    }

    @Test 
    public void testCredentialsNegative() {
        String error = bankService.validateCredentials("cba", "123");
        assertNotNull(error);
        assertEquals("Account ID must be between 4 and 20 characters.", error);
    }

    // prompt for account ID tests
    @Test 
    public void testPromptForAccountId() {
        System.setIn(new ByteArrayInputStream("exampleAccountId\n".getBytes())); // simulate user input for account ID
        bankService = new BankService();
        bankService.bankingRepository = bankingRepository;

        String result = bankService.promptForAccountId();

       assertEquals("exampleaccountid", result);
    }
    // the test below checks that promptForAccountId reprompts until a valid input is provided
    @Test
    public void promptForAccountIdNegative() {
        System.setIn(new ByteArrayInputStream("ab\nvalidacct\n".getBytes(StandardCharsets.UTF_8)));
        bankService = new BankService();
        bankService.bankingRepository = bankingRepository;

        String result = bankService.promptForAccountId();

        assertEquals("validacct", result);
    }

    
    // promptForPin() tests
    @Test
    public void promptForPinPositive_returnsValidSixDigitInput() {
        System.setIn(new ByteArrayInputStream("654321\n".getBytes(StandardCharsets.UTF_8)));
        bankService = new BankService();
        bankService.bankingRepository = bankingRepository;

        String result = bankService.promptForPin();

        assertEquals("654321", result);
    }
    // this test checks that promptForPin reprompts until a valid input is provided
    @Test
    public void promptForPinNegative() {
        System.setIn(new ByteArrayInputStream("abc\n123456\n".getBytes(StandardCharsets.UTF_8)));
        bankService = new BankService();
        bankService.bankingRepository = bankingRepository;

        String result = bankService.promptForPin();

        assertEquals("123456", result);
    }

   
    // signupUser(accountId, pinNumber) tests

    @Test
    public void signupUserPositive() throws SQLException {
        when(bankingRepository.getAllAccounts()).thenReturn(new ArrayList<>());
        when(bankingRepository.signupUser(any(Account.class))).thenReturn(true);

        String result = bankService.signupUser("brand_new_id", "333444");

        assertEquals("Successfully created account!", result);
        verify(bankingRepository).signupUser(argThat(a ->
                a.getAccountId().equals("brand_new_id")
                        && a.getPin().equals("333444")
                        && a.getBalance() == 0.0));
    }

    @Test
    public void signupUserNegative() throws SQLException {
        Account existing = new Account("already_exisiting_id", "000000", 50.0);
        when(bankingRepository.getAllAccounts()).thenReturn(List.of(existing));

        String result = bankService.signupUser("already_exisiting_id", "123456");

        assertEquals("Username already exists", result);
        verify(bankingRepository, never()).signupUser(any());
    }

    
    // getLoggedInAccount(accountId, pin) tests 
   

    @Test
    public void getLoggedInAccountPositive() {
        Account expected = new Account("account1", "123456", 100.0);
        when(bankingRepository.loginUser("account1", "123456")).thenReturn(expected);

        Account actual = bankService.getLoggedInAccount("account1", "123456");

        assertNotNull(actual);
        assertEquals("account1", actual.getAccountId());
    }

    @Test // the test below tests whether getLoggedInAccount returns null for invalid credentials
    public void getLoggedInAccountNegative() {
        when(bankingRepository.loginUser("account1", "000000")).thenReturn(null);

        Account actual = bankService.getLoggedInAccount("account1", "000000");

        assertNull(actual);
    }

    // getBalance tests

    @Test
    public void getBalancePositive() {
        Account account = new Account("account2", "123456", 250.75);
        assertEquals(250.75, bankService.getBalance(account), 0.001);
    }

    @Test // this test ensure that the balance is correctly returned for an account with zero balance
    public void getBalanceNegative() {
        Account account = new Account("account2", "000000", 0.0);
        assertEquals(0.0, bankService.getBalance(account), 0.001);
    }

    // depositfunds tests

    @Test
    public void depositFundsPositive() {
        Account account = new Account("account3", "123456", 100.0);
        account.setUserId(1);
        when(bankingRepository.depositFunds(account)).thenReturn(account);

        String result = bankService.depositfunds(account, 50.0);

        assertEquals(150.0, account.getBalance(), 0.001);
        assertTrue(result.contains("Transaction successful"));
        verify(bankingRepository).recordTransaction(account, "DEPOSIT", 50.0);// the verify method allows us to check that the specified method was called with the given arguments
    }

    @Test
    // this test ensure that deposits under $1 get rejected
    public void depositFundsNegative() {
        Account account = new Account("account3", "123456", 100.0);

        String result = bankService.depositfunds(account, 0.50);

        assertEquals(100.0, account.getBalance(), 0.001); // unchanged
        assertEquals("Deposit amount has to be at least $1\n", result);
        verify(bankingRepository, never()).depositFunds(any());
    }

    // withdraw tests

    @Test
    public void withdrawPositive() {
        Account account = new Account("account4", "123456", 100.0);
        account.setUserId(1);
        when(bankingRepository.depositFunds(account)).thenReturn(account);

        String result = bankService.withdraw(account, 40.0);

        assertEquals(60.0, account.getBalance(), 0.001);
        assertTrue(result.contains("Transaction successful"));
        verify(bankingRepository).recordTransaction(account, "WITHDRAWAL", 40.0);
    }

    @Test
    public void withdrawNegative() {
        Account account = new Account("account4", "123456", 100.0);

        String result = bankService.withdraw(account, 120.0);

        assertEquals(100.0, account.getBalance(), 0.001); // unchanged
        assertEquals("Insufficient funds\n", result);
        verify(bankingRepository, never()).depositFunds(any());
    }

    // transferBetweenAccounts tests

    @Test
    public void transferPositive() {
        Account sender = new Account("acc1", "123456", 200.0);
        Account receiver = new Account("acc2", "654321", 50.0);
        when(bankingRepository.loginUser("acc2")).thenReturn(receiver);
        when(bankingRepository.makeTransfer(sender, receiver)).thenReturn(true);

        String result = bankService.transferBetweenAccounts(sender, "acc2", 75.0);

        assertEquals(125.0, sender.getBalance(), 0.001);
        assertEquals(125.0, receiver.getBalance(), 0.001);
        assertTrue(result.contains("allow one business day"));
        verify(bankingRepository).recordTransaction(sender, "TRANSFER_OUT", 75.0);
        verify(bankingRepository).recordTransaction(receiver, "TRANSFER_IN", 75.0);
    }

    @Test // the test below ensures that transfers exceeding the senders balance are rejected
    public void transferNegative() {
        Account sender = new Account("acc1", "123456", 50.0);

        String result = bankService.transferBetweenAccounts(sender, "acc2", 100.0);

        assertEquals(50.0, sender.getBalance(), 0.001); // unchanged
        assertEquals("Insufficient funds \n", result);
        verify(bankingRepository, never()).loginUser(anyString());
        verify(bankingRepository, never()).makeTransfer(any(), any());
    }

    @Test 
    public void transferNegative_rejectsZeroAmount() {
        Account sender = new Account("acc1", "123456", 200.0);

        String result = bankService.transferBetweenAccounts(sender, "acc2", 0.0);

        assertEquals(200.0, sender.getBalance(), 0.001);
        assertEquals("Transfer amount has to be greater than $0", result);
        verify(bankingRepository, never()).loginUser(anyString());
    }

    @Test
    public void transferNegative_rejectsTransferToSameAccount() {
        Account sender = new Account("acc1", "123456", 200.0);

        String result = bankService.transferBetweenAccounts(sender, "acc1", 50.0);

        assertEquals(200.0, sender.getBalance(), 0.001);
        assertEquals("Cannot transfer to the same accountId", result);
        verify(bankingRepository, never()).loginUser(anyString());
    }

    @Test
    public void transferNegative_rejectsNonexistentReciever() {
        Account sender = new Account("acc1", "123456", 200.0);
        when(bankingRepository.loginUser("ghostAcc")).thenReturn(null);

        String result = bankService.transferBetweenAccounts(sender, "ghostAcc", 50.0);

        assertEquals(200.0, sender.getBalance(), 0.001);
        assertEquals("Account with accountId ghostAcc does not exist", result);
        verify(bankingRepository, never()).makeTransfer(any(), any());
    }

    @Test
    public void transferNegative_rollsBackLocalBalancesWhenRepositoryTransferFails() {
        Account sender = new Account("acc1", "123456", 200.0);
        Account receiver = new Account("acc2", "654321", 50.0);
        when(bankingRepository.loginUser("acc2")).thenReturn(receiver);
        when(bankingRepository.makeTransfer(sender, receiver)).thenReturn(false);

        String result = bankService.transferBetweenAccounts(sender, "acc2", 75.0);

        assertEquals(200.0, sender.getBalance(), 0.001, "Sender's balance should be restored on failure");
        assertEquals("Failed to transfer balance. Please contact a system administrator.", result);
        verify(bankingRepository, never()).recordTransaction(any(), anyString(), anyDouble());
    }

    
    // getTransactionHistory tests

    @Test
    public void getTransactionHistoryPositive() {
        Account account = new Account("acc1", "123456", 100.0);
        List<Transaction> transactions = List.of(
                new Transaction("DEPOSIT", 50.0, new Timestamp(System.currentTimeMillis())),
                new Transaction("WITHDRAWAL", 20.0, new Timestamp(System.currentTimeMillis())),
                new Transaction("TRANSFER", 30.0, new Timestamp(System.currentTimeMillis())));
        when(bankingRepository.getTransactions(account)).thenReturn(transactions);

        String result = bankService.getTransactionHistory(account);

        assertTrue(result.startsWith("1: "));
        assertTrue(result.contains("2: "));
        assertTrue(result.contains("3: "));
        assertTrue(result.contains("DEPOSIT"));
        assertTrue(result.contains("WITHDRAWAL"));
        assertTrue(result.contains("TRANSFER"));
    }

    @Test // this test is able to return a message when no transactions are found
    public void getTransactionHistoryNegative() {
        Account account = new Account("acc1", "123456", 100.0);
        when(bankingRepository.getTransactions(account)).thenReturn(new ArrayList<>());

        String result = bankService.getTransactionHistory(account);

        assertEquals("No transactions found.\n", result);
    }
}


    

