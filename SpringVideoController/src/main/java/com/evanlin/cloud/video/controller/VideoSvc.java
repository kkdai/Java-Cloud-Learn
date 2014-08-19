package com.evanlin.cloud.video.controller;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evanlin.cloud.video.videoDB.videoDB;

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
//@Controller
/*public class VideoSvc {

	public static final String VIDEO_SVC_PATH = "/video";	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	public static final String VIDEO_SEARCH_PATH = VIDEO_SVC_PATH + "/find";
	public static final String DATA_PARAMETER = "data";
	public static final String ID_PARAMETER = "id";	
	public static final String NAME_PARAMETER = "name";

	//@Autowired
	private videoDB videos;
	private static final AtomicLong currentId = new AtomicLong(1L);

	//@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addVideo(@RequestBody Video v){
		long id =  currentId.getAndIncrement();
		v.setId(id);
		return videos.addVideo(v);
	}
	
	//@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList(){
		return videos.getVideos();
	}

	//@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.GET)
	public @ResponseBody Video getVideoDataByID(@PathVariable("id") long id) {
		for(Video v : videos.getVideos()){
			if (v.getId() == id)
				return v;
		}
		return null;
	}
	
	//@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean setVideoData(@PathVariable("id") long id, @RequestBody Video videoData) {
		for(Video v : videos.getVideos()){
			if (v.getId() == id) {
				v.setName(videoData.getName());
				v.setDuration(videoData.getDuration());
				v.setUrl(videoData.getUrl());
				v.setUpload_date(videoData.getUpload_date());
				return true;
			}				
		}
		return false;
	}
	
	//@RequestMapping(value=VIDEO_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findByName(@RequestParam(NAME_PARAMETER) String name){
		return videos.findByNamee(name);
	}
} */
