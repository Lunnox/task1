import org.apache.commons.io.filefilter.FileFilterUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class aa extends JFrame {

    private static AttributeSet mySet;
    private static JTextPane textPane;

    public aa() {

        super("test");

        textPane = new JTextPane();
        setLayout(new BorderLayout());
        add(textPane);

        mySet = StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);

        try {
            addLine("Hello");
            addLine("[INFO] wwef");
            addLine("Hello");
        } catch (BadLocationException ignore) {
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setSize(800, 600);
    }

    private static void addLine(String text) throws BadLocationException {
        textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), text + "\n", text.contains("[INFO]") ? mySet : null);
    }

    public static void main(String[] args) {
        new aa().setVisible(true);
    }
}
/*
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class aa {
   public static List<String> lines= new ArrayList<>();



    public static void main(String[] args) {
            String pth = "D:\\Projects\\data\\java";
            SearchInFile src = new SearchInFile();
        Collection<File> files = src.openFolder(new File(pth),FileFilterUtils.suffixFileFilter(".log"));
        for (File f: files){
            System.out.println("**********************"+f.getAbsolutePath()+"*****************");
            src.searchWordsOnFile(f,"lunnox");


        }

    }
}
*/