drop table if EXISTS account;
CREATE TABLE account(

userId INTEGER PRIMARY KEY AUTOINCREMENT,
accountId TEXT,
pin TEXT,
balance REAL,
createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
);

drop table if EXISTS transactions;
CREATE TABLE transactions(

transactionId INTEGER PRIMARY KEY AUTOINCREMENT,
accountId TEXT,
amount REAL,
type TEXT,
createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
);