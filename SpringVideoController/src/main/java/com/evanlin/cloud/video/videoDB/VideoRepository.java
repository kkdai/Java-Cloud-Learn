package com.evanlin.cloud.video.videoDB;

import java.security.Principal;
import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.evanlin.cloud.video.client.VideoSvcApi;
import com.evanlin.cloud.video.controller.Video;

@RepositoryRestResource(path = VideoSvcApi.VIDEO_SVC_PATH)
public interface VideoRepository extends MongoRepository<Video, Long>{
	public Collection<Video> findByName(@Param(VideoSvcApi.TITLE_PARAMETER) String title);
	public Video findById(@Param("id") String id);
}