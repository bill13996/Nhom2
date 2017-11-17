create database AphroditeTEST9
go
use AphroditeTEST9
go

create table City(
	CityID int identity(1,1) primary key,
	City nvarchar(100) 	----------Change----------
)

create table Account(
	AccountID int identity(1,1) primary key,
	Username varchar(50),
	Password varchar(50),
	Role int,
	CityID int foreign key references City(CityID)----------Change----------
)

create table Model(
	ModelID int identity(100,1) primary key,----------Change----------
	ModelName nvarchar(150),----------Change----------
	DoB date,----------Change----------
	Female bit,----------Change----------
	Body varchar(30),
	CityID int foreign key references City(CityID), ----------Change----------
	Available bit, ----------Change----------
)

create table Customer(
	CusID int identity(1,1) primary key,
	CusName nvarchar(150),----------Change----------
	Company nvarchar(30),----------Change----------
	Phone varchar(30),
	Email varchar(30),
)

create table Contract(
	ContractID int identity(100,1) primary key,----------Change----------
	ConName nvarchar(200),----------Change----------
	CusID int foreign key references Customer(CusID),
	ConFile nvarchar(200),----------Change----------
	ConAddr nvarchar(200),----------Change----------
	ConSignDate date,----------Change----------
	ConStart date,----------Change----------
	ConEnd date,----------Change----------
)

create table ModelContract(
	ContractID int foreign key references Contract(ContractID) ,
	ModelID int foreign key references Model(ModelID),
	Constraint PK_ModelContract primary key (ModelID, ContractID)
)
----------Change----------
create table ModelImg(
	ImgURL varchar(200),
	ModelID int foreign key references Model(ModelID),
	Constraint PK_ModelImg primary key (ModelID)
)
----------Change----------
create table Skills(
	SkillID int identity(1,1) primary key,
	Skill nvarchar(100),----------Change----------
)

create table ModelSkill(
	ModelID int foreign key references Model(ModelID),
	SkillID int foreign key references Skills(SkillID),
	Constraint PK_ModelSkill primary key (ModelID, SkillID)
)

create table Language(
	LanguageID int identity(1,1) primary key,
	Language nvarchar(100),----------Change----------
) 

create table ModelLanguage(
	ModelID int foreign key references Model(ModelID),
	LanguageID int foreign key references Language(LanguageID),
	Constraint PK_ModelLanguage primary key (ModelID, LanguageID)
)


/*----------Change----------
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
*/