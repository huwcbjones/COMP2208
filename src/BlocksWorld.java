import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.Position;
import blocksworld.Search;
import blocksworld.exceptions.InvalidPositionException;
import blocksworld.search.BFS;
import blocksworld.search.DFS;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * BlocksWorld
 *
 * @author Huw Jones
 * @since 08/10/2016
 */
public class BlocksWorld {

    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);

        if (argList.contains("--help") || argList.contains("-h")) {
            help();
        } else if (argList.contains("--type") || argList.contains("-t")) {
            int index = (argList.contains("-t")) ? argList.indexOf("-t") : argList.indexOf("--type");
            try {
                String type = argList.get(index + 1);
                search(type);
            } catch (ArrayIndexOutOfBoundsException e) {
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
        System.out.println("Arguments:");
        System.out.println("  -e, --exit [STATE]\t\tSpecifies exit state.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -s, --start [STATE]\t\tSpecifies the start state");
        System.out.println("  -t, --type\t\tSpecifies the search type:\r\n\t\t\tBFS - Breadth First Search\r\n\t\t\tDFS - Depth First Search\r\n\t\t\tIDS - Iterative Deepening Search\r\n\t\t\tA* - A* Heuristic Search");
        //System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void search(String type) {
        Search search;
        switch (type) {
            case "BFS":
                search = new BFS();

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

    private static Grid parseState(String state) throws ParseException {
        List<String> substrs = Arrays.asList(state.split("|"));
        try {
            int width = Integer.parseInt(substrs.get(0));
            int height = Integer.parseInt(substrs.get(1));
            Grid g = GridController.createGrid(width, height);

            String row;
            char symbol;
            for (int i = 2; i < substrs.size(); i++) {
                row = substrs.get(i);
                for (int x = 0; x < g.getWidth(); x++) {
                    symbol = row.charAt(x);
                    try {
                        if (symbol >= 'a' && symbol <= 'z') {
                            g.placeBlock(symbol, new Position(x, i - 2));
                        } else if (symbol == '*') {
                            g.placeAgent(x, i - 2);
                        }
                    } catch (InvalidPositionException e) {
                        e.printStackTrace();
                    }
                }
            }
            return g;
        } catch (NumberFormatException ex) {
            throw new ParseException(ex.getMessage(), 0);
        }
    }
}
