create database bankdb;

create user 'bank'@'%' identified by 'securepassword';
use bankdb;
grant all on bankdb.* to 'bank'@'%';

create table Users( ID int primary key auto_increment,
					Username varchar(24),
                    FirstName varchar(50),
					LastName varchar(50),
					Password char(128),
                    Salt varchar(20),
                    Email varchar(128),
                    Phone_num char(13),
                    Birth_date date) engine=InnoDB;
                    
create table Accounts ( ID int primary key auto_increment,
						Type varchar(20),
                        Balance double) engine=InnoDB;

create table mappings ( ID int primary key auto_increment,
						UsersID int,
                        AccountsID int,
                        foreign key (UsersID) references Users(ID) on delete cascade,
                        foreign key (AccountsID) references Accounts(ID) on delete cascade) engine=InnoDB;

create table Admins ( 	ID int primary key auto_increment,
						UsersID int,
                        foreign key (UsersID) references Users(ID)) engine=InnoDB;
                        
create table Transactions (	TransactionID int primary key auto_increment,
							SenderID int not null references Users(ID),
                            Amount double not null,
                            Currency varchar(3),
                            ReceiverID int not null references Users(ID)) engine=InnoDB;
                        
create table ExchangeRates (	currency char(3) not null primary key,
								value double not null ) engine=InnoDB;
                                
create index userSearchHelperByUsername on Users(Username, ID, FirstName, LastName, Email, Phone_num, Birth_date);
create index userSearchHelperByID on Users(ID, Username, FirstName, LastName, Email, Phone_num, Birth_date);
create index exchangeRateHelper on ExchangeRates(currency, value);