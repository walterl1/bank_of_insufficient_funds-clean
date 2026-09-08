package org.boif.user;


import org.boif.account.BankAccount;

import java.util.Random;

public class UserAccount {

    private int userId;
    private Long accountId;
    private String pin;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int age;
    private BankAccount bankAccount;
    private static int numberOfBankAccounts;
    private static int numberOfCustomers;


    public UserAccount() {

        Random random = new Random();
        accountId = random.nextLong(0, 20303030);
        userId = random.nextInt(0, 100000);
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId=" + userId +
                ", accountId=" + accountId +
                ", pin='" + pin + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", age=" + age +
                ", bankAccount=" + bankAccount +
                '}';
    }
}
