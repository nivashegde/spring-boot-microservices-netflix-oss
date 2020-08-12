package com.nhegde.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhegde.moviecatalogservice.models.CatalogItem;
import com.nhegde.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private UserRatingInfo userRatingInfo;

    @Autowired
    private MovieInfo movieInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = userRatingInfo.getUserRating(userId);
        return userRating.getRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());

    }

    

	
    
}