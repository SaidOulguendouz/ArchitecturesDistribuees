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

    /*La fonction qui Recherche un animal par son nom*/
    public void find_animal_By_Name(String name) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/find/byName/"+name);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal()));
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
        /*Ajoute un animal*/
        //client.add_animal(new Animal("Bob", "amazon", "Arapaima gigas", UUID.randomUUID()));
        
        /*Modifie l'ensemble des animaux*/
        //client.edit_animals();
        
        /*Supprime l'ensemble des animaux*/
        //client.delete_animals();
        
        /*Crée l’animal identifié par {animal_id}*/
        //client.add_animal_By_Id(new Animal("Test22", "amazon", "Arapaima gigas", UUID.randomUUID()), "b590c595-e559-4153-a1d2-00446d87e200");
        
        /*Modifie l’animal identifié par {animal_id}*/
        //client.edit_animal_By_Id("b590c595-e559-4153-a1d2-00446d87e200");
        
        /*Supprime l’animal identifié par {animal_id}*/
        //client.delete_animal_By_Id("b590c595-e559-4153-a1d2-00446d87e200");
        
        /*Recherche d'un animal par son nom*/
       //client.find_animal_By_Name("Test22");
        
        /*Recherche d'un animal par position*/
       //client.find_animal_By_Position("49.305142d;1.2154067d");
       
       /*Recherche d'un animal près d’une position*/
      //client.find_animal_Near_Position("49.305d;1.2155357d");
        
        /*Récupération des info. Wolfram d’un animal*/
       //client.animal_Infos_Wolfram("b580c595-e559-4153-a1d2-00446d87e200");
        
        /*Récupération des info. Du trajet depuis une position GPS jusqu’à votre centre en utilisant le service Graphhopper*/
       client.animal_Infos_Trajet("40.305d;1.0155357d");
    }
}
