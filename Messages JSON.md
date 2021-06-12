# Projet Lego - Bras articulé

## Description des messages JSON


### Version 1.0

### Identifiant du document : MIAGE:T7:NT:A:1.0
### Auteurs : 
  AUVRAY Alvin
	email : alvin.auvray@etu.univ-nantes.fr
  
DEFOSSE Samuel
email : samuel.defosse@etu.univ-nantes.fr
  
GAMBA Oriane
email : oriane.gamba@etu.univ-nantes.fr
  
THIVET Simon
email : simon.thivet@etu.univ-nantes.fr
  
	

### Résumé :
Ce document détaille le format des messages JSON échangés entre la télécommande et le robot EV3.
#### Date de création : 08/06/2021
#### Date de dernière révision : 08/06/2021
#### Documents référencés :
[MIAGE:T6:SP:A:1.0] - Dossier de programmation
	________________



### Introduction
Le robot EV3 échange différents messages avec l’application Android, avec une syntaxe précise sous format JSON. Chaque message est composé d’un ou plusieurs champs JSON, dont un commun : le champ action, qui correspond à la commande à effectuer par le robot.
Les paragraphes suivants définissent le cas d'utilisation d'un message, et la syntaxe du message et de ses arguments.


#### Authentification au robot depuis la télécommande
```json
{
    "action":"co",
    "login":"pseudonyme utilisateur",
    "pwd":"mot de passe utilisateur"
```
#### Déconnexion du compte authentifié
```json
{
    "action":"deco"
}
```
#### Créer une gamme
```json
{
    "action":"newG",
    "gamme":
    {
        /* Gamme au format JSON */
    }
}
```
#### Modifier une gamme
```json
{
    "action":"modG",
    "gamme":
    {
        /* Gamme au format JSON */
    }
}
```
#### Supprimer une gamme
```json
{
    "action":"delG",
    "idGamme":"ID de la gamme"
}
```
#### Demander l’exécution d’une gamme
```json
{
    "action":"execG",
    "idGamme":"ID de la gamme"
}
```
#### Créer un compte utilisateur. Nécessite d’être administrateur.
```json
{
    "action":"newU",
    "login":"pseudonyme utilisateur",
    "pwd":"mot de passe utilisateur"
}
```
#### Supprimer un compte utilisateur. Nécessite d’être administrateur.
```json
{
    "action":"delU",
    "login":"pseudonyme utilisateur"
}
```
#### Basculer en mode autonome (exécution d’une gamme à l’infini)
```json
{
    "action":"auto"
}
```
#### Basculer en mode manuel 
```json
{
    "action":"manu"
}
```
#### Déclencher la panne
```json
{
    "action":"panne"
}
```
#### Récupérer une liste de logs récents
```json
{
    "action":"logs",
    "date":"Date des logs à récupérer"
}
```
#### Envoyer un ping au robot
```json
{
    "action":"ping"
}
```
