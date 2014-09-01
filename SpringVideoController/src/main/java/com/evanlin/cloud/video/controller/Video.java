package com.evanlin.cloud.video.controller;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
	private String name;
	private String url;
	private long duration;
	private String upload_date;

	public Video(){}
	
	public Video(String name, String url, long duration, String upload_date) {
		this.name = name;
		this.url = url;
		this.duration = duration;
		this.upload_date = upload_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(String upload_date) {
		this.upload_date = upload_date;
	}
	
	/**
	 * Two Videos will generate the same hashcode if they have exactly
	 * the same values for their name, url, upload_date and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing 
		return Objects.hashCode(name,url,duration, upload_date);
	}

	/**
	 * Two Videos are considered equal if they have exactly
	 * the same values for their name, url, upload_date, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Video){
			Video other = (Video)obj;
			// Google Guava provides great utilities for equals too! 
			return Objects.equal(name, other.name) 
					&& Objects.equal(url, other.url)
					&& duration == other.duration
					&& Objects.equal(upload_date, other.upload_date);
		}
		else {
			return false;
		}
	}

}
