public class ArithmeticCoder extends ArithmeticCodingProcessor {
    private int bufferByte;
    private int bufferByteFreeBits;
    private int bitsToFollow;

    public ArithmeticCoder(double normalWeightsMax, double ceilingWeightsMax, FileWriterBin out) {
        super(normalWeightsMax, ceilingWeightsMax, out);
        bufferByte = 0;
        bufferByteFreeBits = 8;
        bitsToFollow = 0;
    }

    @Override
    public void putByte(byte val) {
        // Mask to cast without sign and get index
        int index = val & 0xFF;
        updateWorkingRange(index);
        tryPutBits();
        updateWeight(index);
    }

    @Override
    public void finish() {
        // Just write final byte
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
            if (workingHigh < 0.5) {
                zoomIn(0);
                writeBit(0);
            } else if (workingLow >= 0.5) {
                zoomIn(1);
                writeBit(1);
            } else if (workingLow >= 0.25 && workingHigh < 0.75) {
                zoomIn(0.5);
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
        boolean isLeftInFirstQtr = (workingLow < 0.25);
        writeBit(isLeftInFirstQtr ? 0 : 1);
        writeBit(!isLeftInFirstQtr ? 0 : 1);

        bufferByte <<= bufferByteFreeBits;
        bufferByteFreeBits = 0;
        out.writeByte(bufferByte);
        clearBuffByte();
    }

    private void writeBitPlusFollow(int bit) {
        writeBit(bit);
        while (bitsToFollow > 0) {
            writeBit(bit == 0 ? 1 : 0);
            bitsToFollow--;
        }
    }

    private void writeBit(int bit) {
        if (bufferByteFreeBits == 0) {
            out.writeByte(bufferByte);
            clearBuffByte();
        }
        bufferByte = ((bufferByte << 1) | bit);
        bufferByteFreeBits--;
    }

    private void clearBuffByte() {
        bufferByte = 0;
        bufferByteFreeBits = 8;
    }
}
