package tp.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.shapes.GHPoint;

import tp.model.Animal;
import tp.model.AnimalNotFoundException;
import tp.model.Cage;
import tp.model.Center;
import tp.model.Position;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class MyServiceTP implements Provider<Source> {

    public final static String url = "http://127.0.0.1:8084/";

    public static void main(String args[]) {
        Endpoint e = Endpoint.create(HTTPBinding.HTTP_BINDING, new MyServiceTP());

        e.publish(url);
        System.out.println("Service started, listening on " + url);
        // pour arrÃªter : e.stop();
    }

    private JAXBContext jc;

    @javax.annotation.Resource(type = Object.class)
    protected WebServiceContext wsContext;

    private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");

    public MyServiceTP() {
        try {
            jc = JAXBContext.newInstance(Center.class, Cage.class, Animal.class, Position.class);
        } catch (JAXBException je) {
            System.out.println("Exception " + je);
            throw new WebServiceException("Cannot create JAXBContext", je);
        }

        // Fill our center with some animals
        /*Cage rouen = new Cage(
                "Cage de Rouen",
                new Position( 49.443889, 1.103333),
                20,
                new LinkedList<>()
        );
        
        Cage paris = new Cage(
                "Cage de Paris",
                new Position( 48.856578 , 2.351828),
                30,
                new LinkedList<>()
        );
        
        Cage somalie = new Cage(
                "Cage de Somalie",
                new Position(  2.333333  , 48.85),
                26,
                new LinkedList<>()
        );
        
        Cage bihorel = new Cage(
                "Cage de Bihorel",
                new Position(   49.455278  , 1.116944),
                18,
                new LinkedList<>()
        );
        
        Cage londres = new Cage(
                "Cage de Londres",
                new Position(  51.504872  ,  -0.07857),
                35,
                new LinkedList<>()
        );
        
        Cage canada = new Cage(
                "Cage de Canada",
                new Position(  43.2  ,  -80.38333),
                45,
                new LinkedList<>()
        );
        
        Cage porto_Vecchio = new Cage(
                "Cage de Porto­Vecchio",
                new Position(  41.5895241 , 9.2627),
                28,
                new LinkedList<>()
        );
        
        Cage montreux = new Cage(
                "Cage de Montreux",
                new Position(  46.4307133 , 6.9113575),
                34,
                new LinkedList<>()
        );
        
        Cage villers_Bocage = new Cage(
                "Cage de Villers­Bocage",
                new Position(  50.0218 , 2.3261),
                23,
                new LinkedList<>()
        );*/
        
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

        center.getCages().addAll(Arrays.asList(usa, amazon/*rouen, paris, somalie, bihorel, londres, canada, porto_Vecchio, montreux, villers_Bocage*/));
    }

    public Source invoke(Source source) {
        MessageContext mc = wsContext.getMessageContext();
        String path = (String) mc.get(MessageContext.PATH_INFO);
        String method = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);

        // determine the targeted ressource of the call
        try {
            // no target, throw a 404 exception.
            if (path == null) {
                throw new HTTPException(404);
            }
            // "/animals" target - Redirect to the method in charge of managing this sort of call.
            else if (path.startsWith("animals")) {
                String[] path_parts = path.split("/");
                switch (path_parts.length){
                    case 1 :
                        return this.animalsCrud(method, source);
                    case 2 :
                        return this.animalCrud(method, source, path_parts[1]);
                    case 3 :
                    	return this.animalInfoWolfram(method, source, path_parts[1]);
                    default:
                        throw new HTTPException(404);
                }
            }
            else if (path.startsWith("find")) {
            		String[] path_parts = path.split("/");
                    switch (path_parts.length){
                        case 3 :
                        	switch (path_parts[1]){
                        		case "byName" :
                                    return this.animalFindByName(method, source, path_parts[2]);
                        		case "at" :
                        			return this.animalAtPosition(method, source, path_parts[2]);
                        		case "near" :
                        			return this.animalNearPosition(method, source, path_parts[2]);
                        		default :
                        			throw new HTTPException(404);
                        	}
                        default:
                            throw new HTTPException(404);
                    }
                //throw new HTTPException(503);
            }
            else if(path.startsWith("cage")){
            	String[] path_parts = path.split("/");
                    switch (path_parts[1]){
                    	case "add" :
                    		return this.cageCrud(method, source);
                    	case "delete" :
                    		return this.cageCrud(method, source);
                    	default :
                    		throw new HTTPException(404);
                    }
            }
            else if(path.startsWith("center")){
            	String[] path_parts = path.split("/");
                switch (path_parts.length){
                    case 4 :
                    	return this.infosTrjet(method, source, path_parts[3]);
                    default:
                        throw new HTTPException(404);
                }	
            }
            else if ("coffee".equals(path)) {
                throw new HTTPException(418);
            }
            else {
                throw new HTTPException(404);
            }
        } catch (JAXBException e) {
            throw new HTTPException(500);
        }
    }

    /**
     * Method bound to calls on /animals/{something}
     */
    private Source animalCrud(String method, Source source, String animal_id) throws JAXBException {
    	/*Retourne l’animal identifié par {animal_id}*/
        if("GET".equals(method)){
            try {
                return new JAXBSource(this.jc, center.findAnimalById(UUID.fromString(animal_id)));
            } catch (AnimalNotFoundException e) {
                throw new HTTPException(404);
            }
        }
        
        /*Crée l’animal identifié par {animal_id}*/
        else if("POST".equals(method)){
        	/*On récupère les informations de l'animal depuis la source*/
            Animal animal = unmarshalAnimal(source);
            animal.setId(UUID.fromString(animal_id));
            /*On récupère la cage de l'animal, puis on insert l'animal dans cette cage*/
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        
        /*Modifie l’animal identifié par {animal_id}*/
        else if("PUT".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
           Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            Animal animal;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it2=listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it2.hasNext()){
            		animal= it2.next();
            		/*Si l'id animal=animal_id alors on modifie l'animal*/
            		if(animal.getId().equals(UUID.fromString(animal_id))){
            			animal.setName("Animal Modifié");
            		}
            	}
            }
            return new JAXBSource(this.jc, this.center);
        }
        
        /*Supprime l’animal identifié par {animal_id}*/	
        else if("DELETE".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it3;
            Animal animal;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it3 = listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it3.hasNext()){
            		animal= it3.next();
            		/*Si l'id animal=animal_id alors on supprime l'animal*/
            		if(animal.getId().equals(UUID.fromString(animal_id))){
            			System.out.println("Animal supprimé i ="+ UUID.fromString(animal_id));
            			listAnimals.remove(animal);
            		}
            	}
            }
            return new JAXBSource(this.jc, this.center);
        }
        else{
            throw new HTTPException(405);
        }
    }

    /**
     * Method bound to calls on /cage/{something}
     */
    private Source cageCrud(String method, Source source) throws JAXBException {
    	/*Retourne l'ensemble des cages*/
        if("GET".equals(method)){
            return new JAXBSource(this.jc, center.getCages());
        }
        
        /*Crée une nouvelle cage*/
        else if("POST".equals(method)){
        	/*On récupère la cage depuis la source*/
            Cage cage = unmarshalCage(source);
            /*On insère la nouvelle cage*/
            this.center.getCages().add(cage);
            return new JAXBSource(this.jc, this.center);
        }
        
        /*Supprimer une cage*/	
        else if("PUT".equals(method)){
        	/*On récupère la cage depuis la source*/
            Cage cage = unmarshalCage(source);
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cageCollection;
            Iterator<Cage> it = listCages.iterator();
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cageCollection = it.next();
            		if(cageCollection.getPosition().equals(cage.getPosition())){
            			System.out.println("Cage supprimée Position = "+ cage.getPosition().getLatitude()+" ; "+cage.getPosition().getLongitude());
            			listCages.remove(cageCollection);
            		}
            }
            return new JAXBSource(this.jc, this.center);
        }
        else{
            throw new HTTPException(405);
        }
    }

    /**
     * Method bound to calls on /animals
     */
    private Source animalsCrud(String method, Source source) throws JAXBException {
    	/*Retourne l'ensemble des animaux du centre*/
        if("GET".equals(method)){
            return new JAXBSource(this.jc, this.center);
        }
        
        /*Ajoute un animal dans un centre*/
        else if("POST".equals(method)){
            Animal animal = unmarshalAnimal(source);
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        
        /*Modifie l'ensemble des animaux*/
        else if("PUT".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
           Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            //int i=1;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	
            	/*On parcourt le collection d'animaux*/
            	for(Animal animal : listAnimals){
            		//On modifie chaque animal
            		animal.setName(animal.getName()+" Modifié");
            	}
            }
            return new JAXBSource(this.jc, this.center);
        }
        	
        /*Supprime l'ensemble des animaux*/
        else if("DELETE".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	/*On supprime tous les animaux de la cage*/
            	listAnimals.clear();
            }
            return new JAXBSource(this.jc, this.center);
        }
        else{
        	throw new HTTPException(405);
        }
        	
    }
    
    /**
     * Method bound to calls on /find/byName/{name}
     */
    private Source animalFindByName(String method, Source source, String animal_name) throws JAXBException {
        if("GET".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            Animal animal;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it2=listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it2.hasNext()){
            		animal= it2.next();
            		/*Si le nom de l'animal=animal_name alors on retourne l'animal*/
            		if(animal.getName().equals(animal_name)){
            			try {
                            return new JAXBSource(this.jc, center.findAnimalById(animal.getId()));
                        } catch (AnimalNotFoundException e) {
                            throw new HTTPException(404);
                        }
            		}
            	}
            }
            return null;
        }       
        else{
            throw new HTTPException(405);
        }
    }
    
    /**
     * Method bound to calls on /find/at/{position}
     */
    private Source animalAtPosition(String method, Source source, String position) throws JAXBException {
        if("GET".equals(method)){
        	/*On récupère la Latitude et la Longitude depuis le paramètre String : position
        	 * La Latitude et la Longitude sont séparées pas ";"*/
            String[] posLatLong = position.split(";");
            /*p est la position qui correspond aux coordonnées : posLatLong[0]=Latitude et posLatLong[1]=Longitude*/
            Position p= new Position(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]));
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            Animal animal;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*Si la position de la cage correspond à la position rechrchée alors on retourne l'animal de cette cage*/
            	if(cage.getPosition().equals(p)){
            		/*On récupère l'ensemle des animaux de la cage*/
                	listAnimals = cage.getResidents();
                	it2=listAnimals.iterator();
                	/*On parcourt le collection d'animaux*/
                	while(it2.hasNext()){
                		animal= it2.next();
                			try {
                				/*On retourne l'animal à la position donnée*/
                                return new JAXBSource(this.jc, center.findAnimalById(animal.getId()));
                            } catch (AnimalNotFoundException e) {
                                throw new HTTPException(404);
                            }
                	}
            	}
            }
            return null;
        }       
        else{
            throw new HTTPException(405);
        }
    }

    /**
     * Method bound to calls on /find/at/{position}
     */
    private Source animalNearPosition(String method, Source source, String position) throws JAXBException {
        if("GET".equals(method)){
        	/*On récupère la Latitude et la Longitude depuis le paramètre String : position
        	 * La Latitude et la Longitude sont séparées pas ";"*/
            String[] posLatLong = position.split(";");
            /*p est la position qui correspond aux coordonnées : posLatLong[0]=Latitude et posLatLong[1]=Longitude*/
            Position p= new Position(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]));
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage, cageNear=null;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            Animal animal;
            /*Les varibles pour calculer les distances entre les positions*/
            double distance, distanceNear;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
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
        	it2=listAnimals.iterator();
        	/*On parcourt le collection d'animaux*/
        	while(it2.hasNext()){
        		animal= it2.next();
        			try {
        				/*On retourne l'animal le plus proche de la position donnée*/
                        return new JAXBSource(this.jc, center.findAnimalById(animal.getId()));
                    } catch (AnimalNotFoundException e) {
                        throw new HTTPException(404);
                    }
        	}
        }       
        else{
            throw new HTTPException(405);
        }
        return null;
    }

    /**
     * Method bound to calls on /center/journey/from/{position}
     */
    private Source infosTrjet(String method, Source source, String position) throws JAXBException {
        if("GET".equals(method)){
        	/*
        	//Position de notre centre
        	GHPoint a = new GHPoint(this.center.getPosition().getLatitude()	,this.center.getPosition().getLongitude());
        	
            //p est la position qui correspond aux coordonnées : posLatLong[0]=Latitude et posLatLong[1]=Longitude
        	GHPoint b = new GHPoint(Double.parseDouble(posLatLong[0]), Double.parseDouble(posLatLong[1]));
            
            GraphHopper hopper = new GraphHopper().forDesktop();
            hopper.setInMemory();
            hopper.setDataReaderFile("dataFile.txt");
            hopper.setGraphHopperLocation("data");
            hopper.setEncodingManager(new EncodingManager("car"));
            
            hopper.importOrLoad();
            
            GHRequest req = new GHRequest(a,b);
    		GHResponse rsp = hopper.route(req);
    		for(Throwable thr : rsp.getErrors()){
    			System.out.println(thr);
    		}*/
        	
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
        }       
        else{
            throw new HTTPException(405);
        }
        return null;
    }
    /**
     * Method bound to calls on /animals/{animal_id}/wolf
     */
    private Source animalInfoWolfram(String method, Source source, String animal_id) throws JAXBException {
    	/*Récupération des info. Wolfram d’un animal identifié par {animal_id}*/
        if("GET".equals(method)){
            try {
            	Animal animal= center.findAnimalById(UUID.fromString(animal_id));
                System.out.println("Animal trouvé :");
               	System.out.println("                 -> Nom : "+animal.getName());
               	System.out.println("                 -> Cage : "+animal.getCage());
               	System.out.println("                 -> Species : "+animal.getSpecies());
                return new JAXBSource(this.jc, animal);
            } catch (AnimalNotFoundException e) {
        		System.out.println("--> Cet animal n'existe pas!");
                throw new HTTPException(404);
            }
        }
        else{
            throw new HTTPException(405);
        }
    }
    private Animal unmarshalAnimal(Source source) throws JAXBException {
        return (Animal) this.jc.createUnmarshaller().unmarshal(source);
    }
    private Cage unmarshalCage(Source source) throws JAXBException {
        return (Cage) this.jc.createUnmarshaller().unmarshal(source);
    }
}
