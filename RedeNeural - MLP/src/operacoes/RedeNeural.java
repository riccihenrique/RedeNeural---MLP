package operacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedeNeural {

    private List<Dados> Ldados;
    private List<Neuronio> Lco, Lsaida;
    private List<Double> Lerros = new ArrayList<>();

    private double[][] pesosOculta, pesosSaida;
    private int[][] mDesejada;
    private double erro, erroRede = 10, txA;
    private String faOculta, faSaida;
    private int qtIt, qtCo;
    private List<String> rotulos;
    private boolean finished = false;

    public RedeNeural(List<Dados> Ldados, double erro, String fao, String fas, double txA, int qtIt, int qtCo) {
        this.Ldados = Ldados;
        this.erro = erro;
        this.faOculta = fao;
        this.faSaida = fas;
        this.txA = txA;
        this.qtIt = qtIt;
        this.qtCo = qtCo;
        inicializaCamadas();
    }

    private int getQtSaida() {
        rotulos = new ArrayList();

        for (int i = 0; i < Ldados.size(); i++)
            if (!rotulos.contains(Ldados.get(i).getClasse())) 
                rotulos.add(Ldados.get(i).getClasse());

        return rotulos.size();
    }

    public void inicializaCO() {
        //init camada oculta
        Lco = new ArrayList<>();
        for (int i = 0; i < qtCo; i++)
            Lco.add(new Neuronio());
    }

    public void inicializaSaida() {
        //init camada de saida
        Lsaida = new ArrayList<>();
        for (int i = 0; i < getQtSaida(); i++)
            Lsaida.add(new Neuronio());
    }

    public void inicializaCamadas() {
        inicializaCO();
        inicializaSaida();

        pesosOculta = new double[qtCo][Ldados.get(0).getAtributos().size()];
        pesosSaida = new double[getQtSaida()][qtCo];
        mDesejada = new int[getQtSaida()][getQtSaida()];

        Random rand = new Random();
        for (int i = 0; i < qtCo; i++)
            for (int j = 0; j < Ldados.get(0).getAtributos().size(); j++)
                pesosOculta[i][j] = rand.nextInt(5) - 2.0;

        for (int i = 0; i < getQtSaida(); i++)
            for (int j = 0; j < qtCo; j++) 
                pesosSaida[i][j] = rand.nextInt(5) - 2.0;

        for (int i = 0; i < getQtSaida(); i++) {
            for (int j = 0; j < getQtSaida(); j++) {

                if (i == j)
                    mDesejada[i][i] = 1;
                else if (faOculta.equals("t"))
                    mDesejada[i][j] = -1;
                else
                    mDesejada[i][j] = 0;
            }
        }
    }

    public double linear(Double net) {
        return net / 10.0;
    }

    public double Dlinear(Double net) {
        return 1.0 / 10.0;
    }

    public double logistica(Double net) {
        return 1.0 / (1.0 + Math.pow(Math.E, -net));
    }

    public double Dlogistica(Double net) {
        return net * (1.0 - net);
    }

    public double hiperbolica(Double net) {
        return (1.0 - Math.pow(Math.E, -2.0 * net)) / (1.0 + Math.pow(Math.E, -2.0 * net));
    }

    public double Dhiperbolica(Double net) {
        return 1.0 - net * net;
    }

    public void treinar() {
        int epocas = 0;
        while (erro < erroRede && epocas < qtIt) {
            for (Dados d : Ldados) {
                for (int i = 0; i < Lco.size(); i++) // Cálculo do NET
                {
                    double soma = 0;
                    for (int j = 0; j < d.getAtributos().size(); j++)
                        soma += d.getAtributos().get(j) * pesosOculta[i][j];
                        
                    Lco.get(i).setNet(soma);

                    if (faOculta.equals("t")) //tg hiperbolica                    
                        Lco.get(i).setI(hiperbolica(Lco.get(i).getNet()));
                    else if (faOculta.equals("lo"))
                        Lco.get(i).setI(logistica(Lco.get(i).getNet()));
                    else 
                        Lco.get(i).setI(linear(Lco.get(i).getNet()));
                }

                for (int i = 0; i < Lsaida.size(); i++) {
                    double soma = 0;
                    for (int j = 0; j < Lco.size(); j++)
                        soma += Lco.get(j).getI() * pesosSaida[i][j];
                    
                    Lsaida.get(i).setNet(soma);

                    if (faSaida.equals("t")) //tg hiperbolica
                        Lsaida.get(i).setI(hiperbolica(Lsaida.get(i).getNet()));
                    else if (faSaida.equals("lo"))
                        Lsaida.get(i).setI(logistica(Lsaida.get(i).getNet()));
                    else 
                        Lsaida.get(i).setI(linear(Lsaida.get(i).getNet()));                    
                }

                erroRede = 0;
                int index = rotulos.indexOf(d.getClasse());
                for (int i = 0; i < Lsaida.size(); i++) {
                    Lsaida.get(i).setErro(mDesejada[index][i] - Lsaida.get(i).getI());
                    erroRede += Math.pow(Lsaida.get(i).getErro(), 2);

                    if (faOculta.equals("t"))
                        Lsaida.get(i).setGradiente(Lsaida.get(i).getErro() * Dhiperbolica(Lsaida.get(i).getI()));
                    else if (faOculta.equals("lo"))
                        Lsaida.get(i).setGradiente(Lsaida.get(i).getErro() * Dlogistica(Lsaida.get(i).getI()));
                    else 
                        Lsaida.get(i).setGradiente(Lsaida.get(i).getErro() * Dlinear(Lsaida.get(i).getNet()));
                }

                erroRede /= 2;                

                for (int i = 0; i < Lco.size(); i++) {
                    double somErro = 0;
                    for (int j = 0; j < Lsaida.size(); j++) 
                        somErro += Lsaida.get(j).getGradiente() * pesosSaida[j][i];
                    
                    if (faOculta.equals("t"))
                        Lco.get(i).setErro(somErro * Dhiperbolica(Lco.get(i).getI()));
                    else if (faOculta.equals("lo"))
                        Lco.get(i).setErro(somErro * Dlogistica(Lco.get(i).getI()));
                    else 
                        Lco.get(i).setErro(somErro * Dlinear(Lco.get(i).getNet()));
                }

                //Atualização de pesos da camada de saida
                for (int i = 0; i < Lsaida.size(); i++)
                    for (int j = 0; j < Lco.size(); j++)
                        pesosSaida[i][j] = pesosSaida[i][j] + txA * Lsaida.get(i).getGradiente() * Lco.get(j).getI();

                //Atualização de pesos da camada oculta
                for (int i = 0; i < Lco.size(); i++)
                    for (int j = 0; j < d.getAtributos().size(); j++) 
                        pesosOculta[i][j] = pesosOculta[i][j] + txA * Lco.get(i).getGradiente() * d.getAtributos().get(j);
            }

            System.out.println("Epoca: " + epocas + " Erro:" + erroRede);
            Lerros.add(erroRede);

            if (erroRede < erro)
                break;
            
            epocas++;
        }
        this.finished = true;
    }

    public int[][] teste(List<Dados> Lteste) {
        
        int [][] mConfusao = new int[getQtSaida()][getQtSaida()];
        int cA = 0, tot = 0;
        for (Dados d : Lteste) {
            for (int i = 0; i < Lco.size(); i++) // Cálculo do NET
            {
                double soma = 0;
                for (int j = 0; j < d.getAtributos().size(); j++)
                    soma += d.getAtributos().get(j) * pesosOculta[i][j];
                
                Lco.get(i).setNet(soma);

                if (faOculta.equals("t")) //tg hiperbolica                
                    Lco.get(i).setI(hiperbolica(Lco.get(i).getNet()));
                else if (faOculta.equals("lo"))
                    Lco.get(i).setI(logistica(Lco.get(i).getNet()));
                else 
                    Lco.get(i).setI(linear(Lco.get(i).getNet()));
            }

            for (int i = 0; i < Lsaida.size(); i++) {
                double soma = 0;
                for (int j = 0; j < Lco.size(); j++) 
                    soma += Lco.get(j).getI() * pesosSaida[i][j];
                
                Lsaida.get(i).setNet(soma);

                if (faSaida.equals("t")) //tg hiperbolica
                    Lsaida.get(i).setI(hiperbolica(Lsaida.get(i).getNet()));
                else if (faSaida.equals("lo"))
                    Lsaida.get(i).setI(logistica(Lsaida.get(i).getNet()));
                else
                    Lsaida.get(i).setI(linear(Lsaida.get(i).getNet()));
            }

            int index = 0;
            double maior = Lsaida.get(0).getI();

            for (int i = 1; i < Lsaida.size(); i++)                
                if (Lsaida.get(i).getI() > maior) {
                    maior = Lsaida.get(i).getI();
                    index = i;
                }
            
            int i = 0;
            for (; i < rotulos.size(); i++) {
                if(rotulos.get(i).equals(d.getClasse()))
                    break;
            }
            
            mConfusao[i][index]++;
        }
        return mConfusao;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public List<Double> getErrors() {
        return this.Lerros;
    }

    public List<String> getRotulos()
    {
        return rotulos;
    }
    
}
