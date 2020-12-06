package com.earthmooncomet.application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Main extends Application {
	//Variables
	private static final int WIDTH=700;
	private static final int HEIGHT=500;
	private double anchorX, anchorY;
	//Keep track of new value for angle x and angle y
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	private final Sphere earth = new Sphere(50);
	private final Sphere asteroid1 = new Sphere(8);
	private final Sphere asteroid2 = new Sphere(8);
	private final Sphere asteroid3 = new Sphere(8);
	
	class SmartGroup extends Group {		  
	    Rotate r;
	    Transform t = new Rotate();
	 
	    void rotateByX(int ang) {
	      r = new Rotate(ang, Rotate.X_AXIS);
	      t = t.createConcatenation(r);
	      this.getTransforms().clear();
	      this.getTransforms().addAll(t);
	    }
	 
	    void rotateByY(int ang) {
	      r = new Rotate(ang, Rotate.Y_AXIS);
	      t = t.createConcatenation(r);
	      this.getTransforms().clear();
	      this.getTransforms().addAll(t);
	    }
  }

	private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
		   Rotate xRotate;
		   Rotate yRotate;
		   group.getTransforms().addAll(
		       xRotate = new Rotate(0, Rotate.X_AXIS),
		       yRotate = new Rotate(0, Rotate.Y_AXIS)
		   );
		   xRotate.angleProperty().bind(angleX);
		   yRotate.angleProperty().bind(angleY);
		 
		   scene.setOnMousePressed(event -> {
		     anchorX = event.getSceneX();
		     anchorY = event.getSceneY();
		     anchorAngleX = angleX.get();
		     anchorAngleY = angleY.get();
		   });
		 
		   scene.setOnMouseDragged(event -> {
		     angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
		     angleY.set(anchorAngleY + anchorX - event.getSceneX());
		   });
		   stage.addEventHandler(ScrollEvent.SCROLL, event -> {
			   double delta = event.getDeltaY();
			   group.translateZProperty().set(group.getTranslateZ()+delta);
		   });
		 }

	
	@Override
	public void start(Stage stage) throws Exception {
		//Setting up camera from a fixed point
		Camera camera = new PerspectiveCamera(true);
	    camera.setNearClip(1);
	    camera.setFarClip(10000);
		camera.translateZProperty().set(-300);
		
		//set up the group world
		SmartGroup world = new SmartGroup();
		world.getChildren().add(createEarth());
		world.getChildren().addAll(prepareLightSource());
		world.getChildren().add(new AmbientLight());
		
	//	asteroid1.translateXProperty().set(-120);
		asteroid1.translateXProperty().set(-50);
		asteroid1.translateYProperty().set(-150);
		asteroid1.translateZProperty().set(300);
		
		PhongMaterial asteroidMaterial1 = new PhongMaterial();
		asteroidMaterial1.setDiffuseMap(new Image(getClass().getResourceAsStream("/Resources/asteroid-s.gif")));
		 
		 
		asteroid1.setMaterial(asteroidMaterial1);
		asteroid2.setMaterial(asteroidMaterial1);
		asteroid3.setMaterial(asteroidMaterial1);
		
		asteroid2.translateXProperty().set(-80);
		asteroid2.translateYProperty().set(-180);
		asteroid2.translateZProperty().set(400);
		
		asteroid3.translateXProperty().set(-100);
		asteroid3.translateYProperty().set(-180);
		asteroid3.translateZProperty().set(350);
		
		/*
		 * asteroid2.translateXProperty().bind(asteroid1.translateXProperty());
		 * asteroid2.translateYProperty().bind(asteroid1.translateYProperty());
		 * asteroid3.translateXProperty().bind(asteroid1.translateXProperty());
		 * asteroid3.translateYProperty().bind(asteroid1.translateYProperty());
		 */		
		
		Slider slider = createSlider();
		world.translateZProperty().bind(slider.valueProperty());
		
		//Set up the root (main group)
		Group root = new Group();
		root.getChildren().add(world);
		root.getChildren().add(createImageView());
		root.getChildren().add(slider);
		world.getChildren().add(asteroid1);
		world.getChildren().add(asteroid2);
		world.getChildren().add(asteroid3);
		//Setting up the scene
		Scene scene = new Scene(root, WIDTH, HEIGHT,true);
	    scene.setFill(Color.SILVER);
	    scene.setCamera(camera);
	    
	    initMouseControl(world, scene, stage);
	    createAnimation();
	    
	    //Setting up the stage - the basics
	    stage.setTitle("Earth 3D model");
	    stage.setScene(scene);
	    stage.show();
	    
	}

	private void createAnimation() {
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long arg0) {
				// TODO Auto-generated method stub
				earth.rotateProperty().set(earth.getRotate()+0.2);
				pointLight.setRotate(pointLight.getRotate()+0.2);
				asteroid1.setRotationAxis(Rotate.Z_AXIS);
				asteroid1.setRotate(asteroid1.getRotate()+0.15);
				asteroid2.setRotationAxis(Rotate.Z_AXIS);
				asteroid2.setRotate(asteroid2.getRotate()+0.15);
				asteroid3.setRotationAxis(Rotate.Z_AXIS);
				asteroid3.setRotate(asteroid3.getRotate()+0.15);
				
				  asteroid1.translateXProperty().set(asteroid1.getTranslateX() - 0.22);
				  asteroid1.translateYProperty().set(asteroid1.getTranslateY() + 0.15);
				  asteroid1.translateZProperty().set(asteroid1.getTranslateZ() + 0.15);
				  asteroid2.translateXProperty().set(asteroid2.getTranslateX() - 0.22);
				  asteroid2.translateYProperty().set(asteroid2.getTranslateY() + 0.15);
				  asteroid2.translateZProperty().set(asteroid2.getTranslateZ() + 0.15);
				  asteroid3.translateXProperty().set(asteroid3.getTranslateX() - 0.22);
				  asteroid3.translateYProperty().set(asteroid3.getTranslateY() + 0.15);
				  asteroid3.translateZProperty().set(asteroid3.getTranslateZ() + 0.15);
				 
			}
		};
		timer.start();
	}

	//Adding background galaxy image
	private ImageView createImageView() {
		Image image = new Image(Main.class.getResourceAsStream("/Resources/galaxy.jpg"));
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.getTransforms().add(new Translate( -image.getWidth()/2, -image.getHeight()/2, 800));
		return imageView;
	}
	
	//Create slider to for scene
	private Slider createSlider() {
		Slider slider = new Slider();
		
		slider.setMax(100); 
		slider.setMin(-200); 
		slider.setPrefWidth(120d);
		slider.setLayoutX(40);
		slider.setLayoutY(-60);
		slider.setTranslateZ(5);
		slider.setOrientation(Orientation.VERTICAL);
		slider.setShowTickLabels(true);
		
		slider.setStyle("-fx-base: black");

		return slider;
	}

	private final PointLight pointLight = new PointLight();
	
	private Node[] prepareLightSource() {
		
		pointLight.setColor(Color.ANTIQUEWHITE);
		pointLight.getTransforms().add(new Translate(60,-20,80));
		pointLight.setRotationAxis(Rotate.Y_AXIS);
		
		Sphere moon = new Sphere(14);
		PhongMaterial moonSurface = new PhongMaterial();
		moonSurface.setDiffuseMap
					(new Image(getClass().getResourceAsStream("/Resources/moon-texture.jpg")));
		
		  moonSurface.setSelfIlluminationMap (new
		  Image(getClass().getResourceAsStream("/Resources/moon-illuminate.jpg")));
		 
		moon.setMaterial(moonSurface);
		moon.getTransforms().setAll(pointLight.getTransforms());
		moon.rotateProperty().bind(pointLight.rotateProperty());
		moon.rotationAxisProperty().bind(pointLight.rotationAxisProperty());
	 
	    //Return lights
	    return new Node[]{pointLight, moon};
		
	}
	

	//Create the Earth
	private Node createEarth() {
		PhongMaterial earthSurface = new PhongMaterial();
		earthSurface.setDiffuseMap
					(new Image(getClass().getResourceAsStream("/Resources/earth-d.jpg")));
		earthSurface.setSelfIlluminationMap
					(new Image(getClass().getResourceAsStream("/Resources/earth-l.jpg")));
		earthSurface.setSpecularMap
					(new Image(getClass().getResourceAsStream("/Resources/earth-s.jpg")));
		earthSurface.setBumpMap
					(new Image(getClass().getResourceAsStream("/Resources/earth-n.jpg")));
		earth.setMaterial(earthSurface);
		earth.setRotationAxis(Rotate.Y_AXIS);
		return earth;
	}
	
	public static void main(String[] args) { launch(args); }
}

