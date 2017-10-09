/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minerador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import static minerador.AssociadorWeka.ARQUIVO_ASSOCIACAO;
import static minerador.AssociadorWeka.NUMERO_REGRAS;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.core.Instances;

/**
 *
 * @author rafao
 */
public class AprioriStrategy implements AssociacaoStrategy{

    @Override
    public List<AssociationRule> rodarAssociacao() throws FileNotFoundException, IOException, Exception {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(ARQUIVO_ASSOCIACAO));
        Instances data = new Instances(reader);
        reader.close();

        Apriori apriori = new Apriori();
        apriori.setMinMetric(0.7);
        apriori.setNumRules(NUMERO_REGRAS);
        apriori.buildAssociations(data);
        return apriori.getAssociationRules().getRules();
    }
    
}
