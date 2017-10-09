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
import weka.associations.AssociationRule;
import weka.associations.FPGrowth;
import weka.core.Instances;

/**
 *
 * @author rafao
 */
public class FPGrowthStrategy implements AssociacaoStrategy{

    @Override
    public List<AssociationRule> rodarAssociacao() throws FileNotFoundException, IOException, Exception {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(ARQUIVO_ASSOCIACAO));
        Instances data = new Instances(reader);
        reader.close();

        FPGrowth growth = new FPGrowth();
        growth.setMinMetric(0.7);
        growth.setNumRulesToFind(NUMERO_REGRAS);
        growth.buildAssociations(data);
        return growth.getAssociationRules().getRules();
    }
    
}
