package com.Rezistr.Controller;

import com.Rezistr.MyUtils;
import com.Rezistr.QuestionTestStructure;
import com.Rezistr.StructureAnalizator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class MainController{
	private class NestedWindow {
		public Stage  stage;
		public Object controller;

		public NestedWindow(Stage stage, Object controller) {
			this.stage = stage;
			this.controller = controller;
		}
	}

	private        HashMap<RadioButton, QuestionTestStructure.Answer> radioAnswerMap;
	static private List<QuestionTestStructure>                        listTest;
	@FXML
	public         Button                                             button;
	@FXML
	public         Label                                              labelMax;
	@FXML
	public         Label                                              labelCur;
	@FXML
	public         GridPane                                           gridPane;
	@FXML
	public         AnchorPane                                         mainPane;

	private int          curPage = 1;
	private int          maxPage = 1;
	private int          gridColumns;
	private int          gridRows;
	private NestedWindow errorPage;
	StructureAnalizator structureAnalizator;

	public void init(List<QuestionTestStructure> list, StructureAnalizator structureAnalizator) throws IOException {
		listTest = list;
		gridColumns = gridPane.getColumnConstraints().size();
		gridRows = gridPane.getRowConstraints().size();
		maxPage = (int) Math.ceil(list.size() / ((float) (gridRows * gridColumns)));
		labelMax.setText(maxPage + "");
		labelCur.setText(curPage + "");
		radioAnswerMap = new HashMap<>();
		this.structureAnalizator = structureAnalizator;
		allocateTest();
	}

	public void allocateTest() {
		Node cur = gridPane.getChildren().get(0);
		gridPane.getChildren().clear();
		gridPane.getChildren().add(0, cur);
		int listTestBeg = gridColumns * gridRows * (curPage - 1);
		int listTestEnd = listTestBeg + gridColumns * gridRows;
		for (int i = listTestBeg; i < listTestEnd && i < listTest.size(); i++) {
			QuestionTestStructure curQTS = listTest.get(i);
			if (listTest.get(i).vBox == null) {
				VBox vBox = new VBox();
				vBox.setPadding(new Insets(5, 5, 5, 5));
				vBox.setSpacing(3.0);
				Label header = new Label();
				header.setText(curQTS.getQuestion());
				vBox.getChildren().add(header);
				ToggleGroup toggleGroup = new ToggleGroup();
				curQTS.setToggleGroup(toggleGroup);
				for (QuestionTestStructure.Answer answer : curQTS.answers) {
					RadioButton radioButton = new RadioButton(answer.getAnswer());
					radioButton.setToggleGroup(toggleGroup);
//					answer.setObject(radioButton);
					vBox.getChildren().add(radioButton);
					radioAnswerMap.put(radioButton, answer);
				}
				curQTS.setVBox(vBox);
			}
			gridPane.add((VBox) curQTS.vBox, (i - listTestBeg) / gridRows, (i - listTestBeg) % gridRows);
		}
	}

	public void  initInform(){
		FXMLLoader loader  = new FXMLLoader();
		loader.setLocation(getClass().getResource("/FXML/Information.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage stage =  new Stage();
		stage.setScene(new Scene(parent));
		stage.initOwner(mainPane.getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.show();
	}

	private class PhoneCeil {
		float weight;
		String phone;
		String character;
	}

	public void nextPage(ActionEvent actionEvent) {
		if (curPage < maxPage) {
			curPage++;
			labelCur.setText(curPage + "");
			allocateTest();
		}
	}

	public void prevPage(ActionEvent actionEvent) {
		if (curPage > 1) {
			curPage--;
			labelCur.setText(curPage + "");
			allocateTest();
		}
	}

	ArrayList<PhoneCeil> arrayList = new ArrayList<>();

	public void foundSolutions(ActionEvent actionEvent) throws Exception {
//		QuestionTestStructure.sumWeight.forEach((k,v)->System.out.println(k+"  "+v));
		HashMap<String, Float> answerBal = new HashMap<>();
		for (int i = 0; i < listTest.size(); i++) {
			if (listTest.get(i).toggleGroup == null) {
				initError("Нет ответа",
						"Вы пропустили вопросы на странице "
								+ (curPage + 1));
				return;
			}
			RadioButton selected = (RadioButton) ((ToggleGroup) listTest.get(i).toggleGroup).getSelectedToggle();
			if (selected == null) {
				initError("Нет ответа",
						"Вы не ответили на вопрос под номером "
								+ (i + 1));
				return;
			}
			QuestionTestStructure.Answer answer        = radioAnswerMap.get(selected);
			Set<String>                  curCharacters = answer.getCharacter();
			for (String cur : curCharacters) {
				Float lastValue = answerBal.get(cur);
				if (lastValue == null) {
					lastValue = (float) 0;
				}
				Float avgBal = QuestionTestStructure.sumWeight.get(cur);
				answerBal.put(cur, avgBal * answer.getWeight(cur) + lastValue);
			}
		}
		answerBal = (HashMap<String, Float>) MyUtils.sortByValue(answerBal);
		answerBal.forEach((k, v) -> System.out.println(k + " " + v));


		HashMap<String, Float> avgPhoneBalMap = new HashMap<>();


		for (Map.Entry<String, Float> cur : answerBal.entrySet()) {
			float avgPhoneBal = 0;
			HashMap<String, Float> list        = structureAnalizator.getPhoneBal(cur.getKey());
			System.out.println();
			for (Map.Entry<String, Float> phoneWeight : list.entrySet()) {
				float f = phoneWeight.getValue();
				avgPhoneBal += f;
			}
			avgPhoneBal /= list.size();
			avgPhoneBalMap.put(cur.getKey(),avgPhoneBal);
		}

		HashMap<String,Float> sumScore = new HashMap<>();
		for(Map.Entry<String,Float> cur : answerBal.entrySet()){
			String curCharacter = cur.getKey();
			float curWeightAnswer = cur.getValue();
			float curAvgCharacterBalPhone =  avgPhoneBalMap.get(curCharacter);
			for (Map.Entry<String,Float> curPhoneScore : structureAnalizator.getPhoneBal(curCharacter).entrySet()){
				Float curScore = sumScore.get(curPhoneScore.getKey());
				if(curScore==null){
					curScore=(float)0;
				}
				sumScore.put(curPhoneScore.getKey(),curScore+(curPhoneScore.getValue()/curAvgCharacterBalPhone*curWeightAnswer));
			}
		}
		sumScore = (HashMap<String, Float>) MyUtils.sortByValue(sumScore);
		sumScore.forEach((k,v)->System.out.println(k+"  "+v));

		List<String> names= new ArrayList<>();
		int num = 1;
		for(String s : answerBal.keySet()){
			names.add(num+") "+structureAnalizator.getNameCharacter(s));
			num++;
		}


		ArrayList<String> sumScoreList = new ArrayList<>(sumScore.keySet());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/FXML/ScoreTop.fxml"));
		Parent root      = loader.load();
		ScoreTop scoreTopController = loader.getController();
		scoreTopController.init(names,sumScoreList,structureAnalizator);
		Scene  mainScene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(mainScene);
		stage.show();
		((Stage)labelCur.getScene().getWindow()).close();
	}

	public void initError(String errorName, String errorText) throws Exception {
		if (errorPage == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/FXML/ErrorPage.fxml"));
			Parent    root      = loader.load();
			ErrorPage errorPage = loader.getController();
			Stage     stage     = new Stage();
			stage.setScene(new Scene(root));
			stage.initOwner(this.labelCur.getScene().getWindow());
			stage.initModality(Modality.WINDOW_MODAL);
			this.errorPage = new NestedWindow(stage, errorPage);
		}
		((ErrorPage) this.errorPage.controller).initData(errorName, errorText);
		this.errorPage.stage.show();
	}
}
