package com.evanlin.cloud.client.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.evanlin.cloud.video.controller.Video;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestData {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static Video randomVideo() {
		String id = UUID.randomUUID().toString();
		String title = "Video-"+id;
		String url = "http://coursera.org/some/video-"+id;
		long duration = 60 * (int)Math.rint(Math.random() * 60) * 1000; // random time up to 1hr
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_date = sdf.format(date);
		return new Video(title, url, duration, current_date);
	}
	
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
