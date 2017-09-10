package ar.org.crossknight.twinify.console;

public final class App {

    private App() {}

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            Command[] commands = {new ScanCommand(), new CompareCommand(), new CloneCommand()};
            for (Command command: commands) {
                if (command.getName().equalsIgnoreCase(args[0])) {
                    try {
                        command.execute(args[1]);
                    } catch (TerminationException ex) {
                        // Safe to ignore
                    }
                    return;
                }
            }
        }
        help();
    }

    private static void help() {
        write("Usage:    twinify [command] [options]");
        write("Commands:");
        write("          scan  [twin path]      Creates a snapshot of the twin");
        write("          compare [donor path]   Compares the donor to the twin snapshot,");
        write("                                 creating a delta");
        write("          clone [twin path]      Applies delta to the twin");
    }

    public static void write(String text) {
        System.out.println(text);
    }

}