package de.ljw.aachen.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuController {

    @FXML
    void onCashUp(ActionEvent event) {
        log.info("cash up");
    }

    @FXML
    void onCheckBalance(ActionEvent event) {
        log.info("check balance");
    }

    @FXML
    void onImport(ActionEvent event) {
        log.info("import accounts");
    }

}
