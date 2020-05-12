/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Alexandre
 */
public class Neuronio {

       private int entrada;
       private double net, erro, i;
       private List<Double> pesos;

    public Neuronio(int qtdpesos) {
        pesos = new ArrayList<>();
        Random rand = new Random();
        
        
        for (int i = 0; i < qtdpesos; i++) {
            Double peso = rand.nextInt(5)-2.0;            
            this.pesos.add(peso); 
        }
        
    }

    
    public String pesos()
    {
        String s="";
        
        for (Double d: pesos)
          s+=d+" ";  
        
        return s;
    }
       
}
