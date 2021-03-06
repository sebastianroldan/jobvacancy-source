package com.jobvacancy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.jobvacancy.service.JobOfferService;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A JobOffer.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "JOB_OFFER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull        
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags; 

    @ManyToOne
    private User owner;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
    	this.tags = tags;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobOffer jobOffer = (JobOffer) o;

        if ( ! Objects.equals(id, jobOffer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", location='" + location + "'" +
                ", description='" + description + "'" +
                '}';
    }

	public boolean hasTags() {
		return (numberOfTags()>0);
	}

	public int numberOfTags() {
		if (tags.isEmpty()){
			return 0;
		}else{
			return tags.split(",").length;
		}
	}
	
	public List<String> tagList(){
		int lengthArray = tags.split(",").length;
		List<String> tagList = new LinkedList<String>();
		String[] tagArray = new String[lengthArray];
		tagArray = tags.split(",");
		for (int i=1; i< tagArray.length; i++){
			tagArray[i]=tagArray[i].substring(1);
		}
		tagList = Arrays.asList(tagArray);
		return tagList;
	}

	public boolean validate() {
		if (tags == null){
			tags = "";
		}
		Pattern regex = Pattern.compile("(\\w+(\\s\\w+)*(\\,\\s\\w+(\\s\\w+)*)*)|\\w+(\\s\\w+)*|\\s+");
        return regex.matcher(tags).matches()||!this.hasTags();
	}

	public boolean contain(String word) {
		JobOfferService service = new JobOfferService(this);
		return (service.contain(word));
	}
}
