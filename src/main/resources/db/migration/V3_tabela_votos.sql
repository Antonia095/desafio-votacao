CREATE TABLE votos (
   id BIGINT PRIMARY KEY,
   id_usuario BIGINT NOT NULL,
   id_sessao_votacao BIGINT NOT NULL,
   tipo_voto VARCHAR(10),

   CONSTRAINT fk_votos_sessao
       FOREIGN KEY (id_sessao_votacao) REFERENCES sessoes(id),

   CONSTRAINT uk_usuario_sessao
       UNIQUE (id_usuario, id_sessao_votacao)
);
