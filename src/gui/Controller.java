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
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

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
    ChoiceBox<String> encodingChoiceBox;

    @FXML
    ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        charSwaps = new LinkedList<>();
        keyGenerateButton.setOnAction(e -> generateKey());
        keySaveButton.setOnAction(e -> saveKey());
        keyReadButton.setOnAction(e -> readKey());
        keyReadButton.setDisable(true);
        encryptButton.setOnAction(e -> encrypt());
        originalTextArea.setWrapText(true);
        encryptedTextArea.setWrapText(true);
        readFileButton.setOnAction(e -> loadFileAndGenerateStats());
        progressBar.setProgress(0);
        changeButton.setOnAction(e -> swapLetters(false));
        reverseButton.setOnAction(e -> swapLetters(true));
        setDefaultEncodingList();
    }

    private void generateKey(){
        characterList = KeyGenerator.generateKey();
        keyGenerateLabel.setText("Klucz wygenerowany.");
        keyGenerateLabel.setTextFill(Color.GREEN);
        //keySaveButton.setDisable(false);
        encryptButton.setDisable(false);
    }

    private void saveKey(){
        try {
            KeyGenerator.saveKeyToFile(characterList, new File("key.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyGenerateLabel.setText("Klucz zapisany.");
    }

    private void readKey(){
        List<Character> list = null;
        try {
            list = KeyGenerator.readKeyFromFile(new File("key.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list != null){
            characterList = list;
        }
    }

    private void encrypt(){
        originalText = originalTextArea.getText();
        encryptedText = EncryptingTools.encrypt(originalText, KeyGenerator.getCharactersInOriginalOrder(), characterList,
                progressBar, progressBarStatus);
        encryptedTextArea.setText(encryptedText);
        unlockFeatures();
    }

    private void unlockFeatures(){
        originalTextArea.setEditable(false);
        readFileButton.setDisable(false);
        encodingChoiceBox.setDisable(false);
        encryptedTextChars.setDisable(false);
        utfChars.setDisable(false);
        changeButton.setDisable(false);
        reverseButton.setDisable(false);
    }

    private void loadFileAndGenerateStats(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik ze statystyką");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(keyGenerateLabel.getScene().getWindow());
        if(file != null){
            String text = StatCounter.parseFile(file, Charset.forName(encodingChoiceBox.getValue()));
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

    private void setDefaultEncodingList(){
        List<String> list = Charset.availableCharsets().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        encodingChoiceBox.setItems(FXCollections.observableArrayList(list));
        encodingChoiceBox.setValue("windows-1250");
    }

    private void swapLetters(boolean reverse){
        char to, from;
        if(reverse && !charSwaps.isEmpty()){
            CharSwap charSwap = charSwaps.pop();
            to = charSwap.getFrom();
            from = charSwap.getTo();
        }
        else if(reverse && charSwaps.isEmpty()){
            return;
        }
        else{
            from = encryptedTextChars.getValue();
            to = utfChars.getValue();
        }
        CharSwap charSwap;
        if((charSwap = EncryptingTools.swapLetters(from, to, encryptedTextArea)) != null){
            FXCollections.replaceAll(encryptedTextChars.getItems(), from, to);
            if(reverse){
                StringBuilder stringBuilder = new StringBuilder();
                for(CharSwap cs : charSwaps){
                    stringBuilder.append(cs.toString() + '\n');
                }
                swapHistoryTextArea.setText(stringBuilder.toString());
            }
            else{
                charSwaps.push(charSwap);
                swapHistoryTextArea.setText(charSwap + "\n" + swapHistoryTextArea.getText());
            }
        }
    }

}
