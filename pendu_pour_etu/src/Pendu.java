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
        this.chargerImages("img/");
        this.panelCentral= new BorderPane();
        this.chrono= new Chronometre();
        this.clavier =new Clavier("abcdefghijklmnopqrstuvwxyz", new ControleurLettres(modelePendu,this));
        this.leNiveau= new Text();   
        this.niveaux= new ArrayList<>();
        this.niveaux.add("Facile");
        this.niveaux.add("Moyen");
        this.niveaux.add("Difficile");
        this.niveaux.add("Expert");
        this.niveaux= Arrays.asList("facile","moyen","diff","exp");
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
        ImageView image1 = new ImageView("file:img/home.png");
        home.setOnAction(new RetourAccueil(modelePendu,this));
        home.setGraphic(image1);
        image1.setFitHeight(40);
        image1.setFitWidth(40);

        Button param = new Button();
        ImageView image2 = new ImageView("file:img/parametres.png");
        image2.setFitHeight(40);
        image2.setFitWidth(40);

        
        param.setGraphic(image2);

        Button info = new Button();
        ImageView image3 = new ImageView("file:img/info.png");
        info.setOnAction(new ControleurInfos(this));
        info.setGraphic(image3);
        image3.setFitHeight(40);
        image3.setFitWidth(40);

        hb.getChildren().addAll(home, param, info);
        bp.setRight(hb);
        bp.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        return bp;
    }

    // /**
    // * @return le panel du chronomètre
    // */
    private TitledPane leChrono(){

        TitledPane res = new TitledPane();
        res.setText("Chronomètre");
        res.setCollapsible(false);
        res.setContent(this.chrono);
        return res;
    }

    // /**
    // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
    // * de progression et le clavier
    // */
    private Pane fenetreJeu(){
        
        BorderPane res = new BorderPane();

        VBox vb1 = new VBox();
        this.motCrypte= new Text(this.modelePendu.getMotCrypte());
        vb1.getChildren().add(this.motCrypte);
        this.dessin= new ImageView(this.lesImages.get(0));
        vb1.getChildren().add(this.dessin);
        this.pg= new ProgressBar();
        this.pg.setProgress(0);
        vb1.getChildren().add(this.pg);
        this.clavier= new Clavier("abcdefghijklmnopqrstuvwxyz", new ControleurLettres(this.modelePendu,this));
        vb1.getChildren().add(this.clavier);


        VBox vb2 = new VBox();
        //this.leNiveau= new Text("Niveau "+this.niveaux.get(this.modelePendu.getNiveau()));
        vb2.getChildren().add(this.leNiveau);
        vb2.getChildren().add(this.leChrono());
        Button btnMot= new Button("nouveau mot");
        btnMot.setOnAction(new ControleurLancerPartie(modelePendu,this));
        vb2.getChildren().add(btnMot);

        res.setLeft(vb1);
        res.setRight(vb2);
        return res;
    }

    /**
    * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
    jeu
    */
    private Pane fenetreAccueil(){
        VBox vbox = new VBox();
        Button b1 = new Button("Lancer une partie");
        b1.setOnAction(new ControleurLancerPartie(this.modelePendu,this));
        vbox.getChildren().add(b1);
        
        VBox vb = new VBox();
        RadioButton rd = new RadioButton(niveaux.get(0));
        rd.setOnAction(new ControleurNiveau(modelePendu,this));
        RadioButton rd2 = new RadioButton(niveaux.get(1));
        rd2.setOnAction(new ControleurNiveau(modelePendu,this));
        RadioButton rd3 = new RadioButton(niveaux.get(2));
        rd3.setOnAction(new ControleurNiveau(modelePendu,this));
        RadioButton rd4 = new RadioButton(niveaux.get(3));
        rd4.setOnAction(new ControleurNiveau(modelePendu,this));
        vb.getChildren().addAll(rd, rd2, rd3, rd4);

        TitledPane tpane = new TitledPane("niveau de difficulté", vb);
        vbox.getChildren().add(tpane);

        return vbox;
    }

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
        this.panelCentral.setCenter(this.fenetreAccueil());
    }

    public void modeJeu() {        
        this.panelCentral.setCenter(this.fenetreJeu());
    }

    public void modeParametres() {
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie() {
        this.modeJeu();
        this.modelePendu.setMotATrouver();
        this.modelePendu.defClavier(this.clavier);
        this.chrono.resetTime();
        this.chrono.start();
        this.majAffichage();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants()));
        double avancé = Double.valueOf(this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants())/Double.valueOf(this.modelePendu.getNbErreursMax());
        this.pg.setProgress(avancé);
        if (this.modelePendu.gagne()){
            this.popUpMessageGagne().showAndWait();
        }
        else if (this.modelePendu.gagne()){ 
            this.popUpMessagePerdu().showAndWait();
        }
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        return chrono;
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Règle du pendu");
        alert.setContentText("trouvez le mot caché en choisissant des lettres, a chaque erreurs un bonhomme se formera et si vous perdez il sera pendu");
        return alert;
    }

    public Alert popUpMessageGagne() {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vous avez gagné !!!");
        this.chrono.stop();
        alert.setContentText("Félicitation");
        return alert;
    }

    public Alert popUpMessagePerdu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Perdu");
        this.chrono.stop();
        alert.setContentText(" Peut mieux faire");
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
