public class Main {
    static final int ARG_NUM_FOR_FILENAME = 0;

    public static void main(String[] args) {
        String configFilename = args[ARG_NUM_FOR_FILENAME];

        Config config = new Config(configFilename);

        if (config.getWorkType() == Config.WorkType.CODING) {
            Coding coding = new Coding(config.getFileToRead(), config.getBufferSize());
            coding.writeCodedFile(config.getFileToWrite());
        } else if (config.getWorkType() == Config.WorkType.DECODING) {
            CodedText codedText = new CodedText(config.getFileToRead(), config.getBufferSize());
            Decoding decoding = new Decoding(codedText);
            decoding.writeDecodedFile(config.getFileToWrite());
        }
    }
}
