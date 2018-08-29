import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class SearchInFile {
    public SearchInFile(){

    }

    public Collection<File> openFolder(File selectedDirectory, IOFileFilter filter){
        Collection<File> pathFiles =FileUtils.listFiles(selectedDirectory,filter,TrueFileFilter.INSTANCE);

        return pathFiles;
    }
    public boolean searchWordsOnFile(File file, String searchText){
        int wordPosition=0, currentLine=0 ;

        long sizeFile = file.length();

        try (LineIterator itr = FileUtils.lineIterator(file)){
            while(itr.hasNext()){
                currentLine++;
                String str = itr.nextLine().toLowerCase();
                String word= searchText.toLowerCase();
                if (str.contains(word)){

                    wordPosition = str.lastIndexOf(word);
                    System.out.println(currentLine+"/"+wordPosition+" Searching word: "
                            +searchText.toUpperCase()+str.substring(wordPosition+searchText.length()));

                    return true;

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



}
