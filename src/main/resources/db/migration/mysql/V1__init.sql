CREATE TABLE empresa (
	`id_empresa` BIGINT NOT NULL AUTO_INCREMENT,
	`cnpj` VARCHAR(255) NOT NULL,
	`data_atualizacao` DATETIME NOT NULL,
	`data_criacao` DATETIME NOT NULL,
	`razao_social` VARCHAR(255) NOT NULL,
	PRIMARY KEY (id_empresa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE funcionario (
	`id_funcionario` BIGINT NOT NULL AUTO_INCREMENT,
	`cpf` VARCHAR(255) NOT NULL,
	`data_atualizacao` DATETIME NOT NULL,
	`data_criacao` DATETIME NOT NULL,
	`email` VARCHAR(255) NOT NULL,
	`nome` VARCHAR(255) NOT NULL,
	`perfil` VARCHAR(255) NOT NULL,
	`qtd_horas_almoco` FLOAT,
	`qtd_horas_trabalho_dia` FLOAT,
	`senha` VARCHAR(255) NOT NULL,
	`valor_hora` DECIMAL(19,2),
	`empresa`_id_empresa BIGINT,
	PRIMARY KEY (id_funcionario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE lancamento (
	id_lancamento BIGINT NOT NULL AUTO_INCREMENT,
	data DATETIME NOT NULL,
	data_atualizacao DATETIME NOT NULL,
	data_criacao DATETIME NOT NULL,
	descricao VARCHAR(255) NOT NULL,
	localizacao VARCHAR(255) NOT NULL,
	tipo VARCHAR(255) NOT NULL,
	funcionario_id_funcionario BIGINT,
	PRIMARY KEY (id_lancamento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE funcionario 
	ADD CONSTRAINT FK9i856e68r3nr09dcspi67uhra 
	FOREIGN KEY (empresa_id_empresa) 
	REFERENCES empresa (id_empresa);

ALTER TABLE lancamento 
	ADD CONSTRAINT FK59yn65wfa4u6t5j24rdqr57pg 
	FOREIGN KEY (funcionario_id_funcionario) 
	REFERENCES funcionario (id_funcionario);