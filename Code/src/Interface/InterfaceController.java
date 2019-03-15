package Interface;
import Data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InterfaceController {

    private ObservableList<PhysicalRunway> runways = FXCollections.observableArrayList();
    private ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    @FXML
    private Button plusButton;
    @FXML
    private Button addObsButton;

    @FXML
    public ComboBox<PhysicalRunway> runwaySelection = new ComboBox();
    @FXML
    public ComboBox<Obstacle> obstacleSelection = new ComboBox();

    private void loadRunways(){
        runways.removeAll();
        createRunwaysList();
    }

    private void loadObstacles(){
        obstacles.removeAll();
        loadObstaclesList();
    }


    public void handlePlusButtonAction(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddObstacle.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add obstacle");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println("Cant load new window");
        }
    }

    public void handleAddObsButtonAction(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("AddObstacleOnRunway.fxml")));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add obstacle");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println("Cant load new window");
        }
    }

    @FXML
    public void initialize(){
        runwaySelection.getItems().removeAll(runwaySelection.getItems());
        loadRunways();
        runwaySelection.getItems().addAll(runways);

        obstacleSelection.getItems().removeAll(obstacleSelection.getItems());
        loadObstacles();
        obstacleSelection.getItems().addAll(obstacles);

        plusButton.setOnAction(this::handlePlusButtonAction);
        addObsButton.setOnAction(this::handleAddObsButtonAction);

        PhysicalRunway runway = runways.get(0);
        String[] runwayNames = runway.getName().split("/");
        runwayLabel1.setText(runwayNames[0]);
        runwayLabel2.setText(runwayNames[1]);

        runwaySelected();
    }

    public void createRunwaysList(){
        File file = new File("runways.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try{
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        NodeList physicalRunways = document.getElementsByTagName("PhysicalRunway");

        for(int i = 0; i < physicalRunways.getLength(); i++)
        {
            Element physicalRunway = (Element)physicalRunways.item(i);
            NodeList logicalRunways = physicalRunway.getElementsByTagName("LogicalRunway");
            Element logicalRunway = (Element) logicalRunways.item(0);
            String ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
            int LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
            int TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
            int TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
            int ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
            int displacedThreshold=  Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
            Runway runway1 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

            logicalRunway = (Element) logicalRunways.item(1);

            ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
            LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
            TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
            TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
            ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
            displacedThreshold=  Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
            Runway runway2 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

            PhysicalRunway runway = new PhysicalRunway(runway1, runway2);
            runways.add(runway);

        }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    private void loadObstaclesList(){
        File file = new File("obstacles.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList obstaclesList = document.getElementsByTagName("Obstacle");

            for(int i = 0; i<obstaclesList.getLength(); i ++){
                Element obstacle = (Element)obstaclesList.item(i);
                String name = obstacle.getElementsByTagName("Name").item(0).getTextContent();
                int height = Integer.parseInt(obstacle.getElementsByTagName("Height").item(0).getTextContent());
                Obstacle obstacle1 = new Obstacle(name,height,0,0,0);
                obstacles.add(obstacle1);

            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public void runwaySelected(ActionEvent actionEvent) {
        PhysicalRunway runway = runwaySelection.getValue();
        String[] runwayNames = runway.getName().split("/");
        runwayLabel1.setText(runwayNames[0]);
        runwayLabel2.setText(runwayNames[1]);
        runwayG.setScaleX(runway.getWidth()/3800.0);
    }

    @FXML
    public Text runwayLabel1;
    @FXML
    public Text runwayLabel2;
    @FXML
    public Group runwayG;
    @FXML
    public Slider rotationSlider;

    public void rotate() {
        runwayGroup.setRotate(rotationSlider.getValue());
    }

    @FXML
    public Group runwayGroup;
    @FXML
    public Slider zoomSlider;

    public void zoom() {
        runwayGroup.setScaleX(zoomSlider.getValue()/50);
        runwayGroup.setScaleY(zoomSlider.getValue()/50);
    }

    private double x,y;

    public void move(MouseEvent mouseEvent) {
        runwayGroup.setTranslateX(mouseEvent.getX()-x);
        runwayGroup.setTranslateY(mouseEvent.getY()-y);
    }

    public void getPos(MouseEvent mouseEvent) {
        x = mouseEvent.getX()-runwayGroup.getTranslateX();
        y = mouseEvent.getY()-runwayGroup.getTranslateY();
    }

    public void scroll(ScrollEvent scrollEvent) {
        zoomSlider.setValue(zoomSlider.getValue()+(scrollEvent.getDeltaY()/10));
        zoom();
    }
}
