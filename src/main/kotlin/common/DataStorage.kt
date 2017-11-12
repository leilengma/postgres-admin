package common

import javafx.collections.FXCollections
import java.sql.Connection

object DataStorage {
    var tables = FXCollections.observableArrayList<String>(ArrayList<String>())
}



