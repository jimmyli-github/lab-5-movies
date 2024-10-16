package movies;

import cs.Genre;
import cs.MovieMaps;
import cs.TitleType;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * The subclass of the IMDB abstract class that implements all the required
 * abstract query methods.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class MyIMDB extends IMDB {
    /** The minimum number of votes a movie needs to be considered for top ranking */
    private final static int MIN_NUM_VOTES_FOR_TOP_RANKED = 1000;

    /**
     * Create IMDB using the small or large dataset.
     *
     * @param small true if the small dataset is desired, otherwise the large one
     * @throws FileNotFoundException
     */
    public MyIMDB(boolean small) throws FileNotFoundException {
        super(small);
    }

    @Override
    public Collection<Movie> getMovieTitleWithWords(String type, String words) {
        // we simply loop over movieList and add to our list the movies that
        // have the same type, and contain the words substring
        List<Movie> result = new LinkedList<>();

        // TODO Activity 1.2
        for (Movie movie : movieList) {
            TitleType titleType = TitleType.valueOf(type);   // titleType is type
            // for a particular Movie, movie
            if (movie.getTitleType() == titleType && movie.getTitle().contains(words)) {
                // there is a match
                result.add(movie);
            }
        }

        return result;
    }

    @Override
    public Movie findMovieByID(String ID) {
        // TODO Activity 2.3
        return movieMap.get(ID);
    }

    @Override
    public Collection<Movie> getMoviesByYearAndGenre(String type, int year, String genre) {
        // we use Movie's natural order comparison which is to order Movie's of a
        // type by title and then year
        Set<Movie> result = new TreeSet<>();

        // TODO Activity 3.2
        Genre genre1 = Genre.valueOf(genre);
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getGenres().contains(genre1) && movie.getTitleType() == titleType && movie.getYear() == year) {
                result.add(movie);
            }
        }
        return result;
    }

    @Override
    public Collection<Movie> getMoviesByRuntime(String type, int start, int end) {
        // we use a comparator which orders Movie's of a type by descending runtime
        // and then title
        Set<Movie> result = new TreeSet<>(new MovieComparatorRuntime());

        // TODO Activity 4.2
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie: movieList) {
            if (movie.getTitleType() == titleType && start <= movie.getRuntimeMinutes() && movie.getRuntimeMinutes() <= end) {
                result.add(movie);
            }
        }

        return result;
    }

    @Override
    public Collection<Movie> getMoviesMostVotes(int num, String type) {
        // use a comparator that orders Movie's of a type by descending number
        // of votes

        List<Movie> result = new LinkedList<>();

        // TODO Activity 5.3
        TitleType titleType = TitleType.valueOf(type);
        movieList.sort(new MovieComparatorVotes());
        for (Movie movie : movieList) {
            if (movie.getTitleType() == titleType && result.size() < num) {
                result.add(movie);
            }
        }

        return result;
    }

    @Override
    public Map<Integer, List<Movie>> getMoviesTopRated(int num, String type, int start, int end) {
        Map<Integer, List<Movie>> result = new TreeMap<>();

        // TODO Activity 6.2
        ArrayList<Rating> ratingList = new ArrayList<>();
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getRating().getNumVotes() >= MIN_NUM_VOTES_FOR_TOP_RANKED && movie.getTitleType() == titleType && start <= movie.getYear() && movie.getYear() <= end) {
                ratingList.add(movie.getRating());
            }
        }
        Collections.sort(ratingList);
        for (int i = start; i <= end; i++) {
            result.put(i, new ArrayList<>());
            for (Rating rating : ratingList) {
                String id = rating.getID();
                if (result.get(i).size() < num && findMovieByID(id).getYear() == i) {
                    result.get(i).add(findMovieByID(id));
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, List<Movie>> getMoviesTopRatedByGenre(int num, String type, String genre) {
        Map<String, List<Movie>> result = new TreeMap<>();

        ArrayList<Rating> ratingList = new ArrayList<>();
        TitleType titleType = TitleType.valueOf(type);
        for (Movie movie : movieList) {
            if (movie.getRating().getNumVotes() >= MIN_NUM_VOTES_FOR_TOP_RANKED && movie.getTitleType() == titleType && movie.getGenres().contains(Genre.valueOf(genre))) {
                ratingList.add(movie.getRating());
            }
        }
        Collections.sort(ratingList);

        result.put(genre, new ArrayList<>());
        for (Rating rating : ratingList) {
            String id = rating.getID();
            if (result.get(genre).size() < num) {
                result.get(genre).add(findMovieByID(id));

            }
            else {
                break;
            }
        }

        return result;
    }
}