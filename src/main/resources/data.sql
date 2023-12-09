create table Calculations (
    id bigint not null primary key auto_increment,
    expression text not null,
    result decimal(10,2) not null,
    createdAt datetime not null default current_timestamp
);

create table Users (
    id bigint not null primary key auto_increment,
    username varchar(100) not null,
    fullName varchar(200) not null,
    password varchar(100) not null,
    createdAt datetime not null default current_timestamp
);

insert into Calculations (expression, result) values
('2+2', 4),
('5/2', 2.5),
('4*5', 20),
('10-3', 7);

insert into Users (username, fullName, password) values
('sopongebob', 'SpongeBob SquarePants', '$2a$10$NkxZUB.Mcypa4ffulOnUgOLZ.8VKSCR.34nZZzBF19Xzm.V1ce9r6'), -- pass: sponge123
('patrick', 'Patrick Star', '$2a$10$PeOSL54bGFSBRilJp7XfZuIeP70LThlLbTct4.emWEl1EjNTzcl7.') -- pass: patrick@star