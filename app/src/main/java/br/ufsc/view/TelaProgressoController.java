package br.ufsc.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author hiury
 */
public class TelaProgressoController implements Initializable {

    @FXML
    private ProgressIndicator indicadorProgresso;
    @FXML
    private Label labelTexto1;
    
    public void setLabelTexto1(String texto){
        this.labelTexto1.setText(texto);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
