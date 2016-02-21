package com.jobvacancy.seeker;

import static org.junit.Assert.*;

import org.junit.Test;

public class SeekerFactoryTest {

	private String word;
	
	@Test
	public void getSeekerMustReturnASimpleSeekerJobTest(){
		word = "java";
		SeekerJob seeker = SeekerFactory.getSeeker(word);
		assertEquals(SimpleSeekerJob.class,seeker.getClass());
	}
	
	@Test
	public void getSeekerMustReturnAAdvancedSeekerOperatorAndTest(){
		word = "java AND junit";
		SeekerJob seeker = SeekerFactory.getSeeker(word);
		assertEquals(AdvancedSeekerOperatorAnd.class,seeker.getClass());
	}
}
