package com.jobvacancy.seeker;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.junit.Assert.assertEquals;

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
public class AdvancedSeekerJobTest {
	
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    
    @Inject
    private JobOfferRepository jobOfferRepository;
	
    private JobOffer jobOffer;
    private JobOffer jobOffer2;
	
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
    public void searchWithANDOperator() throws Exception {
    	
    	jobOffer.setTitle("Tester java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorAnd();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"JAVA AND junit");           
        
        assertEquals(1,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Tester java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
    }
    
    @Test
    @Transactional
    public void searchWithOperatorAnd() throws Exception {
    	
    	jobOffer.setTitle("Tester java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorAnd();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"tester and programador");           
        
        assertEquals(1,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Tester java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
    }
    
    @Test
    @Transactional
    public void searchOperatorAndWithWordsInDescriptionAndTitle() throws Exception {
    	
    	jobOffer.setTitle("Desarrollo java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorAnd();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"desarrollo ANd APLICACIONES");           
        
        assertEquals(2,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Desarrollo java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
        assertThat(listJobOffer.get(1).getTitle()).isEqualTo("Desarrollo movil");
        assertThat(listJobOffer.get(1).getLocation()).isEqualTo("Lanus");
        assertThat(listJobOffer.get(1).getDescription()).isEqualTo("Desarrollador juniors para aplicaciones movil y juegos");
    }
    
    @Test
    @Transactional
    public void searchMustReturnEmptyList() throws Exception {
    	
    	jobOffer.setTitle("Desarrollo java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorAnd();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"desarrollo ANd APLICACIONES anD hibernate");           
        
        assertEquals(0,listJobOffer.size());
    }
    
    @Test
    @Transactional
    public void searchMustReturnJobOffer2() throws Exception {
    	
    	jobOffer.setTitle("Desarrollo java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorAnd();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"juniors ANd an droid anD desaRRollo");           
        
        assertEquals(1,listJobOffer.size());
        
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Desarrollo movil");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo("Lanus");
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Desarrollador juniors para aplicaciones movil y juegos");
    }
    
    @Test
    @Transactional
    public void searchWithOperatorOr() throws Exception {
    	
    	jobOffer.setTitle("Tester java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        SeekerJob seeker = new AdvancedSeekerOperatorOr();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = seeker.search(list,"tester OR ios");           
        
        assertEquals(2,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Tester java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
        assertThat(listJobOffer.get(1).getTitle()).isEqualTo("Desarrollo movil");
        assertThat(listJobOffer.get(1).getLocation()).isEqualTo("Lanus");
        assertThat(listJobOffer.get(1).getDescription()).isEqualTo("Desarrollador juniors para aplicaciones movil y juegos");
    }
}
