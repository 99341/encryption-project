// Author: Przemysław Sałęga

package gui;

import backend.key.EncryptingTools;
import backend.key.KeyGenerator;
import backend.stats.StatCounter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable{

    //app elements
    private List<Character> characterList;
    private String originalText;
    private String encryptedText;

    private Map<Character, Integer> modelStats;
    private Map<Character, Integer> encryptedTextStats;

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

    //choice boxes
    @FXML
    ChoiceBox encryptedTextChars;
    @FXML
    ChoiceBox utfChars;

    @FXML
    ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        keyGenerateButton.setOnAction(e -> generateKey());
        keySaveButton.setOnAction(e -> saveKey());
        encryptButton.setOnAction(e -> encrypt());
        originalTextArea.setWrapText(true);
        encryptedTextArea.setWrapText(true);
        readFileButton.setOnAction(e -> loadFileAndGenerateStats());
        progressBar.setProgress(0);
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
        String fileName = "pan_tadeusz_ksiega_I.txt";
        String path = System.getProperty("user.dir") + "/" + fileName;
        String text = StatCounter.parseFile(new File(path), Charset.forName("Cp1250"));
        modelStats = StatCounter.createStats(text);
        statTextArea.setText("Statystyka języka na podstawie pliku " + fileName + ":\n"
                + StatCounter.parseStats(modelStats));
        statLoadedLabel.setText("Statystyka wygenerowana.");
        statLoadedLabel.setTextFill(Color.GREEN);
        generateEncryptedTextStats();
        addOptionsToChoiceBoxes();
    }

    private void generateEncryptedTextStats(){
        encryptedTextStats = StatCounter.createStats(encryptedText);
    }

    private void addOptionsToChoiceBoxes(){
        List<Character> encryptedCharsList = encryptedTextStats.entrySet().stream().map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Character> modelCharsList = modelStats.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        encryptedTextChars.setItems(FXCollections.observableArrayList(encryptedCharsList));
        utfChars.setItems(FXCollections.observableArrayList(modelCharsList));
    }

}
