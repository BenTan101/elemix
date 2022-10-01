package sample.model;

import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class UserCompound extends Compound {
    private static ArrayList<HighlightedLabel> allAddedProperties;
    private static ArrayList<UserCompound> allUserCompounds;

    private static String propertiesFileLocation;
    private static String userCompoundsFileLocation;

    private ArrayList<HighlightedLabel> properties;
    private String description;

    //CONSTRUCTORS
    public UserCompound(String name, String formula) {
        super(name, formula);
        this.properties = new ArrayList<HighlightedLabel>();
    }

    public UserCompound(String name, String formula, String description) {
        this(name, formula);
        this.description = description;
    }

    //FILE IO
    public static void initialiseProperties() {
        File properties = new File("Properties.csv");
        File UCs = new File("UserCompounds.csv");

        try {
            properties.createNewFile();
            UCs.createNewFile();

            propertiesFileLocation = properties.getAbsolutePath();
            userCompoundsFileLocation = UCs.getAbsolutePath();

            allAddedProperties = new ArrayList<HighlightedLabel>();

            BufferedReader br = new BufferedReader(new FileReader(propertiesFileLocation));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                allAddedProperties.add(new HighlightedLabel(tokens[0].substring(1, tokens[0].length() - 1), PropertyColour.valueOf(tokens[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialiseUserCompounds() {
        allUserCompounds = new ArrayList<UserCompound>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(userCompoundsFileLocation));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                UserCompound UC = new UserCompound(tokens[0].substring(1, tokens[0].length() - 1), tokens[1]);
                for (int i = 2; i < tokens.length - 1; i++) {
                    for (HighlightedLabel highlightedLabel : allAddedProperties) {
                        if (highlightedLabel.getText().equals(tokens[i].substring(1, tokens[i].length() - 1)))
                            UC.addProperty(highlightedLabel);
                    }
                }
                UC.setDescription(tokens[tokens.length - 1].substring(1, tokens[tokens.length - 1].length() - 1));
                allUserCompounds.add(UC);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void passPropertiesToFile() {
        try {
            if (allAddedProperties == null) return;

            PrintWriter output = new PrintWriter(new File(propertiesFileLocation));

            for (HighlightedLabel property : allAddedProperties) {
                output.println(String.format("\"%s\",%s", property.getText(), property.getPropertyColour().getName()));
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void passUCsToFile() {
        try {
            if (allUserCompounds == null) return;

            PrintWriter output = new PrintWriter(new File(userCompoundsFileLocation));

            for (UserCompound UC : allUserCompounds) {
                output.print(String.format("\"%s\",%s,", UC.getName(), UC.getFormulaWithCharge()));
                for (HighlightedLabel property : UC.properties) {
                    output.print(String.format("\"%s\",", property));
                }
                output.println('"' + UC.description + '"');
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //GETTERS
    public TextArea getFormattedTextArea(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setPrefHeight(50);
        textArea.setWrapText(true);
        String descTextAreaDefaultStyle = "-fx-border-color: transparent; -fx-border-width: 0 0 2 0;" +
                "-fx-background-color: transparent; -fx-focus-color: transparent; " +
                "-fx-text-box-border: transparent; -fx-border-insets: 1;";
        textArea.setStyle(descTextAreaDefaultStyle);
        textArea.setEditable(false);
        return textArea;
    }

    public TextArea getTFName() {
        return getFormattedTextArea(getName());
    }

    public TextArea getTFFormattedFormula() {
        return getFormattedTextArea(getFormattedFormula());
    }

    public FlowPane getFormattedProperties() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);

        flowPane.widthProperty().addListener(observable -> {
            flowPane.setPrefWrapLength(flowPane.getWidth());
            flowPane.setPrefHeight(Region.USE_PREF_SIZE);
        });
        if (properties.size() > 0)
            properties.sort(Comparator.comparing(Labeled::getText));

        flowPane.getChildren().addAll(properties);
        return flowPane;
    }

    public TextArea getTFDescription() {
        return getFormattedTextArea(getDescription());
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<HighlightedLabel> getProperties() {return properties;}

    public static ArrayList<HighlightedLabel> getAllAddedProperties() {
        return allAddedProperties;
    }

    public static ArrayList<UserCompound> getAllUserCompounds() {
        return allUserCompounds;
    }

    public static HighlightedLabel getPropertyByName(String propertyName) {
        for (HighlightedLabel highlightedLabel : allAddedProperties) {
            if (highlightedLabel.getText().equals(propertyName))
                return highlightedLabel;
        }
        return null;
    }

    //SETTERS
    public void addProperty(HighlightedLabel propertyLabel) {
        properties.add(new HighlightedLabel(propertyLabel));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void addNewProperty(HighlightedLabel propertyLabel) {
        allAddedProperties.add(new HighlightedLabel(propertyLabel));
        allAddedProperties.sort(Comparator.comparing(Labeled::getText));
    }
}
