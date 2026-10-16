package com.example.examen.repository;

import com.example.examen.models.mongo.ShowComment;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<ShowComment, String> {
	
	 List<ShowComment> findByShowId(Long showId);
}

