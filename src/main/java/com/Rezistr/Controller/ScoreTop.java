package com.Rezistr.Controller;

import com.Rezistr.StructureAnalizator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class ScoreTop {
	public Label l1;
	public Label l2;
	public Label l3;
	public Label l4;
	public Label l5;
	private List<String> namePhones;
	StructureAnalizator structureAnalizator;

	public void Next(ActionEvent actionEvent) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/FXML/TopPhone.fxml"));
		Parent   root               = loader.load();
		TopPhone topPhoneController = loader.getController();
		topPhoneController.init(namePhones,structureAnalizator);
		Scene mainScene = new Scene(root);
		Stage stage     = new Stage();
		stage.setScene(mainScene);
		stage.show();
		((Stage)l1.getScene().getWindow()).close();

	}

	public void init(List<String> characertList, List<String> namePhones, StructureAnalizator structureAnalizator){
		Label[] arr = {l1,l2,l3,l4,l5};
		for(int i=0; i<characertList.size() && i!=5;i++){
			arr[i].setText(characertList.get(i));
		}
		this.namePhones = namePhones;
		this.structureAnalizator = structureAnalizator;
	}

}
