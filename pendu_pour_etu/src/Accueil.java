import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class Accueil extends VBox {

    private MotMystere modelePendu;
    private Pendu appli;
  /*  private Pendu vuePendu;
    Stage stage= new Stage();
    stage.setTitle("IUTEAM'S - La  plateforme de jeux de l'IUTO");

    public void start(){
    HBox h1 = new HBox();
    Label l1= new Label("Jeu du pendu");
    h1.getChildren().add(l1);

    Button home= new Button();
    ImageView image1=new ImageView("file:img/home.png");
    home.setGraphic(image1);
    home.setOnAction(null);/* 


    Button param= new Button();
    ImageView image2=new ImageView("file:img/parametres.png");
    param.setGraphic(image2);
    param.setOnAction(null);/* 

    Button info = new Button();
    ImageView image3=new ImageView("file:img/info.png");
    info.setGraphic(image3);
    info.setOnAction(null);/* 

    h1.getChildren().addAll(home,param,info);
    }
    
}*/

    public void start(){
    VBox vbox=  new VBox();
    Button b1= new Button("Lancer une partie");
    b1.setOnAction(new ControleurLancerPartie(modelePendu,appli));
    vbox.getChildren().add(b1);
    TitledPane tpane = new TitledPane();
    tpane.setText("niveau de difficulté");
    RadioButton rb= new RadioButton(MotMystere.FACILE,MotMystere.MOYEN,MotMystere.DIFFICILE,MotMystere.EXPERT);
    }




}
