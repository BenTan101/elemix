package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Cation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DisplacementController implements Initializable {
    @FXML
    Button metalReactantButton;
    @FXML
    Button cationReactantButton;
    @FXML
    Button productOneButton;
    @FXML
    Button productTwoButton;
    @FXML
    Label headerLabel;
    @FXML
    Button selectMetalButton;
    @FXML
    Button selectCationButton;
    @FXML
    Button calculateButton;
    @FXML
    Button resetButton;

    private Cation selectedMetal;
    private Cation selectedIon;

    private ResourceBundle messages = Main.getMessages();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText(messages.getString("DisplacementHeader"));
        selectMetalButton.setText(messages.getString("Select"));
        selectCationButton.setText(messages.getString("Select"));
        resetButton.setText(messages.getString("Reset"));
        calculateButton.setText(messages.getString("Calculate"));
        metalReactantButton.setText(messages.getString("Metal"));
        cationReactantButton.setText(messages.getString("Cation"));
        productOneButton.setText(messages.getString("Product"));
        productTwoButton.setText(messages.getString("Product"));
    }

    public void returnToMenu(ActionEvent event) {
        Main.openMenu();
    }

    public void proceedReaction(ActionEvent event) {
        if (!(selectedMetal == null) && !(selectedIon == null)) {
            selectedMetal.setMoles(1);
            selectedIon.setMoles(1);

            metalReactantButton.setText(selectedMetal.getFormattedFormulaWithMoles());
            cationReactantButton.setText(selectedIon.getFormattedFormulaWithMoles());

            Cation[] products = selectedMetal.displace(selectedIon);

            if (products == null) {
                productOneButton.setText(selectedMetal.getFormattedFormula());
                productOneButton.setStyle(metalReactantButton.getStyle());

                productTwoButton.setText(selectedIon.getFormattedFormula());
                productTwoButton.setStyle(cationReactantButton.getStyle());
                return;
            }

            selectedMetal.setMoles(products[0].getMoles());
            selectedIon.setMoles(products[1].getMoles());

            metalReactantButton.setText(selectedMetal.getFormattedFormulaWithMoles());
            cationReactantButton.setText(selectedIon.getFormattedFormulaWithMoles());
            productOneButton.setText(products[0].getFormattedFormulaWithMoles());
            productTwoButton.setText(products[1].getFormattedFormulaWithMoles());

            productOneButton.setStyle(getProductButtonStyle(products[0]) + "-fx-text-fill: black;");
            productTwoButton.setStyle(getProductButtonStyle(products[1]) + "-fx-text-fill: black;");
        }
    }

    private String getProductButtonStyle(Cation cation) {
        String buttonColor = (cation.isReduced()) ? cation.getPrecipitateHex() : cation.getSolutionHex();

        return "-fx-background-color: " + buttonColor + "; -fx-background-radius: 15;";
    }

    private void displayCompounds(ArrayList<Cation> arrayList, Button button, boolean isCharged) {
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

                Button b;
                String buttonColorString;
                Color buttonColor;

                if (isCharged) {
                    b = new Button(arrayList.get(5 * j + i).toString());
                    buttonColorString = arrayList.get(5 * j + i).getSolutionHex();
                    buttonColor = arrayList.get(5 * j + i).getSolutionColor();
                } else {
                    b = new Button(arrayList.get(5 * j + i).getReducedForm().toString());
                    buttonColorString = arrayList.get(5 * j + i).getPrecipitateHex();
                    buttonColor = arrayList.get(5 * j + i).getPrecipitateColor();
                }

                String defaultStyle = "-fx-background-color: " + buttonColorString + "; -fx-background-radius: 10; -fx-text-fill: black;";

                //For hover style
                double brighteningFactor = 1.05;
                String hoverStyle = String.format("-fx-background-color: rgb(%s,%s,%s);",
                        Math.min(255, buttonColor.getRed() * 255 * brighteningFactor), Math.min(255, buttonColor.getGreen() * 255 * brighteningFactor),
                        Math.min(255, buttonColor.getBlue() * 255 * brighteningFactor))
                        + "-fx-background-radius: 10; -fx-text-fill: black;";

                b.setStyle(defaultStyle);
                b.setOnMouseEntered(e -> b.setStyle(hoverStyle));
                b.setOnMouseExited(e -> b.setStyle(defaultStyle));
                b.setPrefSize(80, 80);

                int finalJ = j;
                int finalI = i;
                b.setOnAction(e -> {
                    if (button.equals(metalReactantButton)) {
                        selectedMetal = arrayList.get(5 * finalJ + finalI).getReducedForm();
                    } else {
                        selectedIon = arrayList.get(5 * finalJ + finalI);
                    }

                    button.setText(b.getText());
                    button.setStyle(defaultStyle + "-fx-background-radius: 15;");
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

    public void selectMetal(ActionEvent event) {
        displayCompounds(Cation.getCations(), metalReactantButton, false);
    }

    public void selectCation(ActionEvent event) {
        displayCompounds(Cation.getCations(), cationReactantButton, true);
    }

    public void resetReaction(ActionEvent event) {
        Button[] equationButtons = new Button[]{metalReactantButton, cationReactantButton, productOneButton, productTwoButton};
        String[] buttonTexts = new String[]{"Metal", "Cation", "Product", "Product"};

        for (int i = 0; i < equationButtons.length; i++) {
            equationButtons[i].setStyle("");
            equationButtons[i].setText(buttonTexts[i]);
        }

        selectedMetal = null;
        selectedIon = null;
    }
}
