package sample.model;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public interface Alertable {
    default void newAlert(Alert.AlertType alertType, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
