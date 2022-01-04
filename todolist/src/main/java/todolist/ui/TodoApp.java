package todolist.ui;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TodoApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent parent = FXMLLoader.load(getClass().getResource("Todo.fxml"), ResourceBundle.getBundle("todolist.ui.App"));
    stage.setScene(new Scene(parent));
    stage.show();
  }

  public static void main(String[] args) {
    launch(TodoApp.class, args);
  }
}