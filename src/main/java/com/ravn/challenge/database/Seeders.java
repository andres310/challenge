package com.ravn.challenge.database;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ravn.challenge.entity.Category;
import com.ravn.challenge.entity.Movie;
import com.ravn.challenge.entity.Rating;
import com.ravn.challenge.entity.Role;
import com.ravn.challenge.entity.User;
import com.ravn.challenge.repository.ICategoryRepository;
import com.ravn.challenge.repository.IMovieRepository;
import com.ravn.challenge.repository.IRatingRepository;
import com.ravn.challenge.repository.IRoleRepository;
import com.ravn.challenge.repository.IUserRepository;

@Component
public class Seeders {

	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final ICategoryRepository categoryRepository;
	private final IMovieRepository movieRepository;
	private final IRatingRepository ratingRepository;
	private final PasswordEncoder passwordEncoder;

	public Seeders(IUserRepository userRepository, IRoleRepository roleRepository, 
			ICategoryRepository categoryRepository, IMovieRepository movieRepository,
			IRatingRepository ratingRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.categoryRepository = categoryRepository;
		this.movieRepository = movieRepository;
		this.ratingRepository = ratingRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@EventListener()
	public void seed(ContextRefreshedEvent event) {
		try {
			List<User> users = this.seedUsersTable();
			this.seedRolesTable(users);
			List<Category> categories = this.seedCategoriesTable();
			List<Movie> movies = this.seedMoviesTable(categories);
			this.seedRatingsTable(movies, users.get(0));
		} catch (Exception e) {
			// Database has been seeded
		}

	}
	
	private List<User> seedUsersTable() {
		String pass = this.passwordEncoder.encode("123");
		
		User u1 = new User();
		u1.setUsername("jane");
		u1.setEmail("jane@gmail.com");
		u1.setPassword(pass);
		u1.setCreatedDate(LocalDateTime.now());
		
		User u2 = new User();
		u2.setUsername("john");
		u2.setEmail("john@gmail.com");
		u2.setPassword(pass);
		u2.setCreatedDate(LocalDateTime.now());
		
		return this.userRepository.saveAll(List.of(u1, u2));
	}
	
	private List<Role> seedRolesTable(List<User> users) {
		Role r1 = new Role();
		r1.setName("ADMIN");
		r1.setDescription("Administrator");
		r1.setCreatedBy("jane");
		
		Role r2 = new Role();
		r2.setName("USER");
		r2.setDescription("Signed in user");
		r2.setCreatedBy("jane");
		
		List<Role> savedRoles = this.roleRepository.saveAll(List.of(r1, r2));
		
		List<User> half1 = users.subList(0, users.size());
		List<User> half2 = users.subList((users.size() - 1) / 2, users.size() - 1);
		half1.stream().forEach(user -> user.setRoles(List.of(r1)));
		half2.stream().forEach(user -> user.setRoles(List.of(r2)));
		
		List<User> usersRoles = Stream.concat(half1.stream(), half2.stream())
				.collect(Collectors.toList());
		this.userRepository.saveAll(usersRoles);
		
		return savedRoles;
	}
	
	private List<Category> seedCategoriesTable() {
		List<String> names = List.of(
				"Comedy", 
				"Romance", 
				"Horror", 
				"Action", 
				"Western", 
				"Science Fiction", 
				"Drama", 
				"Thriller", 
				"Fantasy"
		);
		
		List<Category> cats = names.stream().map(name -> {
			Category cat = new Category();
			cat.setName(name);
			cat.setActive(true);
			cat.setCreatedBy("jane");
			return cat;
		}).collect(Collectors.toList());
		
		return this.categoryRepository.saveAll(cats);
	}
	
	private List<Movie> seedMoviesTable(List<Category> categories) {
		List<Movie> movs = categories.stream().map(cat -> {
			Movie mov = new Movie();
			mov.setCategory(cat);
			mov.setName(String.format("%s Movie", cat.getName()));
			mov.setSynopsis(String.format("%s movie description...", cat.getName()));
			mov.setYear("2024");
			mov.setCreatedBy("jane");
			return mov;
		}).collect(Collectors.toList());
		
		return this.movieRepository.saveAll(movs);
	}
	
	private List<Rating> seedRatingsTable(List<Movie> movies, User user) {
		List<Rating> rats = movies.stream().map(mov -> {
			Rating rat = new Rating();
			/*rat.setIdMovie(mov.getIdMovie());
			rat.setIdUser(user.getIdUser());*/
			rat.setMovie(mov);
			rat.setUser(user);
			rat.setStars(BigDecimal.valueOf(5));
			rat.setCreatedBy("jane");
			return rat;
		}).collect(Collectors.toList());
		
		return this.ratingRepository.saveAll(rats);
	}
}
