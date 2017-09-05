package dados_instituicao;

//Aqui pode ser usado o strategy, pra decidir qual implementação de ColetorDados será instanciada
public class ColetorDadosFactory {

	private static ColetorDadosFactory factory = new ColetorDadosFactory();
	private ColetorDados coletorDados = new ColetorDadosImpl();
	
	public static ColetorDadosFactory getInstance() {
		return factory;
	}
	
	private ColetorDadosFactory() {
		
	}
	
	public ColetorDados getColetorInstance() {
		return coletorDados;
	}
}
