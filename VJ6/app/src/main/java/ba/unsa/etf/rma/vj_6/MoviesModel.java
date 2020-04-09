package ba.unsa.etf.rma.vj_6;

import java.util.ArrayList;
import java.util.Arrays;

public class MoviesModel {

    public static ArrayList<Movie> movies = new ArrayList<Movie>() {
        {
            add(new Movie("Fight Club",
                    "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground fight clubs forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
                    "10.12.1999.","https://www.imdb.com/title/tt0137523/",
                    "action",
                    new ArrayList<String>(Arrays.asList("Edward Norton", "Brad Pitt", "Helena Bonham Carter", "Meat Loaf", "Zach Grenier")),
                    new ArrayList<String>(Arrays.asList("Reservoir Dogs", "Jackie Brown", "Pulp Fiction", "Memento", "Seven"))
            ));
            add(new Movie("Pulp Fiction",
                    "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
                    "14.10.1994.","https://www.imdb.com/title/tt0110912/",
                    "crime",
                    new ArrayList<String>(Arrays.asList("John Travolta", "Samuel L. Jackson", "Tim Roth", "Bruce Willis", "Uma Thurman")),
                    new ArrayList<String>(Arrays.asList("Reservoir Dogs", "Jackie Brown", "Fight Club", "Memento", "Seven"))
            ));
            add(new Movie("The Lord of the Rings: The Fellowship of the Ring",
                    "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.",
                    "10.12.2001","https://www.imdb.com/title/tt0120737/",
                    "fantasy",
                    new ArrayList<String>(Arrays.asList("Ian McKellen", "Elijah Wood","Viggo Mortensen","Liv Tyler","Sean Astin")),
                    new ArrayList<String>(Arrays.asList("Lord of the Rings: The Two Towers", "Lord of the Rings: The Return of the King", " An Unexpected Journey", "The Desolation of Smaug ", "The Battle of the Five Armies"))
            ));
            add(new Movie("Serenity",
                    "The crew of the ship Serenity try to evade an assassin sent to recapture one of their members who is telepathic.",
                    "30.09.2005","https://www.imdb.com/title/tt0379786/",
                    "scifi",
                    new ArrayList<String>(Arrays.asList("Nathan Fillion", "Alan Tudyk", "Adam Baldwin", "Summer Glau", "Chiwetel Ejiofor")),
                    new ArrayList<String>(Arrays.asList("Guardians of the Galaxy", "The Fifth Element", "The Avengers", "Star Trek Beyond", "Farscape: The Peacekeeper Wars"))
            ));
            add(new Movie("Shaun of the Dead",
                    "A man's uneventful life is disrupted by the zombie apocalypse.",
                    "09.04.2004","https://www.imdb.com/title/tt0365748/",
                    "comedy",
                    new ArrayList<String>(Arrays.asList("Simon Pegg", "Kate Ashfield", "Nick Frost", "Lucy Davis", "Dylan Moran")),
                    new ArrayList<String>(Arrays.asList("Hot Fuzz", "The World's End", "Paul", "Zombieland ", "Dawn of the Dead"))
            ));
            add(new Movie("Watchmen",
                    "In 1985 where former superheroes exist, the murder of a colleague sends active vigilante Rorschach into his own sprawling investigation, uncovering something that could completely change the course of history as we know it.",
                    "23.02.2009.","https://www.imdb.com/title/tt0409459/",
                    "superhero",
                    new ArrayList<String>(Arrays.asList("Malin Akerman", "Billy Crudup", "Matthew Goode", "Carla Gugino", "Jackie Earle Haley")),
                    new ArrayList<String>(Arrays.asList("Batman: The Killing Joke", "V for Vendetta", "Dredd", "Sin City", "Dark City"))
            ));

        }
    };
}
