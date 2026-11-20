
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

create table rol(id int not null auto_increment,
nombre varchar(45) not null,
primary key(id));

create table usuario(id int not null auto_increment,
nombre varchar(45) not null,
apellidos varchar(45) not null,
email varchar(45) not null,
activo boolean not null,
primary key (id));

create table credenciales(id int not null auto_increment,
username varchar(45) not null,
password varchar(45) not null,
usuario_id int not null,
rol_id int not null,
primary key (id),
foreign key (usuario_id) references usuario(id),
foreign key (rol_id) references rol(id));

create table bodega(id int not null auto_increment,
nombre varchar(45) not null,
ubicacion varchar(45) not null,
capacidad int not null,
usuario_id int not null,
primary key (id),
foreign key (usuario_id) references usuario(id)
);

create table inventario(id int not null auto_increment,
bodega_id int not null,
producto_id int not null,
stock int not null,
primary key (id),
foreign key (bodega_id) references bodega(id),
foreign key (producto_id) references producto(id));

create table movimiento(id int not null auto_increment,
fecha timestamp not null  ,
tipo_movimiento enum("entrada","salida","transferencia") not null,
usuario_id int not null,
bodega_origen_id int not null,
bodega_destino_id int not null,
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
insert into producto (nombre, descripcion, categoria, precio) values
('laptop asus tuf gaming f16','intel core i7-13650hx, rtx 4060, 16gb ram, ssd 1tb', 'computadores',   5600000.00),
('laptop hp victus 16',       'intel core i7-14700hx, rtx 4060, 16gb ram, ssd 512gb','computadores',   5850000.00),
('audífonos sony wh-ch720n',  'inalámbricos, cancelación de ruido activa, bluetooth 5.2, usb-c','periféricos', 490000.00),
('audífonos hyperx cloud iii','inalámbricos 2.4ghz, dts spatial audio, usb-c',      'periféricos',     520000.00),
('ssd kingston kc3000 1tb',   'nvme pcie 4.0, lectura 7000mb/s, factor m.2 2280',           'Almacenamiento', 310000.00);

insert into usuario (id, nombre, apellidos, email, activo)
values (2, 'Juan Sebastian', 'Toscano Duarte', 'juansetoscano@gmail.com', 1);

insert into credenciales (id, username, password, usuario_id, rol_id)
values (2, 'Encargado_Juan', '091108', 2, 2);

insert into usuario (id, nombre, apellidos, email, activo)
values (3, 'Julian Camilo Tristan', 'Vargas Lagos', 'trifuerzatristan@gmail.com', 1);

insert into credenciales (id, username, password, usuario_id, rol_id)
values (3, 'Encargado_Julian', 'wachuwachuwa', 3, 2);

insert into bodega (nombre, ubicacion, capacidad, usuario_id) values
('Bodega Central Bucaramanga', 'Calle 35 #28-46, Centro, Bucaramanga, Santander', 5000, 2),
('Bodega Central Cúcuta',      'Av. 7 #15-32, Centro, Cúcuta, Norte de Santander', 5000, 3);