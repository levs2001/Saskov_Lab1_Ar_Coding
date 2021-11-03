import java.io.*;
import java.util.Scanner;

public class Config {
    private static final int PARAM_NAME_IND = 0;
    private static final int PARAM_IND = 1;
    private static final String MODE_STRING = "mode";
    private static final String FILE_TO_READ_STRING = "file_to_read";
    private static final String FILE_TO_WRITE_STRING = "file_to_write";
    private static final String BUFFER_SIZE_STRING = "buffer_size";
    private static final String CODING_MODE_STRING = "coding";
    private static final String DECODING_MODE_STRING = "decoding";
    private static final String SPLITTER_STRING = "=";

    enum WorkType {
        CODING,
        DECODING,
        ERROR
    }

    private WorkType wType;
    private String fileToRead;
    private String fileToWrite;
    private int bufferSize;

    public Config(String configFilename) {
        FileReader configF = null;
        try {
            configF = new FileReader(configFilename);
        } catch (FileNotFoundException e) {
            wType = WorkType.ERROR;
            MyException.exception(MyException.ExceptionType_e.FILE_CONFIG_NOT_OPEN);
        }

        if (configF != null) {
            Scanner scanner = new Scanner(configF);
            while (scanner.hasNext()) {
                setParam(scanner.nextLine());
            }
        }
    }

    public WorkType getWorkType() {
        return wType;
    }

    public String getFileToRead() {
        return fileToRead;
    }

    public String getFileToWrite() {
        return fileToWrite;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    private void setParam(String paramStr) {
        String paramSet[] = paramStr.split(SPLITTER_STRING);

        switch (paramSet[PARAM_NAME_IND]) {
            case MODE_STRING:
                setWorkType(paramSet[PARAM_IND]);
                break;
            case FILE_TO_READ_STRING:
                fileToRead = paramSet[PARAM_IND];
            case FILE_TO_WRITE_STRING:
                fileToWrite = paramSet[PARAM_IND];
                break;
            case BUFFER_SIZE_STRING:
                bufferSize = Integer.parseInt(paramSet[PARAM_IND]);
                break;
            default:
                MyException.exception(MyException.ExceptionType_e.CONFIG_UNKNOWN_PARAM);
        }

    }

    private void setWorkType(String workTypeStr) {
        if (workTypeStr.equals(CODING_MODE_STRING)) {
            wType = WorkType.CODING;
        } else if (workTypeStr.equals(DECODING_MODE_STRING)) {
            wType = WorkType.DECODING;
        } else {
            wType = WorkType.ERROR;
            MyException.exception(MyException.ExceptionType_e.CONFIG_NO_WORKTYPE);
        }
    }
}