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
	public void returnASimpleSeekerJobTest(){
		word = "android apps";
		SeekerJob seeker = SeekerFactory.getSeeker(word);
		assertEquals(SimpleSeekerJob.class,seeker.getClass());
	}
	
	@Test
	public void getSeekerMustReturnAAdvancedSeekerOperatorANDTest(){
		word = "java AND junit";
		SeekerJob seeker = SeekerFactory.getSeeker(word);
		assertEquals(AdvancedSeekerOperatorAnd.class,seeker.getClass());
	}
	
	@Test
	public void getSeekerMustReturnAAdvancedSeekerOperatorORTest(){
		word = "android OR movil";
		SeekerJob seeker = SeekerFactory.getSeeker(word);
		assertEquals(AdvancedSeekerOperatorOr.class,seeker.getClass());
	}
	
}
