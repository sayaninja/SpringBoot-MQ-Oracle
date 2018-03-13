# SpringBoot-MQ-Oracle

SpringBoot example to work with MQ and Oracle XE. Oracle has a table named 'Customer' and two stored procedures (successful and unsuccessful)
- GET_NAME_FROM_ID: gets name of customer for passed id
- THROW_EX_PROCEDURE: throws exception

The purpose of having successful and unsuccessful procedures is to test transactionality of getting messages from MQ. When exception is thrown, the message should be left in the queue.

### Prerequisites

Install and configure following programs (details are in application.yml):
- IBM WebSphere MQ
- [Oracle Database 11g Express Edition (Oracle Database XE)](http://www.oracle.com/technetwork/database/database-technologies/express-edition/overview/index.html)  
- [Oracle SQL Developer](http://www.oracle.com/technetwork/developer-tools/sql-developer/overview/index.html)
