/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacoes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class TrataDados {
    
    public static List<Dados> leDados(String caminho) throws FileNotFoundException, IOException
    {
       BufferedReader csvReader = new BufferedReader(new FileReader(caminho));
       String linha;
       List<Dados> LD = new ArrayList<>();
       
       linha = csvReader.readLine();
       while ((linha = csvReader.readLine()) != null) 
       {
            String[] data = linha.split(",");
             Dados d = new Dados();
            
            for(int i = 0; i < data.length-1; i++)
            {
                d.setAtributos(Double.parseDouble(data[i]));                
            }
            d.setClasse(data[data.length-1]);           
            
            LD.add(d);
        }
        csvReader.close();         
        
        return LD;
    }
    
    
}
