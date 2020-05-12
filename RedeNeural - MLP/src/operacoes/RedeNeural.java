package operacoes;

import java.util.ArrayList;
import java.util.List;

public class RedeNeural {
    
    private List<Dados> Ldados;
    private List<Camada> Lcamadas;
    
    private double erro;
    private String fa;
    private int txA, qtIt, qtCo;

    public RedeNeural(List<Dados> Ldados, double erro, String fa, int txA, int qtIt, int qtCo) {
        this.Ldados = Ldados;
        this.erro = erro;
        this.fa = fa;
        this.txA = txA;
        this.qtIt = qtIt;
        this.qtCo = qtCo;
        inicializaCamadas();        
    }  
        
    private int getQtSaida()
    {   
        List<String> L = new ArrayList();
        
        for(int i=0; i<Ldados.size(); i++)
        {
           if(!L.contains(Ldados.get(i).getClasse()))
               L.add(Ldados.get(i).getClasse());
        }
        
        return L.size();
    }
    
    public void inicializaCamadas()
    {
        int qtdNeuronios = (int)(Ldados.size()+getQtSaida())/2;
        Lcamadas = new ArrayList(); 
        
        for (int i = 0; i < qtCo; i++) {
            Camada c = new Camada(qtdNeuronios,Ldados.size());
            Lcamadas.add(c);
            
        }
    }
    
    
    public void treinar()
    {
        int cont = 0;        
        
        for (Camada c: Lcamadas)
        {    
            System.out.println(cont++);
            for (Neuronio n: c.getLneuronio())
                System.out.println(n.pesos());
            System.out.println(" ");
        }
    }
    
    private int getMaior()
    {
        return 1;
    }
    
    private int getMenor()
    {
        return 1;
    }
    
}
