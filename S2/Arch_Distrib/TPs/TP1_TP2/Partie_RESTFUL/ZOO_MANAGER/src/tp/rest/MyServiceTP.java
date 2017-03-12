package tp.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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

        center.getCages().addAll(Arrays.asList(usa, amazon));
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

                        		case "near" :
                        			
                        		default :
                        			throw new HTTPException(404);
                        	}
                        default:
                            throw new HTTPException(404);
                    }
                //throw new HTTPException(503);
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
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it2=listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it2.hasNext()){
            		Animal animal= it2.next();
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
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it3 = listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it3.hasNext()){
            		Animal animal= it3.next();
            		/*Si l'id animal=animal_id alors on supprime l'animal*/
            		if(animal.getId().equals(UUID.fromString(animal_id))){
            			System.out.println("Animal supprimer i ="+ UUID.fromString(animal_id));
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
            int i=1;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it2=listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it2.hasNext()){
            		/*On modifie chaque animal*/
            		it2.next().setName("Animale"+i);
            		i++;
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
            	listAnimals.remove(listAnimals);
            }
            return new JAXBSource(this.jc, this.center);
        }
        else{
        	throw new HTTPException(405);
        }
        	
    }
    
    /**
     * Method bound to calls on /find/byName/{something}
     */
    private Source animalFindByName(String method, Source source, String animal_name) throws JAXBException {
        if("GET".equals(method)){
        	/*Onrécupère l'ensemble des cages*/
        	Collection<Cage> listCages = this.center.getCages();
            Cage cage;
            Collection<Animal> listAnimals;
            Iterator<Cage> it = listCages.iterator();
            Iterator<Animal> it2;
            
            /*On parcourt le collection de cages*/
            while(it.hasNext()){
            	cage = it.next();
            	/*On récupère l'ensemle des animaux de la cage*/
            	listAnimals = cage.getResidents();
            	it2=listAnimals.iterator();
            	/*On parcourt le collection d'animaux*/
            	while(it2.hasNext()){
            		Animal animal= it2.next();
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

    private Animal unmarshalAnimal(Source source) throws JAXBException {
        return (Animal) this.jc.createUnmarshaller().unmarshal(source);
    }
}
