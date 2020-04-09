drop database if exists BlogDB;

create database BlogDB;

use BlogDB;

create table Users(
	Id int primary key auto_increment,
    username varchar(45) not null,
    `password` varchar(100) not null,
    enabled tinyint(1) not null
);

create table Content(
	Id int primary key auto_increment,
    userId int not null,
    isstatic tinyint not null,
    datepublished DATE,
    pageName varchar(50),
    title varchar(45) not null,
    body varchar(10000) not null,
    approved tinyint not null,
    foreign key (userId) references Users(Id)
);

create table Role(
	Id int primary key auto_increment,
    userRole varchar(45) not null
);

create table Tag(
	Id int primary key auto_increment,
    hashTag varchar(45) not null
);

create table Content_Tag(
	primary key (Id_Content, Id_Tag),
	Id_Content int not null,
    Id_Tag int not null,
	foreign key (Id_Content) references Content(Id),
    foreign key (Id_Tag) references Tag(Id)
);

create table User_Role(
	primary key (Id_Users, Id_Role),
	Id_Users int not null,
    Id_Role int not null,
	foreign key (Id_Users) references Users(Id),
    foreign key (Id_Role) references Role(Id)
);

insert into Users(id, username, `password`,enabled)
    values(1,"admin", "password", 1),
        (2,"user","password", 1);

insert into role (id, userRole)
    values(1,"ROLE_ADMIN"), (2,"ROLE_USER");
    
insert into User_Role(`Id_Users`,`Id_Role`)
    values(1,1),(1,2),(2,2);
    
UPDATE users SET password = '$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS' WHERE id = 1;
UPDATE users SET password = '$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS' WHERE id = 2;




