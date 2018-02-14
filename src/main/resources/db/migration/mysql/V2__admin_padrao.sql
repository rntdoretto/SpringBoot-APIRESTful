INSERT INTO empresa 
            (id, 
             cnpj, 
             data_atualizacao, 
             data_criacao, 
             razao_social) 
VALUES      (NULL, 
             '61372084000189', 
             CURRENT_DATE(), 
             CURRENT_DATE(), 
             'Doretto TI'); 

INSERT INTO funcionario 
            (id, 
             cpf, 
             data_atualizacao, 
             data_criacao, 
             email, 
             nome, 
             perfil, 
             qtd_horas_almoco, 
             qtd_horas_trabalho_dia, 
             senha, 
             valor_hora, 
             empresa_id) 
VALUES      (NULL, 
             '30788714015', 
             CURRENT_DATE(), 
             CURRENT_DATE(), 
             'admin@doretto.com', 
             'ADMIN', 
             'ROLE_ADMIN', 
             NULL, 
             NULL, 
             '$2a$10$3JngdAbTTUN1P9do.CSMWuqOMLqzfXgdN0N7U/OACNLLY3YQrCE4O', 
             NULL, 
             (SELECT id 
              FROM   empresa 
              WHERE  cnpj = '61372084000189')); 