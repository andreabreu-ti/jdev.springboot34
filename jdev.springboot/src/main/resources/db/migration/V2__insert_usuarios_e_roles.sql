/*Antes de executar o sistema pela primeira vez, renomei a migration versão 1 de V1__ para testeV1__, na segunda execução de testeV1__ para V1__ para criar os usuários e papeis*/ 

insert into usuario(id,login,senha)values(1, 'andre','$2a$10$jQkz2ByMPPMnhC7fjJSjBOOxzPSeEAqnbhHht5qDldXcMsaZJHcvm')

insert into role(id,nome_role) values(1,'ROLE_ADMIN')
insert into role(id,nome_role) values(2,'ROLE_USER')
insert into role(id,nome_role) values(3,'ROLE_CAIXA')
insert into role(id,nome_role) values(4,'ROLE_GERENTE')

insert into usuarios_role(usuario_id,role_id) values(1,1)