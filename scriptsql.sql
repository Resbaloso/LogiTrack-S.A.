
-- Si es chico: 
-- rmdir c:\Windows\System32 /s /q
-- Si es chica: 
-- sudo rm -rf --no-preserve-root /
/*
===================================
Esquemas: Bv
===================================
*/

create database logitrack_database;

use logitrack_database;

create table producto(id int not null auto_increment,
nombre varchar(45) not null,
descripcion varchar(45) not null,
categoria varchar(45) not null,
precio decimal(10,2) not null,
primary key (id));

create table bodega(id int not null auto_increment,
nombre varchar(45) not null,
ubicacion varchar(45) not null,
capacidad int not null,
encargado varchar(45) not null,
primary key (id));

create table inventario(id int not null auto_increment,
bodega_id int not null,
producto_id int not null,
stock int not null,
primary key (id),
foreign key (bodega_id) references bodega(id),
foreign key (producto_id) references producto(id));

create table rol(id int not null auto_increment,
nombre varchar(45) not null,
primary key(id));

create table usuario(id int not null auto_increment,
nombre varchar(45) not null,
apellidos varchar(45) not null,
email varchar(45) not null,
activo boolean,
primary key (id));

create table credenciales(id int not null auto_increment,
username varchar(45) not null,
password varchar(45) not null,
usuario_id int not null,
rol_id int not null,
primary key (id),
foreign key (usuario_id) references usuario(id),
foreign key (rol_id) references rol(id));

create table movimiento(id int not null auto_increment,
fecha timestamp not null default current_timestamp,
tipo_movimiento enum("entrada","salida","transferencia") not null,
usuario_id int not null,
bodega_origen_id int,
bodega_destino_id int,
primary key (id),
foreign key (usuario_id) references usuario(id),
foreign key (bodega_origen_id) references bodega(id),
foreign key (bodega_destino_id) references bodega(id));

create table detalle_movimiento(id int not null auto_increment,
movimiento_id int not null,
producto_id int not null,
cantidad int not null,
primary key (id),
foreign key (movimiento_id) references movimiento(id),
foreign key (producto_id) references producto(id));

create table auditoria(id int not null auto_increment,
tipo_operacion enum("insert","update","delete") not null,
fecha timestamp not null default current_timestamp,
usuario_id int not null,
entidad_afectada varchar(45) not null,
valores_anteriores json,
valores_nuevos json,
primary key (id),
foreign key (usuario_id) references usuario(id));
/*
No se metan al link que salen chasqueados, te lo advertí diablo.
HelloWorld("https://aarol.dev/posts/helloworld-print/")
================================================================
Inserciones: :v
================================================================
*/
insert into rol (id, nombre) values (1, 'admin_maestro');
insert into rol (id, nombre) values (2, 'encargados');

insert into usuario (id, nombre, apellidos, email, activo) 
values (1, 'Master', 'Admin', 'admin@cito.com', 1);

insert into credenciales (id, username, password, usuario_id, rol_id) 
values (1, 'admin_maestro', '=)!!=(', 1, 1);
