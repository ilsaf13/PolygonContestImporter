package pcms2.properties;

import polygon.properties.PointsPolicy;

public enum Scoring {
    SUM("sum"),
    GROUP("group"),
    MIN("min");

    private String word;

    Scoring(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

    /**
     * Maps Polygon points policy to PCMS Scoring.
     */
    public static Scoring parse(String pointsPolicy) {
        switch (pointsPolicy) {
            case "each-test":
                return Scoring.SUM;
            case "complete-group":
                return Scoring.GROUP;
        }
        throw new AssertionError("Couldn't parse scoring = '" + pointsPolicy + "'");
    }

    public static Scoring parse(PointsPolicy pointsPolicy) {
        switch (pointsPolicy) {
            case EACH_TEST:
                return Scoring.SUM;
            case COMPLETE_GROUP:
                return Scoring.GROUP;
        }
        throw new AssertionError("Couldn't parse scoring = '" + pointsPolicy + "'");
    }

    /**
     * Gets enum scoring from PCMS scoring.
     */
    public static Scoring getScoring(String word) {
        switch (word) {
            case "sum":
                return Scoring.SUM;
            case "group":
                return Scoring.GROUP;
            case "min":
                return Scoring.MIN;
        }
        throw new AssertionError("Couldn't parse scoring = '" + word + "'");
    }
}
