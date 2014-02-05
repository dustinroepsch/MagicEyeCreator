import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Dustin on 2/4/14.
 */
public class Main extends JFrame {
    private JMenuBar menuBar;
    private JLabel pattern;
    private JLabel map;
    private BufferedImage patternImage;
    private BufferedImage mapImage;
    private JFileChooser fc;
    private Main(){
        super("Magic Eye Creator");
        setSize(new Dimension(600, 600));
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fc = new JFileChooser();
        menuBar = createMenu();
        add(menuBar, BorderLayout.PAGE_START);

        patternImage = new BufferedImage(100,500,BufferedImage.TYPE_3BYTE_BGR);
        pattern = new JLabel(new ImageIcon(patternImage));

        add(pattern,BorderLayout.LINE_START);
        mapImage = new BufferedImage(500,500,BufferedImage.TYPE_3BYTE_BGR);
        map = new JLabel(new ImageIcon(mapImage));

        add(map,BorderLayout.CENTER);
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage render = new BufferedImage(500,500,BufferedImage.TYPE_3BYTE_BGR);
                for (int y = 0; y < 500; y++){
                    for (int x = 0; x < 500; x++){
                        if (x < 100){
                            render.setRGB(x,y,patternImage.getRGB(x,y));

                        }
                        else{
                            //System.out.println("" + x + " " + y);
                            //System.out.println(getOffset(mapImage.getRGB(x, y)));
                            render.setRGB(x, y, render.getRGB(x - (100 - getOffset(mapImage.getRGB(x, y))), y));
                        }

                    }
                }

                fc.showOpenDialog(getComponent(0));
                try {
                    ImageIO.write(render,"png",fc.getSelectedFile());
                    JOptionPane.showMessageDialog(getComponent(0),"Save worked!");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getComponent(0),"an error occured!");
                }

            }
        });
        menu.add(file);

        JMenuItem openMap = new JMenuItem("Open a map");
        file.add(openMap);
        file.add(save);
        JMenuItem openPattern = new JMenuItem("Open a pattern");
        file.add(openPattern);

        JMenuItem randomPattern = new JMenuItem("Create a random pattern");
        file.add(randomPattern);




        randomPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(100,500, BufferedImage.TYPE_3BYTE_BGR);
                for(int i = 0; i < 100; i++){
                    for (int j = 0; j < 500; j++){
                        image.setRGB(i,j,((int)(Math.random()*2) == 0)? 0: 16777215);
                    }
                }
                pattern.setIcon(new ImageIcon(image));
                patternImage = image;
            }
        });



        openMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.showOpenDialog(getComponent(0));
                try {
                    BufferedImage chosenImage = ImageIO.read(fc.getSelectedFile());
                    map.setIcon(new ImageIcon(chosenImage));
                    mapImage = chosenImage;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        openPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.showOpenDialog(getComponent(0));
                try {
                    BufferedImage chosenImage = ImageIO.read(fc.getSelectedFile());
                    pattern.setIcon(new ImageIcon(chosenImage));
                    patternImage = chosenImage;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return menu;
    }

    private int getOffset(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        return (int) (r * 10/255.0);
    }

    public static void main(String[] args){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    Main main = new Main();
                    main.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
