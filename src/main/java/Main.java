import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import service.Service;
import ui.UI;


public class Main extends Application{

    @Override
    public void start(Stage stage) {
        stage.setTitle("");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Populatia");
        yAxis.setLabel("Valoarea minimului");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Min history");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0;i<Service.minHistory.size();i++){
            series.getData().add(new XYChart.Data( i,Service.minHistory.get(i)));
        }

        Scene scene  = new Scene(lineChart,1200,800);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.displayMainMenu();
        launch(args);
    }
}
