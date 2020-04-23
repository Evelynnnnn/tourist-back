package com.shilei.tourist.utils;



import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Camera {
    public  void main() throws InterruptedException {

        Webcam webcam = Webcam.getDefault();

        //webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);

        panel.setFPSDisplayed(true);

        panel.setDisplayDebugInfo(true);

        panel.setImageSizeDisplayed(true);

        panel.setMirrored(true);

        JFrame window = new JFrame("Test webcam panel");

        //window.setMinimumSize(new Dimension(800,600));

        window.add(panel);

        window.setResizable(true);

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        window.pack();

        window.setVisible(true);

        Thread.currentThread().sleep(5000);


        savePic(window);

        window.setVisible(false);

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

     static String savePic(JFrame jFrame){
        Container container = jFrame.getContentPane();

        BufferedImage img = new BufferedImage(
                jFrame.getWidth(), jFrame.getHeight(),BufferedImage.TYPE_INT_RGB
        );
        Graphics2D graphics2D = img.createGraphics();
        container.printAll(graphics2D);
        File file = new File("D://test.jpg");
        try{
            ImageIO.write(img,"jpg",file);
        }catch (IOException e){
            e.printStackTrace();
        }
        graphics2D.dispose();
        return file.toString();
    }
}
