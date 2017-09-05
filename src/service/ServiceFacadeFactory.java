package service;

public class ServiceFacadeFactory {
	private ServiceFacade service = new ServiceFacadeImpl();
	
	public ServiceFacade getInstance() {
		return service;
	}
}
