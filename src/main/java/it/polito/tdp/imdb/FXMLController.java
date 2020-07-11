/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		txtResult.setText("Selezionare un anno dal menù.");
    		return;
    	}
    	txtResult.appendText("Grafo creato");
    	this.model.creaGrafo(anno);
    	txtResult.appendText("\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    	
    	this.boxRegista.getItems().addAll(this.model.elencoRegisti());
    	
    	this.btnAdiacenti.setDisable(false);
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Director scelto = this.boxRegista.getValue();
    	
    	if(scelto == null) {
    		txtResult.setText("Selezionare un regista dal menù.");
    		return;
    	}
    	
    	txtResult.appendText("REGISTI ADIACENTI A: "+scelto+"\n"+this.model.registiAdiacenti(scelto));
    	
    	this.btnCercaAffini.setDisable(false);
    }

    @FXML
    void doRicorsione(ActionEvent event) {

    	txtResult.clear();
    	
    	Director scelto = this.boxRegista.getValue();
    	
    	if(scelto == null) {
    		txtResult.setText("Selezionare un regista dal menù.");
    		return;
    	}
    	
    	String cond = this.txtAttoriCondivisi.getText();
    	
    	try {
    		int c = Integer.parseInt(cond);
    		
        	txtResult.appendText("Gruppo di registi massimo a partire da "+ scelto +" è composto da:\n"+this.model.cercaAffini(c, scelto));

    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero positivo.");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().addAll(this.model.elencoAnni());
    	this.btnAdiacenti.setDisable(true);
    	this.btnCercaAffini.setDisable(true);
    }
    
}
