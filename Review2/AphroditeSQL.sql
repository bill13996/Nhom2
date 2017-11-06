create database AphroditeSQL

use AphroditeSQL
go

create table Account(
	AccountID int identity(1,1) primary key,
	Username varchar(30),
	Password varchar(30),
	Role int,
	City varchar(30),
)

create table Model(
	ModelID int identity(1,100) primary key,
	ModelName varchar(30),
	Age int,
	City varchar(30),
	Body varchar(30),
	Availability bit,
)

create table Customer(
	CusID int identity(1,1) primary key,
	CusName varchar(30),
	Company varchar(30),
	Phone varchar(30),
	Email varchar(30),
)

create table Contract(
	ContractID int identity(1,100) primary key,
	CusID int foreign key references Customer(CusID),
	CusFile nvarchar(50),
	ContractDate date,
	ConStart date,
	ConEnd date,
)

create table ModelContract(
	ContractID int foreign key references Contract(ContractID) ,
	ModelID int foreign key references Model(ModelID),
	Constraint PK_ModelContract primary key (ModelID, ContractID)
)

create table Skills(
	SkillID int identity(1,1) primary key,
	Skill varchar(30),
)

create table ModelSkill(
	ModelID int foreign key references Model(ModelID),
	SkillID int foreign key references Skills(SkillID),
	Constraint PK_ModelSkill primary key (ModelID, SkillID)
)

create table Language(
	LanguageID int identity(1,1) primary key,
	Language varchar(30),
) 

create table ModelLanguage(
	ModelID int foreign key references Model(ModelID),
	LanguageID int foreign key references Language(LanguageID),
	Constraint PK_ModelLanguage primary key (ModelID, LanguageID)
)

create table City(
	CityID int identity(1,1) primary key,
	City varchar(30) 	
)

create table ModelCity(
	ModelID int foreign key references Model(ModelID),
	CityID int foreign key references City(CityID),
	Constraint PK_ModelCity primary key (ModelID, CityID)
)

create table AccountCity(
	AccountID int foreign key references Account(AccountID),
	CityID int foreign key references City(CityID),
	Constraint PK_AccountCity primary key (AccountID, CityId)
)