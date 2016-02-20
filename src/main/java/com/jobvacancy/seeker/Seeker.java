package com.jobvacancy.seeker;

import java.util.regex.Pattern;

public class Seeker {

	private static Pattern regex;
	
	public static SeekerJob getSeeker(String word) {
        if (isAdvancedSearch(word)){
        	return new AdvancedSearchOperatorAnd();
        }else{
        	return new SimpleSeekerJob();
        }
	}

	private static boolean isAdvancedSearch(String word) {
		regex = Pattern.compile("\\w+(\\s\\w+)*(\\s(AND)\\s\\w+(\\s\\w+)*)+");
		return regex.matcher(word.toUpperCase()).matches();
	}

}
