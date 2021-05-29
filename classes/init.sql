CREATE TABLE IF NOT EXISTS Gamme (
    id int NOT NULL,
    description varchar2(128),
    PRIMARY KEY (id)
 );
 
 CREATE TABLE IF NOT EXISTS Operation (
    id int NOT NULL,
    fkGamme int NOT NULL,
    description varchar2(128),
    PRIMARY KEY (id),
    FOREIGN KEY (fkGamme) REFERENCES Gamme(id)
 );
 
 CREATE TABLE IF NOT EXISTS Tache(
    id int NOT NULL,
    fkOpe int NOT NULL,
    description varchar2(128),
    portMoteur varchar2,
    valeur int,
    typeA varchar2(54),
    PRIMARY KEY (id),
    FOREIGN KEY (fkOpe) REFERENCES Operation(id)
 );
 
 
 CREATE TABLE IF NOT EXISTS RapportPanne(
    dateR date NOT NULL,
    erreur varchar2(128),
    PRIMARY KEY (dateR)
 );
 
 CREATE TABLE IF NOT EXISTS Config(
    nom varchar2(54) NOT NULL,
    valeur varchar2(128),
    PRIMARY KEY (nom)
 );
 
 CREATE TABLE IF NOT EXISTS Utilisateur(
    nom varchar2(54) NOT NULL,
    passwd varchar2(54),
    fkRole varchar2(54);
    PRIMARY KEY (nom),
    FOREIGN KEY (fkRole) Role(nom)
 );
 
 CREATE TABLE IF NOT EXISTS Role(
    nom varchar2(54) NOT NULL,
    isAdmin int, --Booleen n'existe pas en SQL 0:Faux 1:True
    PRIMARY KEY (nom)
 );
