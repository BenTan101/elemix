package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AcidBaseController implements Resizable, Initializable {
    //Reactant one is the metal.
    @FXML
    Button acidReactantButton;
    //Reactant two is the ion.
    @FXML
    Button baseReactantButton;
    @FXML
    Button saltProductButton;
    @FXML
    Button waterProductButton;
    @FXML
    Button calculateButton;
    @FXML
    Button resetButton;
    @FXML
    Label headerLabel;
    @FXML
    Button selectAcidButton;
    @FXML
    Button selectBaseButton;

    private Acid selectedAcid;
    private Base selectedBase;

    private String COOL_BLUE_COLOR = "#61c6fa";
    private String SALT_PLUM_COLOR = "#b861fa";
    private String BASE_BLUE_COLOR = "#6191fa";
    private String ACID_RUBY_COLOR = "#fa6161";

    private ResourceBundle messages = Main.getMessages();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText(messages.getString("AcidBaseLabel"));
        selectAcidButton.setText(messages.getString("Select"));
        selectBaseButton.setText(messages.getString("Select"));
        acidReactantButton.setText(messages.getString("Acid"));
        baseReactantButton.setText(messages.getString("Base"));
        saltProductButton.setText(messages.getString("Salt"));
        waterProductButton.setText(messages.getString("Water"));
        calculateButton.setText(messages.getString("Calculate"));
        resetButton.setText(messages.getString("Reset"));
    }

    public void returnToMenu(ActionEvent event) {
        Main.openMenu();
    }

    public void selectAcid(ActionEvent event) {
        displayCompounds(Acid.getAcids(), acidReactantButton);
    }

    public void selectBase(ActionEvent event) {
        displayCompounds(Base.getBases(), baseReactantButton);
    }

    public void proceedReaction(ActionEvent event) {
        if (!(selectedAcid == null) && !(selectedBase == null)) {
            selectedAcid.setMoles(1);
            selectedBase.setMoles(1);

            Compound[] products = selectedAcid.neutralise(selectedBase);

            if (products == null) {
                saltProductButton.setText(selectedAcid.getFormattedFormula());
                waterProductButton.setText(selectedBase.getFormattedFormula());
                return;
            }

            acidReactantButton.setText(selectedAcid.getFormattedFormulaWithMoles());
            baseReactantButton.setText(selectedBase.getFormattedFormulaWithMoles());
            saltProductButton.setText(products[0].getFormattedFormulaWithMoles());
            waterProductButton.setText(products[1].getFormattedFormulaWithMoles());

            String hexTransparencyFactor = "80";
            acidReactantButton.setStyle("-fx-text-fill: black; -fx-background-color: " + ACID_RUBY_COLOR + hexTransparencyFactor + ";");
            baseReactantButton.setStyle("-fx-text-fill: black; -fx-background-color: " + BASE_BLUE_COLOR + hexTransparencyFactor + ";");
            saltProductButton.setStyle("-fx-text-fill: black; -fx-background-color: " + SALT_PLUM_COLOR + hexTransparencyFactor + ";");
            waterProductButton.setStyle("-fx-text-fill: black; -fx-background-color: " + COOL_BLUE_COLOR + hexTransparencyFactor + ";");

            int defaultFontSize = 25;
            setResizableFont(acidReactantButton, defaultFontSize);
            setResizableFont(baseReactantButton, defaultFontSize);
            setResizableFont(saltProductButton, defaultFontSize);
            setResizableFont(waterProductButton, defaultFontSize);

        }
    }

    public void resetReaction(ActionEvent event) {
        Button[] equationButtons = new Button[]{acidReactantButton, baseReactantButton, saltProductButton, waterProductButton};
        String[] buttonTexts = new String[]{"Acid", "Base", "Salt", "Water"};

        for (int i = 0; i < equationButtons.length; i++) {
            equationButtons[i].setStyle("");
            equationButtons[i].setText(buttonTexts[i]);
        }

        selectedAcid = null;
        selectedBase = null;
    }

    private void displayCompounds(ArrayList<AcidBase> arrayList, Button button) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        Stage miniStage = new Stage();

        //Adds elements
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < arrayList.size() / 5 + 1; j++) {
                if (5 * j + i >= arrayList.size())
                    break;

                Button b = new Button(arrayList.get(5 * j + i).toString());

                b.setPrefSize(80, 80);
                setResizableFont(b, 20);

                String defaultStyle = "-fx-text-fill: black;";
                String hoverStyle = "-fx-text-fill: black;";
                if (button.equals(acidReactantButton)) {
                    defaultStyle += "-fx-background-radius: 10; -fx-background-color: " + ACID_RUBY_COLOR + "80;";
                    hoverStyle += "-fx-background-radius: 10; -fx-background-color: " + ACID_RUBY_COLOR + "50;";
                    b.setStyle(defaultStyle);
                    String finalHoverStyle = hoverStyle;
                    b.setOnMouseEntered(e -> b.setStyle(finalHoverStyle));
                    String finalDefaultStyle = defaultStyle;
                    b.setOnMouseExited(e -> b.setStyle(finalDefaultStyle));
                } else {
                    defaultStyle += "-fx-background-radius: 10; -fx-background-color: " + BASE_BLUE_COLOR + "80;";
                    hoverStyle += "-fx-background-radius: 10; -fx-background-color: " + BASE_BLUE_COLOR + "50;";
                    b.setStyle(defaultStyle);
                    String finalHoverStyle = hoverStyle;
                    b.setOnMouseEntered(e -> b.setStyle(finalHoverStyle));
                    String finalDefaultStyle = defaultStyle;
                    b.setOnMouseExited(e -> b.setStyle(finalDefaultStyle));
                }

                int finalJ = j;
                int finalI = i;
                b.setOnAction(e -> {
                    if (button.equals(acidReactantButton)) {
                        selectedAcid = (Acid) arrayList.get(5 * finalJ + finalI);
                        acidReactantButton.setText(selectedAcid.toString());
                        acidReactantButton.setStyle("-fx-text-fill: black; -fx-background-color: " + ACID_RUBY_COLOR + "80;");
                    } else {
                        selectedBase = (Base) arrayList.get(5 * finalJ + finalI);
                        baseReactantButton.setText(selectedBase.toString());
                        baseReactantButton.setStyle("-fx-text-fill: black; -fx-background-color: " + BASE_BLUE_COLOR + "80;");

                    }
                    miniStage.close();
                });

                gridPane.add(b, i, j);
            }
        }

        Scene scene = new Scene(gridPane);
        Main.setCurrentStyle(scene);
        miniStage.setTitle("Choose Compound");
        miniStage.initModality(Modality.APPLICATION_MODAL);
        miniStage.setScene(scene);
        miniStage.setResizable(false);
        miniStage.show();
    }
}
