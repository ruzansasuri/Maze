/**
 * Maze.java
 * @author: Ruzan Sasuri            rps713@g.rit.edu
 * @author: Akash Venkartachalam    av2833@g.rit.edu
 * @author: Ghodratollah Aalipour   ga5481@g.rit.edu
 *
 * Versions: $Id: Maze.java v1.0 $
 * Revision: First Revision.
 */
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

/***
 * Maze
 * Finds a path through the maze that is stored in a file.
 */
public class Maze
{
    private static char maze[][];//Used to store the maze
    private static String path;//Used to store the current path being traversed
    private static int x;//Used to store the current x coordinate.
    private static int y;//Used to store the current y coordinate
    private static boolean forward;//Used to tell whether the path is moving forward or backward
    /**
     * Finds the number of lines in the file.
     * @param f The file handler that points to a text file that holds the maze.
     * @return len The number of lines in the file.
     * @throws IOException
     */
    private static int findSize(File f)throws IOException
    {
        Scanner s = new Scanner(f);
        int len = 0;
        while(s.hasNext())// Loops through file till no more lines are found.
        {
            s.nextLine();
            len++;
        }
        return len;
    }
    /**
     * Finds the entrance among the top and let side of the maze.
     * @return found whether the entrance exists or not.
     */
    private static boolean findEnterance()
    {
        boolean found = false;
        for(int i = 0; i < maze[0].length; i++)//Goes through top of maze to find the entrance.
        {
            if(maze[0][i] == ' ')
            {
                x = 0;
                y = i;
                found = true;
                break;
            }
        }
        if(!found)
        {
            for(int i = 1; i < maze.length; i++)//Loops through the left side of the maze to find the entrance.
            {
                if(maze[i][0] == ' ')
                {
                    x = i;
                    y = 0;
                    found = true;
                    break;
                }
            }
        }
        if(!found)
        {
            System.out.println("No enterance.");
        }
        maze[x][y] = '.';
        return found;
    }
    /**
     * If a path exists in the maze that hasn't been used it
     * adds the direction to path and converts the space to a dot.
     * If a path does not exist it converts the space to a /.
     * @return found whether the entrance exists or not.
     */
    private static void findPath()
    {
        if(maze[x + 1][y] == ' ')
        {
            forward = true;
            path += 'd';
            maze[x][y] = '.';
        }
        else if(maze[x][y + 1] == ' ')
        {
            forward = true;
            path += 'r';
            maze[x][y] = '.';
        }
        else if(maze[x - 1][y] == ' ')
        {
            forward = true;
            path += 'u';
            maze[x][y] = '.';
        }
        else if(maze[x][y - 1] == ' ')
        {
            forward = true;
            path += 'l';
            maze[x][y] = '.';
        }
        else
        {
            forward = false;
            maze[x][y] = '/';
        }
    }

    /**
     * Traverses through the available path. If dead end encountered, goes backward till it finds another path.
     * @return None
     */
    private static void traverse()
    {
        if (x == 0)
        {
            path += 'd';
        } else if (y == 0)
        {
            path += 'r';
        }
        while(true)//Loops till the end of the maze is found.
        {
            while (forward) // Loops till a dead end is found.
            {
                switch (path.charAt(path.length() - 1))
                {
                    case 'u':
                        x--;
                        break;
                    case 'd':
                        x++;
                        break;
                    case 'r':
                        y++;
                        break;
                    case 'l':
                        y--;
                        break;
                }
                if (x != maze.length - 1 && y != maze[0].length - 1)
                {
                    findPath();
                }
                else
                {
                    break;
                }
            }
            if (x == maze.length - 1 || y == maze[0].length - 1)
            {
                maze[x][y] = '.';
                System.out.println("Found a path...Printing now.");
                print();
                break;
            }
            else
            {
                while (!forward)//Loops till it reaches a point where more paths are available.
                {
                    if(x != 0 && y != 0)
                    {
                        findPath();
                        if(forward)
                        {
                            break;
                        }
                    }
                    else
                    {
                        System.out.println("No path found.");
                        return;
                    }
                    switch (path.charAt(path.length() - 1))
                    {
                        case 'u':
                            x++;
                            break;
                        case 'd':
                            x--;
                            break;
                        case 'r':
                            y--;
                            break;
                        case 'l':
                            y++;
                            break;
                    }
                    path = path.substring(0, path.length() - 1);
                }
            }
        }
    }

    /**
     * Prints the arra containing the maze.
     * @return None
     */
    private static void print()
    {
        for(int i = 0; i < maze.length; i++)//Traverses the rows of the array to print the maze.
        {
            for(int j = 0; j < maze[i].length; j++)//Traverses the elements of a particular row to print the maze.
            {
                if(maze[i][j] == '/')
                {
                    maze[i][j] = ' ';
                }
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * The main method
     * @param ag If path entered, it is used as the file location for the maze.txt file. If not entered, the default IntelliJ directory is used.
     * @throws IOException
     */
    public static void main(String ag[]) throws IOException
    {
        path = "";
        String loc;
        forward = true;
        if(ag.length == 0)
        {
            System.out.println("Enter the maze text file as an argument. Using IntelliJ config for now.");
            loc = System.getProperty("user.dir") + "\\src\\maze.txt";
        }
        else
        {
            loc = Arrays.toString(ag);
        }
        System.out.println("Using file location at: " + loc);
        File f = new File(loc);
        Scanner sc = new Scanner(f);
        int bre = sc.nextLine().length();
        sc.close();
        sc = new Scanner(f);
        int len = findSize(f);
        maze = new char[len][bre];
        for(int i = 0; i < maze.length; i++)//Loops through the maze in the file,
        {                                   //and stores all the lines as rows in the array.
            maze[i] = sc.nextLine().toCharArray();
        }
        if(!findEnterance())
        {
            System.exit(0);
        }
        traverse();
    }
}
