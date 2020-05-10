package com.Rezistr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class QuestionTestStructure {

	public class Answer {
		private String                   answer;
		private HashMap<String, Integer> weight;
		public Answer(String answer) {
			this.answer = answer;
			this.weight = new HashMap<>();
		}
		public boolean addWeight(String character, int weight) {
			if (this.weight.containsKey(character)) {
				return false;
			}
			this.weight.put(character, weight);
			return true;
		}

		public int getWeight(String character) {
			return this.weight.get(character);
		}

		public Set<String> getCharacter() {
			return this.weight.keySet();
		}

		public String getAnswer() {
			return answer;
		}
	}
	private       String                 question;
	public        Set<Answer>            answers;
	public        Object                 vBox;
	public        Object                 toggleGroup;
	public static HashMap<String, Float> sumWeight;

	public QuestionTestStructure(String question) {

		this.question = question;
		this.answers = new HashSet<>();
	}

	public String getQuestion() {
		return question;
	}

	public Answer addAnswer(String answerText) {
		Answer answer = new Answer(answerText);
		this.answers.add(answer);
		return answer;
	}
	public void setVBox(Object header){
		this.vBox = header;
	}
	public void setToggleGroup(Object toggleGroup) {
		this.toggleGroup = toggleGroup;
	}
}
