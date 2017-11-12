package common

import java.sql.ResultSet

fun getAllTables() {
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs: ResultSet = stat.executeQuery("select table_name from information_schema.tables where table_schema = 'public';")
        DataStorage.tables.clear()
        while (rs.next()) {
            DataStorage.tables.add(rs.getString("table_name"))
        }
        stat.close()
    }

}

fun getCollumsName(tableName:String):List<String>
{
    val columns = ArrayList<String>()
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs = stat.executeQuery("select column_name from information_schema.columns where\n" +
                "table_name='$tableName';")

        while (rs.next()){
            columns.add(rs.getString("column_name"))
        }
    }
    return columns
}

private fun extractQueryResult(rs:ResultSet, attrs:List<String>):List<Map<String,String>>
{
    val rows = ArrayList<HashMap<String, String>>()
    while(rs.next()){
        val row = HashMap<String, String>()
        for(column in attrs){
            row.put(column, rs.getString(column))
        }
        rows.add(row)
    }
    return rows
}

fun getAllDataFromTable(tableName: String, columns: List<String>):List<Map<String,String>>
{
    val query = "select * from $tableName;"
    return executeQueryInDataTable(query, columns)
}

fun executeQueryInDataTable(query: String, columns: List<String>):List<Map<String, String>>
{
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs = stat.executeQuery(query)
        return extractQueryResult(rs, columns)
    }
    return ArrayList<HashMap<String, String>>()
}

fun deleteEntityById(tableName: String, id:String){
    val query = "delete from $tableName where id = $id"
    val stat = connection?.createStatement()
    stat?.execute(query)
}