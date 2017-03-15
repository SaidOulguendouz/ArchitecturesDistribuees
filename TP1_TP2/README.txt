/************************************************************************************************************************/
/************************************ Documentation de notre service RESTFUL ********************************************/
/************************************************************************************************************************/

- Notre service offre actuellement les fonctions suivantes :

	1- Ajouter une cage
        client.add_cage(Cage g);
        
        2- Supprimer une cage
        client.delete_cage(Cage g);
        
        3- Ajoute un animal
        client.add_animal(Animal a);
        
        4- Modifie l'ensemble des animaux : la modification rajoute "Modifié" au nom de chaque animal
        client.edit_animals();
        
        5- Supprime l'ensemble des animaux
        client.delete_animals();
        
        6- Crée l’animal identifié par {animal_id}
        client.add_animal_By_Id(new Animal(Stirng nomAnimal, Stirng nomCage, Stirng nomSpecies, UUID.randomUUID()), String animal_id);
        
        7- Modifie l’animal identifié par {animal_id}
        client.edit_animal_By_Id(Stirng animal_id);
        
        8- Supprime l’animal identifié par {animal_id}
        client.delete_animal_By_Id(Stirng animal_id);
        
        9- Recherche d'un animal par son nom
       client.find_animal_By_Name(Stirng nomAnimal);
        
        10- Recherche d'un animal par position
       client.find_animal_By_Position("latitude;longitude");
       
       11- Recherche d'un animal près d’une position
      client.find_animal_Near_Position("latitude;longitude");
        
        12- Récupération des info. Wolfram d’un animal
       client.animal_Infos_Wolfram(Stirng animal_id);
        
        13- Récupération des info. Du trajet depuis une position GPS jusqu’à votre centre en utilisant le service Graphhopper
       client.animal_Infos_Trajet("latitude;longitude");