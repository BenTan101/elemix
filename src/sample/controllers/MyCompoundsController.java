package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import sample.Main;
import sample.model.*;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyCompoundsController implements Initializable, Alertable {
    @FXML
    TableView<UserCompound> compoundsTable;
    @FXML
    TableColumn<Object, Object> nameColumn;
    @FXML
    TableColumn<Object, Object> formulaColumn;
    @FXML
    TableColumn<Object, Object> molarMassColumn;
    @FXML
    TableColumn<Object, Object> propertiesColumn;
    @FXML
    TableColumn<Object, Object> descriptionColumn;
    @FXML
    HBox bottomHBox;
    @FXML
    Button createButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Label headerLabel;

    private TextField nameTF = TextFields.createClearableTextField();
    private TextField formulaTF = TextFields.createClearableTextField();
    private TextArea descTextArea = new TextArea();
    private Label emptyMolarMassLabel = new Label();
    private VBox propertiesVBox = new VBox();
    private Button selectPropertiesButton;
    private Button newPropertyButton;
    private Button editPropertiesButton;

    private ResourceBundle messages = Main.getMessages();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newAlert(Alert.AlertType.INFORMATION, messages.getString("FormulaDisambiguation"), messages.getString("FormulaDisambiguationText"));

        //I18N
        nameColumn.setText(messages.getString("NameColumnHeader"));
        formulaColumn.setText(messages.getString("FormulaColumnHeader"));
        molarMassColumn.setText(messages.getString("MolarMassColumnHeader"));
        propertiesColumn.setText(messages.getString("PropertiesColumnHeader"));
        descriptionColumn.setText(messages.getString("DescriptionColumnHeader"));

        nameTF.setPromptText(messages.getString("NameColumnHeader"));
        formulaTF.setPromptText(messages.getString("FormulaColumnHeader"));
        descTextArea.setPromptText(messages.getString("DescriptionColumnHeader"));

        selectPropertiesButton = new Button(messages.getString("SelectProperties"));
        newPropertyButton = new Button(messages.getString("NewProperties"));
        editPropertiesButton = new Button(messages.getString("EditProperties"));

        createButton.setText(messages.getString("Create"));
        editButton.setText(messages.getString("Edit"));
        deleteButton.setText(messages.getString("Delete"));

        headerLabel.setText(messages.getString("MyCompoundsHeader"));

        nameColumn.setReorderable(false);
        formulaColumn.setReorderable(false);
        molarMassColumn.setReorderable(false);
        propertiesColumn.setReorderable(false);
        descriptionColumn.setReorderable(false);

        //Sets the maximum length of names, formulae and description.
        setWordCountThreshold(nameTF, 40, messages.getString("CompoundName"));
        setWordCountThreshold(formulaTF, 40, messages.getString("Formula"));
        setWordCountThreshold(descTextArea, 250, messages.getString("Description"));

        bottomHBox.setPadding(new Insets(bottomHBox.getSpacing() / 2.0));

        //initialise properties stuff first, and make them blue.
        Button[] propertyButtons = new Button[]{selectPropertiesButton, newPropertyButton, editPropertiesButton};

        String propertyButtonsDefaultStyle = "-fx-effect: null; -fx-font-size: 12; -fx-background-color: #61c6fa;";
        String propertyButtonsHoverStyle = "-fx-effect: null; -fx-font-size: 12; -fx-background-color: #82d1fa;";

        for (Button propertyButton : propertyButtons) {
            propertyButton.setStyle(propertyButtonsDefaultStyle);
            propertyButton.setOnMouseEntered(e -> propertyButton.setStyle(propertyButtonsHoverStyle));
            propertyButton.setOnMouseExited(e -> propertyButton.setStyle(propertyButtonsDefaultStyle));
        }

        //Initialise description TextArea
        descTextArea.setWrapText(true);
        String descTextAreaDefaultStyle = "-fx-border-color: #61c6fa; -fx-border-width: 0 0 2 0;" +
                "-fx-background-color: transparent; -fx-focus-color: transparent; " +
                "-fx-text-box-border: transparent; -fx-border-insets: 1;";
        descTextArea.setStyle(descTextAreaDefaultStyle);

        //Event handlers for property buttons
        newPropertyButton.setOnAction(e -> {
            Stage miniStage = new Stage();
            VBox vBox = new VBox();
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setPadding(new Insets(15));
            TextField propertyNameTF = TextFields.createClearableTextField();

            setWordCountThreshold(propertyNameTF, 20, messages.getString("Property"));

            HighlightedLabel sampleTextLabel = new HighlightedLabel();
            sampleTextLabel.textProperty().bind(propertyNameTF.textProperty());
            AtomicBoolean colourSelected = new AtomicBoolean(false);

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(15));
            gridPane.setHgap(15);
            gridPane.setVgap(15);

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < PropertyColour.values().length / 5 + 1; j++) {
                    if (5 * j + i >= PropertyColour.values().length)
                        break;

                    Button button = new Button();
                    button.setPrefSize(50, 50);
                    String buttonColor = PropertyColour.values()[(5 * j + i)].toString();

                    String defaultStyle = getButtonDefaultStyle(buttonColor);
                    String hoverStyle = getButtonHoverStyle(buttonColor);

                    button.setStyle(defaultStyle);
                    button.setOnMouseEntered(mouseEvent -> button.setStyle(hoverStyle));
                    button.setOnMouseExited(mouseEvent -> button.setStyle(defaultStyle));
                    button.setOnMousePressed(mouseEvent -> button.setStyle(defaultStyle));

                    int finalJ = j;
                    int finalI = i;
                    button.setOnAction(event -> {
                        sampleTextLabel.setStyle("-fx-text-fill: black;");
                        sampleTextLabel.setPropertyColour(PropertyColour.values()[(5 * finalJ + finalI)]);
                        colourSelected.set(true);
                    });

                    gridPane.add(button, i, j);
                }
            }

            vBox.getChildren().addAll(new Label(messages.getString("PropertyName")), propertyNameTF, sampleTextLabel);

            Button OKButton = new Button("OK");
            OKButton.setPrefSize(40, 40);
            OKButton.setStyle("-fx-effect: null");
            OKButton.setOnAction(event -> {
                if (sampleTextLabel.getText().isEmpty()) {
                    newAlert(Alert.AlertType.ERROR, messages.getString("NoTextInput"), messages.getString("NoTextInputText"));
                } else if (!colourSelected.get()) {
                    newAlert(Alert.AlertType.ERROR, messages.getString("NoColourSelected"), messages.getString("NoColourSelectedText"));
                } else if (containsPropertyWithName(sampleTextLabel.getText())) {
                    repeatNameAlert();
                } else {
                    UserCompound.addNewProperty(sampleTextLabel);
                    miniStage.close();
                    newAlert(Alert.AlertType.INFORMATION, messages.getString("PropertyCreated"), "");
                }
            });

            vBox.getChildren().addAll(gridPane, OKButton);

            Scene scene = new Scene(vBox);
            Main.setCurrentStyle(scene);
            miniStage.setTitle(messages.getString("NewProperty"));
            miniStage.initModality(Modality.APPLICATION_MODAL);
            miniStage.setScene(scene);
            miniStage.setResizable(false);
            miniStage.show();
        });

        selectPropertiesButton.setOnAction(e -> {
            if (!isCompoundSelected()) {
                noCompoundSelectedAlert();
                return;
            }

            if (UserCompound.getAllAddedProperties().size() == 0) {
                newAlert(Alert.AlertType.WARNING, messages.getString("NoAddedProperties"), messages.getString("NoAddedPropertiesText"));
                return;
            }

            UserCompound selectedUserCompound = getSelectedCompound();

            Stage miniStage = new Stage();
            VBox vBox = new VBox();
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setPadding(new Insets(15));

            Text nameLabel = new Text(selectedUserCompound.getName());
            nameLabel.setStyle("-fx-font-size: 15;");
            vBox.getChildren().add(nameLabel);

            double maxCheckBoxWidth = 0;
            ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
            UserCompound.getAllAddedProperties().sort(Comparator.comparing(Labeled::getText));
            for (HighlightedLabel highlightedLabel : UserCompound.getAllAddedProperties()) {
                CheckBox checkBox = new CheckBox(highlightedLabel.getText());
                checkBox.setStyle(highlightedLabel.getStyle());

                Text temporaryText = new Text(highlightedLabel.getText());
                checkBox.setPrefWidth(30 + temporaryText.getLayoutBounds().getWidth());

                checkBoxes.add(checkBox);
                vBox.getChildren().add(checkBox);

                maxCheckBoxWidth = Math.max(maxCheckBoxWidth, checkBox.getPrefWidth());
                if (selectedUserCompound.getProperties().contains(highlightedLabel)) {
                    checkBox.setSelected(true);
                }
            }

            Button selectAllButton = new Button(messages.getString("SelectAll"));
            Button deselectAllButton = new Button(messages.getString("DeselectAll"));
            selectAllButton.setStyle("-fx-effect: null; -fx-font-size: 12;");
            deselectAllButton.setStyle("-fx-effect: null; -fx-font-size: 12;");

            selectAllButton.setOnAction(e2 -> {
                for (CheckBox checkBox : checkBoxes) {
                    checkBox.setSelected(true);
                }
            });

            deselectAllButton.setOnAction(e2 -> {
                for (CheckBox checkBox : checkBoxes) {
                    checkBox.setSelected(false);
                }
            });

            HBox buttonHBox = new HBox(selectAllButton, deselectAllButton);
            buttonHBox.setSpacing(10);
            vBox.getChildren().add(buttonHBox);

            vBox.setPrefWidth(Math.max(350, maxCheckBoxWidth + 15 + vBox.getInsets().getLeft() * 2));

            Button OKButton = new Button(messages.getString("OK"));
            OKButton.setPrefSize(30, 30);
            OKButton.setStyle("-fx-effect: null; -fx-font-size: 12;");
            OKButton.setOnAction(event -> {
                miniStage.close();
                getSelectedCompound().getProperties().clear();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        getSelectedCompound().addProperty(UserCompound.getPropertyByName(checkBox.getText()));
                    }
                }
                updateTable();
            });

            Label emptySpacingLabel = new Label();
            emptySpacingLabel.setPrefHeight(15);
            vBox.getChildren().addAll(emptySpacingLabel, OKButton);
            Scene scene = new Scene(vBox);
            Main.setCurrentStyle(scene);
            miniStage.setTitle(messages.getString("NewProperty"));
            miniStage.initModality(Modality.APPLICATION_MODAL);
            miniStage.setScene(scene);
            miniStage.setResizable(false);
            miniStage.show();
        });

        editPropertiesButton.setOnAction(e -> {
            if (UserCompound.getAllAddedProperties().size() == 0) {
                newAlert(Alert.AlertType.WARNING, messages.getString("NoAddedProperties"), messages.getString("NoAddedPropertiesText"));
                return;
            }

            Stage miniStage = new Stage();
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(10);
            gridPane.setAlignment(Pos.CENTER_LEFT);
            gridPane.setPadding(new Insets(15));

            Text nameLabel = new Text(messages.getString("EditOrDeleteProperties"));
            nameLabel.setStyle("-fx-font-size: 15;");
            gridPane.add(nameLabel, 0, 0);

            UserCompound.getAllAddedProperties().sort(Comparator.comparing(Labeled::getText));
            for (int i = 1; i < UserCompound.getAllAddedProperties().size() + 1; i++) {
                Button editButton = new Button(messages.getString("Edit"));
                Button deleteButton = new Button(messages.getString("Delete"));
                editButton.setStyle("-fx-effect: null; -fx-font-size: 12;");
                deleteButton.setStyle("-fx-effect: null; -fx-font-size: 12;");

                HighlightedLabel currentHighlightedLabel = UserCompound.getAllAddedProperties().get(i - 1);
                gridPane.add(currentHighlightedLabel, 0, i);
                gridPane.add(editButton, 1, i);
                gridPane.add(deleteButton, 2, i);

                editButton.setOnAction(event -> {
                    Stage miniMiniStage = new Stage();
                    VBox vBox = new VBox();
                    vBox.setSpacing(5);
                    vBox.setAlignment(Pos.CENTER_LEFT);
                    vBox.setPadding(new Insets(15));
                    TextField propertyNameTF = TextFields.createClearableTextField();

                    propertyNameTF.setText(currentHighlightedLabel.getText());
                    int lengthThreshold = 20;
                    setWordCountThreshold(propertyNameTF, lengthThreshold, messages.getString("Property"));

                    HighlightedLabel sampleTextLabel = new HighlightedLabel();
                    sampleTextLabel.setPropertyColour(currentHighlightedLabel.getPropertyColour());
                    sampleTextLabel.textProperty().bind(propertyNameTF.textProperty());
                    AtomicBoolean colourSelected = new AtomicBoolean(false);

                    GridPane gridPane2 = new GridPane();
                    gridPane2.setPadding(new Insets(15));
                    gridPane2.setHgap(15);
                    gridPane2.setVgap(15);

                    for (int k = 0; k < 5; k++) {
                        for (int l = 0; l < PropertyColour.values().length / 5 + 1; l++) {
                            if (5 * l + k >= PropertyColour.values().length)
                                break;

                            Button button = new Button();
                            button.setPrefSize(50, 50);
                            String buttonColor = PropertyColour.values()[(5 * l + k)].toString();

                            String defaultStyle = getButtonDefaultStyle(buttonColor);
                            String hoverStyle = getButtonHoverStyle(buttonColor);

                            button.setStyle(defaultStyle);
                            button.setOnMouseEntered(mouseEvent -> button.setStyle(hoverStyle));
                            button.setOnMouseExited(mouseEvent -> button.setStyle(defaultStyle));
                            button.setOnMousePressed(mouseEvent -> button.setStyle(defaultStyle));

                            button.setOnAction(event3 -> {
                                sampleTextLabel.setPropertyColour(Objects.requireNonNull(PropertyColour.getColourFromHex(buttonColor)));
                                colourSelected.set(true);
                            });

                            gridPane2.add(button, k, l);
                        }
                    }

                    vBox.getChildren().addAll(new Label(messages.getString("PropertyName")), propertyNameTF, sampleTextLabel, gridPane2);

                    Button OKButton = new Button(messages.getString("OK"));
                    OKButton.setPrefSize(40, 40);
                    OKButton.setStyle("-fx-effect: null;");
                    OKButton.setOnAction(event2 -> {
                        if (containsPropertyWithName(propertyNameTF.getText()) && !currentHighlightedLabel.getText().equals(propertyNameTF.getText())) {
                            repeatNameAlert();
                            return;
                        }

                        for (UserCompound userCompound : UserCompound.getAllUserCompounds()) {
                            for (HighlightedLabel highlightedLabel : userCompound.getProperties()) {
                                if (colourSelected.get() && highlightedLabel.equals(currentHighlightedLabel)) {
                                    highlightedLabel.setPropertyColour(sampleTextLabel.getPropertyColour());
                                }

                                if (highlightedLabel.getText().equals(currentHighlightedLabel.getText())) {
                                    highlightedLabel.setText(sampleTextLabel.getText());
                                }
                            }
                        }

                        for (HighlightedLabel highlightedLabel : UserCompound.getAllAddedProperties()) {
                            if (colourSelected.get() && highlightedLabel.equals(currentHighlightedLabel)) {
                                highlightedLabel.setPropertyColour(sampleTextLabel.getPropertyColour());
                            }

                            if (highlightedLabel.getText().equals(currentHighlightedLabel.getText())) {
                                highlightedLabel.setText(sampleTextLabel.getText());
                            }
                        }
                        miniMiniStage.close();
                        miniStage.close();
                        updateTable();
                    });

                    vBox.getChildren().add(OKButton);

                    Scene scene = new Scene(vBox);
                    Main.setCurrentStyle(scene);
                    miniMiniStage.setTitle(messages.getString("SelectProperties"));
                    miniMiniStage.initModality(Modality.APPLICATION_MODAL);
                    miniMiniStage.setScene(scene);
                    miniMiniStage.setResizable(false);
                    miniMiniStage.show();
                });

                deleteButton.setOnAction(event -> {
                    Alert deleteProperty = new Alert(Alert.AlertType.CONFIRMATION);
                    deleteProperty.setHeaderText(messages.getString("DeleteProperty"));
                    deleteProperty.setContentText(messages.getString("DeletePropertiesText"));
                    deleteProperty.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

                    deleteProperty.getButtonTypes().set(1, new ButtonType(messages.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE));

                    Optional<ButtonType> result = deleteProperty.showAndWait();

                    if (!(result.isPresent() && result.get() == ButtonType.OK))
                        return;

                    for (UserCompound userCompound : UserCompound.getAllUserCompounds()) {
                        for (HighlightedLabel highlightedLabel : userCompound.getProperties()) {
                            if (highlightedLabel.equals(currentHighlightedLabel)) {
                                userCompound.getProperties().remove(highlightedLabel);
                                break;
                            }
                        }
                    }

                    for (HighlightedLabel highlightedLabel : UserCompound.getAllAddedProperties()) {
                        if (highlightedLabel.equals(currentHighlightedLabel)) {
                            UserCompound.getAllAddedProperties().remove(highlightedLabel);
                            break;
                        }
                    }

                    updateTable();
                    miniStage.close();
                });
            }

            Scene scene = new Scene(gridPane);
            Main.setCurrentStyle(scene);
            miniStage.setTitle(messages.getString("EditProperties"));
            miniStage.initModality(Modality.APPLICATION_MODAL);
            miniStage.setScene(scene);
            miniStage.setResizable(false);
            miniStage.show();
        });

        propertiesVBox.getChildren().addAll(newPropertyButton, selectPropertiesButton, editPropertiesButton);
        propertiesVBox.setAlignment(Pos.TOP_CENTER);
        propertiesVBox.setSpacing(5);

        //MORE I18N INITIALISATION
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("TFName"));
        formulaColumn.setCellValueFactory(new PropertyValueFactory<>("TFFormattedFormula"));
        molarMassColumn.setCellValueFactory(new PropertyValueFactory<>("molecularMass"));
        propertiesColumn.setCellValueFactory(new PropertyValueFactory<>("formattedProperties"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("TFDescription"));

        //DIMENSION FOMRATTING, WIDTH PROPERTY LISTENERS
        descTextArea.setPrefHeight(bottomHBox.getPrefHeight());

        compoundsTable.widthProperty().addListener(observable -> {
            //RATIOS OF COLUMN WIDTHS
            nameColumn.setPrefWidth(compoundsTable.getWidth() * 0.2);
            formulaColumn.setPrefWidth(compoundsTable.getWidth() * 0.15);
            molarMassColumn.setPrefWidth(compoundsTable.getWidth() * 0.15);
            propertiesColumn.setPrefWidth(compoundsTable.getWidth() * 0.2);
            descriptionColumn.setPrefWidth(compoundsTable.getWidth() * 0.3 - 17.4); //17.4 is the space needed for a scrollbar
        });

        nameColumn.widthProperty().addListener(observable -> nameTF.setPrefWidth(nameColumn.getWidth() - bottomHBox.getSpacing()));
        formulaColumn.widthProperty().addListener(observable -> formulaTF.setPrefWidth(formulaColumn.getWidth() - bottomHBox.getSpacing()));
        molarMassColumn.widthProperty().addListener(observable -> emptyMolarMassLabel.setPrefWidth(molarMassColumn.getWidth() - bottomHBox.getSpacing()));
        propertiesColumn.widthProperty().addListener(observable -> {
            double prefWidth = propertiesColumn.getWidth() - bottomHBox.getSpacing();
            propertiesVBox.setPrefWidth(prefWidth);
            selectPropertiesButton.setPrefWidth(prefWidth);
            newPropertyButton.setPrefWidth(prefWidth);
            editPropertiesButton.setPrefWidth(prefWidth);
        });
        descriptionColumn.widthProperty().addListener(observable -> descTextArea.setPrefWidth(descriptionColumn.getWidth() - bottomHBox.getSpacing()));

        bottomHBox.getChildren().addAll(nameTF, formulaTF, emptyMolarMassLabel, propertiesVBox, descTextArea);
        bottomHBox.setAlignment(Pos.TOP_LEFT);

        updateTable();
    }

    //UPDATERS
    private void updateTable() {
        compoundsTable.getItems().removeAll(compoundsTable.getItems());
        for (UserCompound userCompound : UserCompound.getAllUserCompounds()) {
            compoundsTable.getItems().add(userCompound);
        }
    }

    public void clearAll() {
        nameTF.clear();
        formulaTF.clear();
        descTextArea.clear();
    }

    public void returnToMenu(ActionEvent event) {
        Main.openMenu();
    }

    //GETTERS
    private String getButtonHoverStyle(String buttonColor) {
        return "-fx-effect: dropshadow( gaussian , rgba(66,34,2,0.9) , 15,0,-3,5 );"
                + "-fx-background-color: " + buttonColor + ";\n"
                + "-fx-background-radius: 5;";
    }

    private String getButtonDefaultStyle(String buttonColor) {
        return "-fx-effect: dropshadow( gaussian , rgba(66,34,2,0.4) , 15,0,-3,5 );"
                + "-fx-background-color: " + buttonColor + ";\n"
                + "-fx-background-radius: 5;";
    }

    public String getName() {
        return nameTF.getText();
    }

    public String getFormula() {
        return formulaTF.getText();
    }

    public String getDescription() {
        return descTextArea.getText();
    }

    private UserCompound getSelectedCompound() {
        return compoundsTable.getSelectionModel().getSelectedItem();
    }

    //SETTERS
    private void setWordCountThreshold(TextField textField, int threshold, String fieldName) {
        textField.textProperty().addListener(event -> {
            if (textField.getText().length() > threshold) {
                textField.setText(textField.getText().substring(0, threshold));
                if (messages.getString("MaxLengthText").contains("%s"))
                    newAlert(Alert.AlertType.WARNING, messages.getString("MaxLength"),
                            String.format(messages.getString("MaxLengthText"), fieldName, threshold));
                else
                    newAlert(Alert.AlertType.WARNING, messages.getString("MaxLength"),
                            String.format(messages.getString("MaxLengthText"), threshold));
            }
        });
    }

    private void setWordCountThreshold(TextArea textArea, int threshold, String fieldName) {
        textArea.textProperty().addListener(event -> {
            if (textArea.getText().length() > threshold) {
                textArea.setText(textArea.getText().substring(0, threshold));
                newAlert(Alert.AlertType.WARNING, messages.getString("MaxLength"),
                        String.format(messages.getString("MaxLengthText"), fieldName, threshold));
            }
        });
    }

    //COMPOUND CREATION
    public void createCompound(ActionEvent event) {
        if (areNameOrFormulaTFsEmpty()) {
            newAlert(Alert.AlertType.WARNING, messages.getString("EmptyFields"), messages.getString("EmptyFieldsText"));
            return;
        }

        if (!new Compound().isValidCompound(getFormula())) {
            invalidFormulaAlert();
            return;
        }
        try {
            UserCompound.getAllUserCompounds().add(new UserCompound(getName(), getFormula(), getDescription()));
            newAlert(Alert.AlertType.INFORMATION, messages.getString("CompoundCreated"), "");
        } catch (NumberFormatException e) {
            invalidFormulaAlert();
        }
        updateTable();
        clearAll();
    }

    public void editCompound(ActionEvent event) {
        if (!isCompoundSelected()) {
            noCompoundSelectedAlert();
            return;
        }

        if (areAllTFsEmpty()) {
            newAlert(Alert.AlertType.WARNING, messages.getString("NoFields"), messages.getString("NoFieldsText"));
            return;
        }

        if (!nameTF.getText().isEmpty())
            getSelectedCompound().setName(nameTF.getText());

        if (!formulaTF.getText().isEmpty()) {
            if (!new Compound().isValidCompound(getFormula())) {
                invalidFormulaAlert();
                return;
            }
            getSelectedCompound().setFormula(formulaTF.getText());
        }

        if (!descTextArea.getText().isEmpty())
            getSelectedCompound().setDescription(descTextArea.getText());

        updateTable();
        clearAll();
        newAlert(Alert.AlertType.INFORMATION, messages.getString("CompoundEdited"), "");
    }

    public void deleteCompound(ActionEvent event) {
        if (!isCompoundSelected()) {
            noCompoundSelectedAlert();
            return;
        }

        Alert deleteCompound = new Alert(Alert.AlertType.CONFIRMATION);
        deleteCompound.setHeaderText(messages.getString("DeleteCompound"));
        deleteCompound.setContentText(messages.getString("DeleteCompoundText"));
        deleteCompound.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        deleteCompound.getButtonTypes().set(1, new ButtonType(messages.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE));

        Optional<ButtonType> result = deleteCompound.showAndWait();

        if (!(result.isPresent() && result.get() == ButtonType.OK))
            return;

        UserCompound.getAllUserCompounds().remove(getSelectedCompound());
        updateTable();
    }

    //VALIDATORS
    private boolean areNameOrFormulaTFsEmpty() {
        return nameTF.getText().isEmpty() || formulaTF.getText().isEmpty();
    }

    private boolean areAllTFsEmpty() {
        return nameTF.getText().isEmpty() && formulaTF.getText().isEmpty() && descTextArea.getText().isEmpty();
    }

    private boolean containsPropertyWithName(String name) {
        for (HighlightedLabel property : UserCompound.getAllAddedProperties()) {
            if (property.getText().equals(name)) return true;
        }
        return false;
    }

    private boolean isCompoundSelected() {
        return !(getSelectedCompound() == null);
    }

    //ALERTS
    private void invalidFormulaAlert() {
        newAlert(Alert.AlertType.ERROR, messages.getString("InvalidFormula"), messages.getString("InvalidFormulaText"));
    }

    private void noCompoundSelectedAlert() {
        newAlert(Alert.AlertType.WARNING, messages.getString("NoCompoundSelected"), messages.getString("NoCompoundSelectedText"));
    }

    private void repeatNameAlert() {
        newAlert(Alert.AlertType.ERROR, messages.getString("RepeatName"), messages.getString("RepeatNameText"));
    }
}
