create table Calculations (
    id bigint not null primary key auto_increment,
    expression text not null,
    result decimal(10,2) not null,
    createdAt datetime not null default current_timestamp
);

insert into Calculations (expression, result) values
('2+2', 4),
('5/2', 2.5),
('4*5', 20),
('10-3', 7);