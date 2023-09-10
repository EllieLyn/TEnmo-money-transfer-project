BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;
DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

-- Sequence to start transfer_id values at 3001 instead of 1
CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	account_transfer_from int NOT NULL,
	account_transfer_to int NOT NULL,
	transfer_type varchar(10) NOT NULL,
	transfer_status varchar(10) NOT NULL,
	amount decimal(11, 2) NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfer_from_user FOREIGN KEY (account_transfer_from) REFERENCES account (account_id),
	CONSTRAINT FK_transfer_to_user FOREIGN KEY (account_transfer_to) REFERENCES account (account_id),
	CONSTRAINT CHK_type CHECK (transfer_type in ('request', 'send')),
	CONSTRAINT CHK_status CHECK (transfer_status in ('pending', 'approved', 'rejected')),
	CONSTRAINT CHK_accounts CHECK ((account_transfer_from <> account_transfer_to)),
	CONSTRAINT CHK_amount CHECK (amount>0)
);

 INSERT INTO tenmo_user (username, password_hash)
 VALUES ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
        ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy'),
        ('testUser1', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkresfsddNLKEuE57JAEy'),
        ('testUser2', '$2a$10$Ud8gSvRSresMijNgxXWzcexeXlVs4kWDOkresfsddNLKEuE57JAEy'),
        ('testUser3', '$2a$10$Ud8geSfc4G1MijNgxXWzcexeXlVs4kWDOkresfsddNLKEuE57JAEy');

 INSERT INTO account (user_id, balance)
 VALUES (1001, 1000),
        (1002, 2000),
        (1003, 1000),
        (1004, 2000);

 INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)
 VALUES (2001, 2002, 'request', 'pending', 200.00);
 INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)
 VALUES (2001, 2002, 'send', 'approved', 100.00);
 INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)
 VALUES (2002, 2001, 'request', 'rejected', 60.00);
 INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)
 VALUES (2002, 2001, 'request', 'rejected', 300.00);
 INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)
 VALUES (2003, 2004, 'request', 'rejected', 200.00);

COMMIT;