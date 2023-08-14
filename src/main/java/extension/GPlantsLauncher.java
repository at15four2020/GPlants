package extension;

import gearth.extensions.ExtensionForm;
import gearth.extensions.ExtensionFormCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GPlantsLauncher extends ExtensionFormCreator {

    @Override
    protected ExtensionForm createForm(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GPlants.class.getResource("/ui/g_plants.fxml"));
        Parent root = loader.load();

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/g_plants.png")));

        primaryStage.setTitle("GPlants");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);

        return loader.getController();
    }

    public static void main(String[] args) {
        runExtensionForm(args, GPlantsLauncher.class);
    }

}