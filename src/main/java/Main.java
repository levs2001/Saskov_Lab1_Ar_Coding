public class Main {
    static final int ARG_NUM_FOR_FILENAME = 0;

    public static void main(String[] args) {
        String configFilename = args[ARG_NUM_FOR_FILENAME];

        Config config = new Config(configFilename);

        MyReader reader = new MyReader(config.getFileToRead(), config.getBufferSize());
        MyWriter writer = new MyWriter(config.getFileToWrite(), config.getBufferSize());

        ArithmeticCodingProcessor coding = null;
        if (config.getWorkType() == Config.WorkType.CODING) {
            coding = new ArithmeticCoder(190, 256, writer);
        } else if (config.getWorkType() == Config.WorkType.DECODING) {
            coding = new ArithmeticDecoder(190, 256, writer);
        }

        if (coding != null) {
            byte b = reader.readByte();
            while (b != MyReader.END_STREAM) {
                coding.putByte(b);
                b = reader.readByte();
            }

            coding.finish();
        }

        reader.close();
        writer.close();
    }
}
