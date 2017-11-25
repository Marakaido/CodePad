package editor;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorArea extends CodeArea {
    public EditorArea() {
        super();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
    }
}
