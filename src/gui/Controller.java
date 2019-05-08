// Author: Przemysław Sałęga

package gui;

import backend.key.CharSwap;
import backend.key.EncryptingTools;
import backend.key.KeyGenerator;
import backend.stats.StatCounter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class Controller implements Initializable{

    //app elements
    private List<Character> characterList;
    private String originalText;
    private String encryptedText;

    private Map<Character, Integer> modelStats;
    private Map<Character, Integer> encryptedTextStats;

    private LinkedList<CharSwap> charSwaps;

    //labels
    @FXML
    Label keyGenerateLabel;
    @FXML
    Label statLoadedLabel;
    @FXML
    Label progressBarStatus;

    //buttons
    @FXML
    Button keyGenerateButton;
    @FXML
    Button keyReadButton;
    @FXML
    Button keySaveButton;
    @FXML
    Button readFileButton;
    @FXML
    Button changeButton;
    @FXML
    Button reverseButton;
    @FXML
    Button encryptButton;

    //text areas
    @FXML
    TextArea originalTextArea;
    @FXML
    TextArea encryptedTextArea;
    @FXML
    TextArea statTextArea;
    @FXML
    TextArea swapHistoryTextArea;

    //choice boxes
    @FXML
    ChoiceBox<Character> encryptedTextChars;
    @FXML
    ChoiceBox<Character> utfChars;

    @FXML
    ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        charSwaps = new LinkedList<>();
        keyGenerateButton.setOnAction(e -> generateKey());
        keySaveButton.setOnAction(e -> saveKey());
        encryptButton.setOnAction(e -> encrypt());
        originalTextArea.setWrapText(true);
        encryptedTextArea.setWrapText(true);
        readFileButton.setOnAction(e -> loadFileAndGenerateStats());
        progressBar.setProgress(0);
        changeButton.setOnAction(e -> swapLetters());
    }

    private void generateKey(){
        characterList = KeyGenerator.generateKey();
        keyGenerateLabel.setText("Klucz wygenerowany.");
        keyGenerateLabel.setTextFill(Color.GREEN);
        keySaveButton.setDisable(false);
        encryptButton.setDisable(false);
    }

    private void saveKey(){
        KeyGenerator.saveKeyToFile(characterList, new File("klucz.txt"));
        keyGenerateLabel.setText("Klucz zapisany.");
    }

    private void encrypt(){
        originalText = originalTextArea.getText();
        encryptedText = EncryptingTools.encrypt(originalText, KeyGenerator.getCharactersInOriginalOrder(), characterList,
                progressBar, progressBarStatus);
        encryptedTextArea.setText(encryptedText);
    }

    private void loadFileAndGenerateStats(){
        //String fileName = "pan_tadeusz_ksiega_I.txt";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik ze statystyką");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(keyGenerateLabel.getScene().getWindow());
        if(file != null){
            String text = StatCounter.parseFile(file, Charset.forName("Cp1250"));
            modelStats = StatCounter.createStats(text);
            statTextArea.setText("Statystyka języka na podstawie pliku " + file.getName() + ":\n"
                    + StatCounter.parseStats(modelStats));
            statLoadedLabel.setText("Statystyka wygenerowana.");
            statLoadedLabel.setTextFill(Color.GREEN);
            generateEncryptedTextStats();
            addOptionsToChoiceBoxes();
        }
    }

    private void generateEncryptedTextStats(){
        encryptedTextStats = StatCounter.createStats(encryptedText);
    }

    private void addOptionsToChoiceBoxes(){
        modelStats = StatCounter.statsSort(modelStats);
        encryptedTextStats = StatCounter.statsSort(encryptedTextStats);
        List<Character> encryptedCharsList = new ArrayList<>(encryptedTextStats.keySet());
        List<Character> modelCharsList = new ArrayList<>(modelStats.keySet());
        encryptedTextChars.setItems(FXCollections.observableArrayList(encryptedCharsList));
        utfChars.setItems(FXCollections.observableArrayList(modelCharsList));
    }

    private void swapLetters(){
        char from = encryptedTextChars.getValue();
        char to = utfChars.getValue();
        CharSwap charSwap;
        if((charSwap = EncryptingTools.swapLetters(from, to, encryptedTextArea)) != null){
            FXCollections.replaceAll(encryptedTextChars.getItems(), from, to);
            charSwaps.push(charSwap);
            swapHistoryTextArea.setText(charSwap + "\n" + swapHistoryTextArea.getText());
        }
    }

}
