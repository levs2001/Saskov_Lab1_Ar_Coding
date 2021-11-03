import java.util.HashMap;

public class Coding {
    private static final double SEGMENT_BEG = 0;
    private static final double SEGMENT_END = 1;

    private class Segment {
        public double left;
        public double right;
    }

    private HashMap<Integer, Double> symsProb;
    private HashMap<Integer, Segment> segments;
    private double result;
    private int textLength = 0;

    public Coding(String filename, int bufferSize) {
        symsProb = Alphabet.getSymsProbability(filename, bufferSize);
        segments = defineSegments();
        result = arithmeticCoding(filename, bufferSize);
    }

    public void writeCodedFile(String filename) {
        FileWriterBin fileWriterBin = new FileWriterBin(filename);

        fileWriterBin.writeDouble(result);
        fileWriterBin.writeInt(textLength);
        for (Integer sym : symsProb.keySet()) {
            fileWriterBin.writeByte(sym);
            fileWriterBin.writeDouble(symsProb.get(sym));
        }

        fileWriterBin.close();
    }

    private double arithmeticCoding(String filename, int bufferSize) {
        double left = SEGMENT_BEG;
        double right = SEGMENT_END;
        textLength = 0;
        FileReaderBuff buffFile = new FileReaderBuff(filename, bufferSize);

        Integer sym = buffFile.readSym();
        while (sym != FileReaderBuff.END_STREAM) {
            textLength++;

            double newRight = left + (right - left) * segments.get(sym).right;
            double newLeft = left + (right - left) * segments.get(sym).left;
            left = newLeft;
            right = newRight;
            sym = buffFile.readSym();

        }
        buffFile.close();

        return (left + right) / 2;
    }

    private HashMap<Integer, Segment> defineSegments() {
        HashMap<Integer, Segment> segments = new HashMap<>();
        double left = SEGMENT_BEG;
        for (Integer sym : symsProb.keySet()) {
            segments.put(sym, new Segment());
            segments.get(sym).left = left;
            segments.get(sym).right = left + symsProb.get(sym);
            left = segments.get(sym).right;
        }

        return segments;
    }
}
