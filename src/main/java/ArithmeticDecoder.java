public class ArithmeticDecoder extends ArithmeticCodingProcessor {
    private static final int FIRST_BIT_MASK = 0x01;
    private static final int SHIFT_TO_FIRST_BIT = 7;
    private static final int NO_IND = -1;
    private static final int MIN_PROBABLE_DIST_FOR_SPLP_INDS = 1;

    private boolean isEnd;
    private double probableLow;
    private double probableHigh;
    private int lowSplitPointInd;
    private int highSplitPointInd;

    public ArithmeticDecoder(MyWriter out) {
        super(out);
        isEnd = false;
        probableLow = SEGM_MIN;
        probableHigh = SEGM_MAX;

        lowSplitPointInd = NO_IND;
        highSplitPointInd = splitPoints.length;
    }

    @Override
    public void putByte(byte val) {
        if (isEnd)
            return;
        for (int i = SHIFT_TO_FIRST_BIT; i >= 0; i--) {
            int bit = (val >> i) & FIRST_BIT_MASK;
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
        double shrinkPoint = getShrinkPoint();
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
        if (highSplitPointInd - lowSplitPointInd <= MIN_PROBABLE_DIST_FOR_SPLP_INDS) {
            int index = lowSplitPointInd;
            lowSplitPointInd = NO_IND;
            highSplitPointInd = splitPoints.length;
            if (index == ALPHABET_LEN) {
                isEnd = true;
            } else {
                updateWorkingRange(index);
                tryGetCloser();
                updateWeight(index);
                out.writeByte(index);
            }
        }
    }

    private void updateWorkingRange(int index) {
        double range = workingHigh - workingLow;
        workingHigh = workingLow + splitPoints[index + 1] * range;
        workingLow = workingLow + splitPoints[index] * range;
    }

    private void tryGetCloser() {
        while (true) {
            if (workingHigh < SECOND_QTR_MAX) {
                getCloser(SEGM_MIN);
            } else if (workingLow >= SECOND_QTR_MAX) {
                getCloser(SEGM_MAX);
            } else if (workingLow >= FIRST_QTR_MAX && workingHigh < THIRD_QTR_MAX) {
                getCloser(SECOND_QTR_MAX);
            } else {
                break;
            }
        }

    }

    private void getCloser(double expandPoint) {
        workingLow = NARROW_COEF * workingLow - expandPoint;
        workingHigh = NARROW_COEF * workingHigh - expandPoint;
        probableLow = NARROW_COEF * probableLow - expandPoint;
        probableHigh = NARROW_COEF * probableHigh - expandPoint;
    }

    private double getShrinkPoint() {
        return probableLow + (probableHigh - probableLow) / NARROW_COEF;
    }
}
