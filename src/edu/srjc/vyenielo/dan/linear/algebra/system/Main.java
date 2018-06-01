/**
 * Application entry point.
 * Dan Vyenielo : dvyenielo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Simple Matrix Solutions");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
