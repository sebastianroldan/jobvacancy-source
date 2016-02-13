package com.jobvacancy.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JobOfferTest {

	private JobOffer jobOffer;
	private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";

    @Before
    public void initTest() {
        jobOffer = new JobOffer();
        jobOffer.setTitle(DEFAULT_TITLE);
        jobOffer.setLocation(DEFAULT_LOCATION);
        jobOffer.setDescription(DEFAULT_DESCRIPTION);
        jobOffer.setTags("");
    }
	
	@Test
	public void hasTagsMustBeFalse(){
		assertFalse(jobOffer.hasTags());
	}
	
	@Test
	public void hasTagsMustBeTrue(){
		jobOffer.setTags("java");
		assertTrue(jobOffer.hasTags());
	}
	
	@Test
	public void tagListMustContainOneTag(){
		jobOffer.setTags("java");
		assertEquals(1,jobOffer.tagList().size());
		assertTrue(jobOffer.tagList().contains("java"));
	}
	
	@Test
	public void specialCharacterAreNotValid(){
		jobOffer.setTags("%&/");
		assertFalse(jobOffer.validate());
	}
	
	@Test
	public void wordsSeparateWithCommaWithoutWhiteSpacesAreNotValid(){
		jobOffer.setTags("java,tester");
		assertFalse(jobOffer.validate());
	}
	
	@Test
	public void whiteSpacesSeparateWithCommaAreNotValid(){
		jobOffer.setTags("java, ,tester");
		assertFalse(jobOffer.validate());
	}
	
	@Test
	public void wordSeparateWhitCommaAndWhiteSpacesAreValidate(){
		jobOffer.setTags("java, spring, hibernate");
		assertTrue(jobOffer.validate());
	}
	
	@Test
	public void containsMustReturnTrue(){
		jobOffer.setTags("java, spring, hibernate");
		assertTrue(jobOffer.contain("spring"));
		assertTrue(jobOffer.contain("java"));
		assertTrue(jobOffer.contain("hibernate"));
	}
	
	@Test
	public void containsMustReturnFalse(){
		jobOffer.setTags("java, spring, hibernate");
		assertFalse(jobOffer.contain("javax"));
		assertFalse(jobOffer.contain("angular"));
		assertFalse(jobOffer.contain("jquery"));
	}
	
	@Test
	public void supressWhiteSpacesTest(){
		String sinespacios = jobOffer.supressWhiteSpaces("boca juniors");
		assertEquals("bocajuniors",sinespacios);
	}
}
