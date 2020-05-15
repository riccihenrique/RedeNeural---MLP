package operacoes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NormalizaDados {

    public static List<Object> leDados(String caminhoTreinamento, String caminhoTeste) {
        String linha;
        List<Dados> LD = new ArrayList<>();
        List<Object> listas = new ArrayList<>();
        List<Dados> Lt = new ArrayList<>();

        double[] maior, menor;
        String[] data;
        
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(caminhoTreinamento));
            
            linha = csvReader.readLine();
            listas.add(linha);
            data = linha.split(",");

            maior = new double[data.length - 1];
            menor = new double[data.length - 1];

            for (int i = 0; i < maior.length; i++) {
                maior[i] = Double.MIN_VALUE;
                menor[i] = Double.MAX_VALUE;
            }

            while ((linha = csvReader.readLine()) != null) {
                data = linha.split(",");
                Dados d = new Dados();

                for (int i = 0; i < data.length - 1; i++) {
                    d.setAtributos(Double.parseDouble(data[i]));

                    if (maior[i] < Double.parseDouble(data[i])) {
                        maior[i] = Double.parseDouble(data[i]);
                    }

                    if (menor[i] > Double.parseDouble(data[i])) {
                        menor[i] = Double.parseDouble(data[i]);
                    }
                }
                d.setClasse(data[data.length - 1]);

                LD.add(d);
            }
            csvReader.close();

            for (Dados d : LD) {
                for (int i = 0; i < d.getAtributos().size(); i++) {
                    Double newValor = (d.getAtributos().get(i) - menor[i]) / (maior[i] - menor[i]);

                    d.getAtributos().set(i, newValor);
                }
            }

            csvReader = new BufferedReader(new FileReader(caminhoTeste));
            linha = csvReader.readLine();
            while ((linha = csvReader.readLine()) != null) {
                data = linha.split(",");
                Dados d = new Dados();

                for (int i = 0; i < data.length - 1; i++) {

                    Double newValor = (Double.parseDouble(data[i]) - menor[i]) / (maior[i] - menor[i]);
                    d.setAtributos(newValor);
                }
                d.setClasse(data[data.length - 1]);

                Lt.add(d);
            }
            csvReader.close();
        }
        catch(IOException | NumberFormatException e) {
            
        }

        listas.add(LD);
        listas.add(Lt);

        return listas;
    }
}