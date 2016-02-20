package com.jobvacancy.web.rest;

import com.jobvacancy.Application;
import com.jobvacancy.domain.JobOffer;
import com.jobvacancy.domain.User;
import com.jobvacancy.repository.JobOfferRepository;

import com.jobvacancy.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the JobOfferResource REST controller.
 *
 * @see JobOfferResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobOfferResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String UPDATED_LOCATION = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private UserRepository userRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Inject
    private JobOfferRepository jobOfferRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobOfferMockMvc;

    private JobOffer jobOffer;
    private JobOffer jobOffer2;
    
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobOfferResource jobOfferResource = new JobOfferResource();
        ReflectionTestUtils.setField(jobOfferResource, "jobOfferRepository", jobOfferRepository);

        // TODO: this should be refactored in a based class because is a common concern
        Optional<User> user =  userRepository.findOneByLogin("user");
        when(mockUserRepository.findOneByLogin(Mockito.any())).thenReturn(user);

        ReflectionTestUtils.setField(jobOfferResource, "userRepository", mockUserRepository);
        this.restJobOfferMockMvc = MockMvcBuilders.standaloneSetup(jobOfferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

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

    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }

    @Test
    @Transactional
    public void createJobOffer() throws Exception {
        int databaseSizeBeforeCreate = jobOfferRepository.findAll().size();

        // Create the JobOffer

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeCreate + 1);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createJobOfferWithoutTags() throws Exception {
        jobOfferRepository.findAll().size();

        // Create the JobOffer
        jobOffer.setTags(null);
        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());
        
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertEquals(0,testJobOffer.numberOfTags());
        assertFalse(testJobOffer.hasTags());
    }
    
    @Test
    @Transactional
    public void createJobOfferWithOneTag() throws Exception {
        jobOfferRepository.findAll().size();

        // Create the JobOffer
        jobOffer.setTags("java");
        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());
        
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertEquals(1,testJobOffer.numberOfTags());
        assertTrue(testJobOffer.hasTags());
        assertEquals("java",testJobOffer.tagList().get(0));
    }
    
    @Test
    @Transactional
    public void createJobOfferWithMoreThanOneTag() throws Exception {
        jobOfferRepository.findAll().size();

        // Create the JobOffer
        jobOffer.setTags("java, spring, hibernate");
        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        
        assertEquals(3,testJobOffer.numberOfTags());
        assertTrue(testJobOffer.hasTags());
        assertEquals("java",testJobOffer.tagList().get(0));
        assertEquals("spring",testJobOffer.tagList().get(1));
        assertEquals("hibernate",testJobOffer.tagList().get(2));
    }

    @Test
    @Transactional
    public void createJobOfferWithMoreThanOneTagAndWhiteSpacesBetween() throws Exception {
        jobOfferRepository.findAll().size();

        // Create the JobOffer
        jobOffer.setTags("Google Maps, Games");
        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        
        assertEquals(2,testJobOffer.numberOfTags());
        assertTrue(testJobOffer.hasTags());
        assertEquals("Google Maps",testJobOffer.tagList().get(0));
        assertEquals("Games",testJobOffer.tagList().get(1));
    }

    @Test
    @Transactional
    public void createJobOfferTagsWhiteSpacesBetween() throws Exception {
        jobOfferRepository.findAll().size();

        // Create the JobOffer
        jobOffer.setTags("Apps, google maps, git, project manager");
        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isCreated());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        
        assertEquals(4,testJobOffer.numberOfTags());
        assertTrue(testJobOffer.hasTags());
        assertEquals("Apps",testJobOffer.tagList().get(0));
        assertEquals("google maps",testJobOffer.tagList().get(1));
        assertEquals("git",testJobOffer.tagList().get(2));
        assertEquals("project manager",testJobOffer.tagList().get(3));
    }
    
    @Test
    @Transactional
    public void checkTagsWithSpecialCharsAreNotValidate() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field tags
        jobOffer.setTags("%&/");

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void checkTagsWithoutWhiteSpaceAfterCommaAreNotValidate() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field tags
        jobOffer.setTags("java,spring, junior");

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void checkTagsWithTwoCommaAreNotValidate() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field tags
        jobOffer.setTags("java, , spring");

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void checkTagsWhithoutWhiteSpacesAfterCommaAreNotValidate() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field tags
        jobOffer.setTags("java,spring");

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobOfferRepository.findAll().size();
        // set the field null
        jobOffer.setTitle(null);

        // Create the JobOffer, which fails.

        restJobOfferMockMvc.perform(post("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isBadRequest());

        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void getJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

        // Get the jobOffer
        restJobOfferMockMvc.perform(get("/api/jobOffers/{id}", jobOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobOffer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobOffer() throws Exception {
        // Get the jobOffer
        restJobOfferMockMvc.perform(get("/api/jobOffers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

		int databaseSizeBeforeUpdate = jobOfferRepository.findAll().size();

        // Update the jobOffer
        jobOffer.setTitle(UPDATED_TITLE);
        jobOffer.setLocation(UPDATED_LOCATION);
        jobOffer.setDescription(UPDATED_DESCRIPTION);

        restJobOfferMockMvc.perform(put("/api/jobOffers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOffer)))
                .andExpect(status().isOk());

        // Validate the JobOffer in the database
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeUpdate);
        JobOffer testJobOffer = jobOffers.get(jobOffers.size() - 1);
        assertThat(testJobOffer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobOffer.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testJobOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteJobOffer() throws Exception {
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);

		int databaseSizeBeforeDelete = jobOfferRepository.findAll().size();

        // Get the jobOffer
        restJobOfferMockMvc.perform(delete("/api/jobOffers/{id}", jobOffer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobOffer> jobOffers = jobOfferRepository.findAll();
        assertThat(jobOffers).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void searchSimpleWordJava() throws Exception {

        jobOffer2.setTags("java");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"java");
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"junit");
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"IOS");

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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"PROJECT MANAJER");
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"an droid");           
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"Movil");           
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"Movil");           
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"  ");           
        
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

        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"APPS");           
        
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
        
        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"AndRoid");           
        
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
    
    @Test
    @Transactional
    public void searchWhitANDOperator() throws Exception {
    	
    	jobOffer.setTitle("Tester java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"JAVA AND junit");           
        
        assertEquals(1,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Tester java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
    }
    
    @Test
    @Transactional
    public void searchWhitOperatorAnd() throws Exception {
    	
    	jobOffer.setTitle("Tester java");
    	jobOffer.setDescription("Programador java para testeo de aplicaciones");
    	jobOffer.setTags("java, junit, tester, android");
    	jobOffer2.setTitle("Desarrollo movil");
    	jobOffer2.setDescription("Desarrollador juniors para aplicaciones movil y juegos");
    	jobOffer2.setTags("Apps, android, ios, movil");
    	
        // Initialize the database
        jobOfferRepository.saveAndFlush(jobOffer);
        jobOfferRepository.saveAndFlush(jobOffer2);
        
        JobOfferResource resource = new JobOfferResource();
        List<JobOffer> list = jobOfferRepository.findAll();
        List<JobOffer> listJobOffer = resource.search(list,"tester and programador");           
        
        assertEquals(1,listJobOffer.size());
        assertThat(listJobOffer.get(0).getTitle()).isEqualTo("Tester java");
        assertThat(listJobOffer.get(0).getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(listJobOffer.get(0).getDescription()).isEqualTo("Programador java para testeo de aplicaciones");
    }
}
