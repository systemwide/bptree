
/*****************************************************************************************
 * @file  MovieDB.java
 *
 * @author   John Miller
 */

import static java.lang.System.out;

import java.util.Random;
// for help with random number generator


/*****************************************************************************************
 * The MovieDB class makes a Movie Database.  It serves as a template for making other
 * databases.  See "Database Systems: The Complete Book", second edition, page 26 for more
 * information on the Movie Database schema.
 */
class MovieDB
{
    /*************************************************************************************
     * Main method for creating, populating and querying a Movie Database.
     * @param args  the command-line arguments
     */
    public static void main (String [] args)
    {
        out.println ();

        Table movie = new Table ("movie", "title year length genre studioName producerNo",
                                          "String Integer Integer String String Integer", "title year");

        Table cinema = new Table ("cinema", "title year length genre studioName producerNo",
                                            "String Integer Integer String String Integer", "title year");

        Table movieStar = new Table ("movieStar", "name address gender birthdate",
                                                  "String String Character String", "name");

        Table starsIn = new Table ("starsIn", "movieTitle movieYear starName",
                                              "String Integer String", "movieTitle movieYear starName");

        Table movieExec = new Table ("movieExec", "certNo name address fee",
                                                  "Integer String String Float", "certNo");

        Table studio = new Table ("studio", "name address presNo",
                                            "String String Integer", "name");
        
        
        String[] randGenres = {"Action", "SciFi", "RomCom", "Drama", "Thriller", "Horror"};
  
/********************************************
 *  Code to generate large sets of tuples for movie table
 *  @author Ben Rotolo       
 */
        Random rand = new Random();
       
        // Generate 150 tuples for movie table
        for(int i = 0;i<=150;i++)
        {
        	int randDate = rand.nextInt(97) + 1920;
        	int randLength = rand.nextInt(90) + 90;
        	int randProducer = rand.nextInt(50);
        	int randStudio = rand.nextInt(20) + 1;
        	String thisGenre = randGenres[rand.nextInt(6)];
        	
        	Comparable [] filmX = {"Film_"+ i, randDate, randLength, thisGenre, "Studio_" + randStudio, randProducer};
        	
        	// populate table movie
        	movie.insert(filmX);
        	// populate cinema with only even entries.
        	if(i%2 == 0) cinema.insert(filmX);
        }
        
        movie.print();
        out.println();
        
 /********************************************
  *  Code to generate large sets of tuples for moveStar table
  *  @author Ben Rotolo       
  */
        for(int i = 0;i<=200;i++)
        {
        	// random variables for movie star bio
        	int randMonth = rand.nextInt(12) +1;
        	int randDay = rand.nextInt(28) + 1;
        	int randYear = rand.nextInt(80) + 19;
        	int randAddress = rand.nextInt(3000);
        	char starGender;
        	// decide gender
        	if(i % 2 == 0) starGender = 'F';
        	else starGender = 'M';
        	String randBirthday = Integer.toString(randMonth) + "/" + Integer.toString(randDay) + "/" + Integer.toString(randYear);
        	
        	Comparable [] starX = {"StarName_"+i, "Hollywood", starGender, randBirthday};
        	movieStar.insert(starX);
        }

        out.println ();
        
        movieStar.print ();
        
        // Table "starsin" is not used in testing so I have not implemented it here
        
        // cast is not used in testing cases so I opted not to alter it
        Comparable [] cast0 = { "Star_Wars", 1977, "Carrie_Fisher" };
        out.println ();
        starsIn.insert (cast0);
        starsIn.print ();

        /********************************************
         *  Code to generate large sets of tuples for movieExec table
         *  @author Ben Rotolo       
         */
        
        for(int i = 0;i <= 50;i++)
        {
        	int randSalary = rand.nextInt() + 10000;
        	Comparable [] execX = { i, "Exec_" + i, "Hollywood", (float)randSalary };
        	movieExec.insert(execX);
        }
        out.println();
        movieExec.print ();

        
        /********************************************
         *  Code to generate large sets of tuples for studio table
         *  @author Ben Rotolo       
         */
        
        for(int i= 0;i<=20;i++)
        {
        	Comparable [] studioX = {"Studio_" + i, "Holloywood, CA", i * 111};
        	studio.insert(studioX);
         }
        
        
        out.println ();
        studio.print ();

        movie.save ();
        cinema.save ();
        movieStar.save ();
        starsIn.save ();
        movieExec.save ();
        studio.save ();

        movieStar.printIndex ();

        //--------------------- project: title year

        out.println ();
        Table t_project = movie.project ("title year");
        t_project.print ();
        t_project.save();

        //--------------------- project: non-existent arguments
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Testing made up terms");
        Table t_project2 = movie.project ("made up terms");
        t_project2.print ();
        
        //--------------------- project: no arguments
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Testing no terms");
        Table t_project3 = movie.project ("");
        t_project3.print ();


        out.println();
        Table t_select_no_argument = movie.select();
        t_select_no_argument.print();
        t_select_no_argument.save();
        
        
        //--------------------- select: equals, &&

        out.println ();
        Table t_select = movie.select (t -> t[movie.col("title")].equals ("Film_22") &&
                                            t[movie.col("year")].equals (1977));
        t_select.print ();
        t_select.save();
        
        
        //--------------------- select: equals, ||
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Testing Select with .equals and || ");
        out.println ();
        Table t_select_or = movie.select (t -> t[movie.col("title")].equals ("Film_44") ||
                                             t[movie.col("title")].equals ("Film_55")) ;
        t_select_or.print ();
        t_select_or.save();
        
        //--------------------- select: !equals
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Testing Select with !equals");
        out.println ();
        Table t_select_not = movie.select (t -> !t[movie.col("title")].equals ("Star_Wars"));
                                            
        t_select_not.print ();
        t_select_not.save();
        
        //--------------------- select: <

        out.println ();
        Table t_select2 = movie.select (t -> (Integer) t[movie.col("year")] < 1980);
        t_select2.print ();

        //--------------------- indexed select: key

        out.println ();
        Table t_iselect = movieStar.select (new KeyType ("StarName_25"));
        t_iselect.print ();
        
        //--------------------- select with blank key
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Select with a  ' ' for key...");
        Table t_iselect_blank = movieStar.select (new KeyType (" "));
        t_iselect_blank.print ();
        
        //--------------------- select without args
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Select with out arguments...");
        Table t_iselect_no_arg = movieStar.select ();
        t_iselect_no_arg.print ();

     
        //--------------------- union: movie UNION cinema

        out.println ();
        Table t_union = movie.union (cinema);
        t_union.print ();
        
        //--------------------- union: movie UNION cinema
        /**
         *@author Ben Rotolo
         *
         */
        out.println("self-Union movie with movie");
        Table t_union_self = movie.union(movie);
        t_union_self.print();

        
        //--------------------- union: movie UNION movie
        /**
         *@author Ben Rotolo
         *
         */
        out.println("self-Union movie with movie");
        Table t_union_self1 = movie.union(movie);
        t_union_self1.print();

        //--------------------- minus: movie MINUS cinema

        out.println ();
        Table t_minus = movie.minus (cinema);
        t_minus.print ();
        
        //--------------------- minus: movie MINUS movie
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Movie minus self...");
        Table self_minus = movie.minus (movie);
        self_minus.print ();

        
        //--------------------- equi-join: movie JOIN studio ON studioName = name

        out.println ();
        Table t_join = movie.join ("studioName", "name", studio);
        t_join.print ();

        
        //--------------------- natural join: movie JOIN studio

        out.println ();
        Table t_join2 = movie.join (cinema);
        t_join2.print ();
        
        //--------------------- natural join: join on self.
        /**
         *@author Ben Rotolo
         *
         */
        out.println ("Movie join with movie");
        Table self_join = movie.join (movie);
        self_join.print ();
        
      //---------------------- cross product of two disparate tables: move and movieStar  
        
        out.println("Cross Product: movie and movieStar have no common attributes");
        Table cross_product = movie.join(movieStar);
        cross_product.print();

    } // main

} // MovieDB class
