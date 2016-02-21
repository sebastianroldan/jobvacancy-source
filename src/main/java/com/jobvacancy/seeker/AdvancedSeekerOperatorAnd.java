package com.jobvacancy.seeker;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.jobvacancy.domain.JobOffer;

public class AdvancedSeekerOperatorAnd implements SeekerJob {

	private List<String> searchWords;
	
	@Override
	public List<JobOffer> search(List<JobOffer> allJobs, String word) {
		List<JobOffer> lista = new LinkedList<JobOffer>();
		listAWordToSearch(word.toUpperCase());
		for (JobOffer job:allJobs){
			if (haveAllWordToSearch(job)){
				lista.add(job);
			}
		}
		return lista;
	}

	private boolean haveAllWordToSearch(JobOffer job) {
		boolean matches = true;
		for (String word:searchWords){
			matches = matches && job.contain(word);
		}
		return matches;
	}

	private void listAWordToSearch(String word){
		List<String> listWord = new LinkedList<String>();
		listWord = Arrays.asList(word.split("(\\s(AND)\\s)"));
		searchWords = listWord;
	}
}
