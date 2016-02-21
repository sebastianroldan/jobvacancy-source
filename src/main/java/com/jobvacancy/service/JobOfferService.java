package com.jobvacancy.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.jobvacancy.domain.JobOffer;

public class JobOfferService {
	
	private JobOffer jobOffer;

	public JobOfferService(JobOffer jobOffer) {
		this.jobOffer = jobOffer;
	}

	public boolean contain(String word) {
		return (containTag(word)||containTitle(word)||containDescription(word));
	}
	
	private boolean containDescription(String word) {
		if (jobOffer.getDescription() == null){
			return false;
		}else{
			return descriptionWordList().contains(this.supressWhiteSpaces(word).toUpperCase());
		}
	}

	private List<String> descriptionWordList() {
		return convertToUpperCaseList(jobOffer.getDescription());
	}

	private boolean containTitle(String word) {
		return titleWordList().contains(this.supressWhiteSpaces(word).toUpperCase());
	}

	private boolean containTag(String word){
		if (jobOffer.getTags() == null){
			return false;
		}else{
			return this.upperCaseAndSuppresWhiteSpacesListAWords(jobOffer.tagList()).contains(
				this.supressWhiteSpaces(word).toUpperCase());
		}
	}
	
	private List<String> upperCaseAndSuppresWhiteSpacesListAWords(List<String> words){
		List<String> list = new LinkedList<String>();
		String modifiedWord;
		for (String word:words){
			modifiedWord = supressWhiteSpaces(word);
			list.add(modifiedWord.toUpperCase());
		}
		return list;
	}

	private List<String> titleWordList(){
		return convertToUpperCaseList(jobOffer.getTitle());
	}
	
	private List<String> convertToUpperCaseList(String text) {
		List<String> list = new LinkedList<String>();
		String[] array = text.split("\\s+");
		for (int i=0;i< array.length; i++){
			array[i]=array[i].toUpperCase();
		}
		list = Arrays.asList(array);
		return list;
	}

	private String supressWhiteSpaces(String word) {
		return word.replaceAll("\\s", "");
	}
}
