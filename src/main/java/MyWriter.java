import java.io.*;

public class MyWriter {
    //TODO: Remove extra functions, make buffered
    private final BufferedOutputStream fileBuffStream;

    MyWriter(String filename, int bufferSize) {
        fileBuffStream = new BufferedOutputStream(getFileStream(filename), bufferSize);
    }

    public void writeByte(int sym) {
        try {
            fileBuffStream.write(sym);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_CODED_FILE);
        }
    }

    public void flush() {
        try {
            fileBuffStream.flush();
        } catch (IOException e) {
            //TODO: Make special exception for this case
            MyException.exception(MyException.ExceptionType_e.FILE_CLOSE_ERR);
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
}
