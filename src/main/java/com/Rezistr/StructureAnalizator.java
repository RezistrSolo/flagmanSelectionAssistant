package com.Rezistr;

import alice.tuprolog.*;

import java.io.*;
import java.lang.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureAnalizator {


	private Prolog        prolog;
	public  EngineManager engine;

	public StructureAnalizator(InputStream inputStream) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "windows-1251");
			BufferedReader    bufferedReader    = new BufferedReader(inputStreamReader);
			StringBuilder     stringBuilder     = new StringBuilder();
			while (bufferedReader.ready()) {
				stringBuilder.append(bufferedReader.readLine()).append('\n');
			}
			Theory theory = new Theory(stringBuilder.toString());
			this.prolog = new Prolog();
			this.prolog.setTheory(theory);
			this.engine = this.prolog.getEngineManager();
		} catch (InvalidTheoryException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<QuestionTestStructure> parseQuestions() {
		List<SolveInfo>             questionsSolve = questionsParseStep("X", "Y");
		List<QuestionTestStructure> questionList   = new ArrayList<>();
		HashMap<String, Float>      sumWeight      = new HashMap<>();
		int                         i              = 1;
		HashMap<String,Float> maxWeightsQuestion = new HashMap<>();
		for (SolveInfo si : questionsSolve) {
			try {
				maxWeightsQuestion.clear();
				QuestionTestStructure curQTS = new QuestionTestStructure(i + ") " + ((Struct) si.getVarValue("X")).getName());
				questionList.add(curQTS);
				ArrayList<Term> curAnswers = (ArrayList<Term>) listParseStep((si.getBindingVars().get(1).getTerm()));

				for (Term term : curAnswers) {
					QuestionTestStructure.Answer answer = curQTS.addAnswer(((Struct) ((Struct) term).getArg(0)).getName());
					ArrayList<Term> curWeights = (ArrayList<Term>) listParseStep(((Struct) term).getArg(1));
					for (Term wi : curWeights) {
						String character = ((Struct)((Struct)wi).getArg(0)).getName();
						Integer weight = Integer.parseInt(((Struct) wi).getArg(1).toString());
						answer.addWeight(
								character
								,weight);

						Float curWeight = maxWeightsQuestion.get(character);
						if(curWeight==null){
							curWeight= (float) 0;
						}
						if (curWeight<weight){
							maxWeightsQuestion.put(character, (float)(weight));
						}
					}
				}
				for(Map.Entry<String,Float> cur : maxWeightsQuestion.entrySet()){
					String character=cur.getKey();
					float weight = cur.getValue();
					Float curWeight = sumWeight.get(character);
					if(curWeight==null){
						curWeight= (float) 0;
					}
					sumWeight.put(character,weight+curWeight);
				}
				i++;
			} catch (NoSolutionException e) {
				e.printStackTrace();
			}
		}
		calculate(sumWeight);
		QuestionTestStructure.sumWeight=sumWeight;

		return questionList;
	}

	private  void calculate(HashMap<String,Float> map){
		float avg = 0;
		for(Float cur : map.values()){
			avg+=cur;
		}
		avg/=map.size();
		for(String s :map.keySet()){
			float curBal = map.get(s);
			curBal=avg/curBal;
			 map.put(s,curBal);
		}
	}

	private List<SolveInfo> questionsParseStep(String nameVar1, String nameVar2) {
		Term            goal      = new Struct("question", new Var(nameVar1), new Var(nameVar2));
		return solveAll(goal);
	}

	private Object listParseStep(Term answerPList) {
		return MyUtils.translate(answerPList);
	}

	private List<SolveInfo> solveAll(Term goal) {
		List<SolveInfo> solution  = new ArrayList<>();
		SolveInfo       solveInfo = this.engine.solve(goal);
		if (solveInfo.isSuccess()) {
			solution.add(solveInfo);
			try {
				while (true) {
					SolveInfo cur = engine.solveNext();
					if (!cur.isSuccess())
						break;
					solution.add(cur);
				}
			} catch (NoMoreSolutionException ignored) {
			}
		}
		return solution;
	}

	public String getNameCharacter(String character) throws NoSolutionException {
		Term nameTerm = new Struct("name",new Struct(character),new Var("Name"));
		SolveInfo solveInfo = engine.solve(nameTerm);
		return ((Struct)solveInfo.getVarValue("Name")).getName();

	}

	public String getDescription(String namePhone) throws NoSolutionException {
		Term description = new Struct("description",new Struct(namePhone),new Var("Descr"));
		SolveInfo solveInfo = engine.solve(description);
		return ((Struct)solveInfo.getVarValue("Descr")).getName();

	}


	public HashMap<String, Float> getPhoneBal(String character){
		HashMap<String,Float> weight = new HashMap<>();
		Term wght = new Struct("phone",new Var("A"),new Struct(character),new Var("B"));
		SolveInfo       solveInfo = this.engine.solve(wght);
		if (solveInfo.isSuccess()) {
			try {
//				System.out.println((solveInfo.getVarValue("B")).toString());
				weight.put((((Struct)solveInfo.getVarValue("A")).getName()),Float.parseFloat((solveInfo.getVarValue("B")).toString()));
				while (true) {
					SolveInfo cur = engine.solveNext();
					if (!cur.isSuccess())
						break;
					weight.put((((Struct)cur.getVarValue("A")).getName()),Float.parseFloat(cur.getVarValue("B").toString()));
				}
			} catch (NoMoreSolutionException ignored) {
			} catch (NoSolutionException e) {
				e.printStackTrace();
			}
		}
		return weight;
	}
	public void assertField(){
		Term assertAnswer  = new Struct("assertAnswer");
		solveAll(assertAnswer);
		Term assertWeight  = new Struct("assertWeight");
		solveAll(assertWeight);
		Term retractAnswer = new Struct("retractAnswer");
		solveAll(retractAnswer);

	}
	public void retractFiled(){
		Term retractWeight = new Struct("retractWeight");
		solveAll(retractWeight);
	}
}


//	private SolveInfo templateParser(String termTheory, Struct template){
//		try {
//			Theory      theory = new Theory(termTheory);
//			Prolog      prolog = new Prolog();
//			Term        goal   = template;
//			prolog.setTheory(theory);
//			EngineManager engine = prolog.getEngineManager();
//			SolveInfo info = engine.solve(goal);
//			return info;
//		} catch (InvalidTheoryException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
