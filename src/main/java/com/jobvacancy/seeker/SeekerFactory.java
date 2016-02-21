package com.jobvacancy.seeker;

import java.util.regex.Pattern;

public class SeekerFactory {

	private static final String AND = "AND";
	private static final String OR = "OR";
	private static Pattern regex;
	
	public static SeekerJob getSeeker(String word) {
        if (isAdvancedSearch(word, AND)){
        	return new AdvancedSeekerOperatorAnd();
        }else{
        	if (isAdvancedSearch(word, OR)){
        		return new AdvancedSeekerOperatorOr();
        	}else{
        		return new SimpleSeekerJob();
        	}
        }
	}

	private static boolean isAdvancedSearch(String word, String operator) {
		regex = Pattern.compile("\\w+(\\s\\w+)*(\\s("+operator+")\\s\\w+(\\s\\w+)*)+");
		return regex.matcher(word.toUpperCase()).matches();
	}
}
