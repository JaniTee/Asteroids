package asteroids;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsSovellus extends Application {

    public static int LEVEYS = 640;
    public static int KORKEUS = 400;

    public void start(Stage ikkuna) throws Exception {
        Pane ruutu = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        ruutu.getChildren().add(text);
        ruutu.setPrefSize(LEVEYS, KORKEUS);

        AtomicInteger pisteet = new AtomicInteger();

        Alus alus = new Alus(LEVEYS / 2, KORKEUS / 2);
        List<Asteroidi> asteroidit = new ArrayList<>();
        List<Ammus> ammukset = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Random arpoja = new Random();
            Asteroidi asteroidi = new Asteroidi(arpoja.nextInt(LEVEYS / 3), arpoja.nextInt(KORKEUS));
            asteroidit.add(asteroidi);
        }

        ruutu.getChildren().add(alus.getHahmo());
        asteroidit.forEach(asteroidi -> ruutu.getChildren().add(asteroidi.getHahmo()));

        Point2D liike = new Point2D(1, 0);

        Scene nakyma = new Scene(ruutu);

        Map<KeyCode, Boolean> painetutNapit = new HashMap<>();

        nakyma.setOnKeyPressed(event -> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });

        nakyma.setOnKeyReleased(event -> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {

            @Override
            public void handle(long nykyhetki) {
                if (painetutNapit.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
                    alus.kaannaVasemmalle();
                }

                if (painetutNapit.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
                    alus.kaannaOikealle();
                }

                if (painetutNapit.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
                    alus.kiihdyta();
                }

                alus.liiku();

                asteroidit.forEach(asteroidi -> asteroidi.liiku());

                asteroidit.forEach(asteroidi -> {
                    if (alus.tormaa(asteroidi)) {
                        stop();
                    }

                    //Luodaan ammus painettaesa SPACE nappainta.   
                    if (painetutNapit.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && ammukset.size() < 3) {
                        Ammus ammus = new Ammus((int) alus.getHahmo().getTranslateX(), (int) alus.getHahmo().getTranslateY());
                        ammus.getHahmo().setRotate(alus.getHahmo().getRotate());
                        ammukset.add(ammus);

                        ammus.kiihdyta();
                        ammus.setLiike(ammus.getLiike().normalize().multiply(3));

                        ruutu.getChildren().add(ammus.getHahmo());
                    }

                    ammukset.forEach(ammus -> ammus.liiku());

                    ammukset.forEach(ammus -> {
                        asteroidit.forEach(asteroidia -> {
                            if (ammus.tormaa(asteroidia)) {
                                ammus.setElossa(false);
                                asteroidia.setElossa(false);
                            }
                        });

                        if (!ammus.isElossa()) {
                            text.setText("Points: " + pisteet.addAndGet(1000));
                        }
                    });

                    ammukset.stream()
                            .filter(ammus -> !ammus.isElossa())
                            .forEach(ammus -> ruutu.getChildren().remove(ammus.getHahmo()));
                    ammukset.removeAll(ammukset.stream()
                            .filter(ammus -> !ammus.isElossa())
                            .collect(Collectors.toList()));

                    asteroidit.stream()
                            .filter(asteroidia -> !asteroidia.isElossa())
                            .forEach(asteroidia -> ruutu.getChildren().remove(asteroidia.getHahmo()));
                    asteroidit.removeAll(asteroidit.stream()
                            .filter(asteroidia -> !asteroidia.isElossa())
                            .collect(Collectors.toList()));

                });

                if (Math.random() < 0.005) {
                    Asteroidi asteroidi = new Asteroidi(LEVEYS, KORKEUS);
                    if (!asteroidi.tormaa(alus)) {
                        asteroidit.add(asteroidi);
                        ruutu.getChildren().add(asteroidi.getHahmo());
                    }
                }
            }
        }.start();

        ikkuna.setTitle("Asteroids!");
        ikkuna.setScene(nakyma);
        ikkuna.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static int osiaToteutettu() {
        // Ilmoita tämän metodin palautusarvolla kuinka monta osaa olet tehnyt
        return 4;
    }

}
