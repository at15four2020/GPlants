package extension;

import gearth.extensions.ThemedExtensionFormCreator;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class GPlantsLauncher extends ThemedExtensionFormCreator {

    @Override
    protected String getTitle() {
        return "G-Plants";
    }

    @Override
    protected URL getFormResource() {
        return getClass().getResource("/ui/g_plants.fxml");
    }

    @Override
    protected void initialize(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/g_plants.png")));

        primaryStage.setTitle("GPlants");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
    }

    public static void main(String[] args) {
        runExtensionForm(args, GPlantsLauncher.class);
    }

}