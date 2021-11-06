public class ArithmeticDecoder extends ArithmeticCodingProcessor {
    private boolean isEnd;
    private double probableLow;
    private double probableHigh;
    int lowSplitPointInd;
    int highSplitPointInd;

    public ArithmeticDecoder(double normalWeightsMax, double ceilingWeightsMax, MyWriter out) {
        super(normalWeightsMax, ceilingWeightsMax, out);
        isEnd = false;
        probableLow = 0;
        probableHigh = 1;

        lowSplitPointInd = -1;
        highSplitPointInd = splitPoints.length;
    }

    @Override
    public void putByte(byte val) {
        if (isEnd)
            return;
        for (int i = 7; i >= 0; i--) {
            int bit = (val >> i) & 0x01;
            processNextBit(bit);
            tryOutputByte();
        }
    }

    @Override
    public void finish() {
        while (!isEnd) {
            processNextBit(0);
            tryOutputByte();
        }
        out.flush();
    }

    private void processNextBit(int bit) {
        double shrinkPoint = shrinkPoint();
        if (bit == 0) {
            probableHigh = shrinkPoint;
        } else {
            probableLow = shrinkPoint;
        }
    }

    private void tryOutputByte() {
        double range = workingHigh - workingLow;
        for (int i = lowSplitPointInd + 1; i < highSplitPointInd; i++) {
            if ((workingLow + splitPoints[i] * range) <= probableLow) {
                lowSplitPointInd = i;
            } else
                break;
        }
        for (int i = highSplitPointInd - 1; i > lowSplitPointInd; i--) {
            if ((workingLow + splitPoints[i] * range) >= probableHigh) {
                highSplitPointInd = i;
            } else
                break;
        }
        if (highSplitPointInd - lowSplitPointInd <= 1) {
            int index = lowSplitPointInd;
            lowSplitPointInd = -1;
            highSplitPointInd = splitPoints.length;
            if (index == alphabetLen) {
                isEnd = true;
                ///
//                out.flush();
            } else {
                updateWorkingRange(index);
                tryZoomIn();
                updateWeight(index);
                out.writeByte(index);
            }
        }
//        return rc;
    }

    private void updateWorkingRange(int index) {
        double range = workingHigh - workingLow;
        workingHigh = workingLow + splitPoints[index + 1] * range;
        workingLow = workingLow + splitPoints[index] * range;
    }

    private void tryZoomIn() {
        while (true) {
            if (workingHigh < 0.5) {
                zoomIn(0);
            } else if (workingLow >= 0.5) {
                zoomIn(1);
            } else if (workingLow >= 0.25 && workingHigh < 0.75) {
                zoomIn(0.5);
            } else {
                break;
            }
        }

    }

    private void zoomIn(double expandPoint) {
        workingLow = 2 * workingLow - expandPoint;
        workingHigh = 2 * workingHigh - expandPoint;
        probableLow = 2 * probableLow - expandPoint;
        probableHigh = 2 * probableHigh - expandPoint;
    }

    private double shrinkPoint() {
        return probableLow + (probableHigh - probableLow) / 2;
    }
}
