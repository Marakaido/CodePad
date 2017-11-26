import editor.EditorArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
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
import java.util.Optional;

import static javafx.scene.control.Alert.*;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        buildStage(primaryStage);
    }

    @FXML public void initialize() {
        addNewTab(new ActionEvent());
    }

    public void bindMenuItemsAvailability() {
        EditorArea editorArea = getActiveFileTab().getEditorArea();

        boolean hasSelection = editorArea.getSelection().getLength() == 0;
        copyMenuItem.disableProperty().setValue(hasSelection);
        pasteMenuItem.disableProperty().setValue(hasSelection);
        cutMenuItem.disableProperty().setValue(hasSelection);
        deleteMenuItem.disableProperty().setValue(hasSelection);
    }

    public void addNewTab(ActionEvent actionEvent) {
        Tab tab = new Tab("new");
        EditorArea editorArea = new EditorArea();
        FileTab fileTab = new FileTab(tab, editorArea);
        editorArea.richChanges().subscribe(arg -> {
            if(fileTab.isModified()) statusLabel.setText("modified");
            else statusLabel.setText("saved");
        });
        this.fileTabs.add(fileTab);
        this.editorTabPane.getTabs().add(tab);
        statusLabel.setText("new");
    }

    public void openFileInNewTab(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        list.stream().filter(Objects::nonNull).forEach(this::loadFile);
        statusLabel.setText("saved");
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
            statusLabel.setText("new");
        }
        catch(IOException e) {
            alert("Error", "Could not create file", AlertType.ERROR);
        }
    }

    public void openFileInNewWindow(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            buildStage(stage);
        }
        catch (IOException e) {
            alert("Error", "Could not open new window", AlertType.ERROR);
        }
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
            statusLabel.setText("saved");
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
        FileTab fileTab = getActiveFileTab();
        fileTab.getEditorArea().copy();
    }

    public void paste(ActionEvent actionEvent) {
        FileTab fileTab = getActiveFileTab();
        fileTab.getEditorArea().paste();
    }

    public void cut(ActionEvent actionEvent) {
        FileTab fileTab = getActiveFileTab();
        fileTab.getEditorArea().cut();
    }

    public void delete(ActionEvent actionEvent) {
        FileTab fileTab = getActiveFileTab();
        fileTab.getEditorArea().deleteText(fileTab.getEditorArea().getSelection());
    }

    private void alert(String title, String header, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();
    }

    private void loadFile(File file) {
        final Tab tab = new Tab(file.getName());
        final EditorArea editorArea = new EditorArea();
        final FileTab fileTab = new FileTab(file, defaultCharset, tab, editorArea);
        editorArea.richChanges().subscribe(arg -> {
           if(fileTab.isModified()) statusLabel.setText("modified");
           else statusLabel.setText("saved");
        });

        fileTabs.add(fileTab);
        editorTabPane.getTabs().add(tab);
        editorTabPane.getSelectionModel().select(tab);
    }

    private void saveFile(FileTab fileTab) throws IOException {
        if(!fileTab.hasFile()) throw new IllegalStateException();
        fileTab.updateData();
        Files.write(fileTab.getFile().toPath(), fileTab.getData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private FileTab getActiveFileTab() {
        final Tab activeTab = editorTabPane.getSelectionModel().getSelectedItem();
        Optional<FileTab> fileTab = fileTabs.stream().filter(item -> item.getTab().equals(activeTab)).findFirst();
        return fileTab.get();
    }

    private void buildStage(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource("main.fxml"));
        primaryStage.setTitle("Codepad");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(
                App.class.getResource("style.css").toExternalForm(),
                App.class.getResource("keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private final List<FileTab> fileTabs = new ArrayList<>(10);
    private Stage stage;
    private final Charset defaultCharset = Charset.forName("UTF-8");
    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    @FXML private TabPane editorTabPane;
    @FXML private MenuItem copyMenuItem;
    @FXML private MenuItem pasteMenuItem;
    @FXML private MenuItem cutMenuItem;
    @FXML private MenuItem deleteMenuItem;
    @FXML private Label statusLabel;
}
