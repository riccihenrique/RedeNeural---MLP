package operacoes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RedeNeural implements Serializable {

    private final List<Dados> dadosTreino;
    private List<Neuronio> camadaOculta, camadaSaida;
    private List<Double> erros = new ArrayList<>();
    private List<String> rotulos;

    private double[][] pesosOculta, pesosSaida;
    private int[][] mDesejada;
    private final double erro, txAprendizado;
    private double erroRede = 10;
    private final String fAtivacaoOculta, fAtivacaoSaida;
    private final int qtdEpocas, qtdOculta;
    private int nSaidas;
    private boolean finished = false;

    public RedeNeural(List<Dados> dadosTreino, double erro, String fao, String fas, double txAprendizado, int qtdEpocas, int qtdOculta) {
        this.dadosTreino = dadosTreino;
        this.erro = erro;
        this.fAtivacaoOculta = fao;
        this.fAtivacaoSaida = fas;
        this.txAprendizado = txAprendizado;
        this.qtdEpocas = qtdEpocas;
        this.qtdOculta = qtdOculta;
        inicializaCamadas();
    }

    private int getQtSaida() {
        rotulos = new ArrayList();

        for (int i = 0; i < dadosTreino.size(); i++)
            if (!rotulos.contains(dadosTreino.get(i).getClasse())) 
                rotulos.add(dadosTreino.get(i).getClasse());

        return rotulos.size();
    }

    public void inicializaCO() {
        //init camada oculta
        camadaOculta = new ArrayList<>();
        for (int i = 0; i < qtdOculta; i++)
            camadaOculta.add(new Neuronio());
    }

    public void inicializaSaida() {
        //init camada de saida
        camadaSaida = new ArrayList<>();
        for (int i = 0; i < getQtSaida(); i++)
            camadaSaida.add(new Neuronio());
    }

    public void inicializaCamadas() {
        inicializaCO();
        inicializaSaida();
        
        nSaidas = getQtSaida();
        
        pesosOculta = new double[qtdOculta][dadosTreino.get(0).getAtributos().size()];
        pesosSaida = new double[nSaidas][qtdOculta];
        mDesejada = new int[nSaidas][nSaidas];

        Random rand = new Random();
        for (int i = 0; i < qtdOculta; i++)
            for (int j = 0; j < dadosTreino.get(0).getAtributos().size(); j++)
                pesosOculta[i][j] = rand.nextInt(5) - 2.0;

        for (int i = 0; i < nSaidas; i++)
            for (int j = 0; j < qtdOculta; j++) 
                pesosSaida[i][j] = rand.nextInt(5) - 2.0;

        for (int i = 0; i < nSaidas; i++) {
            for (int j = 0; j < nSaidas; j++) {
                if (i == j)
                    mDesejada[i][i] = 1;
                else if (fAtivacaoSaida.equals("t"))
                    mDesejada[i][j] = -1;
                else
                    mDesejada[i][j] = 0;
            }
        }
    }

    public double linear(double net) {
        return net / 10.0;
    }

    public double derivadaLinear(double net) {
        return 1.0 / 10.0;
    }

    public double logistica(double net) {
        return 1.0 / (1.0 + Math.pow(Math.E, -net));
    }

    public double derivadaLogistica(double net) {
        return net * (1.0 - net);
    }

    public double hiperbolica(double net) {
        double d = Math.pow(Math.E, -2.0 * net);
        return (1.0 - d) / (1.0 + d);
    }

    public double derivadaHiperbolica(double net) {
        return 1.0 - Math.pow(net, 2);
    }

    public void treinar() {
        int epocas = 0;
        erros= new ArrayList<>();
        finished = false;
        double som_erro;
        while (erro < erroRede && epocas < qtdEpocas) {
            som_erro = 0;
            for (Dados d : dadosTreino) {                
                for (int i = 0; i < camadaOculta.size(); i++) { // Cálculo do NET camada oculta
                    double soma = 0;
                    for (int j = 0; j < d.getAtributos().size(); j++)
                        soma += d.getAtributos().get(j) * pesosOculta[i][j];
                        
                    camadaOculta.get(i).setNet(soma);

                    if (fAtivacaoOculta.equals("t")) //tg hiperbolica                    
                        camadaOculta.get(i).setI(hiperbolica(camadaOculta.get(i).getNet()));
                    else if (fAtivacaoOculta.equals("lo"))
                        camadaOculta.get(i).setI(logistica(camadaOculta.get(i).getNet()));
                    else 
                        camadaOculta.get(i).setI(linear(camadaOculta.get(i).getNet()));
                }

                for (int i = 0; i < camadaSaida.size(); i++) { // Cálculo do NET camada saída
                    double soma = 0;
                    for (int j = 0; j < camadaOculta.size(); j++)
                        soma += camadaOculta.get(j).getI() * pesosSaida[i][j];
                    
                    camadaSaida.get(i).setNet(soma);

                    if (fAtivacaoSaida.equals("t")) //tg hiperbolica
                        camadaSaida.get(i).setI(hiperbolica(camadaSaida.get(i).getNet()));
                    else if (fAtivacaoSaida.equals("lo"))
                        camadaSaida.get(i).setI(logistica(camadaSaida.get(i).getNet()));
                    else 
                        camadaSaida.get(i).setI(linear(camadaSaida.get(i).getNet()));                    
                }
                
                // Calculo do erro da camada de saida
                erroRede = 0;
                int index = rotulos.indexOf(d.getClasse());
                for (int i = 0; i < camadaSaida.size(); i++) {
                    camadaSaida.get(i).setErro((double) mDesejada[index][i] - camadaSaida.get(i).getI());
                    erroRede += Math.pow(camadaSaida.get(i).getErro(), 2);

                    if (fAtivacaoSaida.equals("t"))
                        camadaSaida.get(i).setGradiente(camadaSaida.get(i).getErro() * derivadaHiperbolica(camadaSaida.get(i).getI()));
                    else if (fAtivacaoSaida.equals("lo"))
                        camadaSaida.get(i).setGradiente(camadaSaida.get(i).getErro() * derivadaLogistica(camadaSaida.get(i).getI()));
                    else 
                        camadaSaida.get(i).setGradiente(camadaSaida.get(i).getErro() * derivadaLinear(camadaSaida.get(i).getI()));
                }

                erroRede /= 2;
                som_erro += erroRede;
                // calculo do erro da camada oculta               
                for (int i = 0; i < camadaOculta.size(); i++) {
                    double somErro = 0;
                    for (int j = 0; j < camadaSaida.size(); j++) 
                        somErro += camadaSaida.get(j).getGradiente() * pesosSaida[j][i];
                    
                    if (fAtivacaoOculta.equals("t"))
                        camadaOculta.get(i).setGradiente(somErro * derivadaHiperbolica(camadaOculta.get(i).getI()));
                    else if (fAtivacaoOculta.equals("lo"))
                        camadaOculta.get(i).setGradiente(somErro * derivadaLogistica(camadaOculta.get(i).getI()));
                    else 
                        camadaOculta.get(i).setGradiente(somErro * derivadaLinear(camadaOculta.get(i).getI()));
                }

                //Atualização de pesos da camada de saida
                for (int i = 0; i < camadaSaida.size(); i++)
                    for (int j = 0; j < camadaOculta.size(); j++)
                        pesosSaida[i][j] = pesosSaida[i][j] + txAprendizado * camadaSaida.get(i).getGradiente() * camadaOculta.get(j).getI();

                //Atualização de pesos da camada oculta
                for (int i = 0; i < camadaOculta.size(); i++)
                    for (int j = 0; j < d.getAtributos().size(); j++) 
                        pesosOculta[i][j] = pesosOculta[i][j] + txAprendizado * camadaOculta.get(i).getGradiente()* d.getAtributos().get(j);
            }
            
            erroRede = som_erro / dadosTreino.size();
            //System.out.println("Epoca: " + epocas + " Erro:" + erroRede);
            erros.add(erroRede);

            if (erroRede < erro)
                break;
            
            epocas++;
        }
        this.finished = true;
    }

    public int[][] teste(List<Dados> dadosTeste) {        
        int [][] mConfusao = new int[getQtSaida()][getQtSaida()];
        
        for (Dados d : dadosTeste) {
            for (int i = 0; i < camadaOculta.size(); i++) { // Cálculo do NET            
                double soma = 0;
                for (int j = 0; j < d.getAtributos().size(); j++)
                    soma += d.getAtributos().get(j) * pesosOculta[i][j];
                
                camadaOculta.get(i).setNet(soma);

                if (fAtivacaoOculta.equals("t")) //tg hiperbolica                
                    camadaOculta.get(i).setI(hiperbolica(camadaOculta.get(i).getNet()));
                else if (fAtivacaoOculta.equals("lo"))
                    camadaOculta.get(i).setI(logistica(camadaOculta.get(i).getNet()));
                else 
                    camadaOculta.get(i).setI(linear(camadaOculta.get(i).getNet()));
            }

            for (int i = 0; i < camadaSaida.size(); i++) {
                double soma = 0;
                for (int j = 0; j < camadaOculta.size(); j++) 
                    soma += camadaOculta.get(j).getI() * pesosSaida[i][j];
                
                camadaSaida.get(i).setNet(soma);

                if (fAtivacaoSaida.equals("t")) //tg hiperbolica
                    camadaSaida.get(i).setI(hiperbolica(camadaSaida.get(i).getNet()));
                else if (fAtivacaoSaida.equals("lo"))
                    camadaSaida.get(i).setI(logistica(camadaSaida.get(i).getNet()));
                else
                    camadaSaida.get(i).setI(linear(camadaSaida.get(i).getNet()));
            }

            int index = 0;
            double maior = camadaSaida.get(0).getI();

            for (int i = 1; i < camadaSaida.size(); i++)  // Verifica qual é o neoronio de maior valor e pega o indice dele              
                if (camadaSaida.get(i).getI() > maior) {
                    maior = camadaSaida.get(i).getI();
                    index = i;
                }
            
            int i = rotulos.indexOf(d.getClasse());            
            mConfusao[i][index]++;
        }
        
        int c = 0;
        for(int i = 0; i < getQtSaida(); i++) {
            for(int j = 0; j < getQtSaida(); j++) {
                if(i == j)
                    c += mConfusao[i][j];
                System.out.print(mConfusao[i][j] + " ");
            }
            
            System.out.println("");
        }
        
        System.out.println("Acertos " + c + " de " + dadosTeste.size());
        
        return mConfusao;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public List<Double> getErrors() {
        return this.erros;
    }

    public List<String> getRotulos() {
        return rotulos;
    }
    
    public int getEpocas() {
        return this.qtdEpocas;
    }
}
