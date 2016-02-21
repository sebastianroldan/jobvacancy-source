package com.jobvacancy.seeker;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.jobvacancy.Application;
import com.jobvacancy.domain.JobOffer;
import com.jobvacancy.repository.JobOfferRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SimpleSeekerJobTest {
	
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private JobOffer jobOffer;
    private JobOffer jobOffer2;
    
    @Inject
    private JobOfferRepository jobOfferRepository;
	
    @Before
    public void initTest() {
        jobOffer = new JobOffer();
        jobOffer.setTitle(DEFAULT_TITLE);
        jobOffer.setLocation(DEFAULT_LOCATION);
        jobOffer.setDescription(DEFAULT_DESCRIPTION);
        jobOffer.setTags("");
        jobOffer2 = new JobOffer();
    	jobOffer2.setTitle("Tester");
        jobOffer2.setLocation("Lanus");
        jobOffer2.setDescription("Junit");
        jobOffer2.setTags("");

    }

    @Test
    @Transactional
    public void searchSimpleWordJava() throws Exception {

        jobOffer2.setTags("java");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"java");
        assertEquals("Tester",listJobOffer.get(0).getTitle());
        assertEquals("Lanus",listJobOffer.get(0).getLocation());
        assertEquals("Junit",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);   
    }
    
    @Test
    @Transactional
    public void searchAWordWithCapitalLetters() throws Exception {

        jobOffer2.setTags("java, hibernate, JUNIT");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"junit");
        
        assertEquals("Tester",listJobOffer.get(0).getTitle());
        assertEquals("Lanus",listJobOffer.get(0).getLocation());
        assertEquals("Junit",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);
    }
    
    @Test
    @Transactional
    public void searchAWordWithCapitalLettersBetween() throws Exception {

    	jobOffer.setTags("android, iOs");
        jobOffer2.setTags("apps, web, IOS, android");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"IOS");

        assertEquals("Tester",listJobOffer.get(1).getTitle());
        assertEquals("Lanus",listJobOffer.get(1).getLocation());
        assertEquals("Junit",listJobOffer.get(1).getDescription());
        assertTrue(listJobOffer.size()==2);
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }
    
    @Test
    @Transactional
    public void searchAWordWithWhiteSpacesBetween() throws Exception {

    	JobOffer jobOffer2 = new JobOffer();
    	jobOffer2.setTitle("Test");
        jobOffer2.setLocation("Quilmes");
        jobOffer2.setDescription("Junit");
        jobOffer2.setTags("java, hibernate, project manajer");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"PROJECT MANAJER");
        
        assertEquals("Test",listJobOffer.get(0).getTitle());
        assertEquals("Quilmes",listJobOffer.get(0).getLocation());
        assertEquals("Junit",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);
        
    }
    
    @Test
    @Transactional
    public void searchASingleWordSeparateWhiteSpacesBetween() throws Exception {

        jobOffer2.setTags("apps, web, Unit Test, android");
        jobOffer.setTags("android, unit TEST");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"an droid");           
        
        assertTrue(listJobOffer.size()==2);
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertEquals("Tester",listJobOffer.get(1).getTitle());
        assertEquals("Lanus",listJobOffer.get(1).getLocation());
        assertEquals("Junit",listJobOffer.get(1).getDescription());
    }
    
    @Test
    @Transactional
    public void searchAWordIncludeInTitle() throws Exception {

        jobOffer2.setTags("apps");
        jobOffer2.setTitle("Movil");
        jobOffer.setTags("android");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"Movil");           
        
        assertEquals("Movil",listJobOffer.get(0).getTitle());
        assertEquals("Lanus",listJobOffer.get(0).getLocation());
        assertEquals("Junit",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);
    }
    
    @Test
    @Transactional
    public void searchAWordIncludeInTitleComplex() throws Exception {

        jobOffer2.setTags("apps");
        jobOffer2.setTitle("Aplicaciones Movil");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"Movil");           
        
        assertEquals("Aplicaciones Movil",listJobOffer.get(0).getTitle());
        assertEquals("Lanus",listJobOffer.get(0).getLocation());
        assertEquals("Junit",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);
    }
    
    @Test
    @Transactional
    public void searchIsEmptyMustReturnAllJobs() throws Exception {

    	// Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"  ");           
        
        assertTrue(listJobOffer.size()==2);
    }
    
    @Test
    @Transactional
    public void searchAWordIncludeInDescription() throws Exception {

        jobOffer2.setTags("android, ios");
        jobOffer2.setTitle("Aplicaciones moviles");
        jobOffer2.setDescription("Desarrollo de apps para IOs y Android");
        jobOffer.setTags("android");
        
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"APPS");           
        
        assertEquals("Aplicaciones moviles",listJobOffer.get(0).getTitle());
        assertEquals("Lanus",listJobOffer.get(0).getLocation());
        assertEquals("Desarrollo de apps para IOs y Android",listJobOffer.get(0).getDescription());
        assertTrue(listJobOffer.size()==1);
    }
    
    @Test
    @Transactional
    public void searchAWordIncludeInDescriptionTagsAndTitle() throws Exception {

        jobOffer2.setTags("android, ios");
        jobOffer2.setTitle("Aplicaciones moviles");
        jobOffer2.setDescription("Desarrollo de apps para IOs y Android");
        jobOffer.setTags("android");
        JobOffer jobOffer3 = new JobOffer();
        jobOffer3.setTitle("Desarrollo en android");
        jobOffer3.setDescription("Apps para moviles");
        jobOffer3.setLocation("San Justo");
        jobOffer3.setTags("apps, movil");
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        jobOfferRepository.saveAndFlush(jobOffer3);
        
        SeekerJob seeker = new SimpleSeekerJob();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"AndRoid");           
        
        assertTrue(listJobOffer.size()==3);
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertEquals("Aplicaciones moviles",listJobOffer.get(1).getTitle());
        assertEquals("Lanus",listJobOffer.get(1).getLocation());
        assertEquals("Desarrollo de apps para IOs y Android",listJobOffer.get(1).getDescription());
        assertEquals("Desarrollo en android",listJobOffer.get(2).getTitle());
        assertEquals("San Justo",listJobOffer.get(2).getLocation());
        assertEquals("Apps para moviles",listJobOffer.get(2).getDescription());
    }

}
