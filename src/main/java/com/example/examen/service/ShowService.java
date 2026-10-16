package com.example.examen.service;


import com.example.examen.models.mongo.CommentResponseDTO;
import com.example.examen.models.response.ShowResponse;
import com.example.examen.models.response.api.TvMazeSearchResponse;
import com.example.examen.models.response.api.TvMazeShow;
import com.example.examen.repository.CommentRepository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

	private final RestClient restClient;
	private final CommentRepository commentRepository;

	public ShowService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
		this.restClient = RestClient.builder()
				.baseUrl("http://tvmaze.com")
				.build();
	}

	/*** A- Endpoint search: flujo***/
	public List<ShowResponse> searchShows(String searchQuery) {
		if (searchQuery == null || searchQuery.isBlank()) {
			return Collections.emptyList();
		}

		// 1. Consumir API externa de TVMaze
		List<TvMazeSearchResponse> externalResponse = restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/search/shows")
						.queryParam("q", searchQuery)
						.build())
				.retrieve()
				.body(new ParameterizedTypeReference<List<TvMazeSearchResponse>>() {});

		if (externalResponse == null) {
			return Collections.emptyList();
		}

		/*** 2.mongo- Endpoint search: Modificar el endpoint para que posteriormente a la
        consulta del API, agregue al arreglo de la respuesta, un arreglo con los
        comentarios guardados para cada show. **/
		return externalResponse.stream()
				.map(item -> {
					var show = item.show();

					// Extraer dinámicamente el canal prioritario
					String channelName = null;
					if (show.network() != null) {
						channelName = show.network().name();
					} else if (show.webChannel() != null) {
						channelName = show.webChannel().name();
					}

					List<CommentResponseDTO> dbComments = commentRepository.findByShowId(show.id())
							.stream()
							.map(c -> new CommentResponseDTO(c.getComment(), c.getRating()))
							.toList();

					return new ShowResponse(
							show.id(),
							show.name(),
							channelName,
							show.summary(),
							show.genres()
							);
				})
				.toList();
	}


	/***B- Endpoint show:: flujo***/
	public Optional<TvMazeShow> getShowById(Long showId) {
		try {
			TvMazeShow show = restClient.get()
					.uri("/shows/{show_id}", showId)
					.retrieve()
					.body(TvMazeShow.class);

			return Optional.ofNullable(show);
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}
	}



}
