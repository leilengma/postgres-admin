package common

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import java.sql.Connection

object DataStorage {
    var tables = FXCollections.observableArrayList<String>(ArrayList<String>())
    var customQueryData = FXCollections.observableArrayList<Map<String, String>>(
            ArrayList<HashMap<String, String>>()
    )
    var customQueryResult = SimpleBooleanProperty()
}



