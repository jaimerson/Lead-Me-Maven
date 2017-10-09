/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minerador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import weka.associations.AssociationRule;

/**
 *
 * @author rafao
 */
public interface AssociacaoStrategy {
    public List<AssociationRule> rodarAssociacao() throws FileNotFoundException, IOException, Exception;
}
