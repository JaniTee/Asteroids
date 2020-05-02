/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import javafx.scene.shape.Polygon;
import java.util.Random;

/**
 *
 * @author jt
 */
public class Asteroidi extends Hahmo {
    
    public double pyorimisliike;
    
    public Asteroidi(int x, int y) {
        super(new MonikulmioTehdas().luoMonikulmio(), x, y);
        
        Random arpoja = new Random();
        
        //Arvotaan asteroidin suunta.
        super.getHahmo().setRotate(arpoja.nextInt(360));
        
        //Arvotaan asteroidin nopeus.
        int kiihdytystenMaara = 1 + arpoja.nextInt(10);
        for(int i = 0; i < kiihdytystenMaara; i++) {
            kiihdyta();
        }
        
        //Arvotaan pyorimisliike.
        this.pyorimisliike = 0.5 - arpoja.nextDouble();
    }
    
    @Override
    public void liiku() {
        super.liiku();
        super.getHahmo().setRotate(super.getHahmo().getRotate() + pyorimisliike);
    }
    
}
