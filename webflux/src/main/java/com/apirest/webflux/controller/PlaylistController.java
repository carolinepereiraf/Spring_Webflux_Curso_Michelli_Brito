package com.apirest.webflux.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.webflux.document.Playlist;
import com.apirest.webflux.services.PlaylistService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class PlaylistController {

	@Autowired
	private PlaylistService service;

	/**
	 * Retorna Fluxo de todas as playlists salvas no banco de dados
	 * 
	 * @return
	 */
	@GetMapping(value = "/playlist")
	public Flux<Playlist> getPlaylist() {
		return service.findAll();
	}

	/**
	 * Retorna um único elemento (Mono) do objeto playlist, de acordo com o id. 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/playlist/{id}")
	public Mono<Playlist> getPLaylistById(@PathVariable String id) {
		return service.findById(id);
	}
	
	/**
	 * Método para salvar a playlist no banco de dados
	 * @param playlist
	 * @return
	 */
	@PostMapping(value="/playlist")
	public Mono<Playlist> save(@RequestBody Playlist playlist){
		return service.save(playlist);
	}
	
	
	@GetMapping(value="/playlist/webflux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Tuple2<Long, Playlist>> getPlaylistByWebflux(){

		System.out.println("---Start get Playlists by WEBFLUX--- " + LocalDateTime.now());
		Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
        Flux<Playlist> playlistFlux = service.findAll();

        return Flux.zip(interval, playlistFlux);
        
	}

}
