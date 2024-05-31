import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */
    private Button bJouer;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono
     * ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.panelCentral= new BorderPane();
        this.clavier =new Clavier("abcdefghijklmnopqrstuvwxyz", new ControleurLettres(modelePendu,this));
        this.chargerImages("./img");
        // A terminer d'implementer
    }

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre() {

        Stage stage = new Stage();
        stage.setTitle("IUTEAM's - La plateforme de jeux de l'IUTO");

        BorderPane bp = new BorderPane();

        Label l1 = new Label("Jeu du pendu");
        bp.setLeft(l1);

        HBox hb = new HBox();

        Button home = new Button();
        ImageView image1 = new ImageView("file:pendu_pour_etu/img/home.png");
        home.setOnAction(null);
        home.setGraphic(image1);
        image1.setFitHeight(40);
        image1.setFitWidth(40);

        Button param = new Button();
        ImageView image2 = new ImageView("file:pendu_pour_etu/img/parametres.png");
        image2.setFitHeight(40);
        image2.setFitWidth(40);

        param.setOnAction(null);
        param.setGraphic(image2);

        Button info = new Button();
        ImageView image3 = new ImageView("file:pendu_pour_etu/img/info.png");
        info.setOnAction(null);
        info.setGraphic(image3);
        image3.setFitHeight(40);
        image3.setFitWidth(40);

        hb.getChildren().addAll(home, param, info);
        bp.setRight(hb);
        bp.setBackground(new Background(new BackgroundFill(Color.CYAN, null, null)));
        return bp;
    }

    // /**
    // * @return le panel du chronomètre
    // */
    // private TitledPane leChrono(){
    // A implementer
    // TitledPane res = new TitledPane();
    // return res;
    // }

    // /**
    // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
    // * de progression et le clavier
    // */
    // private Pane fenetreJeu(){
    // A implementer
    // Pane res = new Pane();
    // return res;
    // }

    // /**
    // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
    // jeu
    // */
    // private Pane fenetreAccueil(){
    // A implementer
    // Pane res = new Pane();
    // return res;
    // }

    /**        this.panelCentral= new BorderPane();

     * charge les images à afficher en fonction des erreurs
     * 
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire) {
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        VBox vbox = new VBox();
        Button b1 = new Button("Lancer une partie");
        b1.setOnAction(new ControleurLancerPartie(modelePendu,this));
        vbox.getChildren().add(b1);
        
        VBox vb = new VBox();
        RadioButton rd = new RadioButton("FACILE");
        RadioButton rd2 = new RadioButton("MOYEN");
        RadioButton rd3 = new RadioButton("DIFFICILE");
        RadioButton rd4 = new RadioButton("EXPERT");
        vb.getChildren().addAll(rd, rd2, rd3, rd4);

        TitledPane tpane = new TitledPane("niveau de difficulté", vb);
        vbox.getChildren().add(tpane);
        this.panelCentral.setCenter(vbox);
    }

    public void modeJeu() {        
        HBox hbox= new HBox();
        
        this.pg= new ProgressBar(modelePendu.getMotATrouve().length());
        this.dessin= new ImageView("file:pendu_pour_etu/img/pendu0.png");
        Label motcrypt= new Label(modelePendu.getMotCrypte());
        VBox vb1 = new VBox();
        vb1.getChildren().add(motcrypt);
        vb1.getChildren().addAll(this.dessin,this.pg,this.clavier);
        hbox.getChildren().add(vb1);

        VBox vb2 = new VBox();
        this.leNiveau = new Text("niveau : ");
        this.chrono = new Chronometre();
        this.bJouer = new Button("nouveau mot");

        vb2.getChildren().addAll(this.leNiveau,this.chrono,this.bJouer);
        hbox.getChildren().add(vb2);
        this.panelCentral.setCenter(hbox);

    }

    public void modeParametres() {
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie() {
        modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        String lettre="";
        String motmyst="";
        for (String mot : modelePendu.getLettresEssayees()){
            if (modelePendu.getMotATrouve().contains(mot)){
                lettre=mot;
            }
        }
        for (char let:modelePendu.getMotATrouve().toCharArray()){
            if (lettre.equals(lettre)){
                motmyst+=lettre;
            }
            else{motmyst+="*";}
        }
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        // A implémenter
        return null; // A enlever
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessageGagne() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessagePerdu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * 
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * 
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }
}
