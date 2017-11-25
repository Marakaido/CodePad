import editor.EditorArea;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileTab {
    public FileTab(final File file, final Charset charset, final Tab tab, final EditorArea editorArea) {
        this(tab, editorArea);
        this.setFile(file, charset);
    }

    public FileTab(final Tab tab, final EditorArea editorArea) {
        this.tab = tab;
        this.tab.setContent(editorArea);
        this.editorArea = editorArea;
    }

    public File getFile() { return file; }
    public void setFile(File file, Charset charset) {
        try {
            if(!file.exists())
                this.data = Files.readAllBytes(file.toPath());
            else this.data = new byte[0];
            this.file = file;
            this.charset = charset;
            tab.setText(file.getName());
            this.updateText();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Charset getCharset() { return charset; }
    public void setCharset(Charset charset) { this.charset = charset; }

    public Tab getTab() { return tab; }
    public void setTab(Tab tab) { this.tab = tab; }

    public EditorArea getEditorArea() { return editorArea; }
    public void setEditorArea(EditorArea editorArea) { this.editorArea = editorArea; }

    public byte[] getData() {
        this.data = editorArea.getText().getBytes(charset);
        return data;
    }

    public boolean hasFile() { return file != null; }

    public CodeArea getEditor() { return this.editorArea; }

    public void updateText() {
        editorArea.replaceText(0, 0, new String(data, charset));
    }

    private File file;
    private Charset charset;
    private Tab tab;
    private EditorArea editorArea;
    private byte[] data;
}
