/**
 * 
 */
package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;

/**
 * Controller for entity 'Genre'
 * All routes follow the path '/genre/'
 * @author Michael Obst
 */
@Controller
@RequestMapping("/genre")
public class GenreController {
	
	private GenreRepository genreRepository;

	/**
	 * constructor-based injection of the repository
	 */
	public GenreController(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	
	/**
	 * route 'list' shows all genres
	 */
	@GetMapping("list")
	public String getAll(Model model) {
		model.addAttribute("genres", genreRepository.findAll());
		
		return "genres";
	}
	
	/**
	 * route 'edit' (GET-method) shows 
	 * an existing genre if an id has been given or (if not)
	 * an empty input mask for a new genre 
	 */
	@GetMapping("edit")
	public String getGenre(Model model, @RequestParam(required = false) Long id) {
		
		Genre genre;
		
		// if an id was sent as parameter
		if (id != null) {
			Optional<Genre> optionalGenre = genreRepository.findById(id);
			// if database object could be retrieved
			if (optionalGenre.isPresent()) {
				//set genre to the object retrieved
				genre = optionalGenre.get();
			} else {
				genre = new Genre();
			}
		} else {
			genre = new Genre();
		}
		
		// add genre to the view model (either empty or prefilled with DB data)
		model.addAttribute("genre", genre);	
		
		return "genre";
	}
	
	/**
	 * route 'edit' (POST-method) saves the inserted data as a new genre and 
	 * redirects to the list of genres
	 */
	@PostMapping("edit")
	public String postGenre(@ModelAttribute Genre genre) {
		
		// save for both cases: new genre and update genre
		genreRepository.save(genre);
		
		return "redirect:list";
	}
	
	/**
	 * route 'delete' (POST-method) deletes the genre with the given id and 
	 * redirects to the list of genres
	 */
	@GetMapping("delete")
	public String deleteGenre(@RequestParam Long id) {

		genreRepository.deleteById(id);

		return "redirect:list";
	}

}
