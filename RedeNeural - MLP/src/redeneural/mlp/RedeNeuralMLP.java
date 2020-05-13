package redeneural.mlp;

import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import operacoes.Dados;
import operacoes.RedeNeural;
import operacoes.NormalizaDados;

public class RedeNeuralMLP extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        //launch(args);
        
        
        
        //RedeNeural rede = new RedeNeural(NormalizaDados.leDados("C:\\base.csv"), 0, "", 0, 0, 5);
        //rede.treinar();
        
        List<Object> dados = NormalizaDados.leDados("C:\\ALOcorno.csv", "C:\\ALOcorno.csv");
        
        
        String rotulos = (String)dados.get(0);
        List<Dados> LDados = (List<Dados>)dados.get(1);
        List<Dados> LTestes = (List<Dados>)dados.get(2);
        
        for (Dados d:LDados)
        {
            for (int i = 0; i < d.getAtributos().size(); i++) {
                System.out.print(d.getAtributos().get(i)+" ");                
            }
            System.out.println(" ");
        }
        
        
        
        
    }
}
