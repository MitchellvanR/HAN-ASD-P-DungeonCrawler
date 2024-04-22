package asd.project.unittest.dao;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.mockito.stubbing.Answer;

public class ResultSetCreator {

  private final String[] columnNames;
  private final Object[][] data;
  private int rowIndex;

  ResultSetCreator(String[] columnNames, Object[][] data) {
    this.columnNames = columnNames;
    this.data = data;
    this.rowIndex = -1;
  }

  public static ResultSet create(String[] columnNames, Object[][] data) throws SQLException {
    return new ResultSetCreator(columnNames, data).buildMock();
  }

  ResultSet buildMock() throws SQLException {
    ResultSet mockResultSet = mock(ResultSet.class);

    Answer<?> getDataWithString = invocation -> {
      String columnName = invocation.getArgument(0, String.class);
      int columnIndex = Arrays.asList(columnNames).indexOf(columnName);
      return data[rowIndex][columnIndex];
    };

    when(mockResultSet.getString(anyString())).then(getDataWithString);

    when(mockResultSet.getInt(anyString())).then(getDataWithString);

    when(mockResultSet.getFloat(anyString())).then(getDataWithString);

    when(mockResultSet.next()).then(invocation -> ++rowIndex < data.length);

    return mockResultSet;
  }
}
