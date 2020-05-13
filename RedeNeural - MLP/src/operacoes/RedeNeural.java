package operacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedeNeural {
    
    private List<Dados> Ldados;
    //private List<Camada> Lcamadas;
    
    private List<Neuronio> Lco, Lsaida;
    private double [][] pesosOculta, pesosSaida;
    private int [][] mDesejada;
   
    
    
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
        pesosOculta = new double[qtCo][Ldados.get(0).getAtributos().size()];
        pesosSaida = new double[getQtSaida()][qtCo];
        mDesejada = new int[getQtSaida()][getQtSaida()];
        
        Random rand = new Random();
        
        
        for (int i = 0; i < qtCo; i++) {
            for (int j = 0; j < Ldados.get(0).getAtributos().size(); j++) {
                pesosOculta[i][j] = rand.nextInt(5)-2.0;
            }
        }
        
        for (int i = 0; i < getQtSaida(); i++) {
            for (int j = 0; j < qtCo; j++) {
                pesosSaida[i][j] = rand.nextInt(5)-2.0;
            }
        }      
        
        for (int i = 0; i < getQtSaida(); i++)
        {        
            mDesejada[i][i] = 1;
            for (int j = 0; j < getQtSaida(); j++) {
                
                if(i!=j && fa.equals("t"))
                    mDesejada[i][j] = -1;
                else
                     mDesejada[i][j] = 0;
            }
        }
    }
    
    
    
    /*public void inicializaCamadas()
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
    }*/
    
    private int getMaior()
    {
        return 1;
    }
    
    private int getMenor()
    {
        return 1;
    }
    
}
