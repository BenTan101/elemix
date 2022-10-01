package sample.model;

import javafx.scene.paint.Color;

public enum PropertyColour {
    ROSE_PINK("#ffcdd2"),
    RED_PINK("#f8bbd0"),
    PALE_PURPLE("#e1bee7"),
    PALE_LAVENDER("#d1c4e9"),
    PALE_GREY_BLUE("#c5cae9"),
    ALICE_BLUE("#bbdefb"),
    BABY_BLUE("#b3e5fc"),
    PALE_CYAN("#b2ebf2"),
    PALE_TEAL("#b2dfdb"),
    GREY_GREEN("#c8e6c9"),
    PALE_CHARTREUSE("#dcedc8"),
    PALE_YELLOW_CHARTREUSE("#f0f4c3"),
    PASTEL_YELLOW("#fff9c4"),
    PASTEL_AMBER("#ffecb3"),
    PASTEL_ORANGE("#ffe0b2"),
    MANDARIN_RED("#ffccbc"),
    GREY_BROWN("#d7ccc8"),
    BLUE_GREY("#cfd8dc");

    private final String hex;
    private final Color hexColor;

    //CONSTRUCTOR
    PropertyColour(String hex) {
        this.hex = hex;
        this.hexColor = Color.web(hex);
    }

    //GETTERS
    public Color getHexColor() {
        return hexColor;
    }

    public String getFormattedRGB() {
        return String.format("rgb(%s,%s,%s)", hexColor.getRed() * 255, hexColor.getGreen() * 255, hexColor.getBlue() * 255);
    }

    public String getName() { return name(); }

    public static PropertyColour getColourFromHex(String hex) {
        for (PropertyColour propertyColour : PropertyColour.values()) {
            if (propertyColour.hex.equals(hex))
                return propertyColour;
        }
        return null;
    }

    @Override
    public String toString() {
        return hex;
    }
}
