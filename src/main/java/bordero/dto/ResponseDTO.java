package bordero.dto;

public class ResponseDTO<T extends DTO>  {

    public final T value;

    public final int code;

    private ResponseDTO() {
        this(null, 0);
        }

    public ResponseDTO(T value) {
        this(value, 0);
    }

    public ResponseDTO(int code) {
        this(null, code);
    }

    public ResponseDTO(T value, int msg) {
        this.value = value;
        this.code = msg;
    }

    public boolean isValid() {
        return value != null;
    }

}
