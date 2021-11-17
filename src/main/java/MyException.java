public class MyException {
    public enum ExceptionType_e {
        FILE_CONFIG_NOT_OPEN,
        FILE_READING_NOT_OPEN,
        FILE_READING_ERR,
        FILE_WRITING_NOT_OPEN,
        CONFIG_UNKNOWN_PARAM,
        CONFIG_NO_WORKTYPE,
        FILE_CLOSE_ERR,
        CANT_WRITE_IN_CODED_FILE,
        CANT_WRITE_IN_DECODED_FILE,
        FLUSH_ERROR
    }

    static public void exception(ExceptionType_e eType) {
        switch (eType) {
            case FILE_CONFIG_NOT_OPEN -> System.out.println("Error: Can't open file with config!");
            case FILE_READING_NOT_OPEN -> System.out.println("Error: Can't open file for reading!");
            case FILE_WRITING_NOT_OPEN -> System.out.println("Error: Can't open file for writing!");
            case CONFIG_UNKNOWN_PARAM -> System.out.println("Error: Unknown param, check config file!");
            case CONFIG_NO_WORKTYPE -> System.out.println("Error: You didn't fill worktype in config!");
            case FILE_CLOSE_ERR -> System.out.println("Error: closing files error!");
            case CANT_WRITE_IN_CODED_FILE -> System.out.println("Error: Can't write in coded file!");
            case CANT_WRITE_IN_DECODED_FILE -> System.out.println("Error: Can't write in file!");
            case FILE_READING_ERR -> System.out.println("Error: Can't read byte from file");
            case FLUSH_ERROR -> System.out.println("Error: during flushing buffer");
        }
    }
}
