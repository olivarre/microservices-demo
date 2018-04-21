/* (REO) Added products table  */
drop table T_PRODUCT if exists;

create table T_PRODUCT (
						ID 				bigint identity primary key, 
						NUMBER 			varchar(9),
                        NAME 			varchar(100) not null, 
						MANUFACTURER 	varchar(100) not null, 
						PRICE 			decimal(9,2), 
						unique(NUMBER)
						);
                        
ALTER TABLE T_PRODUCT ALTER COLUMN PRICE SET DEFAULT 0.0;
