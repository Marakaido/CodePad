import editor.EditorArea;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

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
            if(file.exists())
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
    public void setCharset(Charset charset) {
        this.charset = charset;
        this.data = this.editorArea.getText().getBytes(this.charset);
    }

    public Tab getTab() { return tab; }
    public void setTab(Tab tab) { this.tab = tab; }

    public EditorArea getEditorArea() { return editorArea; }
    public void setEditorArea(EditorArea editorArea) { this.editorArea = editorArea; }

    public byte[] getData() { return data; }

    public boolean hasFile() { return file != null; }

    public CodeArea getEditor() { return this.editorArea; }

    public void updateText() {
        editorArea.replaceText(0, editorArea.getLength(), new String(data, charset));
    }
    public void updateData() { data = editorArea.getText().getBytes(charset); }

    public boolean isModified() {
        if(!isModified)
            isModified = !Arrays.equals(editorArea.getText().getBytes(), data);

        return isModified;
    }

    private File file;
    private Charset charset;
    private Tab tab;
    private EditorArea editorArea;
    private boolean isModified = false;
    private byte[] data;
}
