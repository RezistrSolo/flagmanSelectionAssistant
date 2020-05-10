package com.Rezistr;

import com.Rezistr.Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;


public class Main extends Application {
	private static  MainController mainController;
	public static void main(String args[]) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		StructureAnalizator         sa                    = new StructureAnalizator(getClass().getResourceAsStream("/PL/test.pl"));
		List<QuestionTestStructure> test = sa.parseQuestions();
		FXMLLoader loader = new FXMLLoader();
		URL        xmlUrl =  getClass().getResource("/FXML/main.fxml");
		loader.setLocation(xmlUrl);
		Parent root      = loader.load();
		mainController = loader.getController();
		mainController.init(test,sa);
		Scene  mainScene = new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(true);
		primaryStage.show();
		mainController.initInform();
	}
}