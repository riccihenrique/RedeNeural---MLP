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
      
        List<Object> dados = NormalizaDados.leDados("/home/henrique/Documentos/RedeNeural---MLP/RedeNeural - MLP/src/base_treinamento.csv", "/home/henrique/Documentos/RedeNeural---MLP/RedeNeural - MLP/src/base_teste.csv");
        
        String rotulos = (String)dados.get(0);
        List<Dados> LDados = (List<Dados>)dados.get(1);
        List<Dados> LTestes = (List<Dados>)dados.get(2);

        RedeNeural rede = new RedeNeural(LDados, 0, "lo", "lo", 0.2, 3000, 5);
        rede.treinar();
        rede.teste(LTestes);
        
        System.exit(0);
        
    }
}
