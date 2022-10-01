package sample;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.model.AcidBase;
import sample.model.Cation;
import sample.model.Element;
import sample.model.UserCompound;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    private static Scene scene;
    private static Locale locale;
    private static Stage thisPrimaryStage;
    private static String style;

    @Override
    public void start(Stage primaryStage) throws Exception {
        style = "Light";

        final int SPLASH_SCREEN_WIDTH = 450;
        final int SPLASH_SCREEN_HEIGHT = 300;

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(20));
        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setFill(Paint.valueOf("#fcbf7e"));
        rectangle.setY(SPLASH_SCREEN_HEIGHT * 0.65);

        ImageView elemixIV = new ImageView(getClass().getResource("/sample/view/Icons/ElemixIcon.png").toExternalForm());
        ImageView conicalFlaskIV = new ImageView(getClass().getResource("/sample/view/Icons/ConicalFlaskIcon.png").toExternalForm());
        elemixIV.setFitWidth(SPLASH_SCREEN_WIDTH * 0.7);
        elemixIV.setPreserveRatio(true);
        conicalFlaskIV.setFitHeight(SPLASH_SCREEN_HEIGHT * 0.4);
        conicalFlaskIV.setPreserveRatio(true);
        conicalFlaskIV.setX(SPLASH_SCREEN_WIDTH * 0.65);
        conicalFlaskIV.setY(SPLASH_SCREEN_HEIGHT * 0.05);

        anchorPane.setStyle("-fx-background-color: #e9eef2;");

        Scene splashScene = new Scene(anchorPane, SPLASH_SCREEN_WIDTH, SPLASH_SCREEN_HEIGHT);
        Stage splashStage = new Stage();
        splashStage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/ConicalFlaskIcon.png")));
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);

        anchorPane.getChildren().addAll(elemixIV, conicalFlaskIV, rectangle);

        FadeTransition rectFadeTransition = new FadeTransition(Duration.millis(3000), rectangle);
        rectFadeTransition.setFromValue(1.0f);
        rectFadeTransition.setToValue(0.3f);
        rectFadeTransition.setCycleCount(4);
        rectFadeTransition.setAutoReverse(true);

        TranslateTransition rectTranslateTransition = new TranslateTransition(Duration.millis(3000), rectangle);
        rectTranslateTransition.setFromX(45);
        rectTranslateTransition.setToX(SPLASH_SCREEN_WIDTH - rectangle.getWidth() - 30 - 20);
        rectTranslateTransition.setCycleCount(4);
        rectTranslateTransition.setAutoReverse(true);

        RotateTransition rectRotateTransition = new RotateTransition(Duration.millis(3000), rectangle);
        rectRotateTransition.setByAngle(180f);
        rectRotateTransition.setCycleCount(4);
        rectRotateTransition.setAutoReverse(true);

        Rectangle bar = new Rectangle(SPLASH_SCREEN_WIDTH, 15);
        bar.setFill(Paint.valueOf("#fcbf7e"));
        bar.setArcHeight(5);
        bar.setArcWidth(15);
        bar.setX(-bar.getWidth());
        bar.setY(SPLASH_SCREEN_HEIGHT * 0.85);
        anchorPane.getChildren().add(bar);

        TranslateTransition barTranslateTransition = new TranslateTransition(Duration.millis(12000), bar);
        barTranslateTransition.setToX(SPLASH_SCREEN_WIDTH);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(rectFadeTransition, rectTranslateTransition,
                rectRotateTransition, barTranslateTransition);
        parallelTransition.setCycleCount(1);
        parallelTransition.play();
        parallelTransition.setOnFinished(e -> {
            try {
                splashStage.close();
                locale = new Locale("en", "UK");
                showMainStage(primaryStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        splashStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UserCompound.passPropertiesToFile();
            UserCompound.passUCsToFile();
        }));
    }

    public static void showMainStage() {
        try {
            showMainStage(thisPrimaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showMainStage(Stage primaryStage) throws IOException {
        thisPrimaryStage = primaryStage;
        Parent root = FXMLLoader.load(Main.class.getResource("view/Menu.fxml"));
        primaryStage.setTitle("Elemix");
        scene = new Scene(root, 900, 600);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/ConicalFlaskIcon.png")));
        setStyle(style);
        primaryStage.setScene(scene);
        primaryStage.show();
        initialiseDatabases();
    }

    private static void initialiseDatabases() {
        Element.initialisePeriodicTable();
        Cation.initialiseCations();
        AcidBase.initialiseAcidBases();
        UserCompound.initialiseProperties();
        UserCompound.initialiseUserCompounds();
    }

    public static void setCurrentStyle(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Main.scene.getStylesheets().get(0));
    }

    public static void setStyle(String style) {
        Main.style = style;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Main.class.getResource("view/Stylesheets/" + style + ".css").toExternalForm());
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale newLocale) {
        locale = newLocale;
    }

    public static ResourceBundle getMessages() {
        return ResourceBundle.getBundle("sample/view/I18n/MessagesBundle", locale);
    }

    //CHANGE TABS
    public static void openMenu() {
        try {
            scene.setRoot(FXMLLoader.load(Main.class.getResource("/sample/view/Menu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDisplacement() {
        try {
            scene.setRoot(FXMLLoader.load(Main.class.getResource("/sample/view/Displacement.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openAcidBase() {
        try {
            scene.setRoot(FXMLLoader.load(Main.class.getResource("/sample/view/AcidBase.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openMyCompounds() {
        try {
            scene.setRoot(FXMLLoader.load(Main.class.getResource("/sample/view/MyCompounds.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
