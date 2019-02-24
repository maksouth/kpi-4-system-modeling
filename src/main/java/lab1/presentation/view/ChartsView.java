package lab1.presentation.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kotlin.Pair;
import main.presentation.presenter.ChartsContract;
import main.presentation.presenter.ChartsPresenter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChartsView extends Application implements ChartsContract.View {

    private ChartsContract.Presenter presenter;
    private Stage stage;

    BarChart<String,Number> barChart;
    Button exponentialButton;
    Button normalButton;
    Button uniformButton;

    TextField parameter1Field;
    TextField parameter2Field;

    Label parameter1Label;
    Label parameter2Label;
    Label rngNameLabel;

    Label averageLabel;
    Label dispersionLabel;

    Label verifiedInfoLabel;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.stage = primaryStage;
        AnchorPane root = FXMLLoader.load(getUiUrl());
        stage.setTitle("Random Number Generators");
        stage.setScene(new Scene(root));
        stage.show();

        initializeUI();

        exponentialButton.setOnMouseClicked(event -> presenter.exponentialButtonClicked());
        normalButton.setOnMouseClicked(event -> presenter.normalButtonClicked());
        uniformButton.setOnMouseClicked(event -> presenter.uniformButtonClicked());

        presenter = new ChartsPresenter(this);
        presenter.start();
    }

    private void initializeUI() {
        Scene scene = stage.getScene();

        barChart = (BarChart<String, Number>) scene.lookup("#bar_chart");
        exponentialButton = (Button) scene.lookup("#exponential_button");
        uniformButton = (Button) scene.lookup("#uniform_button");
        normalButton = (Button) scene.lookup("#normal_button");

        parameter1Field = (TextField) scene.lookup("#parameter_1_field");
        parameter2Field = (TextField) scene.lookup("#parameter_2_field");

        parameter1Label = (Label) scene.lookup("#parameter_1_label");
        parameter2Label = (Label) scene.lookup("#parameter_2_label");
        rngNameLabel = (Label) scene.lookup("#rng_name");

        averageLabel = (Label) scene.lookup("#average_label");
        dispersionLabel = (Label) scene.lookup("#dispersion_label");

        verifiedInfoLabel = (Label) scene.lookup("#verified_info_label");
    }

    private URL getUiUrl() throws MalformedURLException {
        return new File("src/resources/sample.fxml").toURL();
    }

    @Override
    public void showExponentialGenerator() {
        rngNameLabel.setText("Exponential law RNG");
        parameter1Label.setText("Lambda");
        parameter2Label.setText("");
    }

    @Override
    public void showNormalGenerator() {
        rngNameLabel.setText("Normal law RNG");
        parameter1Label.setText("Alpha");
        parameter2Label.setText("Sigma");
    }

    @Override
    public void showUniformGenerator() {
        rngNameLabel.setText("Uniform law RNG");
        parameter1Label.setText("a");
        parameter2Label.setText("c");
    }

    @Override
    public void clearCharts() {
        barChart.getData().clear();
    }

    @Override
    public void showBarChart(List<Pair<Double, Integer>> data) {

        XYChart.Series series = new XYChart.Series();
        for (Pair<Double, Integer> pair: data) {
            String interval = String.valueOf(pair.getFirst());
            XYChart.Data element = new XYChart.Data<>(interval, pair.getSecond());
            series.getData().add(element);
        }

        barChart.getData().add(series);
    }

    @Override
    public void showLineChart(List<Pair<Double, Double>> data) {

    }

    @Override
    public void setSecondParameterDisabled(boolean b) {
        parameter2Field.setDisable(b);
    }

    @Override
    public void setFirstParameterName(String value) {
        parameter1Label.setText(value);
    }

    @Override
    public void setSecondParameterName(String value) {
        parameter2Label.setText(value);
    }

    @Override
    public String getFirstParameterValue() {
        return parameter1Field.getText();
    }

    @Override
    public String getSecondParameterValue() {
        return parameter2Field.getText();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void showAverage(double value) {
        averageLabel.setText(String.valueOf(value));
    }

    @Override
    public void showDispersion(double value) {
        dispersionLabel.setText(String.valueOf(value));
    }

    @Override
    public void showVerifiedInfo(String value) {
        verifiedInfoLabel.setText(value);
    }
}
