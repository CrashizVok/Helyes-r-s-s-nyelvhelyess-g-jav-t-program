package com.example.javitoprogram;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import java.util.concurrent.CompletionException;

public class HelloController {
    private final HttpClientService httpClientService = new HttpClientService(System.getenv("API_KEY"));


    @FXML
    private TextArea inputTextArea;


    @FXML
    private Label statusLabel;


    @FXML
    private Button correctButton;


    @FXML
    private MenuItem correctErrorsMenuItem;

    @FXML
    private void initialize() {
        System.out.println("API_KEY: " + (System.getenv("API_KEY") != null));

        if (!httpClientService.hasApiKey()){
            statusLabel.setText("No api key ejnye");
            setActionControlsDisabled(true);
        }
        else {
            statusLabel.setText("mehet");
        }
    }

    @FXML
    protected void onCorrectRequested() {
        String text = inputTextArea.getText();
        if (text.isBlank()){
            return;
        }
        setActionControlsDisabled(true);
        statusLabel.setText("Egy pillanat");

        httpClientService.correctTextAsync(text).thenAccept(corrected -> Platform.runLater(() ->{
            inputTextArea.setText(corrected);
            statusLabel.setText("------");
            setActionControlsDisabled(false);
        }))
            .exceptionally(throwable -> {
                Platform.runLater(() ->{
                    statusLabel.setText(formatErrorMessage(throwable));
                    setActionControlsDisabled(false);
                });
                return null;
            });

    }

    private void setActionControlsDisabled(boolean disabled) {
        correctButton.setDisable(disabled);
        correctErrorsMenuItem.setDisable(disabled);
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