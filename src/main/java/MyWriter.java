import java.io.*;

public class MyWriter {
    private static final int BITS_IN_BYTE_COUNT = 8;
    private static final int SHIFT = 1;

    private final BufferedOutputStream fileBuffStream;
    private int bufferByte = 0;
    private int bufferByteFreeBits = BITS_IN_BYTE_COUNT;

    MyWriter(String filename, int bufferSize) {
        fileBuffStream = new BufferedOutputStream(getFileStream(filename), bufferSize);
    }

    public void writeBit(int bit) {
        if (bufferByteFreeBits == 0) {
            writeByte(bufferByte);
            clearBuffByte();
        }
        bufferByte = ((bufferByte << SHIFT) | bit);
        bufferByteFreeBits--;
    }

    public void writeByte(int sym) {
        try {
            fileBuffStream.write(sym);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_CODED_FILE);
        }
    }

    public void flush() {
        if(bufferByteFreeBits != BITS_IN_BYTE_COUNT) {
            // Case when some bits are written in bufferByte
            bufferByte <<= bufferByteFreeBits;
            bufferByteFreeBits = 0;
            writeByte(bufferByte);
        }

        try {
            fileBuffStream.flush();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FLUSH_ERROR);
        }
    }

    public void close() {
        try {
            fileBuffStream.close();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_CLOSE_ERR);
        }
    }

    private FileOutputStream getFileStream(String filename) {
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_WRITING_NOT_OPEN);
        }
        return fileStream;
    }

    private void clearBuffByte() {
        bufferByte = 0;
        bufferByteFreeBits = BITS_IN_BYTE_COUNT;
    }
}
