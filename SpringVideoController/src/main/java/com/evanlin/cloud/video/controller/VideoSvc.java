package com.evanlin.cloud.video.controller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.evanlin.cloud.video.client.VideoSvcApi;

// Tell Spring that this class is a Controller that should 
// handle certain HTTP requests for the DispatcherServlet
@Controller
public class VideoSvc implements VideoSvcApi {
	
	// An in-memory list that the servlet uses to store the
	// videos that are sent to it by clients
	private List<Video> videos = new CopyOnWriteArrayList<Video>();
	private static final AtomicLong currentId = new AtomicLong(1L);

	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean addVideo(@RequestBody Video v){
		long id =  currentId.getAndIncrement();
		v.setId(id);
		return videos.add(v);
	}
	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Video> getVideoList(){
		return videos;
	}


	@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.GET)
	public @ResponseBody Video getVideoDataByID(@PathVariable long id) {
		for(Video v : videos){
			if (v.getId() == id)
				return v;
		}
		return null;
	}
}
