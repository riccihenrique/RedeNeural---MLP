
package operacoes;

import java.util.ArrayList;
import java.util.List;


public class Camada {
    private List<Neuronio> Lneuronio;

    public Camada(int qtdNeuronios, int qtdCamadaAnt) {
        Lneuronio = new ArrayList<>();
       
        for (int j = 0; j < qtdNeuronios; j++) 
        {               
                Neuronio  n = new Neuronio(j==0?qtdCamadaAnt:qtdNeuronios); 
                Lneuronio.add(n);
        }
        
    }

    public List<Neuronio> getLneuronio() {
        return Lneuronio;
    }
    
    
    
    
}
