package com.Rezistr.Controller;


import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ErrorPage {
	public Label    errorName;
	public TextArea errorText;

	public void initData(String errorName,String errorText){
		this.errorName.setText(errorName);
		this.errorText.clear();
		this.errorText.appendText(errorText);
	}

	public void endClick(ActionEvent actionEvent) {
		((Stage)errorText.getParent().getScene().getWindow()).close();
	}
}
