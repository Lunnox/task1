import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class Launcher extends Application {

    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> comboChooseTypeFile;
    @FXML
    private Button openDirBtn;
    @FXML
    private TabPane tabFolder;
    @FXML
    private TreeView<File> fileTreeViewer;
    private Stage application;
    private SingleSelectionModel<Tab> selectionModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        application = primaryStage;
        Parent app = FXMLLoader.load(getClass().getResource("Frame.fxml"));
        application.setTitle("Task 1");
        application.setScene(new Scene(app));

        application.show();
    }

    @FXML
    private void initialize() {
        selectionModel= tabFolder.getSelectionModel();
        fileTreeViewer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabFolder.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        comboChooseTypeFile.setEditable(true);
        comboChooseTypeFile.getItems().addAll(".log", ".txt",".doc",".docx",".*");
        comboChooseTypeFile.getSelectionModel().selectFirst();

    }

    @FXML
    private void OpenDirClick() {

        tabFolder.getTabs().clear();
        fileTreeViewer.refresh();

        IOFileFilter myFilter =
                FileFilterUtils.suffixFileFilter(comboChooseTypeFile.getValue().toLowerCase());
        String searchedText = searchTextField.getText().isEmpty() ?
                "log" : searchTextField.getText();
        SearchInFile src = new SearchInFile();
        DirectoryChooser openDirDialog = new DirectoryChooser();
        openDirDialog.setInitialDirectory(new File("."));
        File selectedDirectory = openDirDialog.showDialog(application);

        if (selectedDirectory != null) {
            TreeItem<File> filesOnDirectory = new TreeItem<File>(selectedDirectory);
            filesOnDirectory.setExpanded(true);
            Collection<File> files = src.openFolder(selectedDirectory, myFilter);
            for (File f : files) {
                boolean find = src.searchWordsOnFile(f, searchedText);
                if (find) {
                    TreeItem<File> items = new TreeItem<>(f);

                    filesOnDirectory.getChildren().add(items);
                }
            }
            Platform.runLater(() -> {
                fileTreeViewer.setEditable(true);
                fileTreeViewer.setRoot(filesOnDirectory);
                fileTreeViewer.getSelectionModel()
                        .selectedItemProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            try {
                                myEvent(newValue.getValue(),searchedText);
                            }catch (NullPointerException e){

                            }
                        });



            });

        }
    }


    private void myEvent(File file,String text) {
        Tab tab = new Tab();
        tab.setText(file.getName());
        tab.setClosable(true);
        TextArea textarea = new TextArea();
        textarea.prefHeightProperty().bind(tabFolder.heightProperty());
        textarea.prefWidthProperty().bind(tabFolder.widthProperty());
        try (LineIterator itr = FileUtils.lineIterator(file)) {
            while (itr.hasNext()) {
                String line = itr.nextLine();
                if (line.contains(text)){
                    int currentposition = line.lastIndexOf(text);
                    line = line.substring(0,currentposition)+text.toUpperCase()+line.substring(currentposition+text.length());
                }
                textarea.appendText(line);
                textarea.appendText("\n");
            }
        } catch (IOException e) {

        }
        tab.setContent(new ScrollPane(textarea));
        Platform.runLater(() -> {
            tabFolder.getTabs().add(tab);
            selectionModel.select(tab);


        });
    }
}


