package springee.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by JavaEE on 02.12.2017.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchPetException extends RuntimeException{

}
