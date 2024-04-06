package pcms2;

import polygon.ProblemDescriptor;

import java.io.PrintWriter;

public class Mediator {
    //<mediator type = "%copy"/>
    final String type;

    public Mediator(String type) {
        this.type = type;
    }

    public static Mediator parse(ProblemDescriptor problemDescriptor) {
        if (problemDescriptor.getRunCount() == 2)
            return new Mediator("%copy");

        return null;
    }

    public void print(PrintWriter writer, String tabs) {
        writer.printf(tabs + "<mediator type = \"%s\"/>\n", type);
    }
}
