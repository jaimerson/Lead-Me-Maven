package dados_instituicao;

//Aqui pode ser usado o strategy, pra decidir qual implementa��o de ColetorDados ser� instanciada
public class ColetorDadosFactory {

    private static ColetorDadosFactory factory = new ColetorDadosFactory();
    private ColetorDadosFacade coletorDados = new ColetorDadosFacadeImpl();

    public static ColetorDadosFactory getInstance() {
        return factory;
    }

    private ColetorDadosFactory() {

    }

    public ColetorDadosFacade getColetorInstance() {
        return coletorDados;
    }
}
