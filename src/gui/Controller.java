// Author: Przemysław Sałęga

package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

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
        keyGenerateButton.setOnAction(e-> System.out.println("To jeszcze nie działa :)"));
    }
}
