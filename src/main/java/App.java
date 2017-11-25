import editor.EditorArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

public class App extends Application {
    public EditorArea codeArea;
    public TabPane editorTabPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("main.fxml"));
        primaryStage.setTitle("Codepad");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(
                App.class.getResource("style.css").toExternalForm(),
                App.class.getResource("keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addNewTab(ActionEvent actionEvent) {
        editorTabPane.getTabs().add(new Tab("new", new CodeArea()));
    }
}
