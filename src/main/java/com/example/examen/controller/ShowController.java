package com.example.examen.controller;

import com.example.examen.models.mongo.CommentRequest;
import com.example.examen.models.response.ShowResponse;
import com.example.examen.models.response.api.TvMazeShow;
import com.example.examen.service.ShowService;
import com.example.examen.service.nosql.ShowServiceNosql;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

	private final ShowService showService;
	
	@SuppressWarnings("unused")
	private final ShowServiceNosql showServiceNosql;

	public ShowController(ShowService showService, ShowServiceNosql showServiceNosql) {
		this.showService = showService;
		this.showServiceNosql = showServiceNosql;
	}

	/**A- Endpoint search: Crear un endpoint que realice la búsqueda de shows a
	partir de un criterio de búsqueda, retornando un arreglo de shows. **/
	@GetMapping("/search")
	public ResponseEntity<List<ShowResponse>> search(
			@RequestParam(name = "search_query") String searchQuery) {

		List<ShowResponse> results = showService.searchShows(searchQuery);
		return ResponseEntity.ok(results);
	}


	/*** B- Endpoint show: Crear un endpoint que obtenga la información del show
	a partir de su ID, retornando el objeto show.
	○ Api: https://api.tvmaze.com/shows/{show_id}
	○ Recibe: show_id
	○ Retorna: Objeto show completo. **/
	@GetMapping("/{show_id}")
	public ResponseEntity<TvMazeShow> getShowById(@PathVariable(name = "show_id") Long showId) {
		return showService.getShowById(showId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	/***Endpoint comments: Crear un endpoint que permita guardar una
	calificación y comentario en una colección de mongoDB ligadas al ID del
	show, retorna el status de la petición. ***/
    @PostMapping("/comments")
    public ResponseEntity<Map<String, String>> addComment(@Valid @RequestBody com.example.examen.models.request.CommentRequest request) {
    	showServiceNosql.saveComment(request);
        
        // Retorna un objeto indicando el estatus de éxito de la petición HTTP
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "SUCCESS",
                "message", "Comentario y calificación guardados exitosamente"
        ));
    }

}
