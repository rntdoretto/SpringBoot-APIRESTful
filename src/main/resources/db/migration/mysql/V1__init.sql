CREATE TABLE `empresa`
  (
     `id`               BIGINT NOT NULL auto_increment,
     `cnpj`             VARCHAR(255) NOT NULL,
     `data_atualizacao` DATETIME NOT NULL,
     `data_criacao`     DATETIME NOT NULL,
     `razao_social`     VARCHAR(255) NOT NULL,
     PRIMARY KEY (`id`)
  )
engine=innodb
DEFAULT charset=utf8;

CREATE TABLE `funcionario`
  (
     `id`                     BIGINT NOT NULL auto_increment,
     `cpf`                    VARCHAR(255) NOT NULL,
     `data_atualizacao`       DATETIME NOT NULL,
     `data_criacao`           DATETIME NOT NULL,
     `email`                  VARCHAR(255) NOT NULL,
     `nome`                   VARCHAR(255) NOT NULL,
     `perfil`                 VARCHAR(255) NOT NULL,
     `qtd_horas_almoco`       FLOAT,
     `qtd_horas_trabalho_dia` FLOAT,
     `senha`                  VARCHAR(255) NOT NULL,
     `valor_hora`             DECIMAL(19, 2),
     `empresa_id`             BIGINT,
     PRIMARY KEY (`id`)
  )
engine=innodb
DEFAULT charset=utf8;

CREATE TABLE `lancamento`
  (
     `id`               BIGINT NOT NULL auto_increment,
     `data`             DATETIME NOT NULL,
     `data_atualizacao` DATETIME NOT NULL,
     `data_criacao`     DATETIME NOT NULL,
     `descricao`        VARCHAR(255),
     `localizacao`      VARCHAR(255),
     `tipo`             VARCHAR(255) NOT NULL,
     `funcionario_id`   BIGINT,
     PRIMARY KEY (`id`)
  )
engine=innodb
DEFAULT charset=utf8;

ALTER TABLE `funcionario`
  ADD CONSTRAINT fk9i856e68r3nr09dcspi67uhra FOREIGN KEY (`empresa_id`)
  REFERENCES `empresa` (`id`);

ALTER TABLE `lancamento`
  ADD CONSTRAINT fk59yn65wfa4u6t5j24rdqr57pg FOREIGN KEY (`funcionario_id`)
  REFERENCES `funcionario` (`id`); 