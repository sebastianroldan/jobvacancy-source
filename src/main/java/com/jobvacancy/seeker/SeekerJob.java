package com.jobvacancy.seeker;

import java.util.List;

import com.jobvacancy.domain.JobOffer;

public interface SeekerJob {

	public List<JobOffer> search(List<JobOffer> allJobs, String word);

}
