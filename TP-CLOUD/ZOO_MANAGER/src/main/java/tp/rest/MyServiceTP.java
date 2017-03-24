package tp.rest;

import tp.model.*;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.ws.rs.*;
import javax.xml.bind.JAXBException;
import javax.xml.ws.http.HTTPException;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;

@Path("/zoo-manager/")
public class MyServiceTP {

    private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");

    public MyServiceTP() {
        // Fill our center with some animals
Cage rouen = new Cage("Cage de Rouen",new Position( 49.443889, 1.103333),20,new LinkedList<>());
        
        Cage paris = new Cage("Cage de Paris",new Position( 48.856578 , 2.351828),30,new LinkedList<>());
        
        Cage somalie = new Cage("Cage de Somalie",new Position(  2.333333  , 48.85),26,new LinkedList<>());
        
        Cage bihorel = new Cage("Cage de Bihorel",new Position(   49.455278  , 1.116944),18,new LinkedList<>());
        
        Cage londres = new Cage("Cage de Londres",new Position(  51.504872  ,  -0.07857),35,new LinkedList<>());
        
        Cage canada = new Cage("Cage de Canada",new Position(  43.2  ,  -80.38333),45,new LinkedList<>());
        
        Cage porto_Vecchio = new Cage("Cage de Porto­Vecchio",new Position(  41.5895241 , 9.2627),28,new LinkedList<>());
        
        Cage montreux = new Cage("Cage de Montreux",new Position(  46.4307133 , 6.9113575),34,new LinkedList<>());
        
        Cage villers_Bocage = new Cage("Cage de Villers­Bocage",new Position(  50.0218 , 2.3261),23, new LinkedList<>());
        
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
                ))
        );

        center.getCages().addAll(Arrays.asList(usa, amazon, rouen, paris, somalie, bihorel, londres, canada, porto_Vecchio, montreux, villers_Bocage));
    }

    /**
     * GET method bound to calls on /animals/{something}
     */
    @GET
    @Path("/animals/{id}/")
    @Produces("application/xml")
    public Animal getAnimal(@PathParam("id") String animal_id) throws JAXBException {
        try {
            return center.findAnimalById(UUID.fromString(animal_id));
        } catch (AnimalNotFoundException e) {
            throw new HTTPException(404);
        }
    }
    
    /*Crée l’animal identifié par {animal_id}*/
    @POST
    @Path("/animals/{id}/")
    @Produces("application/xml")
    public Animal addAnimal(@PathParam("id") String animal_id) throws JAXBException {
    	/*On récupère les informations de l'animal depuis la source*/
        Animal animal = new Animal("New Animal", "Cage de Rouen", "Rouen", UUID.fromString(animal_id));
        
        /*On récupère la cage de l'animal, puis on insert l'animal dans cette cage*/
        this.center.getCages()
                .stream()
                .filter(cage -> cage.getName().equals(animal.getCage()))
                .findFirst()
                .orElseThrow(() -> new HTTPException(404))
                .getResidents()
                .add(animal);
        return animal;
    }
    
    //*Modifie l’animal identifié par {animal_id}*/
    @PUT
    @Path("/animals/{id}/")
    @Produces("application/xml")
    public Center editAnimal(@PathParam("id") String animal_id) throws JAXBException {
    	/*Onrécupère l'ensemble des cages*/
        Collection<Cage> listCages = this.center.getCages();
        
         Collection<Animal> listAnimals;
         
         /*On parcourt le collection de cages*/
         for(Cage cage : listCages){
         	/*On récupère l'ensemle des animaux de la cage*/
         	listAnimals = cage.getResidents();
         	/*On parcourt le collection d'animaux*/
         	for(Animal animal : listAnimals){
         		/*Si l'id animal=animal_id alors on modifie l'animal*/
         		if(animal.getId().equals(UUID.fromString(animal_id))){
         			animal.setName("Animal Modifié");
         		}
         	}
         }
         return center;
    }
    
    /*Supprime l’animal identifié par {animal_id}*/	
    @DELETE
    @Path("/animals/{id}/")
    @Produces("application/xml")
    public Center deleteAnimal(@PathParam("id") String animal_id) throws JAXBException {
    	/*Onrécupère l'ensemble des cages*/
    	Collection<Cage> listCages = this.center.getCages();
    	
        Collection<Animal> listAnimals;
        
        /*On parcourt le collection de cages*/
        for(Cage cage : listCages){
        	/*On récupère l'ensemle des animaux de la cage*/
        	listAnimals = cage.getResidents();
        	/*On parcourt le collection d'animaux*/
        	for(Animal animal : listAnimals){
        		/*Si l'id animal=animal_id alors on supprime l'animal*/
        		if(animal.getId().equals(UUID.fromString(animal_id))){
        			System.out.println("Animal supprimé i ="+ UUID.fromString(animal_id));
        			listAnimals.remove(animal);
        		}
        	}
        }
        
         return center;
    }

    /**
     * GET method bound to calls on /animals
     */
    @GET
    @Path("/animals/")
    @Produces("application/xml")
    public Center getAnimals(){
        return this.center;
    }

    /**
     * POST method bound to calls on /animals
     */
    @POST
    @Path("/animals/")
    @Consumes({"application/xml", "application/json" })
    public Center postAnimals(Animal animal) throws JAXBException {
        this.center.getCages()
                .stream()
                .filter(cage -> cage.getName().equals(animal.getCage()))
                .findFirst()
                .orElseThrow(() -> new HTTPException(404))
                .getResidents()
                .add(animal);
        return this.center;
    }
    
    @PUT
    @Path("/animals")
    @Consumes({"application/xml", "application/json" })
    public Center editAnimals() throws JAXBException {
    	/*Onrécupère l'ensemble des cages*/
        Collection<Cage> listCages = this.center.getCages();
        
         Collection<Animal> listAnimals;
         
         /*On parcourt le collection de cages*/
         for (Cage cage : listCages){
        	 
         	/*On récupère l'ensemle des animaux de la cage*/
         	listAnimals = cage.getResidents();
         	
         	/*On parcourt le collection d'animaux*/
         	for(Animal animal : listAnimals){
         		//On modifie chaque animal
         		animal.setName(animal.getName()+" Modifié");
         	}
         }
         return center;
    }
    
    @DELETE
    @Path("/animals")
    @Consumes({"application/xml", "application/json" })
    public Center deleteAnimals() throws JAXBException {

    	/*Onrécupère l'ensemble des cages*/
    	Collection<Cage> listCages = this.center.getCages();
    	
        Collection<Animal> listAnimals;
        
        /*On parcourt le collection de cages*/
        for(Cage cage : listCages){
        	
        	/*On récupère l'ensemle des animaux de la cage*/
        	listAnimals = cage.getResidents();
        	/*On supprime tous les animaux de la cage*/
        	listAnimals.clear();
        }
        return center;
    }
    
    /**
     * Method bound to calls on /cage/{something}
     */
    
    /*Retourne l'ensemble des cages*/
    @GET
    @Path("/cage")
    @Produces("application/xml")
    public Collection<Cage> getCages(){
    	return center.getCages();
    }

    /*Crée une nouvelle cage*/
    @POST
    @Path("/cage")
    @Produces("application/xml")
    public Collection<Cage> addCage(){
    	return center.getCages();
    }
    
    /*Supprimer une cage*/	
    @DELETE
    @Path("/cage")
    @Produces("application/xml")
    public Collection<Cage> deleteCage(){
    	return center.getCages();
    }
    
    /**
     * Method bound to calls on /cage/edit
     */
    @PUT
    @Path("/cage/edit/{name}")
    @Produces("application/xml")
    public Center editCage(@PathParam("name") String name){
    	/*Modifie l'ensemble des animaux d'une cage*/

        	/*On récupère l'ensemble des cages*/
           Collection<Cage> listCages = this.center.getCages();

            Collection<Animal> listAnimals;

            
            /*On parcourt le collection de cages*/
            for(Cage cage : listCages){
            	
            	if(cage.getName().equals(name)){
                	/*On récupère l'ensemle des animaux de la cage*/
                	listAnimals = cage.getResidents();
	            	/*On parcourt le collection d'animaux*/
	            	for(Animal animal : listAnimals){
	            		//On modifie chaque animal
	            		animal.setName(animal.getName()+" Modifié");
	            	}
            	}
            }
            return this.center;
    }
    
    /**
     * Method bound to calls on /cage/edit
     */
    @DELETE
    @Path("/cage/delete/{name}")
    @Produces("application/xml")
    public Center deleteCage(@PathParam("name") String name){
    	/*Supprimer l'ensemble des animaux d'une cage*/
    	
        	/*On récupère l'ensemble des cages*/
           Collection<Cage> listCages = this.center.getCages();

            //int i=1;
            
            /*On parcourt le collection de cages*/
            for(Cage cage : listCages){
            	if(cage.getName().contains(name)){
            		cage.getResidents().clear();
            	}
            }
            return this.center;
    }
    
    /**
     * Method bound to calls on /animalDelete/{name}
     */
    @DELETE
    @Path("/animalDelete/{name}")
    @Produces("application/xml")
    public Center animalDelete(@PathParam("name") String name){
    	/*Supprimer un animal par son nom*/
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();

            Collection<Animal> listAnimals;
            
            /*On parcourt le collection de cages*/
            for(Cage cage : listCages){

            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();

            	/*On parcourt le collection d'animaux*/
            	for(Animal animal : listAnimals){

            		/*Si le nom animal=nomAnimal alors on supprime l'animal*/
            		if(animal.getName().equals(name)){
            			System.out.println("Animal supprimé Nom = "+ animal.getName());
            			listAnimals.remove(animal);
            		}
            	}
            }
            return this.center;
    }
    
    /**
     * Method bound to calls on /find/byName/{name}
     */
    @GET
    @Path("/animals/find/byName/{name}")
    @Produces("application/xml")
    public Animal animalFindByName(@PathParam("name") String name){
    	/*Onrécupère l'ensemble des cages*/
    	Collection<Cage> listCages = this.center.getCages();

        Collection<Animal> listAnimals;

        
        /*On parcourt le collection de cages*/
        for(Cage cage : listCages){

        	/*On récupère l'ensemle des animaux de la cage*/
        	listAnimals = cage.getResidents();

        	/*On parcourt le collection d'animaux*/
        	for(Animal animal : listAnimals){

        		/*Si le nom de l'animal=animal_name alors on retourne l'animal*/
        		if(animal.getName().equals(name)){
        			try {
                        return center.findAnimalById(animal.getId());
                    } catch (AnimalNotFoundException e) {
                        throw new HTTPException(404);
                    }
        		}
        	}
        }
        return new Animal();
    }

    /**
     * Method bound to calls on /find/at/{position}
     */
    @GET
    @Path("/animals/find/at/{position}")
    @Produces("application/xml")
    public Animal animalAtPosition(@PathParam("position") String position){
    	/*On récupère la Latitude et la Longitude depuis le paramètre String : position
    	 * La Latitude et la Longitude sont séparées pas ";"*/
        String[] posLatLong = position.split("-");
        /*p est la position qui correspond aux coordonnées : posLatLong[0]=Latitude et posLatLong[1]=Longitude*/
        Position p= new Position(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]));
    	/*Onrécupère l'ensemble des cages*/
    	Collection<Cage> listCages = this.center.getCages();

        Collection<Animal> listAnimals;

        
        /*On parcourt le collection de cages*/
        for(Cage cage : listCages){
        	/*Si la position de la cage correspond à la position rechrchée alors on retourne l'animal de cette cage*/
        	if(cage.getPosition().equals(p)){
        		/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();

            	/*On parcourt le collection d'animaux*/
            	for(Animal animal : listAnimals){

            			try {
            				/*On retourne l'animal à la position donnée*/
                            return center.findAnimalById(animal.getId());
                        } catch (AnimalNotFoundException e) {
                            throw new HTTPException(404);
                        }
            	}
        	}
        }
    	return new Animal();
    }

    /**
     * Method bound to calls on /find/near/{position}
     */
    @GET
    @Path("/animals/find/near/{position}")
    @Produces("application/xml")
    public Animal animalNearPosition(@PathParam("position") String position){
    	/*On récupère la Latitude et la Longitude depuis le paramètre String : position
    	 * La Latitude et la Longitude sont séparées pas ";"*/
        String[] posLatLong = position.split("-");
        /*p est la position qui correspond aux coordonnées : posLatLong[0]=Latitude et posLatLong[1]=Longitude*/
        Position p= new Position(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]));
    	/*Onrécupère l'ensemble des cages*/
    	Collection<Cage> listCages = this.center.getCages();
    	
        Cage cageNear=null;
        
        Collection<Animal> listAnimals;

        /*Les varibles pour calculer les distances entre les positions*/
        double distance, distanceNear;
        
        /*On parcourt le collection de cages*/
        for(Cage cage : listCages){
            if(cageNear==null){
           		cageNear=cage;
          	}else{
          		/*La distance entre la cage actuelle et la position donnée*/
          		distance= Math.abs(Math.pow(cage.getPosition().getLatitude()-p.getLatitude(), 2)+Math.pow(cage.getPosition().getLongitude()-p.getLongitude(), 2));

          		/*La distance entre la cage suavgardée et la position donnée*/
          		distanceNear= Math.abs(Math.pow(cageNear.getPosition().getLatitude()-p.getLatitude(), 2)+Math.pow(cageNear.getPosition().getLongitude()-p.getLongitude(), 2));
          		
           		/*Si la cage ectuelle est plus proche de la position donnée, alors on sauvgarde cette cage*/
          		if(distance<distanceNear){
          			cageNear=cage;
           		}
           	}
        }
        /*On récupère l'ensemle des animaux de la cage la plus proche de la position donnée*/
    	listAnimals = cageNear.getResidents();

    	/*On parcourt le collection d'animaux*/
    	for(Animal animal : listAnimals){

    			try {
    				/*On retourne l'animal le plus proche de la position donnée*/
                    return center.findAnimalById(animal.getId());
                } catch (AnimalNotFoundException e) {
                    throw new HTTPException(404);
                }
    	}
    	return new Animal();
    }

    /**
     * Method bound to calls on /center/journey/from/{position}
     */
    @GET
    @Path("/animals/center/journey/from/{position}")
    @Produces("application/xml")
    public Center infosTrjet(@PathParam("position") String position){
    	// create one GraphHopper instance
    	GraphHopper hopper = new GraphHopperOSM().forServer();
    	
    	// where to store graphhopper files?
    	hopper.setDataReaderFile("dataFile.osm");
    	hopper.setGraphHopperLocation("data");
    	hopper.setEncodingManager(new EncodingManager("car"));

    	// now this can take minutes if it imports or a few seconds for loading
    	// of course this is dependent on the imported area
    	hopper.importOrLoad();


    	//URL Position
    	String[] posLatLong = position.split(";");
    	
    	GHRequest req = new GHRequest(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]), this.center.getPosition().getLatitude()	,this.center.getPosition().getLongitude()).
    	    setWeighting("fastest").
    	    setVehicle("car").
    	    setLocale(Locale.US);
    	GHResponse rsp = hopper.route(req);

    	// first check for errors
    	if(rsp.hasErrors()) {
    	   System.out.println(rsp.hasErrors());
    	}
		PathWrapper path = rsp.getBest();
		
		System.out.println("-> Distance = " + path.getDistance());
		System.out.println("-> Temps = " + path.getTime());
		
        return this.center;
    }
    
    /**
     * Method bound to calls on /animals/{animal_id}/wolf
     */
    
    @GET
    @Path("/animals/{id}/wolf")
    @Produces("application/xml")
    public Animal animalInfoWolfram(@PathParam("id") String animal_id){
    	try {
        	Animal animal= center.findAnimalById(UUID.fromString(animal_id));
            System.out.println("Animal trouvé :");
           	System.out.println("                 -> Nom : "+animal.getName());
           	System.out.println("                 -> Cage : "+animal.getCage());
           	System.out.println("                 -> Species : "+animal.getSpecies());
            return animal;
        } catch (AnimalNotFoundException e) {
    		System.out.println("--> Cet animal n'existe pas!");
            throw new HTTPException(404);
        }
    }
    
    /**
     * Method bound to calls on /animals/{name}/wolf
     */
    /*
    @GET
    @Path("/animals/{name}/wolf")
    @Produces("application/xml")
    public Animal animalInfoWolfraByName(@PathParam("name") String animal_name){
    	try {
        	Animal animal= center.findAnimalByName(animal_name);
            System.out.println("Animal trouvé :");
           	System.out.println("                 -> Nom : "+animal.getName());
           	System.out.println("                 -> Cage : "+animal.getCage());
           	System.out.println("                 -> Species : "+animal.getSpecies());
            return animal;
        } catch (AnimalNotFoundException e) {
    		System.out.println("--> Cet animal n'existe pas!");
            throw new HTTPException(404);
        }
    }*/
    
    
}
