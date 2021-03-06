USE master
GO
CREATE DATABASE Aphrodite
GO
USE Aphrodite
GO
/****** Object:  Table [dbo].[Account]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Account](
	[AccountID] [int] IDENTITY(1,1) NOT NULL,
	[Username] [varchar](50) NULL,
	[Password] [varchar](50) NULL,
	[Role] [int] NULL,
	[CityID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[AccountID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[City]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[City](
	[CityID] [int] IDENTITY(1,1) NOT NULL,
	[City] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[CityID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Contract]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Contract](
	[ContractID] [int] IDENTITY(100,1) NOT NULL,
	[ConName] [nvarchar](200) NULL,
	[CusID] [int] NULL,
	[ConFile] [nvarchar](200) NULL,
	[ConAddr] [nvarchar](200) NULL,
	[ConSignDate] [date] NULL,
	[ConStart] [date] NULL,
	[ConEnd] [date] NULL,
PRIMARY KEY CLUSTERED 
(
	[ContractID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Customer]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Customer](
	[CusID] [int] IDENTITY(1,1) NOT NULL,
	[CusName] [nvarchar](150) NULL,
	[Company] [nvarchar](30) NULL,
	[Phone] [varchar](30) NULL,
	[Email] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[CusID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Language]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Language](
	[LanguageID] [int] IDENTITY(1,1) NOT NULL,
	[Language] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[LanguageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Model]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Model](
	[ModelID] [int] IDENTITY(100,1) NOT NULL,
	[ModelName] [nvarchar](150) NULL,
	[DoB] [date] NULL,
	[Female] [bit] NULL,
	[Body] [varchar](30) NULL,
	[CityID] [int] NULL,
	[Available] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[ModelID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ModelContract]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ModelContract](
	[ContractID] [int] NOT NULL,
	[ModelID] [int] NOT NULL,
 CONSTRAINT [PK_ModelContract] PRIMARY KEY CLUSTERED 
(
	[ModelID] ASC,
	[ContractID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[ModelImg]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ModelImg](
	[ImgURL] [varchar](200) NULL,
	[ModelID] [int] NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ModelLanguage]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ModelLanguage](
	[ModelID] [int] NOT NULL,
	[LanguageID] [int] NOT NULL,
 CONSTRAINT [PK_ModelLanguage] PRIMARY KEY CLUSTERED 
(
	[ModelID] ASC,
	[LanguageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[ModelSkill]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ModelSkill](
	[ModelID] [int] NOT NULL,
	[SkillID] [int] NOT NULL,
 CONSTRAINT [PK_ModelSkill] PRIMARY KEY CLUSTERED 
(
	[ModelID] ASC,
	[SkillID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[RemPass]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RemPass](
	[RemStatus] [bit] NULL,
	[AccountID] [int] NULL
) ON [PRIMARY]

GO

INSERT INTO RemPass VALUES (0,1)

/****** Object:  Table [dbo].[Skills]    Script Date: 04/12/2017 14:29:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Skills](
	[SkillID] [int] IDENTITY(1,1) NOT NULL,
	[Skill] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[SkillID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET IDENTITY_INSERT [dbo].[Account] ON 

INSERT [dbo].[Account] ([AccountID], [Username], [Password], [Role], [CityID]) VALUES (1, N'admin1', N'123456', 1, 1)
INSERT [dbo].[Account] ([AccountID], [Username], [Password], [Role], [CityID]) VALUES (2, N'truonganh', N'123456', 2, 2)
INSERT [dbo].[Account] ([AccountID], [Username], [Password], [Role], [CityID]) VALUES (3, N'thanhdien', N'123456', 2, 1)
INSERT [dbo].[Account] ([AccountID], [Username], [Password], [Role], [CityID]) VALUES (4, N'khhung', N'123456', 2, 3)
SET IDENTITY_INSERT [dbo].[Account] OFF
SET IDENTITY_INSERT [dbo].[City] ON 

INSERT [dbo].[City] ([CityID], [City]) VALUES (1, N'Ho Chi Minh')
INSERT [dbo].[City] ([CityID], [City]) VALUES (2, N'Ha Noi')
INSERT [dbo].[City] ([CityID], [City]) VALUES (3, N'Da Nang')
SET IDENTITY_INSERT [dbo].[City] OFF
SET IDENTITY_INSERT [dbo].[Contract] ON 

INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (100, N'Ford Showroom', 2, N'', N'District 7', CAST(0x9A3D0B00 AS Date), CAST(0x9C3D0B00 AS Date), CAST(0x9E3D0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (101, N'Hoi thao Ho Guom', 3, N'', N'H? Hoàn Ki?m, HN', CAST(0x9A3D0B00 AS Date), CAST(0x9A3D0B00 AS Date), CAST(0x9F3D0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (103, N'NEXT ASIA', 3, N'', N'C?u Hàm R?ng, Ðà N?ng', CAST(0x963D0B00 AS Date), CAST(0xD23D0B00 AS Date), CAST(0xDE3D0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (104, N'Nuclear Show New Year', 3, N'', N'qu?n 1, HCMC', CAST(0x803D0B00 AS Date), CAST(0xDB3D0B00 AS Date), CAST(0xEF3D0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (105, N'Aptech Gaming Event', NULL, N'', N'FPT Aptech HCMC', CAST(0x953D0B00 AS Date), CAST(0xA03D0B00 AS Date), CAST(0xA53D0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (106, N'Hearthstone International', NULL, N'', N'Phu Tho stadium, HCMC', CAST(0x5E3D0B00 AS Date), CAST(0x373E0B00 AS Date), CAST(0x3E3E0B00 AS Date))
INSERT [dbo].[Contract] ([ContractID], [ConName], [CusID], [ConFile], [ConAddr], [ConSignDate], [ConStart], [ConEnd]) VALUES (107, N'Heiniken Party', 3, N'', N'Ho Guom, Ha Noi', CAST(0x993D0B00 AS Date), CAST(0xF53D0B00 AS Date), CAST(0xF83D0B00 AS Date))
SET IDENTITY_INSERT [dbo].[Contract] OFF
SET IDENTITY_INSERT [dbo].[Customer] ON 

INSERT [dbo].[Customer] ([CusID], [CusName], [Company], [Phone], [Email]) VALUES (1, N'Kenny Hùng', N'Brazzer', N'0909123498', N'tranhdog@gmail.com')
INSERT [dbo].[Customer] ([CusID], [CusName], [Company], [Phone], [Email]) VALUES (2, N'David Hitler', N'Fordded', N'888234', N'dhitler@hotmail.com')
INSERT [dbo].[Customer] ([CusID], [CusName], [Company], [Phone], [Email]) VALUES (3, N'Võ Lê', N'ST', N'0908939923', N'levo69xx@yahoo.com.vn')
INSERT [dbo].[Customer] ([CusID], [CusName], [Company], [Phone], [Email]) VALUES (4, N'Trường Anh', N'macFake', N'01924998711', N'tataaa@gmail.com')
INSERT [dbo].[Customer] ([CusID], [CusName], [Company], [Phone], [Email]) VALUES (5, N'Vũ Trường Điền', N'NuclearWarhead', N'0905223344', N'dienvu@nuc.cc')
SET IDENTITY_INSERT [dbo].[Customer] OFF
SET IDENTITY_INSERT [dbo].[Language] ON 

INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (1, N'English')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (2, N'Japanese')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (3, N'Chinese')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (4, N'Thai')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (5, N'French')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (6, N'Korean')
INSERT [dbo].[Language] ([LanguageID], [Language]) VALUES (7, N'German')
SET IDENTITY_INSERT [dbo].[Language] OFF
SET IDENTITY_INSERT [dbo].[Model] ON 

INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (100, N'Kim Mai Bình', CAST(0x11210B00 AS Date), 1, N'b90w62h88', 1, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (101, N'Lê Hân', CAST(0x7C180B00 AS Date), 1, N'b84w66h91', 2, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (102, N'Phan Thùy Linh', CAST(0x251F0B00 AS Date), 1, N'b80w59h79', 3, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (103, N'Thái Trinh', CAST(0x6B1C0B00 AS Date), 1, N'b80w71h90', 2, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (104, N'Lê Minh Châu', CAST(0x2C210B00 AS Date), 1, N'b89w72h82', 1, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (105, N'Yuki Hana', CAST(0xA5220B00 AS Date), 1, N'b82w57h87', 1, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (106, N'Mao Zedong', CAST(0x20160B00 AS Date), 1, N'b81w60h85', 3, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (107, N'Thi Ngân', CAST(0xE8230B00 AS Date), 1, N'b79w58h80', 1, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (108, N'Trà Kim Minh', CAST(0x5E210B00 AS Date), 1, N'b85w60h88', 2, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (109, N'Jay Park', CAST(0xCC1A0B00 AS Date), 0, N'b97w80h101', 1, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (110, N'Linh Lê', CAST(0x681D0B00 AS Date), 1, N'b90w66h92', 3, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (111, N'Quang Bình Minh', CAST(0xB41F0B00 AS Date), 0, N'b98w76h108', 2, 1)
INSERT [dbo].[Model] ([ModelID], [ModelName], [DoB], [Female], [Body], [CityID], [Available]) VALUES (112, N'Thái Kiên', CAST(0x061D0B00 AS Date), 0, N'b90w76h99', 3, 1)
SET IDENTITY_INSERT [dbo].[Model] OFF
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (100, 100)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (105, 100)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (107, 101)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (103, 102)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (101, 103)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (107, 103)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (100, 104)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (105, 104)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (106, 104)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (104, 105)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (105, 105)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (103, 106)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (104, 107)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (105, 107)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (106, 107)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (101, 108)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (107, 108)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (104, 109)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (105, 109)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (106, 109)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (103, 110)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (101, 111)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (107, 111)
INSERT [dbo].[ModelContract] ([ContractID], [ModelID]) VALUES (103, 112)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/100_0.jpg', 100)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/100_1.jpg', 100)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/101_0.jpg', 101)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/102_0.jpg', 102)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/102_1.jpg', 102)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/102_2.jpg', 102)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/103_0.jpg', 103)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/103_1.jpg', 103)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/104_0.jpg', 104)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/104_1.jpg', 104)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/104_2.jpg', 104)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/105_0.jpg', 105)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/105_1.jpg', 105)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/106_0.jpg', 106)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/107_0.jpg', 107)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/108_0.jpg', 108)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/108_1.jpg', 108)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/109_0.jpg', 109)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/109_1.jpg', 109)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/110_0.jpg', 110)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/110_1.jpg', 110)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/111_0.jpg', 111)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/111_1.jpg', 111)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/112_0.jpg', 112)
INSERT [dbo].[ModelImg] ([ImgURL], [ModelID]) VALUES (N'/mainpanel/images/112_1.jpg', 112)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (100, 5)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (101, 3)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (102, 1)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (104, 1)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (104, 2)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (104, 3)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (105, 2)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (105, 6)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (106, 3)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (106, 7)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (107, 6)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (109, 6)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (110, 1)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (110, 2)
INSERT [dbo].[ModelLanguage] ([ModelID], [LanguageID]) VALUES (112, 1)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (100, 1)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (100, 2)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (101, 3)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (101, 4)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (102, 4)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (103, 2)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (103, 6)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (104, 4)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (104, 5)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (105, 1)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (106, 7)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (107, 4)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (108, 1)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (108, 6)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (109, 3)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (111, 8)
INSERT [dbo].[ModelSkill] ([ModelID], [SkillID]) VALUES (112, 8)
SET IDENTITY_INSERT [dbo].[Skills] ON 

INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (1, N'P.G')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (2, N'MC')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (3, N'Photographic Modeling')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (4, N'Singing')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (5, N'Folk Dancing')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (6, N'Catwalk')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (7, N'Coding')
INSERT [dbo].[Skills] ([SkillID], [Skill]) VALUES (8, N'Body training')
SET IDENTITY_INSERT [dbo].[Skills] OFF
ALTER TABLE [dbo].[Account]  WITH CHECK ADD FOREIGN KEY([CityID])
REFERENCES [dbo].[City] ([CityID])
GO
ALTER TABLE [dbo].[Contract]  WITH CHECK ADD FOREIGN KEY([CusID])
REFERENCES [dbo].[Customer] ([CusID])
GO
ALTER TABLE [dbo].[Contract]  WITH CHECK ADD FOREIGN KEY([CusID])
REFERENCES [dbo].[Customer] ([CusID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Model]  WITH CHECK ADD FOREIGN KEY([CityID])
REFERENCES [dbo].[City] ([CityID])
GO
ALTER TABLE [dbo].[ModelContract]  WITH CHECK ADD FOREIGN KEY([ContractID])
REFERENCES [dbo].[Contract] ([ContractID])
GO
ALTER TABLE [dbo].[ModelContract]  WITH CHECK ADD FOREIGN KEY([ContractID])
REFERENCES [dbo].[Contract] ([ContractID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelContract]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
GO
ALTER TABLE [dbo].[ModelContract]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelImg]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
GO
ALTER TABLE [dbo].[ModelImg]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelLanguage]  WITH CHECK ADD FOREIGN KEY([LanguageID])
REFERENCES [dbo].[Language] ([LanguageID])
GO
ALTER TABLE [dbo].[ModelLanguage]  WITH CHECK ADD FOREIGN KEY([LanguageID])
REFERENCES [dbo].[Language] ([LanguageID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelLanguage]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
GO
ALTER TABLE [dbo].[ModelLanguage]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelSkill]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
GO
ALTER TABLE [dbo].[ModelSkill]  WITH CHECK ADD FOREIGN KEY([ModelID])
REFERENCES [dbo].[Model] ([ModelID])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ModelSkill]  WITH CHECK ADD FOREIGN KEY([SkillID])
REFERENCES [dbo].[Skills] ([SkillID])
GO
ALTER TABLE [dbo].[ModelSkill]  WITH CHECK ADD FOREIGN KEY([SkillID])
REFERENCES [dbo].[Skills] ([SkillID])
ON DELETE CASCADE
GO
