package springee.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by JavaEE on 02.12.2017.
 */

@Data
@AllArgsConstructor
public class ErrorBody {
    private final Integer code = 400;
    private String message;
}
