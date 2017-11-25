import editor.EditorArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.scene.control.Alert.*;

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
        list.stream().filter(Objects::nonNull).forEach(this::loadFile);
    }

    public void createAndOpenFileInNewTab(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("New file");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                if (!file.exists()) file.createNewFile();
                loadFile(file);
            }
        }
        catch(IOException e) {
            alert("Error", "Could not create file", AlertType.ERROR);
        }
    }

    public void openFileInNewWindow(ActionEvent actionEvent) {
    }

    public void saveFileInCurrentTab(ActionEvent actionEvent) {
        try {
            Tab activeTab = editorTabPane.getSelectionModel().getSelectedItem();
            FileTab fileTab = fileTabs.stream().filter(item -> item.getTab() == activeTab).findFirst().get();
            if(!fileTab.hasFile()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                File file = fileChooser.showSaveDialog(stage);
                if(file != null)
                    fileTab.setFile(file, defaultCharset);
            }
            saveFile(fileTab);
        }
        catch (IOException e) {
            alert("Error", "Failed to save file", AlertType.ERROR);
        }
    }

    public void saveFileInCurrentTabAndExit(ActionEvent actionEvent) {
        saveFileInCurrentTab(actionEvent);
        exit(actionEvent);
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copy(ActionEvent actionEvent) {
    }

    public void paste(ActionEvent actionEvent) {
    }

    public void cut(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    private void alert(String title, String header, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();
    }

    private void loadFile(File file) {
        Tab tab = new Tab(file.getName());
        FileTab fileTab = new FileTab(file, defaultCharset, tab, new EditorArea());
        fileTabs.add(fileTab);
        editorTabPane.getTabs().add(tab);
    }

    private void saveFile(FileTab fileTab) throws IOException {
        if(!fileTab.hasFile()) throw new IllegalStateException();
        Files.write(fileTab.getFile().toPath(), fileTab.getData(), StandardOpenOption.CREATE);
    }

    private List<FileTab> fileTabs = new ArrayList<>(10);
    private Stage stage;
    private Charset defaultCharset = Charset.forName("UTF-8");
}
