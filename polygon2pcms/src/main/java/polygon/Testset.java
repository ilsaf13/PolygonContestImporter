package polygon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmlwrapper.XMLElement;

import java.util.*;

public class Testset {
    private final static Logger logger = LogManager.getLogger(Testset.class);
    String name;
    String inputPathPattern;
    String outputPathPattern;
    double timeLimit;
    String memoryLimit;
    int testCount;

    //group name maps to group
    TreeMap<String, Group> groups;
    Test[] tests;

    public static Testset parse(XMLElement testsetElement) {
        Testset ts = new Testset();

        ts.name = testsetElement.getAttribute("name");

        ts.timeLimit = Double.parseDouble(testsetElement.findFirstChild("time-limit").getText());
        ts.memoryLimit = testsetElement.findFirstChild("memory-limit").getText();
        ts.testCount = Integer.parseInt(testsetElement.findFirstChild("test-count").getText());
        ts.inputPathPattern = testsetElement.findFirstChild("input-path-pattern").getText();
        ts.outputPathPattern = testsetElement.findFirstChild("answer-path-pattern").getText();

        //tests
        ts.tests = testsetElement.findFirstChild("tests").findChildrenStream("test").map(Test::parse).toArray(Test[]::new);
        if (ts.tests.length != ts.testCount) {
            logger.warn("test-count = " + ts.testCount +
                    " isn't equal to number of tests found = " + ts.tests.length);
        }

        //groups
        XMLElement groupsElement = testsetElement.findFirstChild("groups");
        if (groupsElement.exists()) {
            ts.groups = new TreeMap<>();
            for (XMLElement xmlElement : groupsElement.findChildren("group")) {
                Group group = Group.parse(xmlElement);
                ts.groups.put(group.name, group);
            }
        }
        return ts;
    }

    public int getSampleTestCount() {
        int res = 0;
        for (int i = 0; i < tests.length; i++) {
            if (tests[i].sample) res++;
        }
        return res;
    }
    public String getName() {
        return name;
    }

    public String getInputPathPattern() {
        return inputPathPattern;
    }

    public String getOutputPathPattern() {
        return outputPathPattern;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public int getTestCount() {
        return testCount;
    }

    public TreeMap<String, Group> getGroups() {
        return groups;
    }

    public Test[] getTests() {
        return tests;
    }
}
