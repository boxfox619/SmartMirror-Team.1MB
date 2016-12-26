package application;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.boxfox.controller.BrowserController;
import com.boxfox.controller.MainFormController;
import com.boxfox.controller.MemoController;
import com.boxfox.dto.CommandJob;
import com.boxfox.dto.YoutubeMusic;
import com.boxfox.reconizer.STTListener;
import com.boxfox.reconizer.VoiceReconizer;
import com.boxfox.sound.MusicManager;
import com.boxfox.sound.NaverAPITTS;
import com.onemb.entertaining.WordGame;
import com.onemb.planb.manager.BaseManager;
import com.onemb.planb.manager.DateManager;
import com.onemb.planb.manager.MemoManager;

import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class MainApplication extends Application {
	private static MainReconizer mainReconizer;
	private static STTListener wordGameReconizer;
	private static VoiceReconizer voiceReconizer;

	private MainFormController mainController;
	private DateManager dateManager;
	private static MemoManager memoManager;
	private static MusicManager musicManager;

	@Override
	public void start(Stage primaryStage) {
		musicManager = new MusicManager();
		dateManager = new DateManager();
		memoManager = new MemoManager();
		dateManager.run();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boxfox/fxml/MainForm.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth());
			primaryStage.setHeight(bounds.getHeight());
			primaryStage.initStyle(StageStyle.UNDECORATED);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest((Event) -> {
				System.exit(0);
			});
			mainController = loader.getController();
			dateManager.registerController(mainController);
			musicManager.registerController(mainController);
			views();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void views() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boxfox/fxml/MemoForm.fxml"));
		Stage stage = new Stage();
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		stage.setX(bounds.getMaxX() - 300);
		stage.setY(bounds.getMaxY() - 300);
		stage.setWidth(200);
		stage.setHeight(200);
		Pane myPane = loader.load();
		stage.initStyle(StageStyle.UNDECORATED);
		Scene scene = new Scene(myPane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		memoManager.registerController((MemoController) loader.getController());
		memoManager.addMemo("test");
		musicManager.getYoutube("����");
	}

	public static void main(String[] args) {
		mainReconizer = new MainReconizer(){
			@Override
			public void OnVoiceReconized(String text) {
				System.out.println(text);
				if(text.contains("���")){
					String str = text.split("���")[0];
					musicManager.getYoutube(str);
				}else if(text.contains("Ʋ��")){
					String str = text.split("Ʋ��")[0];
					musicManager.getYoutube(str);
				}else if(text.contains("�뷡 ��")||text.contains("����")){
					musicManager.stopMusic();
				}else if(text.contains("�޸� �߰�")||text.contains("����")){
					if(text.contains("���")){
						System.out.println(text.split("���")[0]);
						memoManager.addMemo(text.split("���")[0]);
					}else{
						NaverAPITTS.play("����� �߰��ұ��?");
						
					}
				}else if(text.contains("�����ձ�")){
					voiceReconizer.setSTTListener(wordGameReconizer);
				}
			}
		};
		wordGameReconizer = new WordGame();
		voiceReconizer = new VoiceReconizer("APIKEY");
		voiceReconizer.setSTTListener(mainReconizer);
		voiceReconizer.start();
		launch(args);
	}

	public static void drop(STTListener STTListener) {
		voiceReconizer.setSTTListener(mainReconizer);
	}

	public static void response(Object obj) {
		if (obj instanceof YoutubeMusic) {
			YoutubeMusic yMusic = (YoutubeMusic) obj;
			NaverAPITTS.play("ã���ô� ������ " + yMusic.getTitle() + "�� �����Ű���?");
			mainReconizer.setJob(new CommandJob(){
				@Override
				public void work() {
					musicManager.playMusic(yMusic);
				}
				@Override
				public void cancel() {
					NaverAPITTS.play("��������� ��ҵǾ����ϴ�.");
				}
			});
		}else{
			
		}

	}
}
