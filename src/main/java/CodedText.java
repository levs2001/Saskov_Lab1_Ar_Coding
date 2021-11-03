import java.util.HashMap;

public class CodedText {
    public final HashMap<Integer, Double> symsProb;
    public final int textLength;
    public final double codeRes;

    public CodedText(String filename, int bufferSize) {
        FileReaderBuff buffFile = new FileReaderBuff(filename, bufferSize);
        codeRes = buffFile.readDouble();
        textLength = buffFile.readInt();

        symsProb = new HashMap<>();
        int sym = buffFile.readSym();
        while (sym != FileReaderBuff.END_STREAM) {
            double prob = buffFile.readDouble();
            symsProb.put(sym, prob);
            sym = buffFile.readSym();
        }

        buffFile.close();
    }
}

