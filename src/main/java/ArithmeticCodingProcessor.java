import java.util.Arrays;

public abstract class ArithmeticCodingProcessor {
    public static final int alphabetLen = 256;
    protected static final double weightsMin = 1;
    protected final double[] weights;
    protected final double[] splitPoints;
    protected final double normalWeightsMax;
    protected final double ceilingWeightsMax;
    protected final MyWriter out;

    protected double workingLow;
    protected double workingHigh;

    public abstract void putByte(byte val);

    public abstract void finish();

    protected ArithmeticCodingProcessor(double normalWeightsMax, double ceilingWeightsMax, MyWriter out) {
        this.normalWeightsMax = normalWeightsMax;
        this.ceilingWeightsMax = ceilingWeightsMax;
        this.weights = new double[alphabetLen + 1];
        Arrays.fill(this.weights, weightsMin);
        splitPoints = new double[alphabetLen + 2];
        recalculateSplitPoints();
        this.out = out;


        workingLow = 0;
        workingHigh = 1;
    }

    protected void recalculateSplitPoints() {
        double fullSum = 0;
        for (double weight : weights) {
            fullSum += weight;
        }
        double sum = 0;
        splitPoints[0] = 0;
        for (int i = 0; i < alphabetLen + 1; i++) {
            sum += weights[i];
            splitPoints[i + 1] = sum / fullSum;
        }
    }

    protected void fixWeights() {
        double multiplier = normalWeightsMax / ceilingWeightsMax;
        for (int i = 0; i < alphabetLen + 1; i++) {
            weights[i] = Double.max(weights[i] * multiplier, weightsMin);
        }
    }

    protected void updateWeight(int index) {
        weights[index] += 1;
        if (weights[index] >= ceilingWeightsMax) {
            fixWeights();
        }
        recalculateSplitPoints();
    }
}
