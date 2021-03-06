// Author: Przemysław Sałęga

package backend.key;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.List;

public class EncryptingTools {

    // zrobić przy użyciu Task i bindowania
    public static String encrypt(String text, List<Character> charList, List<Character> cypher,
                                 ProgressBar progressBar, Label progressBarStatus){
        char[] chars = new char[text.length()];
        if(progressBarStatus != null){
            progressBarStatus.setText("Szyfrowanie...");
        }
        for(int i = 0; i<text.length(); i++){
            if(progressBar != null){
                progressBar.setProgress(i/text.length());
            }
            int j = 0;
            for(Character character: charList){
                if(character == text.charAt(i)){
                    break;
                }
                j++;
            }
            chars[i] = cypher.get(j);
        }
        if(progressBar != null){
            progressBar.setProgress(1);
        }
        if(progressBarStatus != null){
            progressBarStatus.setText("");
        }
        return new String(chars);
    }

    public static CharSwap swapLetters(char from, char to, TextArea textArea){
        char[] text = textArea.getText().toCharArray();
        boolean exists = false;
        for(int i = 0; i<text.length; i++){
            if(text[i] == from){
                exists = true;
                text[i] = to;
            }
        }
        if(exists){
            textArea.setText(new String(text));
            return new CharSwap(from, to);
        }
        return null;
    }

}
