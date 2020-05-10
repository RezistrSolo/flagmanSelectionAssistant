package com.Rezistr.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Information {
	public Button close;

	public void closeWindow(ActionEvent actionEvent) {
		((Stage)close.getScene().getWindow()).close();
	}
}
