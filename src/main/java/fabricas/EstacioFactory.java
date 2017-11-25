/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabricas;

import com.mycompany.lead.me.CarregadorTelaLogin;
import extrator_dados.Extrator;
import extrator_dados.ExtratorEstacio;
import service.AlunoService;
import service.ComparadorMatrizDisciplina;
import service.ComparadorMatrizDisciplinaEstacio;
import service.MatriculaService;
import service.RequisitosService;
import service.RequisitosServiceEstacio;

/**
 *
 * @author rafao
 */
public class EstacioFactory implements AbstractFactory{

    @Override
    public RequisitosService createRequisitosService() {
        return new RequisitosServiceEstacio();
    }


    @Override
    public ComparadorMatrizDisciplina createComparadorMatrizDisciplina() {
        return new ComparadorMatrizDisciplinaEstacio();
    }

    @Override
    public Extrator createExtrator() {
        return new ExtratorEstacio();
    }

    @Override
    public CarregadorTelaLogin createCarregadorTelaLogin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AlunoService createAlunoService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
