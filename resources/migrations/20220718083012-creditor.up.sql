CREATE TABLE creditor(
  id int auto_increment primary key,
  description varchar(100),
  credit int default (0),
  debit int default (0));
