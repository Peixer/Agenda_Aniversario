﻿create table contato(id int unsigned auto_increment primary key, listaContatos_id int unsigned not null, NOME varchar(60) not null, EMAIL varchar(50) not null, ,DATA_ANIVERSARIO date not null, BYTES_FOTO MEDIUMBLOB, foreign key(listaContatos_id) references listacontatos(id));