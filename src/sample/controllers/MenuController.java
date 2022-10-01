package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.model.UserCompound;

import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Text displacementDescriptionText;
    @FXML
    private Text acidBaseDescriptionText;
    @FXML
    private Text myCompoundsDescriptionText;
    @FXML
    private HBox displacementButtonHBox;
    @FXML
    private HBox acidBaseButtonHBox;
    @FXML
    private HBox myCompoundsButtonHBox;
    @FXML
    private Label displacementHeader;
    @FXML
    private Label acidBaseHeader;
    @FXML
    private Label myCompoundsHeader;

    private ResourceBundle messages = Main.getMessages();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //I18N
        displacementDescriptionText.setText(messages.getString("DisplacementText"));
        acidBaseDescriptionText.setText(messages.getString("AcidBaseText"));
        myCompoundsDescriptionText.setText(messages.getString("MyCompoundsText"));

        displacementHeader.setText(messages.getString("DisplacementHeader"));
        acidBaseHeader.setText(messages.getString("AcidBaseHeader"));
        myCompoundsHeader.setText(messages.getString("MyCompoundsHeader"));

        Text[] descriptionTexts = new Text[]{displacementDescriptionText, acidBaseDescriptionText, myCompoundsDescriptionText};
        for (Text descriptionText : descriptionTexts) {
            displacementButtonHBox.widthProperty().addListener(e -> {
                descriptionText.setWrappingWidth(displacementButtonHBox.getWidth()
                        - displacementButtonHBox.getSpacing() * 2 - displacementButtonHBox.getPadding().getLeft() * 2 - 150 - 30);
            });
        }

        HBox[] buttonHBoxes = new HBox[]{displacementButtonHBox, acidBaseButtonHBox, myCompoundsButtonHBox};

        for (HBox buttonHBox : buttonHBoxes) {
            buttonHBox.setStyle("-fx-background-color: transparent;");
        }
    }

    public void openDisplacementTab(ActionEvent event) {
        Main.openDisplacement();
    }

    public void openAcidBaseTab(ActionEvent event) {
        Main.openAcidBase();
    }

    public void openMyCompoundsTab(ActionEvent event) {
        Main.openMyCompounds();
    }

    //For Info and Help tabs
    private Label getFormattedLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 25;");
        return label;
    }

    private TextArea getFormattedTextArea(String text, int prefHeight) {
        TextArea textArea = new TextArea(text);

        textArea.setPrefHeight(prefHeight);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        String descTextAreaDefaultStyle = "-fx-border-color: #61c6fa; -fx-border-width: 0 0 2 0;" +
                "-fx-background-color: transparent; -fx-focus-color: transparent; " +
                "-fx-text-box-border: transparent; -fx-border-insets: 1; -fx-font-size: 15;";
        textArea.setStyle(descTextAreaDefaultStyle);

        return textArea;
    }

    public void openHelp(ActionEvent event) {
        Stage helpStage = new Stage();
        helpStage.setTitle(messages.getString("HelpHeader"));
        VBox VBox = new VBox();
        VBox.setPadding(new Insets(20));
        VBox.setSpacing(20);

        Label headerLabel = getFormattedLabel(messages.getString("NeedHelpHeader"));

        TextArea textArea = getFormattedTextArea(messages.getString("HelpDescription"), 375);


        VBox.getChildren().addAll(headerLabel, textArea);
        Scene scene = new Scene(VBox, Region.USE_PREF_SIZE, 400);
        helpStage.setScene(scene);
        helpStage.setResizable(false);
        helpStage.initModality(Modality.APPLICATION_MODAL);
        Main.setCurrentStyle(scene);
        helpStage.show();
    }

    public void openInfo(ActionEvent event) {
        Stage infoStage = new Stage();
        infoStage.setTitle(messages.getString("InfoHeader"));
        VBox VBox = new VBox();
        VBox.setPadding(new Insets(20));
        VBox.setSpacing(20);

        Label headerLabel = getFormattedLabel(messages.getString("AboutProgrammerHeader"));

        TextArea textArea = getFormattedTextArea(messages.getString("InfoDescription"), 250);

        VBox.getChildren().addAll(headerLabel, textArea);
        Scene scene = new Scene(VBox, 500, 275);

        infoStage.setScene(scene);
        infoStage.setResizable(false);
        infoStage.initModality(Modality.APPLICATION_MODAL);
        Main.setCurrentStyle(scene);
        infoStage.show();
    }

    public void openSettings(ActionEvent event) {
        Stage settingsStage = new Stage();
        settingsStage.setTitle(messages.getString("SettingsHeader"));
        VBox VBox = new VBox();
        VBox.setPadding(new Insets(20));
        VBox.setSpacing(10);

        Label headerLabel = getFormattedLabel(messages.getString("SettingsHeader"));
        VBox.getChildren().addAll(headerLabel);

        HBox languageHBox = new HBox();
        languageHBox.setAlignment(Pos.CENTER_LEFT);
        languageHBox.setSpacing(10);

        Label languageLabel = new Label(messages.getString("Language"));
        languageLabel.setStyle("-fx-font-size: 15;");
        ComboBox<String> languages = new ComboBox<String>();

        languages.getItems().addAll("English", "Français", "Deutsch", "Italiano");
        Collections.sort(languages.getItems());
        languageHBox.getChildren().addAll(languageLabel, languages);

        Button OKButton = new Button(messages.getString("OK"));

        OKButton.setOnAction(e -> {
            if (languages.getSelectionModel().getSelectedItem() != null) {
                switch (languages.getSelectionModel().getSelectedItem()) {
                    case "English":
                        Main.setLocale(new Locale("en", "UK"));
                        break;
                    case "Français":
                        Main.setLocale(new Locale("fr", "FR"));
                        break;
                    case "Deutsch":
                        Main.setLocale(new Locale("de", "DE"));
                        break;
                    case "Italiano":
                        Main.setLocale(new Locale("it", "IT"));
                }
            }

            settingsStage.close();
            UserCompound.passPropertiesToFile();
            UserCompound.passUCsToFile();
            Main.showMainStage();
        });

        VBox.getChildren().addAll(languageHBox, new Label(), OKButton);

        Scene scene = new Scene(VBox, 300, 175);

        settingsStage.setScene(scene);
        settingsStage.setResizable(false);
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        Main.setCurrentStyle(scene);
        settingsStage.show();
    }
}
