import blocksworld.Grid;
import blocksworld.GridController;
import blocksworld.Pair;
import blocksworld.Position;
import blocksworld.exceptions.InvalidBlockIDException;
import blocksworld.exceptions.InvalidPositionException;
import blocksworld.search.*;
import org.omg.CORBA.DynAnyPackage.Invalid;

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

        BlocksWorld.header();

        try {
            if (argList.contains("--help")) {
                help();
                return;
            }

            int width = -1;
            int height = -1;
            int refresh = -1;

            Long seed = null;
            if (argList.contains("--height") || argList.contains("-h")) {
                int index = (argList.contains("-h")) ? argList.indexOf("-h") : argList.indexOf("--height");
                height = getInt(argList, index);
            }
            if (argList.contains("--width") || argList.contains("-w")) {
                int index = (argList.contains("-w")) ? argList.indexOf("-w") : argList.indexOf("--width");
                width = getInt(argList, index);
            }

            if (argList.contains("--interval") || argList.contains("-i")) {
                int index = (argList.contains("-i")) ? argList.indexOf("-i") : argList.indexOf("--interval");
                refresh = getInt(argList, index);
            }

            if (width == -1 || height == -1) {
                System.out.println("Please specify width/height.");
                return;
            }

            if (argList.contains("--ran") || argList.contains("-r")) {
                int index = (argList.contains("-r")) ? argList.indexOf("-r") : argList.indexOf("--ran");
                seed = getSeed(argList, index);
            }

            if (!argList.contains("--type") && !argList.contains("-t")) {
                System.out.println("Please specify a search type. (See help for more details)");
                return;
            }

            int index = (argList.contains("-t")) ? argList.indexOf("-t") : argList.indexOf("--type");
            try {

                String type = argList.get(index + 1);
                Grid startGrid = null;
                Grid exitGrid = null;
                if (argList.contains("--start") || argList.contains("-s")){
                    int startIndex = (argList.contains("-s")) ? argList.indexOf("-s") : argList.indexOf("--start");
                    startGrid = parseState(argList.get(startIndex + 1), width, height);
                }
                if(argList.contains("--exit") || argList.contains("-e")) {
                    int exitIndex = (argList.contains("-e")) ? argList.indexOf("-e") : argList.indexOf("--exit");
                    exitGrid = parseState(argList.get(exitIndex + 1), width, height);
                }

                search(type, startGrid, exitGrid, seed, refresh);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("No option was specified for " + argList.get(index));
            }
        } catch (ParseException ex) {
            System.out.println("Failed to read input: " + ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("No argument specified: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("No input provided for option: " + ex.getMessage());
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
        System.out.println("  -e, --exit [STATE]\tSpecifies exit state.");
        System.out.println("  -h, --height [HEIGHT]\tSets the grid height.");
        System.out.println("       --help\t\tPrints this help message.");
        System.out.println("  -i, --interval [TIME]\tSets the refresh interval (in ms) - for monitoring search status.");
        System.out.println("  -r, --ran [STATE]\tSpecifies the seed used for the pseudo-random number.");
        System.out.println("  -s, --start [STATE]\tSpecifies the start state");
        System.out.println("  -t, --type [TYPE]\tSpecifies the search type:\r\n\t\t\tBFS - Breadth First Search\r\n\t\t\tDFS - Depth First Search\r\n\t\t\tIDS - Iterative Deepening Search\r\n\t\t\tA* - A* Heuristic Search");
        System.out.println("  -w, --width [WIDTH]\tSets the grid width.");
    }

    private static int getInt(List<String> args, int argIndex) throws ParseException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        String widthStr = args.get(argIndex + 1);
        if (widthStr.substring(0, 1).equals("'")) {
            throw new IllegalArgumentException("No option was specified for " + args.get(argIndex));
        }
        return Integer.parseInt(widthStr);
    }

    private static long getSeed(List<String> args, int argIndex) throws ParseException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        String seedStr = args.get(argIndex + 1);
        return Long.parseLong(seedStr);
    }

    private static Grid parseState(String state, int src_width, int src_height) throws ParseException {
        List<String> substrs = Arrays.asList(state.split(":"));
        try {
            int width = Integer.parseInt(substrs.get(0));
            int height = Integer.parseInt(substrs.get(1));
            if(width != src_width || height != src_height){
                throw new ParseException("Height/Width in state does not match provided height/width.", 0);
            }
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
                            try {
                                g.placeAgent(x, i - 2);
                            } catch (InvalidBlockIDException ex){
                                System.out.println("Multiple agents detected... skipping");
                            }
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

    private static void search(String type, Grid startState, Grid exitState, Long seed, int refreshTime) {
        Search search = null;
        switch (type) {
            case "BFS":
                search = new BFS();
                break;
            case "DFS":
                search = new DFS();
                break;
            case "IDS":
                search = new IDS();
                break;
            case "A*":
                search = new AStar();
                break;
            default:
                header();
                System.out.println(String.format("Type '%s' was not recognised.", type));
                return;
        }
        if (search == null) return;
        if (startState != null) {
            search.setStartState(startState);
        }
        if (exitState != null) {
            search.setExitState(exitState);
        }
        if(seed != null){
            search.setSeed(seed);
        }
        if(refreshTime != -1L){
            search.setRefreshTime(refreshTime);
        }
        search.run();
    }
}
