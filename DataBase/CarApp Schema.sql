-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[account]
CREATE TABLE [dbo].[account]
(
    [username] varchar(50) NOT NULL,
    [password] varchar(50) NOT NULL,


    CONSTRAINT [PK_5] PRIMARY KEY CLUSTERED ([username] ASC)
);
GO



-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[admin]
CREATE TABLE [dbo].[admin]
(
    [username] varchar(50) NOT NULL,


    CONSTRAINT [PK_34] PRIMARY KEY CLUSTERED ([username] ASC),
    CONSTRAINT [FK_45] FOREIGN KEY ([username]) REFERENCES [dbo].[account] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_47] ON [dbo].[admin]
    (
     [username] ASC
        )

GO


-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[customer]
CREATE TABLE [dbo].[customer]
(
    [username]    varchar(50) NOT NULL,
    [email]       varchar(50) NULL,
    [phoneNumber] varchar(50) NOT NULL,
    [suspended]   bit         NOT NULL,
    [birthday]    date        NOT NULL,


    CONSTRAINT [PK_10] PRIMARY KEY CLUSTERED ([username] ASC),
    CONSTRAINT [FK_39] FOREIGN KEY ([username]) REFERENCES [dbo].[account] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_41] ON [dbo].[customer]
    (
     [username] ASC
        )

GO


-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[discountDestination]
CREATE TABLE [dbo].[discountDestination]
(
    [destination] varchar(50) NOT NULL,


    CONSTRAINT [PK_118] PRIMARY KEY CLUSTERED ([destination] ASC)
);
GO

-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[driver]
CREATE TABLE [dbo].[driver]
(
    [username]    varchar(50) NOT NULL,
    [phoneNumber] varchar(50) NOT NULL,
    [email]       varchar(50) NULL,
    [national_id] varchar(14) NOT NULL,
    [license]     varchar(20) NOT NULL,
    [suspended]   bit         NOT NULL,
    [verified]    bit         NOT NULL,
    [balance]     float       NOT NULL      DEFAULT 0


    CONSTRAINT [PK_22] PRIMARY KEY CLUSTERED ([username] ASC),
    CONSTRAINT [FK_42] FOREIGN KEY ([username]) REFERENCES [dbo].[account] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_44] ON [dbo].[driver]
    (
     [username] ASC
        )

GO



-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[publicHolidays]
CREATE TABLE [dbo].[publicHolidays]
(
    [date] date        NOT NULL,
    [name] varchar(50) NOT NULL,


    CONSTRAINT [PK_113] PRIMARY KEY CLUSTERED ([date] ASC)
);
GO


-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[rate]
CREATE TABLE [dbo].[rate]
(
    [rateID]           uniqueidentifier NOT NULL,
    [customerUsername] varchar(50)      NOT NULL,
    [driverUsername]   varchar(50)      NOT NULL,
    [rating]           float            NOT NULL,


    CONSTRAINT [PK_50] PRIMARY KEY CLUSTERED ([rateID] ASC),
    CONSTRAINT [FK_95] FOREIGN KEY ([customerUsername]) REFERENCES [dbo].[customer] ([username]),
    CONSTRAINT [FK_98] FOREIGN KEY ([driverUsername]) REFERENCES [dbo].[driver] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_100] ON [dbo].[rate]
    (
     [driverUsername] ASC
        )

GO

CREATE NONCLUSTERED INDEX [FK_97] ON [dbo].[rate]
    (
     [customerUsername] ASC
        )

GO


-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[request]
CREATE TABLE [dbo].[request]
(
    [requestID]          uniqueidentifier NOT NULL,
    [source]             varchar(50)      NOT NULL,
    [destination]        varchar(50)      NOT NULL,
    [customerUsername]   varchar(50)      NOT NULL,
    [numberOfPassengers] smallint         NOT NULL,


    CONSTRAINT [PK_59] PRIMARY KEY CLUSTERED ([requestID] ASC),
    CONSTRAINT [FK_62] FOREIGN KEY ([customerUsername]) REFERENCES [dbo].[customer] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_64] ON [dbo].[request]
    (
     [customerUsername] ASC
        )

GO

-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[offer]
CREATE TABLE [dbo].[offer]
(
    [offerID]        uniqueidentifier NOT NULL,
    [price]          money            NOT NULL,
    [accepted]       bit              NOT NULL,
    [driverUsername] varchar(50)      NOT NULL,
    [requestID]      uniqueidentifier NOT NULL,


    CONSTRAINT [PK_67] PRIMARY KEY CLUSTERED ([offerID] ASC),
    CONSTRAINT [FK_70] FOREIGN KEY ([requestID]) REFERENCES [dbo].[request] ([requestID]),
    CONSTRAINT [FK_73] FOREIGN KEY ([driverUsername]) REFERENCES [dbo].[driver] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_75] ON [dbo].[offer]
    (
     [driverUsername] ASC
        )

GO


-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[activeOffers]
CREATE TABLE [dbo].[activeOffers]
(
    [username] varchar(50)      NOT NULL,
    [offerID]  uniqueidentifier NOT NULL,


    CONSTRAINT [PK_130] PRIMARY KEY CLUSTERED ([username] ASC, [offerID] ASC),
    CONSTRAINT [FK_127] FOREIGN KEY ([username]) REFERENCES [dbo].[driver] ([username]),
    CONSTRAINT [FK_131] FOREIGN KEY ([offerID]) REFERENCES [dbo].[offer] ([offerID])
);
GO


CREATE NONCLUSTERED INDEX [FK_129] ON [dbo].[activeOffers]
    (
     [username] ASC
        )

GO

CREATE NONCLUSTERED INDEX [FK_133] ON [dbo].[activeOffers]
    (
     [offerID] ASC
        )

GO

-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[event]
CREATE TABLE [dbo].[event]
(
    [eventID] uniqueidentifier NOT NULL,
    [offerID] uniqueidentifier NOT NULL,
    [name]    varchar(50)      NOT NULL,
    [time]    datetime         NOT NULL,


    CONSTRAINT [PK_105] PRIMARY KEY CLUSTERED ([eventID] ASC),
    CONSTRAINT [FK_106] FOREIGN KEY ([offerID]) REFERENCES [dbo].[offer] ([offerID])
);
GO


CREATE NONCLUSTERED INDEX [FK_108] ON [dbo].[event]
    (
     [offerID] ASC
        )

GO

-- ******************SqlDBM: Microsoft SQL Server * *****************
-- ******************************************************************

-- ************************************** [dbo].[favouriteAreas]
CREATE TABLE [dbo].[favouriteAreas]
(
    [area]           varchar(50) NOT NULL,
    [driverUsername] varchar(50) NOT NULL,


    CONSTRAINT [PK_141] PRIMARY KEY CLUSTERED ([area] ASC),
    CONSTRAINT [FK_135] FOREIGN KEY ([driverUsername]) REFERENCES [dbo].[driver] ([username])
);
GO


CREATE NONCLUSTERED INDEX [FK_137] ON [dbo].[favouriteAreas]
    (
     [driverUsername] ASC
        )

GO
CREATE TABLE [discount]
(
    [discountID]    uniqueidentifier NOT NULL,
    [value]      float            NOT NULL,

    CONSTRAINT [PK_144] PRIMARY KEY CLUSTERED ([discountID] ASC),
    CONSTRAINT [FK_149] FOREIGN KEY ([discountID]) REFERENCES [dbo].[offer] ([offerID])
);
GO


CREATE NONCLUSTERED INDEX [FK_151] ON [discount]
    (
     [discountID] ASC
        )

GO


