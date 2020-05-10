package com.Rezistr.Controller;

import alice.tuprolog.NoSolutionException;
import com.Rezistr.PhonePage;
import com.Rezistr.StructureAnalizator;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class TopPhone {
	private int       curPageNum = 0;
	public  Label     curPage;
	public  Label     header;
	public  Label     topNumber;
	public  ImageView image;
	public  TextArea  textDescription;
	private ArrayList<PhonePage> phonePages;

	public void nextPage(ActionEvent actionEvent) {
		if(curPageNum<4){
			curPageNum++;
			allocate();
		}
	}

	public void allocate(){
		PhonePage phonePage = phonePages.get(curPageNum);
		curPage.setText((curPageNum+1)+"");
		topNumber.setText((curPageNum+1)+"");
		header.setText(phonePage.name);
		image.setImage(new Image(phonePage.imageURl));
		textDescription.clear();
		textDescription.appendText(phonePage.textDescription);
	}

	public void prewPage(ActionEvent actionEvent) {
		if(curPageNum>0){
			curPageNum--;
			allocate();
		}
	}
	public void init(List<String> namePhone, StructureAnalizator analizator) throws NoSolutionException {
		this.phonePages =  new ArrayList<>();
		for(int i =0; i<namePhone.size() && i<=5; i++){
			String curDescr =  analizator.getDescription(namePhone.get(i));
			String curImageURL = String.valueOf(getClass().getResource("/img/"+namePhone.get(i).replace(" ","")+".png"));
			phonePages.add(new PhonePage(namePhone.get(i),curImageURL,curDescr));
		}
		allocate();
	}
}
