CREATE TABLE pauta (
   id SERIAL PRIMARY KEY,
   titulo VARCHAR(255) NOT NULL,
   descricao TEXT NOT NULL,
   status VARCHAR(50) NOT NULL
);
