package com.jobvacancy.seeker;

import java.util.LinkedList;
import java.util.List;

import com.jobvacancy.domain.JobOffer;

public class SimpleSeekerJob implements SeekerJob{

	@Override
	public List<JobOffer> search(List<JobOffer> allJobs, String word) {
		List<JobOffer> lista = new LinkedList<JobOffer>();
		for (JobOffer job:allJobs){
    		if (job.contain(word)){
    			lista.add(job);
    		}
    	}
		return lista;
	}

}
