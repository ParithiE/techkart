SET search_path TO users;

-- TechKart demo accounts. Passwords: karthik@techkart.test / karthik123, priya@techkart.test / priya123
-- Demo passwords only. Change or remove these accounts before any real deployment.
insert into users(id, email, password, name, role) values
(3, 'karthik@techkart.test', '$2a$10$bhDVhudlZiu/UHHO7pMo/.JUyISEbl4Ia3LOWr/XoUVs092I664Ku', 'Karthik', 'ROLE_USER'),
(4, 'priya@techkart.test', '$2a$10$I06wiR0R8YN6DR3VWOtlI.vP7eavtbbYDoPvj1cnxcRhbJp3NY1s.', 'Priya', 'ROLE_ADMIN');
