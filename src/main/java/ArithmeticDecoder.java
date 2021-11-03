public class ArithmeticDecoder extends ArithmeticCodingProcessor {
    private boolean isEnd;
    private double probableLow;
    private double probableHigh;
    int lowSplitPointInd;
    int highSplitPointInd;

    public ArithmeticDecoder(double normalWeightsMax, double ceilingWeightsMax, FileWriterBin out) {
        super(normalWeightsMax, ceilingWeightsMax, out);
        isEnd = false;
        probableLow = 0;
        probableHigh = 1;

        lowSplitPointInd = -1;
        highSplitPointInd = splitPoints.length;
    }

    @Override
    public void putByte(byte val) {
        if(isEnd)
            return;
        //TODO: Continue to write from here.

    }

    @Override
    public void finish() {

    }
}
