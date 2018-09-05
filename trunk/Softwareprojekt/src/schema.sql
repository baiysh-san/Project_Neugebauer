CREATE TABLE customer (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	address VARCHAR(50) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE machine (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR (50) NOT NULL,
	description TEXT NOT NULL,
	operative BOOLEAN NOT NULL DEFAULT TRUE,
	not_operative_until TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE product (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR (50) NOT NULL, 
	description TEXT NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE order_ (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	description TEXT NOT NULL,
	customer_id INT(10) UNSIGNED ,
	product_id INT(10) UNSIGNED ,
	amount INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (customer_id) REFERENCES customer (id),
	FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE workschedule (
	id INT(10) UNSIGNED  AUTO_INCREMENT,
	description TEXT,
	product_id INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product (id),
);

CREATE TABLE operation (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	duration INT(10) UNSIGNED NOT NULL DEFAULT '0',
	workschedule_id INT(10) UNSIGNED NOT NULL,
	before_operation_id INT(10),
	after_operation_id INT(10),
	machine_id INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (machine_id) REFERENCES machine (id),
	FOREIGN KEY (workschedule_id) REFERENCES workschedule (id)
);

CREATE TABLE production_order (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	date_of_delivery DATE ,
	workschedule_id INT(10) UNSIGNED,
    order_id INT(10) UNSIGNED,
	PRIMARY KEY (id),
	FOREIGN KEY (order_id) REFERENCES order_ (id),
	FOREIGN KEY (workschedule_id) REFERENCES workschedule (id)
);

CREATE TABLE production_order_operation (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	production_order_id INT(10) UNSIGNED NOT NULL,
	beginn TIMESTAMP ,
	end TIMESTAMP ,
	operation_id INT(10) UNSIGNED,
	PRIMARY KEY (id),
	FOREIGN KEY (operation_id) REFERENCES operation (id),
	FOREIGN KEY (production_order_id) REFERENCES production_order (id)
);

CREATE TABLE employee (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL DEFAULT '0',
	last_name VARCHAR(50) NOT NULL DEFAULT '0',
	address VARCHAR(50) NOT NULL DEFAULT '0',
	available BOOLEAN NOT NULL DEFAULT TRUE,
	not_available_until DATE,
	PRIMARY KEY (id),
);

CREATE TABLE admin (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	login VARCHAR(25) NOT NULL UNIQUE,
	password VARCHAR (25)NOT NULL,
);


CREATE TABLE employee_production_order_operation (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  employee_id INT(10) UNSIGNED NOT NULL,
  production_order_operation_id INT(10) UNSIGNED NOT NULL,
  before_employee_production_order_operation_id INT(10) DEFAULT NULL,
  after_employee_production_order_operation_id INT(10) DEFAULT NULL,
  duration INT(10) UNSIGNED NOT NULL DEFAULT '0',
  beginn TIMESTAMP ,
  end TIMESTAMP ,
  PRIMARY KEY (id),
  FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
  FOREIGN KEY (production_order_operation_id) REFERENCES production_order_operation (id) 
);