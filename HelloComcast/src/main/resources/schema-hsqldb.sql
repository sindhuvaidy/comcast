DROP TABLE comcast_customer IF EXISTS;  

CREATE TABLE comcast_customer(
		first_name	VARCHAR(150),
		last_name	VARCHAR(150),
		customer_id	VARCHAR(10) PRIMARY KEY,
		email		VARCHAR(100)
);
		