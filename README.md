# Projet COO POO :  EasyChat

![](https://img.shields.io/github/languages/count/iattach/Projet_COO_POO)

Ce projet s’agit d’un système de communication qui sert de support aux équipes et groupes de
l’entreprise afin de leur permettre d’accroitre leur efficacité naturelle. Le système utilise
un(des) agent(s) afin d’accomplir sa mission. En tant que relais de communication, le système permet aux intervenants de se coordonner par l’échange de messages textuels entre eux.

## Table de matières

- [Context](#Context)
- [Installation](#Installation)
    - [Récupération&nbsp;space&nbsp;les&nbsp;sources&nbsp;et&nbsp;configuration](#Récupération&nbsp;les&nbsp;sources&nbsp;et&nbsp;configuration)
- [Utilisation](#Utilisation)
    - [Connexion](#Connexion)
    - [Création&nbsp;de&nbsp;compte](#Création&nbsp;de&nbsp;compte)
    - [Sous-menu&nbsp;du&nbsp;système](#Sous-menu&nbsp;du&nbsp;système)
    - [Sous-menu&nbsp;de&nbsp;la&nbsp;conversation](#Sous-menu&nbsp;de&nbsp;la&nbsp;conversation)
- [Spécification](#Spécification)

## Context

L’utilisateur de ce système est un organisme classique représentant un groupement de
personnes qui ont un intérêt commun et qui collaborent entre eux afin d’arriver à leur fin.
Du point de vue de l’utilisateur, le système est utilisé comme un service et lui facilite la
vie. En effet, un administrateur de l’organisme qui souhaite utiliser le système réalise
l’installation des agents sur les postes des personnes amenées à interagir et, après une
configuration minimale du poste de travail, le système est fonctionnel.

Grâce à sa fonction de support aux tâches de communication, le système rend
indirectement un service indispensable à la population de l’organisme classique
représentant un groupement de personnes qui ont un intérêt commun et qui collaborent
entre eux afin d’arriver à leur fin.

Ce système peut aussi être utilisé par les experts et les techniciens chargés de l’administrer et de le déployer sur les postes des personnes amenées à l’utiliser afin de réaliser plus efficacement leur mission.

## Installation

### Récupération les sources et configuration

Tout d’abord, il faut récupérer le code source en ligne, en clonant ce [git](https://github.com/iattach/Projet_COO_POO) et l’application se trouve sous le nom “[EasyChat.jar](./EasyChat.jar)”.

Supposant le serveur TOMCAT9 déployé, il suffit d'insérer le fichier [EasyChat.war](./EasyChat.war) fournit par l'interface manager de TOMCAT. Si celle-ci n'est pas installé, il suffit d'insérer le fichier [EasyChat.war](./EasyChat.war) dans le répertoire WEB racine de TOMCAT. Il faudra alors relancer TOMCAT pour que la modification soit prise en compte. Si vous désirez changer l'adresse du serveur, vous devrez modifier le champ suivant dans le fichier [Controller.SocketInternalNetwork](./Messenger/src/Controller/SocketInternalNetwork.java) :

```java
 public class SocketInternalNetwork {
	protected static final String PresentServer = "https://srv-gei-tomcat.insa-toulouse.fr/EasyChat/Servlet";
}
```
Pour déployer l’application, il faut que la machine sur laquelle est exécuté le fichier EasyChat.jar possède au moins l'environnement JRE 11, disponible sur le site d'ORACLE. Un fois installé, il suffit de lancer la commande 

```sh
java -jar EasyChat.jar.
```

## Utilisation

Après avoir suivi les étapes d’installation,  nous allons expliquer l’utilisation de notre application et les fonctionnalités.

### Connexion

A l’ouverture de l’application nous arrivons sur la page de connexion comme sur l’image ci-dessous. Pour se connecter, il suffit d’entrer son username et son password dans les champs correspondants, puis de cliquer sur le bouton « Sign in ».

Si le compte n'existe pas, nous arrivons à recevoir un message: « Error of connection : account not found !!! ». Dans ce cas là, nous pouvons cliquer sur « Sign up » pour créer un compte.

Si le binôme username password ne correspond à aucun compte existant, la connexion est refusée et un message d’erreur « Error of connection : account not found !!! » apparaît à la place de « Entrez Username/Password ». Dans ce cas, il faut vérifier le mot de passe ou créer un nouveau compte.

###	Création de compte

En tant que nouvel utilisateur, pour créer un compte, il suffit de cliquer sur le bouton « sign up » qui nous amène sur la page de création de compte ci-dessous.

Entrez Username, Password et Nickname dans les champs correspondant.
Cliquez ensuite sur le bouton “create account” 
Si le compte est déjà existant (username existe déjà pour un autre compte), un message
Error: account already exists!!!
Error: username already exists!!!
Error: nickname already exists!!!

Si la création de compte a bien été effectuée, nous revenons automatiquement sur la page de connexion avec un message “ Your account has been created successfully !!!”.

###	Sous-menu du système

Il est possible de se déconnecter de ce système en cliquant sur « Sign out » dans le sous-menu du système. Il affichera un message « Sign out successfully » si la demande de déconnexion est bien prise en compte.

Dans notre sous-menu, il est possible de changer le compte connecté en cliquant « Change account », cette fonctionnalité permet de se déconnecter automatiquement afin d’aller dans la page de connexion pour effectuer une nouvelle connexion d’un autre compte.   

Il est aussi possible de changer le pseudo de son compte grâce à la page de changement de
pseudo ci-dessous. (accessible via les sous-menu Système -> change nickname).
Pour changer son pseudo, il suffit d’entrer le nouveau pseudo dans le champ correspondant
puis de cliquer sur “change”

###	Sous-menu de la conversation

Pour avoir  une conversation avec un autre utilisateur, il suffit de cliquer sur son DisplayName dans l'onglet « choose user ». On arrive sur la page de conversation grâce à cette action.

On peut voir facilement les utilisateurs connectés sur la zone Users Online, et il est possible de leur envoyer des messages en écrivant dans la zone blanche et en cliquant sur « send ».

De plus, il est possible de voir l’historique de la conversation (dans le sous-menu conversation que nous allons expliquer après) où chaque message est daté. 

Nous pouvons consulter les historiques de conversation dans le sous-menu de conversation.

## Spécification

Spécification de l’application :
-	Nom : EasyChat.jar
-	Taille : 10.1 Mo
-	Ports utilisés : 6666, 6667, 6668, 6669, 6670
-	Version de la JDK : Java SE Development Kit 11.0.10 
-	Commande d’exécution : java -jar EasyChat.jar
-	Librairies :
    -	ojdbc8.jar
    -	sqlite-jdbc-3.25.1.jar 

Spécification du serveur de présence :
- Nom du serveur : https://srv-gei-tomcat.insa-toulouse.fr/
- IP du serveur (Dernier accès : 15/2/2021) : 10.1.5.2
- Version du serveur : Apache Tomcat/9.0.16 (Ubuntu)
- Version de la JVM : 11.0.7+10-post-Ubuntu-2ubuntu218.04
- Adresse du servlet : https://srv-gei-tomcat.insa-toulouse.fr/EasyChat/Servlet 
- Librairie utilisée pour générer le servlet :
    - servlet-api-tomcat9.jar
    - Model.jar (librairie créée pour ce projet)
