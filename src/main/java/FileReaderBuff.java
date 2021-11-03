import java.io.*;

public class FileReaderBuff {
    public static final int END_STREAM = -1;
    private final FileInputStream fileStream;
    private final BufferedInputStream fileBuffStream;
    private final DataInputStream dataInputStream;

    FileReaderBuff(String filename, int bufferSize) {
        fileStream = getFileStream(filename);
        fileBuffStream = new BufferedInputStream(fileStream, bufferSize);
        dataInputStream = new DataInputStream(fileBuffStream);
    }

    public int readSym() {
        int sym = END_STREAM;

        try {
            sym = fileBuffStream.read();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_READING_ERR);
        }

        return sym;
    }

    public int readInt() {
        int ans = END_STREAM;
        try {
            ans = dataInputStream.readInt();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_READING_ERR);
        }
        return ans;
    }

    public double readDouble() {
        double ans = END_STREAM;
        try {
            ans = dataInputStream.readDouble();
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.FILE_READING_ERR);
        }
        return ans;
    }

    public void close() {
        try {
            fileBuffStream.close();
            fileStream.close();
            dataInputStream.close();
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