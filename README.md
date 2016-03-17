Implémentation d'une passerelle REST.

Pour tester la passerelle REST: 



	1 - Se rendre dans le répertoire du TP, lancer la commande **java -jar serveurFTP.jar**.
	2 - Lancer un navigateur WEB et accéder à l'adresse suivante : http://localhost:8080/rest/access/
	3 - Ouvrir le projet et executer le main dans le fichier Starter.java	
	4 - Rentrer les informations suivantes :

		- Hostname : localhost
		- Username : user
		- Password : pass
		- Port : 1050

	5 - La connexion au serveur est établie.
	6 - Les commandes possibles sont les suivantes :

		- Accés à un répertoire :
			- http://localhost:8080/rest/access/directory/{directories}

		- Telechargement d'un fichier depuis le repertoire courant :
			- http://localhost:8080/rest/access/download/{filename}

		- Supprimer un fichier du repertoire courant :
			- http://localhost:8080/rest/access/delete/{filename}

		- L'upload de fichier se fait via le formulaire mis en place.
