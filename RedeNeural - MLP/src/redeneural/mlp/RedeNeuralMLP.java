package redeneural.mlp;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import operacoes.RedeNeural;
import operacoes.TrataDados;

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
        
        
        
        RedeNeural rede = new RedeNeural(TrataDados.leDados("C:\\base.csv"), 0, "", 0, 0, 5);
        rede.treinar();
        
    }
}
