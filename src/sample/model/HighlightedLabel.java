package sample.model;

import javafx.scene.control.Label;

public class HighlightedLabel extends Label {
    private PropertyColour propertyColour;

    //CONSTRUCTORS
    public HighlightedLabel() {
        super();
    }

    public HighlightedLabel(String string) {
        super(string);
    }

    public HighlightedLabel(HighlightedLabel highlightedLabel) {
        this(highlightedLabel.getText(), highlightedLabel.getPropertyColour());
    }

    public HighlightedLabel(String string, PropertyColour propertyColour) {
        this(string);
        this.propertyColour = propertyColour;
        this.setStyle("-fx-background-color: " + propertyColour.getFormattedRGB() + ";");
    }

    //GETTER
    public PropertyColour getPropertyColour() {
        return propertyColour;
    }

    //SETTER
    public void setPropertyColour(PropertyColour propertyColour) {
        this.propertyColour = propertyColour;
        this.setStyle("-fx-background-color: " + propertyColour.getFormattedRGB() + ";");
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HighlightedLabel))
            return false;
        HighlightedLabel otherHighlightedLabel = (HighlightedLabel) o;
        return this.getText().equals(otherHighlightedLabel.getText()) && this.getPropertyColour().equals(otherHighlightedLabel.getPropertyColour());
    }
}
