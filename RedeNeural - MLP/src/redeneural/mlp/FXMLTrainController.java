package redeneural.mlp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import operacoes.Dados;
import operacoes.RedeNeural;

public class FXMLTrainController implements Initializable {

    @FXML
    private Label lbTrain;
    @FXML
    private TableView tableConfusion;    
    @FXML
    private LineChart<String, Double> chartLoss;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private PieChart pieChart;
    
    private RedeNeural net;
    private List<Dados> lTeste;
    private Thread thread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Task<Void> exampleTask = new Task<Void>() { 
                @Override protected Void call() throws Exception { // Demais códigos... 
                    Platform.runLater(new Runnable() { 
                        @Override public void run() { // Alteração de componentes 
                            net.treinar();
                            clkChart();
                            showConfusion(net.teste(lTeste));
                        } 
                    }); 
                    return null;
                } 
            }; 
            thread = new Thread(exampleTask);
            thread.start();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void clkChart() {
        xAxis.setLabel("Épocas");
        chartLoss.setTitle("Perdas por Época");
        
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Data Series");

        chartLoss.getData().add(series);
        
        for(int i = 0; i < net.getErrors().size(); i += 50)
            if(i % 1000 == 0)
                series.getData().add(new XYChart.Data<String, Double>(i + "", net.getErrors().get(i)));
            
    }

    public void setNet(RedeNeural net) {
        this.net = net;
    }
    
    public void SetTrest(List<Dados> lTeste) {
        this.lTeste = lTeste;
    }
    
    private void showConfusion(int [][] mConfusion)
    {   
        int total = 0, acertos = 0;        
        List<String> split_label = net.getRotulos();
        split_label.add(0," ");
        
        TableColumn [] tableColumns = new TableColumn[split_label.size()]; // Cria um vetor de colunas da quantidade de colunas do csv
        tableConfusion.getColumns().clear();
        
        for(int i = 0; i < split_label.size(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(split_label.get(i)); // nome da coluna
            col.prefWidthProperty().set((600 / split_label.size()));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){ // Define que o valor das linhas serão retirados de um observablelist de string
               @Override
               public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(j).toString()); // Não sei o que isso faz          
                }                   
            });
            tableConfusion.getColumns().addAll(col);    // Adiciona a coluna
        }
        
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < mConfusion.length; i++) {
             ObservableList<String> row = FXCollections.observableArrayList(); // Observable list de cada linha
             row.add(split_label.get(i+1));
             
            for (int j = 0; j < mConfusion.length; j++)
            {
                row.add(mConfusion[i][j]+""); 
                total+=mConfusion[i][j];
                if(i == j)
                    acertos+=mConfusion[i][j];
                
            } 
             data.add(row); 
        }
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Acerto "+((acertos/((double)total))*100.0)+"%", acertos),
                new PieChart.Data("Erro "+ (((total-acertos)/(double)total)*100.0)+"%", (total-acertos)));
        
                pieChart.setData(pieChartData); 
                pieChart.setTitle("Taxa de Acerto");
        
        tableConfusion.setItems(data); // adiciona o observable list de dados na tabela e show de bola       
        tableConfusion.setVisible(true);
        lbTrain.setText("Treinamento concluído com sucesso!");
        System.out.println(lbTrain.getText());
    }

    @FXML
    private void clkBreakThread(ActionEvent event) {
        thread.stop();
    }
}
