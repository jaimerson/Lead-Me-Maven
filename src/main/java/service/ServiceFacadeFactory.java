package service;

public class ServiceFacadeFactory {
    //Aqui pode ser criado o objeto usando strategy
    private ServiceFacade service = new ServiceFacadeImpl();
    private static ServiceFacadeFactory factory = new ServiceFacadeFactory();
    
    public static ServiceFacadeFactory getInstance() {
        return factory;
    }
    
    private ServiceFacadeFactory(){
        
    }
    
    public ServiceFacade getServiceInstance(){
        return service;
    }
}
