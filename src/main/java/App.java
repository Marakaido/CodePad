import editor.EditorArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App extends Application {
    @FXML public TabPane editorTabPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
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
        Tab tab = new Tab("new");
        FileTab fileTab = new FileTab(tab, new EditorArea());
        this.fileTabs.add(fileTab);
        this.editorTabPane.getTabs().add(tab);
    }

    public void openFileInNewTab(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        list.stream().filter(Objects::nonNull).forEach(file -> {
            Tab tab = new Tab(file.getName());
            FileTab fileTab = new FileTab(file, charset, tab, new EditorArea());
            fileTabs.add(fileTab);
            editorTabPane.getTabs().add(tab);
        });
    }

    private List<FileTab> fileTabs = new ArrayList<>(10);
    private Stage stage;
    private Charset charset = Charset.forName("UTF-8");

    public void createAndOpenFileInNewTab(ActionEvent actionEvent) {
    }

    public void openFileInNewWindow(ActionEvent actionEvent) {
    }

    public void saveFileInCurrentTab(ActionEvent actionEvent) {
    }

    public void saveFileInCurrentTabAndExit(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
    }

    public void copy(ActionEvent actionEvent) {
    }

    public void paste(ActionEvent actionEvent) {
    }

    public void cut(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }
}
