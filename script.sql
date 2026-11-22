DROP TABLE IF EXISTS Pokemon;
DROP TABLE IF EXISTS Adestrador;
DROP TABLE IF EXISTS Pokedex;

CREATE TABLE Pokedex (
    Id SERIAL PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Peso DECIMAL(10, 2),
    Misc TEXT
);

CREATE TABLE Adestrador (
    Id SERIAL PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Nacemento DATE
);

CREATE TABLE Pokemon (
    Id SERIAL PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Nacemento DATE,
    PokedexEntry INTEGER,
    Adestrador INTEGER,
    FOREIGN KEY (PokedexEntry) REFERENCES Pokedex(Id),
    FOREIGN KEY (Adestrador) REFERENCES Adestrador(Id)
);
