/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacoes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class Dados {
    private List<Double> atributos = new ArrayList<>(); //max de 6 elem
    private String classe;

    public Dados(List<Double> atributos, String classe) {
        this.atributos = atributos;
        this.classe = classe;
    }

    public Dados() {
    }
    

    public List<Double> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Double> atributos) {
        this.atributos = atributos;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    public void setAtributos(Double d)
    {
        atributos.add(d);
    }
    
  
    
   
    
    
}

