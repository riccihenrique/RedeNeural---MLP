package redeneural.mlp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import operacoes.Dados;
import operacoes.RedeNeural;

public class FXMLTrainController implements Initializable {

    @FXML
    private Label lbTrain;
    @FXML
    private JFXTextArea taSteps;
    @FXML
    private JFXButton btConfusionMatrix;
    @FXML
    private TableView<String> tableConfusion;
    
    private RedeNeural net;
    private List<Dados> lTeste;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(net == null);
                
                new Thread(() -> {
                    net.treinar();
                }).start();
                
                while(!net.isFinished());
                
                List<Double> errors = net.getErrors();
                taSteps.setText("");
                for(int i = 0; i < errors.size(); i++) {
                    taSteps.setText(taSteps.getText() + "\n" + "Step: " + i + " Loss: " + errors.get(i));
                }
                
            }
        }).start();
    }

    @FXML
    private void clkChart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLGraphic.fxml"));
            Parent root = (Parent) loader.load();
            
            FXMLGraphicController t = loader.getController();
            t.setNet(net);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void clkConfusionMatrix(ActionEvent event) {
        
        int[][] confus = net.getMatrizConfusao();
    }    
    
    public void setNet(RedeNeural net) {
        this.net = net;
    }
    
    public void SetTrest(List<Dados> lTeste) {
        this.lTeste = lTeste;
    }
}
