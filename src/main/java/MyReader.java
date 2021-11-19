import java.io.*;

public class MyReader {
    public static final int END_STREAM = -1;

    private final BufferedInputStream fileBuffStream;

    MyReader(String filename, int bufferSize) {
        fileBuffStream = new BufferedInputStream(getFileStream(filename), bufferSize);
    }

    public int readByte() {
        int dByte = END_STREAM;

        try {
            dByte = fileBuffStream.read();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_READING_ERR);
        }

        return dByte;
    }

    public void close() {
        try {
            fileBuffStream.close();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_CLOSE_ERR);
        }

    }

    private FileInputStream getFileStream(String filename) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_READING_NOT_OPEN);
        }
        return fileStream;
    }
}