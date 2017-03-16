package tp.rest;

import tp.model.Animal;
import tp.model.Cage;
import tp.model.Center;
import tp.model.Position;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class MyClient {
    private Service service;
    private JAXBContext jc;

    private static final QName qname = new QName("", "");
    private static final String url = "http://127.0.0.1:8084";

    public MyClient() {
        try {
            jc = JAXBContext.newInstance(Center.class, Cage.class, Animal.class, Position.class);
        } catch (JAXBException je) {
            System.out.println("Cannot create JAXBContext " + je);
        }
    }
    
    /*La fonction qui permet d'ajouter une cage*/
    public void add_cage(Cage c) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/cage/add");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        Source result = dispatcher.invoke(new JAXBSource(jc, c));
        printSource(result);
    }
    
    /*La fonction qui permet de supprimer une cage*/
    public void delete_cage(Cage c) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/cage/delete");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc, c));
        printSource(result);
    }

    /*La fonction qui permet d'afficher l'ensemble des animaux*/
    public void get_animals() throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }

    /*La fonction qui permet d'ajouter un animal en tant qu'objet donné en paramètre*/
    public void add_animal(Animal animal) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        Source result = dispatcher.invoke(new JAXBSource(jc, animal));
        printSource(result);
    }

    /*La fonction qui permet de Modifie l'ensemble des animaux*/
    public void edit_animals() throws JAXBException{
    	service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc,new Animal()));
        printSource(result);
    }

    /*La fonction qui permet de Modifie l'ensemble des animaux d'une cage*/
    public void edit_animals_Cage(Animal animal) throws JAXBException{
    	service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/cage/edit");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc,animal));
        printSource(result);
    }

    /*La fonction qui permet de Supprime l'ensemble des animaux*/
    public void delete_animals() throws JAXBException{
    	service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
        Source result = dispatcher.invoke(new JAXBSource(jc,new Animal()));
        printSource(result);
    }

    /*La fonction qui permet de Supprime l'ensemble des animaux d'une cage*/
    public void delete_animals_Cage(String cageName) throws JAXBException{
    	service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/cage/deleteAnimals/"+cageName);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc,new Cage()));
        printSource(result);
    }

    /*La fonction qui permet de Crée l’animal identifié par {id}*/
    public void add_animal_By_Id(Animal animal, String id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        Source result = dispatcher.invoke(new JAXBSource(jc, animal));
        printSource(result);
    }

    /*La fonction qui permet de Modifie l’animal identifié par {id}*/
    public void edit_animal_By_Id(String id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }

    /*La fonction qui permet de Supprime l’animal identifié par {id}*/
    public void delete_animal_By_Id(String id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }

    /*La fonction qui permet de Supprime l’animal nomé animalName*/
    public void delete_animal_By_Name(String animalName) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animalDelete");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Animal a=new Animal();
        a.setName(animalName);
        Source result = dispatcher.invoke(new JAXBSource(jc, a));
        printSource(result);
    }

    /*La fonction qui Recherche un animal par son nom*/
    public void find_animal_By_Name(String name) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/find/byName/");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Animal a=new Animal();
        a.setName(name);
        Source result = dispatcher.invoke(new JAXBSource(jc, a));
        printSource(result);
    }
    
    /*La fonction qui Recherche un animal par position*/
    public void find_animal_By_Position(String position) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/find/at/"+position);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }
    
    /*La fonction qui Recherche un animal près d’une position*/
    public void find_animal_Near_Position(String position) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/find/near/"+position);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }
    
    /*La fonction qui Récupère des info. Wolfram d’un animal*/
    public void animal_Infos_Wolfram(String id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id+"/wolf");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }
    
    /*La fonction qui Récupère des info. Wolfram d’un animal*/
    public void animal_Infos_Wolfram_byName(String name) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/wolf");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Animal a=new Animal();
        a.setName(name);
        Source result = dispatcher.invoke(new JAXBSource(jc, a));
        printSource(result);
    }
    
    /*La fonction qui Récupère des info. Du trajet depuis une position GPS jusqu’à votre centre en utilisant le service Graphhopper*/
    public void animal_Infos_Trajet(String position) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/center/journey/from/"+position);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
        printSource(result);
    }
    
    public void printSource(Source s) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(s, new StreamResult(System.out));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) throws Exception {
        MyClient client = new MyClient();
        //Ajouter une cage
        //client.add_cage(new Cage("Cage de Rouen", new Position( 49.443889, 1.103333), 20, new LinkedList<>()));
        
        //Supprimer une cage
        /*Cage g = new Cage();
        g.setPosition(new Position(49.443889, 1.103333));
        client.delete_cage(g);*/
        
        //Ajoute un animal
        //client.add_animal(new Animal("Bob", "amazon", "Arapaima gigas", UUID.randomUUID()));
        
        //Modifie l'ensemble des animaux : la modification rajoute "Modifié" au nom de chaque animal
        //client.edit_animals();
        
        //Supprime l'ensemble des animaux
        //client.delete_animals();
        
        //Crée l’animal identifié par {animal_id}
        //client.add_animal_By_Id(new Animal("Test22", "amazon", "Arapaima gigas", UUID.randomUUID()), "b590c595-e559-4153-a1d2-00446d87e200");
        
        //Modifie l’animal identifié par {animal_id}
        //client.edit_animal_By_Id("b590c595-e559-4153-a1d2-00446d87e200");
        
        //Supprime l’animal identifié par {animal_id}
        //client.delete_animal_By_Id("b590c595-e559-4153-a1d2-00446d87e200");
        
        //Recherche d'un animal par son nom
       //client.find_animal_By_Name("Test22");
        
        /*Recherche d'un animal par position*/
       //client.find_animal_By_Position("49.305142d;1.2154067d");
       
       //Recherche d'un animal près d’une position
      //client.find_animal_Near_Position("49.305d;1.2155357d");
        
        //Récupération des info. Wolfram d’un animal
       //client.animal_Infos_Wolfram("b580c595-e559-4153-a1d2-00446d87e200");
        
        //Récupération des info. Du trajet depuis une position GPS jusqu’à votre centre en utilisant le service Graphhopper
       //client.animal_Infos_Trajet("40.305d;1.0155357d");
        
        /*****************************************************************************************************/
        /********************************************** Scénario *********************************************/
        //Affichez l'ensemble des animaux
        //client.get_animals();
        
        //• Supprimez touts les animaux
        //client.delete_animals();
        
        //• Affichez l'ensemble des animaux
        //client.get_animals();
        
        //• Ajoutez un Panda à Rouen (Latitude : 49.443889 ; Longitude : 1.103333)
        //client.add_cage(new Cage("Cage de Rouen",new Position( 49.443889, 1.103333),20,new LinkedList<>()));
        client.add_animal(new Animal("Panda", "Cage de Rouen", "Rouen", UUID.randomUUID()));
        
        //• Ajoutez un Hocco unicorne à Paris (Latitude : 48.856578 ; Longitude : 2.351828)
        //client.add_cage(new Cage("Cage de Paris",new Position( 48.856578 , 2.351828),30,new LinkedList<>()));
       client.add_animal(new Animal("Hocco unicorne", "Cage de Paris", "Paris", UUID.randomUUID()));
        
        //• Affichez tous les animaux
        client.get_animals();
        
        //• Modifiez l'ensemble des animaux par un Lagotriche à queue jaune à Rouen (Latitude :49.443889 ; Longitude : 1.103333)
        Animal a=new Animal("Lagotriche à queue jaune", "Cage de Rouen", "Rouen", UUID.randomUUID());
        client.edit_animals_Cage(a);
        
        //• Affichez tous les animaux
        client.get_animals();
        
        //• Ajoutez une Océanite de Matsudaira en Somalie (Latitude : 2.333333 ; Longitude : 48.85)
        //client.add_cage(new Cage("Cage de Somalie",new Position(  2.333333  , 48.85),26,new LinkedList<>()));
        client.add_animal(new Animal("Océanite de Matsudaira", "Cage de Somalie", "Somalie", UUID.randomUUID()));
        
        //• Ajoutez un Ara de Spix à Rouen (Latitude : 49.443889 ; Longitude : 1.103333)
        client.add_animal(new Animal("Ara de Spix", "Cage de Rouen", "Rouen", UUID.randomUUID()));
        
        //• Ajoutez un Galago de Rondo à Bihorel (Latitude : 49.455278 ; Longitude : 1.116944)
        //client.add_cage(new Cage("Cage de Bihorel",new Position(   49.455278  , 1.116944),18,new LinkedList<>()));
        client.add_animal(new Animal("Galago de Rondo", "Cage de Bihorel", "Bihorel", UUID.randomUUID()));
        
        //• Ajoutez une Palette des Sulu à Londres (Latitude : 51.504872 ; Longitude : ­0.07857)
        //client.add_cage(new Cage("Cage de Londres",new Position(  51.504872  ,  -0.07857),35,new LinkedList<>()));
        client.add_animal(new Animal("Palette des Sulu", "Cage de Londres", "Londres", UUID.randomUUID()));
        
        //• Ajoutez un Kouprey à Paris (Latitude : 48.856578 ; Longitude : 2.351828)
        client.add_animal(new Animal("Kouprey", "Cage de Paris", "Paris", UUID.randomUUID()));
        
        //• Ajoutez un Tuit­tuit à Paris (Latitude : 48.856578 ; Longitude : 2.351828)
        client.add_animal(new Animal("Tuit­tuit", "Cage de Paris", "Paris", UUID.randomUUID()));
        
        //• Ajoutez une Saïga au Canada (Latitude : 43.2 ; Longitude : ­80.38333)
        //client.add_cage(new Cage("Cage de Canada",new Position(  43.2  ,  -80.38333),45,new LinkedList<>()));
        client.add_animal(new Animal("Saïga", "Cage de Canada", "Canada", UUID.randomUUID()));
        
        //• Ajoutez un Inca de Bonaparte à Porto­Vecchio (Latitude : 41.5895241 ; Longitude : 9.2627)
        //client.add_cage(new Cage("Cage de Porto­Vecchio",new Position(  41.5895241 , 9.2627),28,new LinkedList<>()));
        client.add_animal(new Animal("Inca de Bonaparte", "Cage de Porto­Vecchio", "Porto­Vecchio", UUID.randomUUID()));
        
        //• Affichez l'ensemble des animaux
        client.get_animals();
        
        //• Ajoutez un Râle de Zapata à Montreux (Latitude : 46.4307133; Longitude : 6.9113575)
        //client.add_cage(new Cage("Cage de Montreux",new Position(  46.4307133 , 6.9113575),34,new LinkedList<>()));
        client.add_animal(new Animal("Râle de Zapata", "Cage de Montreux", "Montreux", UUID.randomUUID()));
        
        //• Ajoutez un Rhinocéros de Java à Villers­Bocage (Latitude : 50.0218 ; Longitude : 2.3261)
        //client.add_cage(new Cage("Cage de Villers­Bocage",new Position(  50.0218 , 2.3261),23, new LinkedList<>()));
        client.add_animal(new Animal("Rhinocéros de Java", "Cage de Villers­Bocage", "Villers­Bocage", UUID.randomUUID()));
        
        //• Ajoutez 101 Dalmatiens dans une cage aux USA
        client.add_animal(new Animal("101 Dalmatiens", "usa", "Chipmunk", UUID.randomUUID()));
        
        //• Affichez l'ensemble des animaux
        client.get_animals();
        
        //• Supprimez tous les animaux de Paris
        client.delete_animals_Cage("Paris");
        
        //• Affichez l'ensemble des animaux
        client.get_animals();
        
        //• Recherchez le Galago de Rondo
        client.find_animal_By_Name("Galago de Rondo");
        
        //• Supprimez le Galago de Rondo
        client.delete_animal_By_Name("Galago de Rondo");
        
        //• Supprimez à nouveau le Galago de Rondo
        client.delete_animal_By_Name("Galago de Rondo");
        
        //• Affichez l'ensemble des animaux
        client.get_animals();
        
        //• Affichez les animaux située près de Rouen
        client.find_animal_Near_Position("49.443889;1.103333");
        
        //• Affichez les animaux à Rouen
        
        //• Affichez les informations Wolfram Alpha du Saïga
        client.animal_Infos_Wolfram_byName("Saïga");
        
        //• Affichez les informations Wolfram Alpha de l'Ara de Spix
        client.animal_Infos_Wolfram_byName("Ara de Spix");
        
        //• Affichez le trajet jusqu'au centre de Somalie
        client.animal_Infos_Trajet("2.333333;48.85");
        
        //• Affichez le trajet jusqu'au centre de Londres
        client.animal_Infos_Trajet("51.504872;-0.07857");
        
        //• Supprimez tous les animaux
        client.delete_animals();
        
        //• Affichez l'ensemble des animaux
        client.get_animals();
    }
}
