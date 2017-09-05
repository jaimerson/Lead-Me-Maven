package service;

public class ServiceFacadeFactory {

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
