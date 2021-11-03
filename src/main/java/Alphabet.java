import java.util.HashMap;

public class Alphabet {
    static final int MAX_TEXT_SIZE_WO_LOSSES = 13;
    private static final double PLUS_SYMS_PROB = 1d;

    static HashMap<Integer, Double> getSymsProbability(String filename, int bufferSize) {
        HashMap<Integer, Double> symsProb = new HashMap<>();

        FileReaderBuff buffFile = new FileReaderBuff(filename, bufferSize);

        Integer iSym = buffFile.readSym();
        int textLength = 0;
        while (iSym != FileReaderBuff.END_STREAM) {
            textLength++;
            if (symsProb.containsKey(iSym))
                symsProb.put(iSym, symsProb.get(iSym) + PLUS_SYMS_PROB);
            else
                symsProb.put(iSym, PLUS_SYMS_PROB);

            iSym = buffFile.readSym();
        }

        if (textLength > MAX_TEXT_SIZE_WO_LOSSES) {
            MyException.exception(MyException.ExceptionType_e.BIG_FILE);
        }

        for (Integer sym : symsProb.keySet()) {
            symsProb.put(sym, symsProb.get(sym) / textLength);
        }

        buffFile.close();
        return symsProb;
    }
}
