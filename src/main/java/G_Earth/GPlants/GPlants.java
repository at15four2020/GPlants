package G_Earth.GPlants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gearth.extensions.ExtensionForm;
import gearth.extensions.ExtensionInfo;
import gearth.protocol.HMessage;
import gearth.protocol.HPacket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@ExtensionInfo(
	Title = "GPlants",
	Description = "Treat plants with one click!",
	Version = "1.0",
	Author = "at15four2020"
)
public class GPlants extends ExtensionForm {
	public CheckBox fieldOnlyMy;
	public Button btnTreat;
	public Label labelSize;
	
	private int userUId;
	private String userName;
	private Map<Integer, String> plants = new HashMap<>();

	public static void main(String[] args) {
		runExtensionForm(args, GPlants.class);
	}

	@Override
	public ExtensionForm launchForm(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(GPlants.class.getResource("/ui/g_plants.fxml"));
		Parent root = loader.load();
		
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/g_plants.png")));

		primaryStage.setTitle("GPlants");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.setAlwaysOnTop(true);

		return loader.getController();
	}
	
	@Override
	protected void initExtension() {
		intercept(HMessage.Side.TOCLIENT, 163, x -> handleEnterRoom(x));
		intercept(HMessage.Side.TOCLIENT, 2625, x -> handleUserData(x));
		intercept(HMessage.Side.TOCLIENT, 3989, x -> handleRoomUsers(x));
		
		sendToServer(new HPacket(3009)); // For user data
	}
	
	private void handleEnterRoom(HMessage message) {
		plants.clear();
	}
	
	private void handleUserData(HMessage message) {
		HPacket packet = message.getPacket();
		userUId = packet.readInteger();
		userName = packet.readString();
	}
	
	private void handleRoomUsers(HMessage message) {
		final HPacket packet = message.getPacket();
		int qtd = packet.readInteger();

		for (int i = 0; i < qtd; i++) {
			int userId = packet.readInteger();
			readUnecessaryFields(packet);
			int userType = packet.readInteger();
			switch (userType) {
            case 1:
                packet.readString();
                packet.readInteger();
                packet.readInteger();
                packet.readString();
                packet.readString();
                packet.readInteger();
                packet.readBoolean();
                break;
            case 2:
    			int userSubtype = packet.readInteger();
				packet.readInteger(); // ownerId
				String ownerName = packet.readString();

    			if (userSubtype == 16) {
    				plants.put(userId, ownerName);
    				Platform.runLater(() -> {
    					btnTreat.setDisable(false);
    					labelSize.setText(plants.size()+" found!");
    				});
    			}
				
                packet.readInteger();
                packet.readBoolean();
                packet.readBoolean();
                packet.readBoolean();
                packet.readBoolean();
                packet.readBoolean();
                packet.readBoolean();
                packet.readInteger();
                packet.readString();
                break;
            case 4:

                packet.readString();
                packet.readInteger();
                packet.readString();
                for (int j = packet.readInteger(); j > 0; j--)
                {
                    packet.readShort();
                }
                break;
					
			}
		}
	}
	
	private void readUnecessaryFields(HPacket packet) {
		packet.readString(); // userName
		packet.readString(); // userDesc
		packet.readString(); // userFigure
		packet.readInteger(); // userIndex
		packet.readInteger(); // userX
		packet.readInteger(); // userY
		packet.readString(); // userZ
		packet.readInteger(); // userIDK
	}
	
	private int counter;
	public void start() {
		btnTreat.setDisable(true);
		Boolean onlyMy = fieldOnlyMy.isSelected();
		
		counter = 0;
		for(Map.Entry<Integer, String> entry : plants.entrySet()) {
		    counter++;
			Platform.runLater(() -> {
				labelSize.setText(counter+"/"+plants.size());
			});
		    int plantId = entry.getKey();
		    String ownerName = entry.getValue();
		    
		    if (!onlyMy || (onlyMy && ownerName == userName)) {
				String packet = "{l}{u:2593}{i:"+plantId+"}";
				sendToServer(new HPacket(packet));
				try {
					TimeUnit.SECONDS.sleep(1);;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		btnTreat.setDisable(false);
	}
	
	public void click_btnTreat(ActionEvent actionEvent) {
		new Thread(this::start).start();
	}
}
