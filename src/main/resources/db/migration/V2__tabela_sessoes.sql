CREATE TABLE sessoes (
   id BIGSERIAL PRIMARY KEY,
   id_pauta INTEGER NOT NULL,,
   inicio TIMESTAMP,
   fim TIMESTAMP,
   status VARCHAR(50) NOT NULL

   CONSTRAINT fk_sessoes_pauta
   FOREIGN KEY (id_pauta) REFERENCES pautas(id)
);
