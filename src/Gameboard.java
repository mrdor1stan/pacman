import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameboard extends JPanel implements KeyListener, ActionListener {

    Timer tm = new Timer(5, this);


    int pacmanSpeed=4;

    int timer = 100;
    int dumplingsEaten = 0;

    Character pacman = new Character(PacmanGame.PACMAN_LIVES, pacmanSpeed, new ImageIcon("src/images/cossack1.png"),new Point(PacmanGame.GAME_WIDTH/2,PacmanGame.SCOREBAR_HEIGHT+23*PacmanGame.SQUARE_SIZE));
    //X selects String (vertical movement), Y selects char (horizontal movement)
    double mapx = (pacman.getSpawnPoint().y-3*PacmanGame.SQUARE_SIZE)/28d;
    double mapy = pacman.getSpawnPoint().x/28d;
    Character chmonya = new Character(1, 4, new ImageIcon("src/images/chmonya1.png"), new Point(PacmanGame.WINDOW_WIDTH/2, (PacmanGame.WINDOW_HEIGHT/2)-PacmanGame.SQUARE_SIZE));

    Character brute = new Character(2, 2, new ImageIcon("src/images/brute1.png"), new Point(PacmanGame.WINDOW_WIDTH/2-2*PacmanGame.SQUARE_SIZE, (PacmanGame.WINDOW_HEIGHT/2)-PacmanGame.SQUARE_SIZE));

    ImageIcon dumpIcon = new ImageIcon("src/images/dumpling.png");
    Image dumpImage = dumpIcon.getImage();
    int dotDistance = (int)(PacmanGame.SQUARE_SIZE*0.75/2d);

    int dotSize = PacmanGame.SQUARE_SIZE-2*dotDistance;
    Image dumpling = dumpImage.getScaledInstance((int) (dotSize*1.2), (int) (dotSize*1.2), Image.SCALE_SMOOTH);

    public Gameboard(int width, int height){
        this.setSize(width, height);
        tm.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        printMaze(g2d);
        g2d.drawImage(pacman.getCurrentSprite().getImage(), (int) pacman.getSpawnPoint().getX(), (int) pacman.getSpawnPoint().getY(), this);
        g2d.drawImage(chmonya.getCurrentSprite().getImage(), (int) chmonya.getSpawnPoint().getX(), (int) chmonya.getSpawnPoint().getY(), this);
        g2d.drawImage(brute.getCurrentSprite().getImage(), (int) brute.getSpawnPoint().getX(), (int) brute.getSpawnPoint().getY(), this);
    }

    public void printMaze(Graphics2D g2d){
//        g2d.fillRect(120,0,5,720);
//        g2d.fillRect(0,120,560,5);
        for(int i = 0; i< PacmanGame.levelData.length; i++){
            for(int j = 0; j< PacmanGame.levelData[i].length; j++){
                char current = PacmanGame.levelData[i][j];
//                if(current == PacmanGame.levelData[0][0])
//                    System.out.println(current);
                switch(current){
                    case 'd' -> //g2d.setColor(dotColor);
                        //g2d.fillRect(j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, dotSize, dotSize);
                        // SQUARE_SIZE/4
                            g2d.drawImage(dumpling, j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, this);

                    case 'w' -> {
                        int height  =  PacmanGame.SQUARE_SIZE;
                        int width = PacmanGame.SQUARE_SIZE;
                        int x_pos = j* PacmanGame.SQUARE_SIZE;
                        int y_pos = PacmanGame.SCOREBAR_HEIGHT+i*PacmanGame.SQUARE_SIZE;

                        //lower border
                       if(i+1<PacmanGame.levelData.length)
                           if(PacmanGame.levelData[i+1][j]!='w'){
                           height-=5;
                       }
                        //top border
                        if(i-1>0)
                            if(PacmanGame.levelData[i-1][j]!='w'){
                                y_pos+=5;
                                height-=5;
                            }
                        //left border
                        if(j-1>0)
                            if(PacmanGame.levelData[i][j-1]!='w'){
                                x_pos+=5;
                                width-=5;
                            }
                        //right border
                        if(j+1<PacmanGame.levelData[i].length)
                            if(PacmanGame.levelData[i][j+1]!='w'){
                                width-=5;
                            }
                    g2d.drawRect(x_pos, y_pos, width, height);
                    }
                }
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP){
            System.out.println("go up");
            pacman.setCurrentMovement(Character.Movement.UP);
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            System.out.println("go down");
            pacman.setCurrentMovement(Character.Movement.DOWN);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            System.out.println("go left");
            pacman.setCurrentMovement(Character.Movement.LEFT);
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            System.out.println("go right");
            pacman.setCurrentMovement(Character.Movement.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(pacman.currentMovement == Character.Movement.RIGHT) {
            if(pacmanSpeed==0)
                pacmanSpeed = 4;
            if(pacman.getSpawnPoint().getX() == PacmanGame.LEVEL_DATA.length-PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) mapx][(int) mapy+1] == 'w') {
                pacmanSpeed = 0;
            }
            else {
                //Change cossack's position relative to the map (Y is horizontal movement; addition = moving right
                mapy += 1/7d;
                //Change cossack's position relative to the app (X is horizontal)
                pacman.getSpawnPoint().x = pacman.getSpawnPoint().x + pacmanSpeed;
            }
        }
        else if(pacman.currentMovement == Character.Movement.LEFT) {
            if(pacmanSpeed==0)
                pacmanSpeed = 4;
            if(pacman.getSpawnPoint().getX() == PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) mapx][(int) (mapy-1/7d)] == 'w')
                pacmanSpeed = 0;
            else {
                //Change cossack's position relative to the map (Y is horizontal movement); subtraction = moving left
                mapy -= 1/7d;
                //Change cossack's position relative to the app (X is horizontal)
                pacman.getSpawnPoint().x = pacman.getSpawnPoint().x - pacmanSpeed;
            }
        }
        else if(pacman.currentMovement == Character.Movement.UP) {
            if(pacmanSpeed==0)
                pacmanSpeed = 4;
            if(pacman.getSpawnPoint().getY() == PacmanGame.SCOREBAR_HEIGHT+PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) (mapx-1/7d)][(int) mapy] == 'w')
                pacmanSpeed = 0;
            else {
                mapx -= 1/7d;
                pacman.getSpawnPoint().y = pacman.getSpawnPoint().y - pacmanSpeed;
            }
        }
        else if(pacman.currentMovement == Character.Movement.DOWN) {
            if(pacmanSpeed==0)
                pacmanSpeed = 4;
            if(pacman.getSpawnPoint().getY() == PacmanGame.WINDOW_HEIGHT-PacmanGame.SCOREBAR_HEIGHT-PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) mapx+1][(int) mapy] == 'w')
                pacmanSpeed = 0;
            else {
                mapx += 1/7d;
                pacman.getSpawnPoint().y = pacman.getSpawnPoint().y + pacmanSpeed;
            }
        }
        //X selects String (vertical movement), Y selects char (horizontal movement)
        //Offset X by 3 squares to adjust spawn point
        mapx = (pacman.getSpawnPoint().y-3*PacmanGame.SQUARE_SIZE)/28d;
        mapy = pacman.getSpawnPoint().x/28d;
        if(PacmanGame.levelData[(int) mapx][(int) mapy] == 'd') {
            PacmanGame.levelData[(int) mapx][(int) mapy] = ' ';
            dumplingsEaten++;
            if(dumplingsEaten==5) {
                timer += 20;
                dumplingsEaten = 0;
            }
        }
        System.out.println(timer);
        repaint();
    }

}
