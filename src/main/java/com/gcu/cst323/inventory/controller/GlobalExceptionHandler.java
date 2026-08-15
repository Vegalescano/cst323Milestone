package com.gcu.cst323.inventory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
/**
 * Global exception handler for the application.
 * Catches unexpected application errors and displays a user friendly error page.
 */
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    /**
     * Handles uncaught exceptions from controllers and returns the error page.
     *
     * @param exception exception thrown by the application
     * @param model Spring MVC model used to pass the error message to the view
     * @return error page template
     */
    public String handleException(Exception exception, Model model) {
        logger.error("Application error", exception);
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}