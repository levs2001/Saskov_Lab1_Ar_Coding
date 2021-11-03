import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Decoding {
    private static final double SEGMENT_BEG = 0;

    private class Segment {
        public double left;
        public double right;
        Integer sym;
    }

    private final HashMap<Integer, Segment> segments;
    private final CodedText codedText;
    ArrayList<Integer> result;

    public Decoding(CodedText codedText) {
        this.codedText = codedText;
        segments = defineSegments();
        result = arithmeticDecoding();
    }

    public void writeDecodedFile(String filename) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filename);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_WRITING_NOT_OPEN);
        }

        try {
            for (Integer sym : result)
                fileOutputStream.write(sym);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_DECODED_FILE);
        }
    }

    private ArrayList<Integer> arithmeticDecoding() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        double code = codedText.codeRes;
        for (int i = 0; i < codedText.textLength; i++) {
            for (Integer sym : codedText.symsProb.keySet()) {
                if (code >= segments.get(sym).left && code < segments.get(sym).right) {
                    result.add(sym);
                    code = (code - segments.get(sym).left) / (segments.get(sym).right - segments.get(sym).left);
                    break;
                }
            }
        }
        return result;
    }

    private HashMap<Integer, Segment> defineSegments() {
        HashMap<Integer, Segment> segments = new HashMap<>();
        double left = SEGMENT_BEG;
        for (Integer sym : codedText.symsProb.keySet()) {
            segments.put(sym, new Segment());
            segments.get(sym).left = left;
            segments.get(sym).right = left + codedText.symsProb.get(sym);
            segments.get(sym).sym = sym;
            left = segments.get(sym).right;
        }
        return segments;
    }
}
