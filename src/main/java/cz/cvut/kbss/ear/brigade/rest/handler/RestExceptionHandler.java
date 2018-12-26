package cz.cvut.kbss.ear.brigade.rest.handler;

import cz.cvut.kbss.ear.brigade.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handlers for REST controllers.
 * <p>
 * The general pattern should be that unless an exception can be handled in a more appropriate place it bubbles up to a
 * REST controller which originally received the request. There, it is caught by this handler, logged and a reasonable
 * error message is returned to the user.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static void logException(RuntimeException ex) {
        LOG.error("Exception caught:", ex);
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceException(HttpServletRequest request, PersistenceException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e.getCause()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, NotFoundException e) {
        // Not necessary to log NotFoundException, they may be quite frequent and do not represent an issue with the application
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BrigadeIsFullException.class)
    public ResponseEntity<ErrorInfo> brigadeIsFullException(HttpServletRequest request, BrigadeIsFullException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LateSignOffException.class)
    public ResponseEntity<ErrorInfo> lateSignOff(HttpServletRequest request, LateSignOffException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LateSignOnException.class)
    public ResponseEntity<ErrorInfo> lateSignOn(HttpServletRequest request, LateSignOnException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(AlreadyRatedException.class)
    public ResponseEntity<ErrorInfo> alreadyRatedException(HttpServletRequest request, AlreadyRatedException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BrigadeIsNotFinishedException.class)
    public ResponseEntity<ErrorInfo> brigadeIsNotFinished(HttpServletRequest request, BrigadeIsNotFinishedException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BrigadeNotBelongToEmployerException.class)
    public ResponseEntity<ErrorInfo> brigadeNotBelongToEmployer(HttpServletRequest request, BrigadeNotBelongToEmployerException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(DateToIsBeforeDateFromException.class)
    public ResponseEntity<ErrorInfo> dateToIsBeforeDateFrom(HttpServletRequest request, DateToIsBeforeDateFromException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WorkerDidNotWorkOnBrigadeException.class)
    public ResponseEntity<ErrorInfo> workerDidNotWorkOnBrigade(HttpServletRequest request, WorkerDidNotWorkOnBrigadeException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorInfo> validation(HttpServletRequest request, ValidationException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDenied(HttpServletRequest request, AccessDeniedException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.FORBIDDEN);
    }
}
