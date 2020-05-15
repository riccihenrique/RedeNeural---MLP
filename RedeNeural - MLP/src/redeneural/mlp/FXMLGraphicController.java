package redeneural.mlp;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import operacoes.RedeNeural;

public class FXMLGraphicController implements Initializable {

    @FXML
    private LineChart<String, Double> chartLoss;
    
    private RedeNeural net;
    private List<Double> errors;
    @FXML
    private CategoryAxis xAxis;
    
    
    private ObservableList<String> teste = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setLabel("Épocas");
        chartLoss.setTitle("Perdas por Época");
        
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Data Series");

        chartLoss.getData().add(series);
        
        new Thread(() -> {
            while(net == null);
        
            ScheduledExecutorService scheduledExecutorService;
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
             xAxis.prefWidth(600);
            //scheduledExecutorService.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> {
                    while(!net.isFinished()) {
                        //if(net.getErrors().size() % 200 == 0) {
                            series.getData().clear();
                            for(int i = 0; i < net.getErrors().size(); i += 50)
                                series.getData().add(new XYChart.Data<String, Double>(i + "", net.getErrors().get(i)));
                       // }                        
                    }
                    series.getData().clear();
                        for(int i = 0; i < net.getErrors().size(); i += 50)
                            series.getData().add(new XYChart.Data<String, Double>(i + "", net.getErrors().get(i)));
                    try {
                        Thread.sleep(150);
                    }
                    catch(Exception e) {
                        
                    }
                });
            //}, 0, 1, TimeUnit.SECONDS);
        }).start();
    
    }
    
    public void setNet(RedeNeural net) {
        this.net = net;
    }
}
