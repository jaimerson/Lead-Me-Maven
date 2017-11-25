/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabricas;

import com.mycompany.lead.me.CarregadorTelaLogin;
import com.mycompany.lead.me.CarregadorTelaLoginUFRN;
import extrator_dados.Extrator;
import extrator_dados.ExtratorUFRN;
import service.AlunoService;
import service.AlunoServiceUFRN;
import service.ComparadorMatrizDisciplina;
import service.ComparadorMatrizDisciplinaUFRN;
import service.MatriculaService;
import service.RequisitosService;
import service.RequisitosServiceUFRN;

/**
 *
 * @author rafao
 */
public class UFRNFactory implements AbstractFactory {

    @Override
    public AlunoService createAlunoService() {
        return new AlunoServiceUFRN();
    }

    @Override
    public RequisitosService createRequisitosService() {
        return new RequisitosServiceUFRN();
    }

    @Override
    public ComparadorMatrizDisciplina createComparadorMatrizDisciplina() {
        return new ComparadorMatrizDisciplinaUFRN();
    }

    @Override
    public Extrator createExtrator() {
        return new ExtratorUFRN();
    }

    @Override
    public CarregadorTelaLogin createCarregadorTelaLogin() {
        return new CarregadorTelaLoginUFRN();
    }

}
