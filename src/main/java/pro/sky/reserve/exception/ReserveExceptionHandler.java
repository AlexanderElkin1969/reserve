package pro.sky.reserve.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

public class ReserveExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReserveExceptionHandler.class);

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<?> handlerNotFoundItem(NoSuchElementException e){
        logger.error("Element by this Id is absent.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Element by this Id is absent. " + e.getMessage());
    }

}
