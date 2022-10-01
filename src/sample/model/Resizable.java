package sample.model;

import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public interface Resizable {
    default void setResizableFont(Button button, Integer defaultFontSize) {
        double maxTextWidth = button.getPrefWidth() * 0.85;
        Font defaultFont = Font.font(defaultFontSize);

        Text temporaryText = new Text(button.getText());
        temporaryText.setFont(defaultFont);

        double textWidth = temporaryText.getLayoutBounds().getWidth() + 20;

        if (textWidth > maxTextWidth) {
            double newFontSize = defaultFontSize * maxTextWidth / textWidth;
            button.setStyle(button.getStyle() + "-fx-font-size:" + newFontSize + ";");
        } else {
            button.setStyle(button.getStyle() + "-fx-font-size:" + defaultFontSize + ";");
        }
    }
}
