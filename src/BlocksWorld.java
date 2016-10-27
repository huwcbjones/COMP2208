import blocksworld.Search;
import blocksworld.search.BDF;
import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.exceptions.InvalidPositionException;
import blocksworld.search.DFS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BlocksWorld
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class BlocksWorld {

    public static void main(String[] args){
        List<String> argList = Arrays.asList(args);

        if(argList.contains("--help") || argList.contains("-h")) {
            help();
        } else if(argList.contains("--search") || argList.contains("-s")){
            int index = (argList.contains("-s")) ? argList.indexOf("-s") : argList.indexOf("--search");
            try {
                String type = argList.get(index + 1);
                search(type);
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("No option was specified for " + argList.get(index));
            }
        } else {
            header();
        }
    }

    /**
     * Prints out programme header info
     */
    private static void header() {
        System.out.println("Usage: BlocksWorld [OPTION]...");
        System.out.println("COMP2208 BlocksWorld Search Tool.\n");
    }

    /**
     * Prints out help
     */
    private static void help() {
        BlocksWorld.header();
        //System.out.println("Arguments:");
        //System.out.println("  -b, --batch\t\tDo not enter interactive mode after config is loaded. Requires -n.");
        //System.out.println("  -f, --file\t\tSpecifies file to load simulation config.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        //System.out.println("  -i, --interactive\tRuns in interactive mode.");
        //System.out.println("  -n, --number [INT]\tProcesses [INT] number of months.");
        System.out.println("  -s, --search\t\tSpecifies the search type:\r\n\t\t\tBDF - Breadth First Search\r\n\t\t\tDFS - Depth First Search\r\n\t\t\tIDS - Iterative Deepening Search\r\n\t\t\tA* - A* Heuristic Search");
        //System.out.println("  -o, --output [FILE]\tWhen simulation is complete, saves zoo state to [FILE]. Will overwrite existing files. \n\t\t\tIf no file is specified, will output to stdout. Implies -b, requires -n.");
        //System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void search(String type){
        Search search;
        switch(type){
            case "BDF":
                search = new BDF();

                search.run();
                break;
            case "DFS":
                search = new DFS();

                search.run();
                break;
            case "IDS":
                break;
            case "A*":
                break;
            default:
                header();
                System.out.println(String.format("Option '%s' was not recognised.", type));
        }
    }
}
