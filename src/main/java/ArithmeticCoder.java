public class ArithmeticCoder extends ArithmeticCodingProcessor {
    private static final int MASK_TO_UNSIGN = 0xFF;

    private int bitsToFollow;

    public ArithmeticCoder(MyWriter out) {
        super(out);
        bitsToFollow = 0;
    }

    @Override
    public void putByte(byte val) {
        int index = val & MASK_TO_UNSIGN;
        updateWorkingRange(index);
        tryPutBits();
        updateWeight(index);
    }

    @Override
    public void finish() {
        updateWorkingRange(alphabetLen);
        tryPutBits();
        writeFinalByte();
    }

    private void updateWorkingRange(int index) {
        double range = workingHigh - workingLow;
        workingHigh = workingLow + splitPoints[index + 1] * range;
        workingLow = workingLow + splitPoints[index] * range;
    }

    private void tryPutBits() {
        while (true) {
            if (workingHigh < SECOND_QTR_MAX) {
                zoomIn(SEGM_MIN);
                writeBitPlusFollow(0);
            } else if (workingLow >= SECOND_QTR_MAX) {
                zoomIn(SEGM_MAX);
                writeBitPlusFollow(1);
            } else if (workingLow >= FIRST_QTR_MAX && workingHigh < THIRD_QTR_MAX) {
                zoomIn(SECOND_QTR_MAX);
                bitsToFollow++;
            } else {
                break;
            }
        }
    }

    private void zoomIn(double point) {
        workingLow = 2 * workingLow - point;
        workingHigh = 2 * workingHigh - point;
    }

    private void writeFinalByte() {
        boolean isLeftInFirstQtr = (workingLow < FIRST_QTR_MAX);
        writeBitPlusFollow(isLeftInFirstQtr ? 0 : 1);
        writeBitPlusFollow(!isLeftInFirstQtr ? 0 : 1);
        out.flush();
    }

    private void writeBitPlusFollow(int bit) {
        out.writeBit(bit);
        while (bitsToFollow > 0) {
            out.writeBit(bit == 0 ? 1 : 0);
            bitsToFollow--;
        }
    }
}
