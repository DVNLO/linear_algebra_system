/**
 * The following is a controller for a JavaFX UI. The controller implements
 * functionality for an FXML specified UI containing a MenuBar and TableView.
 * The MenuBar provides the user options for interfacing with the TableView.
 * The MenuBar contains items File and Solve. The File menu has file items
 * New, Open, Save, and Exit, which provided expected functionality for
 * managing the data contained in the TableView. Data contained within the
 * TableView is contained stored in a multi-dimensional array (String[][]).
 * All file operations are stored and represented as Strings. When a user
 * wishes to solve a matrix, the String[][] data is converted into a Matrix<BigDecimal>
 * individual Strings are converted in accordance with Scale and Precision
 * information provided by the user. Rounding is hard-coded, and defaults
 * to HALF_UP. The results of the solved matrix, and refreshed into the
 * display. All operations are performed in memory, without effecting
 * objects stored on the file system.
 * Author: Dan Vyenielo : dvyenielo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra.Matrix;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Pair;

import static edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra.Solver.solve;
import static edu.srjc.vyenielo.dan.linear.algebra.system.Serialize.Serialization.deserialize;
import static edu.srjc.vyenielo.dan.linear.algebra.system.Serialize.Serialization.serialize;

public class Controller implements Initializable
{

    @FXML MenuBar menuBar;
    @FXML TableView<String[]> tableView;
    @FXML Menu menuFile = new Menu("File");
    @FXML Menu menuSolve = new Menu("Solve");
    @FXML MenuItem menuFileNew = new MenuItem("New");
    @FXML MenuItem menuFileOpen = new MenuItem("Open");
    @FXML MenuItem menuFileSave = new MenuItem("Save");
    @FXML MenuItem menuFileExit = new MenuItem("Exit");
    @FXML MenuItem menuSolveRun = new MenuItem("Run");
    String[][] data;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        buildMenuBar();
        setNewAction();
        setOpenAction();
        setSaveAction();
        setExitAction();
        setRunAction();
    }

    public void buildMenuBar()
    {
        menuBar.getMenus().addAll(menuFile, menuSolve);
        menuFile.getItems().addAll(menuFileNew, menuFileOpen, menuFileSave, menuFileExit);
        menuSolve.getItems().addAll(menuSolveRun);
    }

    public void setNewAction()
    {
        menuFileNew.setOnAction((ActionEvent t) -> newMatrix(t));
    }

    public void setOpenAction()
    {
        menuFileOpen.setOnAction((ActionEvent t) -> openMatrix(t));
    }

    public void setSaveAction()
    {
        menuFileSave.setOnAction((ActionEvent t) -> saveMatrix(t));
    }

    public void setExitAction()
    {
        menuFileExit.setOnAction((ActionEvent t) -> System.exit(0));
    }

    public void setRunAction()
    {
        menuSolveRun.setOnAction((ActionEvent t) -> solveMatrix(t));
    }

    public void solveMatrix(ActionEvent A)
    {
        if (data != null)
        {
            try
            {
                Pair<Integer, Integer> result = matrixSolutionPrompt();
                MathContext standards = new MathContext(result.getValue(), RoundingMode.HALF_UP);
                Matrix<BigDecimal> solutionMatrix = new Matrix<>(data.length, data[0].length);
                for (int i = 0; i < solutionMatrix.getRowCount(); i++)
                {
                    for(int j = 0; j < solutionMatrix.getColumnCount(); j++)
                    {
                        BigDecimal temp = new BigDecimal(Double.parseDouble(data[i][j]), standards);
                        temp.setScale(result.getKey());
                        solutionMatrix.setElement(i, j, temp);
                    }
                }
                solve(solutionMatrix, standards);
                for (int i = 0; i < solutionMatrix.getRowCount(); i++)
                {
                    for(int j = 0; j < solutionMatrix.getColumnCount(); j++)
                    {
                        data[i][j] = Double.toString(solutionMatrix.getElement(i,j).doubleValue());
                    }
                }
                tableView.refresh();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for scale and precision information
     * @return Pair<scale, precision> as key and value, respectively.
     */
    public Pair<Integer, Integer> matrixSolutionPrompt()
    {
        // Create the custom dialog.
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Solution Properties");
        dialog.setHeaderText("Please provide scale and precision properties.");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Set the button types.
        ButtonType okayButtonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okayButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField scale = new TextField();
        scale.setPromptText("Scale");
        TextField precision = new TextField();
        precision.setPromptText("Precision");

        grid.add(new Label("Scale:"), 0, 0);
        grid.add(scale, 1, 0);
        grid.add(new Label("Precision:"), 0, 1);
        grid.add(precision, 1, 1);

        // Enable/Disable okay button depending on presence of rows and columns.
        Node okayButton = dialog.getDialogPane().lookupButton(okayButtonType);
        okayButton.setDisable(true);

        scale.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
            {
                scale.setText(newValue.replaceAll("[^\\d]", ""));
            }
            okayButton.setDisable(scale.getText().isEmpty() || precision.getText().isEmpty());
        });

        precision.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
            {
                precision.setText(newValue.replaceAll("[^\\d]", ""));
            }
            okayButton.setDisable(scale.getText().isEmpty() || precision.getText().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> scale.requestFocus());

        // Convert the result to a rows-columns-pair when the okay button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okayButtonType)
            {
                return new Pair<>(Integer.parseInt(scale.getText()), Integer.parseInt(precision.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Generates a new matrix and displays it in the tableview.
     * @param A
     */
    public void newMatrix(ActionEvent A)
    {
        try
        {
            Pair<Integer, Integer> rowsColumns = newMatrixPrompt();
            initializeTable(rowsColumns);
        }
        catch(Exception e)
        {
            System.err.println("newMatrix() failed.");
        }

    }

    /**
     * Initializes the tableView's columns, and provides cellFactories
     * for viewing and editing the underlying data.
     * @param rowsColumns
     */
    public void initializeTable(Pair<Integer,Integer> rowsColumns)
    {
        tableView.setEditable(true);
        tableView.getColumns().clear();
        tableView.getItems().clear();

        data = new String[rowsColumns.getKey()][rowsColumns.getValue()];

        initializeStringMatrix(rowsColumns.getKey(), rowsColumns.getValue());

        for (int i = 0; i < rowsColumns.getValue(); i++)
        {
            final int k = i;
            TableColumn<String[], String> column = new TableColumn(Integer.toString(i));
            column.setCellValueFactory( cellDataFeatures ->
                {
                    String[] x = cellDataFeatures.getValue();
                    return new SimpleStringProperty(x[k]);
                });
            column.setEditable(true);
            column.setPrefWidth(50);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    cellEditEvent ->
                    {
                        (cellEditEvent.getTableView().getItems().get(cellEditEvent.getTablePosition().getRow()))
                                [cellEditEvent.getTablePosition().getColumn()] = cellEditEvent.getNewValue();
                    }
            );
            tableView.getColumns().add(column);
        }
        tableView.getItems().addAll(data);
    }

    /**
     * Initializes table view cells to 0.
     * @param rows
     * @param columns
     */
    public void initializeStringMatrix(int rows, int columns)
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                data[i][j] = "0";
            }
        }
    }

    /**
     * Prompts the user for row and column information via a dialogBox.
     * The source of this code originated here:
     * http://code.makery.ch/blog/javafx-dialogs-official/
     * I performed minimal modification to fit my purposes.
     * @return
     */
    public Pair<Integer, Integer> newMatrixPrompt()
    {
        // Create the custom dialog.
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("New Matrix");
        dialog.setHeaderText("Please specify row and column dimensions.");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Set the button types.
        ButtonType okayButtonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okayButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField rows = new TextField();
        rows.setPromptText("Rows");
        TextField columns = new TextField();
        columns.setPromptText("Columns");

        grid.add(new Label("Rows:"), 0, 0);
        grid.add(rows, 1, 0);
        grid.add(new Label("Columns:"), 0, 1);
        grid.add(columns, 1, 1);

        // Enable/Disable okay button depending on presence of rows and columns.
        Node okayButton = dialog.getDialogPane().lookupButton(okayButtonType);
        okayButton.setDisable(true);

        rows.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                rows.setText(newValue.replaceAll("[^\\d]", ""));
            }
            okayButton.setDisable(rows.getText().isEmpty() || columns.getText().isEmpty());
        });

        columns.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                columns.setText(newValue.replaceAll("[^\\d]", ""));
            }
            okayButton.setDisable(rows.getText().isEmpty() || columns.getText().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> rows.requestFocus());

        // Convert the result to a rows-columns-pair when the okay button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okayButtonType) {
                return new Pair<>(Integer.parseInt(rows.getText()), Integer.parseInt(columns.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Opens a matrix fromm the file system through FileChooser.
     * @param A
     */
    public void openMatrix(ActionEvent A)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Matrix");
        Matrix<String> fileData;
        File file = fileChooser.showOpenDialog(tableView.getScene().getWindow());
        if (file != null)
        {
            try
            {
                fileData = deserialize(file.toPath());
                Pair<Integer, Integer> rowsColumns = new Pair<>(fileData.getRowCount(), fileData.getColumnCount());
                initializeTable(rowsColumns);
                for (int i = 0; i < fileData.getRowCount(); i++)
                {
                    for (int j = 0; j < fileData.getColumnCount(); j++)
                    {
                        data[i][j] = fileData.getElement(i, j);
                    }
                }
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Saves a matrix to the file system via FileChooser.
     * @param A
     */
    public void saveMatrix(ActionEvent A)
    {
        if (data != null)
        {
            Matrix<String> fileData = new Matrix<>(data.length, data[0].length);
            for (int i = 0; i < fileData.getRowCount(); i++)
            {
                for (int j = 0; j < fileData.getColumnCount(); j++)
                {
                    fileData.setElement(i, j, data[i][j]);
                }
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Matrix");
            File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
            if (file != null)
            {
                try
                {
                    serialize(fileData, file.toPath());
                } catch (IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
