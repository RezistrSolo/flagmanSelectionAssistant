package com.Rezistr;

//import
public class PhonePage {
	public String imageURl;
	public String textDescription;
//	public int number;
	public String name;

	public PhonePage(String name, String imageView, String textDescription) {
		this.imageURl = imageView;
		this.textDescription = textDescription;

		this.name = name;
	}
}
