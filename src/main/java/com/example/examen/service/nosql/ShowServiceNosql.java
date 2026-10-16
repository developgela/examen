package com.example.examen.service.nosql;

import com.example.examen.models.mongo.ShowComment;
import com.example.examen.models.request.CommentRequest;
import com.example.examen.repository.CommentRepository;

import org.springframework.stereotype.Service;

@Service
public class ShowServiceNosql {
	
	/*** C- Endpoint comments: Crear un endpoint que permita guardar una
	calificación y comentario en una colección de mongoDB ligadas al ID del
	show, retorna el status de la petición. **/
	  private final CommentRepository commentRepository;

	    public ShowServiceNosql(CommentRepository commentRepository) {
	        this.commentRepository = commentRepository;
	    }

	    public void saveComment(CommentRequest request) {
	        ShowComment newComment = new ShowComment(
	                request.show_id(),
	                request.comment(),
	                request.rating()
	        );
	        commentRepository.save(newComment);
	    }
}
