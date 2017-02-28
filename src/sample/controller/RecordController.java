package sample.controller;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.model.Record;

public class RecordController {

    @FXML
    private TableView<Record> recordTableView;
    @FXML
    private TableColumn<Record, String> listTableColumn;

    public RecordController() {
    }

    @FXML
    private void initialize(){
        //listTableColumn.setCellValueFactory(cellData -> cellData.getValue().getArrayList().get(1));
    }
}
