CREATE TABLE [dbo].[tbMessageInform] (
	[SEQNo] [varchar] (16) NOT NULL ,
	[InfoType] [varchar] (2) NULL ,
	[ResponseStats] [varchar] (5) NULL ,
	[ReceiveDate] [datetime] NULL ,
	[AccountNoReceive] [varchar] (12) NULL ,
	[AccountType] [varchar] (1) NULL ,
	[AccountDate] [varchar] (7) NULL ,
	[Memo1] [varchar] (14) NOT NULL ,
	[TransactionAmount] [money] NULL ,
	[TransactionDate] [varchar] (7) NULL ,
	[TransactionTime] [varchar] (6) NULL ,
	[CrossBankNo] [varchar] (10) NULL ,
	[DebitCredit] [varchar] (1) NULL ,
	[AmountType] [varchar] (1) NULL ,
	[ServiceManNo] [varchar] (5) NULL ,
	[TransactionID] [varchar] (5) NULL ,
	[TransactionType] [varchar] (1) NULL ,
	[AccountNoSend] [varchar] (19) NULL ,
	[Memo2] [varchar] (11) NULL ,
	[CashIncrease] [varchar] (35) NULL ,
	[Remitter] [varchar] (80) NULL ,
	[RemittanceMemo] [varchar] (80) NULL ,
	[MessageContent] [varchar] (350) NULL 
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[tbMessageInform] WITH NOCHECK ADD 
	CONSTRAINT [PK_tbMessageInform] PRIMARY KEY  NONCLUSTERED 
	(
		[SEQNo],
		[Memo1]
	)  ON [PRIMARY] 
GO

