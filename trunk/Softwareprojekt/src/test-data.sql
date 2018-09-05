insert into machine (name, description) values ('Saegemaschine', 'ich bin bereit zu saegen');
insert into machine (name, description) values ('Hobelmaschine', 'mach alles schoen und flach');
insert into machine (name, description) values ('Bohrmaschine', 'ich mache Loeche wo du willst');
insert into machine (name, description) values ('Politur', 'Maschine fuer Oberlfaeche');
insert into machine (name, description) values ('Verpackungsmaschine', 'Maschine fuer die Verpackung');

insert into customer (first_name, last_name, address) values ('Lionel', 'Messi', 'Barcelona, Camp Nou Str. 10');
insert into customer (first_name, last_name, address) values ('Andreas', 'Iniesta', 'Barcelona, Camp Nou Str. 8');
insert into customer (first_name, last_name, address) values ('Mark Andre', 'Ter-Stegen', 'Barcelona, Camp Nou Str. 1');
insert into customer (first_name, last_name, address) values ('Tomas', 'Mueller', 'Muenich');


insert into product (name, description) values ('Tisch', 'ein normaler Tisch');
insert into product (name, description) values ('Stuhl', 'ein normaler Stuhl');
insert into product (name, description) values ('Schrank', 'ein kleiner Schrank');
insert into product (name, description) values ('Kommode', 'eine schoene Komode');
insert into product (name, description) values ('Regal', 'ein normales Regal');
insert into product (name, description) values ('Tuer', 'ein normaler Tuer');
insert into product (name, description) values ('Bett', 'ein normales Bett');

insert into workschedule (description, product_id) values ('Arbeitsplan f?r Tisch', 1);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Stuhl', 2);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Schrank', 3);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Kommode', 4);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Regal', 5);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Tuer', 6);
insert into workschedule (description, product_id) values ('Arbeitsplan f?r Bett', 7);
-- Opertionen von Tish
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 30, null, 2, 1, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 36, 1, 3, 1, 2);				
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 10, 2, 4, 1, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 260, 3, 5, 1, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 15, 4, null, 1, 5);

-- Operationen von Stuhl
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 30, null, 7, 2, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 30, 6, 8, 2, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 10, 7, 9, 2, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 250, 8, 10, 2, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 10, 9, null, 2, 5);
				
-- Operationen von Schrank
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 60, null, 12, 3, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 60, 11, 13, 3, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 30, 12, 14, 3, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 400, 13, 15, 3, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 20, 14, null, 3, 5);

-- Operationen von Kommode
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 60, null, 17, 4, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 50, 16, 18, 4, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 30, 17, 19, 4, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 350, 18, 20, 4, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 15, 19, null, 4, 5);

-- Operationen von Regal
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 45, null, 22, 5, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 45, 21, 23, 5, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 20, 22, 24, 5, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 300, 23, 25, 5, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 15, 24, null, 5, 5);

-- Operationen von Tuer
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 35, null, 27, 6, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 700, 26, 28, 6, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 5, 27, 29, 6, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 180, 28, 30, 6, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 5, 29, null, 6, 5);

-- Operationen von Bett
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Saegen', 30, null, 32, 7, 1);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Hobeln', 32, 31, 33, 7, 2);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Bohren', 15, 32, 34, 7, 3);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id)
				values ('Politur', 260, 33, 35, 7, 4);
insert into operation (name, duration, before_operation_id, after_operation_id, workschedule_id, machine_id) 
				values ('Verpacken', 10, 34, null, 7, 5);

				
				
				
insert into employee (first_name, last_name, address)
								values ('Bernd', 'Leno',  'Leverkusen, Bay Str. 12');
insert into employee (first_name, last_name, address,
                      available, not_available_until)
								values ('Mats', 'Hummels',  'Munich, Allianz Str. 5', false, '2018-07-15');
insert into employee (first_name, last_name, address)
								values ('Jonas', 'Hector',  'Koeln, Rhein Energie Str. 2');
insert into employee (first_name, last_name, address)
								values ('Joshua', 'Kimmich',  'Munich, Allianz Str. 18');
insert into employee (first_name, last_name, address)
								values ('Manuel', 'Neuer',  'Munich, Allianz Str. 1');
insert into employee (first_name, last_name, address)
								values ('Lorius', 'Karius',  'Mainz, Opel Str. 1');
insert into employee (first_name, last_name,  address)
								values ('Jonathan', 'Tah',  'Leverkusen, Bay Str. 24');
insert into employee (first_name, last_name, address)
								values ('Julian', 'Brandt',  'Leverkusen, Bay Str. 20');
insert into employee (first_name, last_name, address)
								values ('Timo', 'Werner',  'Leipzig, Red Bull Str. 9');
insert into employee (first_name, last_name, address)
								values ('Marco', 'Reus', 'Dortmund, Siganl Iduna Str. 24');
 
insert into admin (login, password) values ('serbus', 'qwerty');
insert into admin (login, password) values ('mathum', 'qwerty');
insert into admin (login, password) values ('jonhec', 'qwerty');
insert into admin (login, password) values ('joskim', 'qwerty');
insert into admin (login, password) values ('julbra', 'qwerty');
insert into admin (login, password) values ('lorkar', 'qwerty');
insert into admin (login, password) values ('jontah', 'qwerty');
insert into admin (login, password) values ('timwer', 'qwerty');
insert into admin (login, password) values ( 'marreu', 'qwerty',);

            



								



