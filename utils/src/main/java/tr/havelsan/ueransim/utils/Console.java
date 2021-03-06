package tr.havelsan.ueransim.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

public class Console {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static boolean startOfLine = true;
    private static List<Consumer<String>> printHandlers = new ArrayList<>();

    private static String getTime() {
        Calendar cal = Calendar.getInstance();
        return String.format("[%s] ", DATE_FORMAT.format(cal.getTime()));
    }

    public static void println(Color color, String format, Object... args) {
        if (color == null)
            color = Color.RESET;
        String string = String.format(format, args);
        if (startOfLine) {
            startOfLine = false;
            string = getTime() + string;
        }

        outputLine(color + string + Color.RESET);
        startOfLine = true;
    }

    public static void println(String format, Object... args) {
        String string = String.format(format, args);
        if (startOfLine) {
            startOfLine = false;
            string = getTime() + string;
        }

        outputLine(string);
        startOfLine = true;
    }

    public static void println() {
        outputLine();
        startOfLine = true;
    }

    public static void print(String format, Object... args) {
        String string = String.format(format, args);
        if (startOfLine) {
            startOfLine = false;
            string = getTime() + string;
        }

        output(string);
    }

    public static void print(Color color, String format, Object... args) {
        if (color == null)
            color = Color.RESET;
        String string = String.format(format, args);
        if (startOfLine) {
            startOfLine = false;
            string = getTime() + string;
        }

        output(color + string + Color.RESET);
    }

    public static void printDiv() {
        println("-----------------------------------------------------------------------------");
    }

    public synchronized static void addPrintHandler(Consumer<String> handler) {
        printHandlers.add(handler);
    }

    private synchronized static void outputLine() {
        outputLine("");
    }

    private synchronized static void outputLine(String string) {
        output(String.format("%s%n", string));
    }

    private synchronized static void output(String string) {
        System.out.print(string);

        for (var handler : printHandlers)
            handler.accept(string);
    }
}
