package component

import javafx.scene.control.TableCell

class DataCell():TableCell<Map<String, String>, String>(){
    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        textProperty().set(item)
    }
}