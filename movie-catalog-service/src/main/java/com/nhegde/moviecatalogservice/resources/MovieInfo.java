package com.nhegde.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.nhegde.moviecatalogservice.models.CatalogItem;
import com.nhegde.moviecatalogservice.models.Movie;
import com.nhegde.moviecatalogservice.models.Rating;

@Service
public class MovieInfo {
	
	@Autowired
    private WebClient.Builder webClientBuilder;

	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeOutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
			})
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = webClientBuilder.build().get().uri("http://movie-info-service/movies/"+ rating.getMovieId())
				.retrieve().bodyToMono(Movie.class).block();
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("No Movie", "No Description", 0);
	}
}
