import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        executor = Executors.newSingleThreadExecutor();

        Scene scene = buildScene();
        scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Codepad");
        primaryStage.show();
    }

    @Override public void stop() {
        executor.shutdown();
    }

    private Scene buildScene() {
        BorderPane root = new BorderPane();

        MenuBar menu = buildMenu();
        root.setTop(menu);

        codeArea = new CodeArea();
        Node center = buildCenter(codeArea);
        root.setCenter(center);

        Node footer = buildFooter();
        root.setBottom(footer);

        return new Scene(root, 600, 400);
    }

    private MenuBar buildMenu() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);
        return menuBar;
    }

    private HBox buildFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.getChildren().addAll(new Label("encoding: UTF-8"));
        return footer;
    }

    private Node buildCenter(CodeArea codeArea) {
        final AnchorPane root = new AnchorPane();
        final TabPane tabPane = new TabPane(
                new Tab("new", codeArea)
        );

        root.getChildren().addAll(tabPane);

        AnchorPane.setTopAnchor(tabPane, 0.0);
        AnchorPane.setBottomAnchor(tabPane, 0.0);
        AnchorPane.setLeftAnchor(tabPane, 0.0);
        AnchorPane.setRightAnchor(tabPane, 0.0);

        return root;
    }

    private CodeArea codeArea;
    private ExecutorService executor;
}
