package redeneural.mlp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import operacoes.Dados;
import operacoes.NormalizaDados;
import operacoes.RedeNeural;

public class FXMLMainController implements Initializable {

    @FXML
    private ToggleGroup oculta;
    @FXML
    private ToggleGroup saida;
    @FXML
    private TableView tableData;
    @FXML
    private JFXTextField txtInputLayer;
    @FXML
    private JFXTextField txtOutputLayer;
    @FXML
    private JFXTextField txtHiddenLayer;
    @FXML
    private JFXRadioButton linearHidden;
    @FXML
    private JFXRadioButton logisticaHidden;
    @FXML
    private JFXRadioButton tanhHidden;
    @FXML
    private JFXTextField txtLearningRate;
    @FXML
    private JFXTextField txtMinError;
    @FXML
    private JFXTextField txtSteps;
    @FXML
    private JFXRadioButton linear;
    @FXML
    private JFXRadioButton logistica;
    @FXML
    private JFXRadioButton tanh;
    @FXML
    private HBox hboxItens;
    @FXML
    private JFXButton btTreinar;
    
    private List<Dados> lTreino, lTeste;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private File readFileTest() {
        FileChooser fc = new FileChooser();        
        fc.setTitle("Abrir csv de teste");
        File file_teste = fc.showOpenDialog(null);
        return file_teste;
    }

    public int outputSize() {
        List<String> rotulos = new ArrayList();

        for (int i = 0; i < lTreino.size(); i++) {
            if (!rotulos.contains(lTreino.get(i).getClasse())) {
                rotulos.add(lTreino.get(i).getClasse());
            }
        }
        return rotulos.size();
    }
    
    
    @FXML
    private void clkLoad(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Abrir csv de treinamento");
        //File file_train = fc.showOpenDialog(null);       
        //File file_test = readFileTest();
        
//        List<Object> dados = NormalizaDados.leDados(file_train.getAbsolutePath(), file_test.getAbsolutePath());
        List<Object> dados = NormalizaDados.leDados("/home/henrique/Documentos/RedeNeural---MLP/RedeNeural - MLP/src/base_treinamento.csv",
                                    "/home/henrique/Documentos/RedeNeural---MLP/RedeNeural - MLP/src/base_teste.csv");
        
        String rotulos = (String)dados.get(0);
        lTreino = (List<Dados>)dados.get(1);
        lTeste = (List<Dados>)dados.get(2);
        
        String split_label[] = rotulos.split(",");
        
        int output = outputSize();
        
        txtInputLayer.setText((split_label.length - 1) + "");
        txtOutputLayer.setText(output + "");
        txtHiddenLayer.setText(((int) (output + split_label.length - 1) / 2) + "");        
        
        TableColumn [] tableColumns = new TableColumn[split_label.length]; // Cria um vetor de colunas da quantidade de colunas do csv
        tableData.getColumns().clear();
        for(int i = 0; i < split_label.length; i++) {
            final int j = i;
            TableColumn col = new TableColumn(split_label[i]); // nome da coluna
            col.prefWidthProperty().set((600 / split_label.length));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){ // Define que o valor das linhas serão retirados de um observablelist de string
               @Override
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(j).toString()); // Não sei o que isso faz          
                }                   
            });
            tableData.getColumns().addAll(col);    // Adiciona a coluna
        }
        
        ObservableList<ObservableList> data = FXCollections.observableArrayList(); // Cria o observable lista dos dados
        for(Dados d : lTreino) {
            ObservableList<String> row = FXCollections.observableArrayList(); // Observable list de cada linha
            for(Double value : d.getAtributos())
                row.add(value + ""); // adiciona o valor lido
            row.add(d.getClasse()); // adiciona a classe
            data.add(row);           // adiciona no observable list de dados
        } 
        tableData.setItems(data); // adiciona o observable list de dados na tabela e show de bola
        hboxItens.setDisable(false);
    }

    @FXML
    private void clkModel(ActionEvent event) {
        
    }

    @FXML
    private void cklTrain(ActionEvent event) {
        RedeNeural rede = new RedeNeural(lTreino, Double.parseDouble(txtMinError.getText()), 
                linearHidden.isSelected() ? "li" : tanhHidden.isSelected() ? "t" : "lo",
                linear.isSelected() ? "li" : tanh.isSelected() ? "t" : "lo", Double.parseDouble(txtLearningRate.getText()), 
                Integer.parseInt(txtSteps.getText()), Integer.parseInt(txtHiddenLayer.getText()));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTrain.fxml"));
            Parent root = (Parent) loader.load();
            FXMLTrainController t = loader.getController();
            t.setNet(rede);
            t.SetTrest(lTeste);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
