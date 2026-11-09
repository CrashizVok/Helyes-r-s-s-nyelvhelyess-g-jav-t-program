package com.example.javitoprogram;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import java.util.concurrent.CompletionException;

public class HelloController {
    private final HttpClientService httpClientService = new HttpClientService(System.getenv("GROQ_API_KEY"));


    private TextArea inputTextArea;


    private Label statusLabel;


    private Button correctButton;


    private MenuItem correctErrorsMenuItem;

    @FXML
    private void initialize() {

    }

    @FXML
    protected void onCorrectRequested() {

    }

    private void setActionControlsDisabled(boolean disabled) {

    }

    private String formatErrorMessage(Throwable throwable) {
        Throwable cause = throwable instanceof CompletionException && throwable.getCause() != null
                ? throwable.getCause()
                : throwable;
        String message = cause.getMessage();
        if (message == null || message.isBlank()) {
            return "Correction failed due to an unexpected error.";
        }
        return "Correction failed: " + message;
    }
}