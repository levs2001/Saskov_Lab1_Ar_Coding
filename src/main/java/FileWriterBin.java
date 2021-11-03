import java.io.*;

public class FileWriterBin {
    //TODO: Remove extra functions, make buffered
    private final FileOutputStream fOut;
    private final DataOutputStream dOut;

    FileWriterBin(String filename) {
        fOut = getFileStream(filename);
        dOut = new DataOutputStream(fOut);
    }

    public void writeDouble(double val) {
        try {
            dOut.writeDouble(val);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_CODED_FILE);
        }
    }

    public void writeInt(int val) {
        try {
            dOut.writeInt(val);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_CODED_FILE);
        }
    }

    public void writeByte(int sym) {
        try {
            dOut.write(sym);
        } catch (IOException e) {
            MyException.exception(MyException.ExceptionType_e.CANT_WRITE_IN_CODED_FILE);
        }
    }

    public void close() {
        try {
            dOut.close();
            fOut.close();
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
