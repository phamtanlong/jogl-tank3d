/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myjogl.gameview;

import com.sun.opengl.util.texture.Texture;
import myjogl.particles.ParticalManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import myjogl.*;
import myjogl.utils.*;
import myjogl.gameobjects.*;
import myjogl.particles.Debris;
import myjogl.particles.Explo;
import myjogl.particles.Explo1;
import myjogl.particles.RoundSparks;

/**
 *
 * @author Jundat
 */
public class MainGameView implements GameView {

    public final static int SCORE_DELTA = 10;
    public final static int NUMBER_OF_LIEF = 3;
    public final static int MAX_CURRENT_AI = 4; //maximum current TankAI in 1 screen, at a moment
    //
    public boolean isPause;
    private int numberOfLife = NUMBER_OF_LIEF;
    //tankAI
    private int lastTanks; //so tang con lai, chwa dwa ra
    private int currentTank; //number of tank in screen at a moment
    //
    private Tank playerTank;
    private TankAI tankAis[];
    private SkyBox m_skybox;
    private Camera camera;
    private Writer writer;
    private Vector3 bossPosition;
    //light
    final float[] redLightColorAmbient = {0.0f, 0.0f, 0.0f, 0.0f}; //red
    final float[] redLightColorDisfuse = {2.0f, 2.0f, 2.0f, 1.0f}; //red
    final float[] redLightColorSpecular = {6.0f, 6.0f, 6.0f, 1.0f}; //red
    final float[] redLightPos = {32.0f, 20.0f, 32.0f, 1.0f};
    //
    Point pLevel = new Point(5, 570);
    Point pAI = new Point(5, 530);
    Point pLife = new Point(5, 490);
    //
    Point pScore = new Point(820, 570);
    Point pScoreValue = new Point(838, 530);
    //
    Boss boss;
    //sound
    Sound sBackground;
    //

    public MainGameView() {
        super();
        System.out.println("Go to main game!------------------------------------");
    }

    //
    // handle input
    //
    public void keyPressed(KeyEvent e) {
        if (isPause) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.isPause = true;
            GameEngine.getInst().attach(new PauseView(this));
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (playerTank.isAlive) {
                if (playerTank.fire()) {
                    GameEngine.sFire.clone().setVolume(6.0f);
                    GameEngine.sFire.clone().play();
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void pointerPressed(MouseEvent e) {
    }

    public void pointerMoved(MouseEvent e) {
    }

    public void pointerReleased(MouseEvent e) {
    }

    private void handleInput() {
        if (isPause) {
            return;
        }

        KeyboardState state = KeyboardState.getState();

        //up
        if (state.isDown(KeyEvent.VK_UP)) {
            if (playerTank.isAlive) {
                playerTank.move(CDirections.UP);
                if (this.checkTankCollision(playerTank)) {
                    this.playerTank.rollBack();
                }
            }
        } //down
        if (state.isDown(KeyEvent.VK_DOWN)) {
            if (playerTank.isAlive) {
                playerTank.move(CDirections.DOWN);
                if (this.checkTankCollision(playerTank)) {
                    this.playerTank.rollBack();
                }
            }
        }  //left
        if (state.isDown(KeyEvent.VK_LEFT)) {
            if (playerTank.isAlive) {
                playerTank.move(CDirections.LEFT);
                if (this.checkTankCollision(playerTank)) {
                    this.playerTank.rollBack();
                }
            }
        }  //right
        if (state.isDown(KeyEvent.VK_RIGHT)) {
            if (playerTank.isAlive) {
                playerTank.move(CDirections.RIGHT);
                if (this.checkTankCollision(playerTank)) {
                    this.playerTank.rollBack();
                }
            }
        }

    }

    //
    // end - handle input
    //
    //
    // initialize
    //
    private void setLight() {
        GL gl = Global.drawable.getGL();

        gl.glEnable(GL.GL_LIGHTING);
        // set up static red light
        gl.glEnable(GL.GL_LIGHT1);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, redLightColorAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, redLightColorDisfuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, redLightColorSpecular, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, redLightPos, 0);
    }

    public void loadLevel(int level) {
        Global.level = level;

        try {
            //init map
            TankMap.getInst().LoadMap("data/map/MAP" + Global.level + ".png");

            //boss
            this.bossPosition = TankMap.getInst().bossPosition.Clone();
            boss = new Boss(TankMap.getInst().bossPosition, CDirections.UP);
            boss.load();

            //player
            int size = TankMap.getInst().listTankPosition.size();
            int choose = Global.random.nextInt(size);
            Vector3 v = ((Vector3) TankMap.getInst().listTankPosition.get(choose)).Clone();
            playerTank = new Tank(v, CDirections.UP);
            playerTank.load();
            numberOfLife = NUMBER_OF_LIEF;

            lastTanks = 200; //so tank chua ra
            currentTank = 0; //so tank dang online
            tankAis = new TankAI[MAX_CURRENT_AI];
            for (int i = 0; i < MAX_CURRENT_AI; i++) {
                tankAis[i] = new TankAI();
                tankAis[i].load();
                tankAis[i].isAlive = false;
            }
        } catch (Exception e) {
            System.out.println("Can not file map: MAP" + Global.level);
        }
    }

    public void load() {
        this.setLight();

        //init map
        this.loadLevel(Global.level); //start at Global.level 0

        isPause = false;

        //init variable
        camera = new Camera();
        camera.Position_Camera(19.482517f, 28.869976f, 38.69388f, 19.481977f, 27.494007f, 38.006523f, 0.0f, 1.0f, 0.0f);

        //skybox
        m_skybox = new SkyBox();
        m_skybox.Initialize(5.0f);
        m_skybox.LoadTextures(
                "data/skybox/top.jpg", "data/skybox/bottom.jpg",
                "data/skybox/front.jpg", "data/skybox/back.jpg",
                "data/skybox/left.jpg", "data/skybox/right.jpg");

        //writer
        writer = new Writer("data/font/Motorwerk_80.fnt");
        //sound
        sBackground = ResourceManager.getInst().getSound("sound/bg_game.wav", true);
        sBackground.play();
    }

    public void unload() {
        GameEngine.getInst().tank3d.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        //pre-load main game
        //ResourceManager.getInst().deleteTexture("data/model/triax_wheels.png");

        //skybox
        //ResourceManager.getInst().deleteTexture("data/skybox/top.jpg");
        //ResourceManager.getInst().deleteTexture("data/skybox/bottom.jpg");
        //ResourceManager.getInst().deleteTexture("data/skybox/left.jpg");
        //ResourceManager.getInst().deleteTexture("data/skybox/right.jpg");
        //ResourceManager.getInst().deleteTexture("data/skybox/front.jpg");
        //ResourceManager.getInst().deleteTexture("data/skybox/back.jpg");
    }

    private void createNewAi() {

        //tankAI
        if (currentTank < MAX_CURRENT_AI && lastTanks > 0) { //create new
            for (int i = 0; i < MAX_CURRENT_AI; i++) {
                if (tankAis[i].isAlive == false) {
                    boolean isok = false; //check if have a position for it

                    //get position
                    for (Object v : TankMap.getInst().listTankAiPosition) {
                        Vector3 pos = new Vector3((Vector3) v);
                        tankAis[i].setPosition(pos);

                        //check collision
                        //player tank
                        if (tankAis[i].getBound().isIntersect(playerTank.getBound())) {
                            continue;
                        } else { //list current tank AI
                            boolean isok2 = true;
                            for (int j = 0; j < MAX_CURRENT_AI; j++) {
                                if (tankAis[j].isAlive == true) {
                                    if (tankAis[i].getBound().isIntersect(tankAis[j].getBound())) {
                                        isok2 = false;
                                        break;
                                    }
                                }
                            }

                            if (isok2) {
                                isok = true;
                                break;
                            }
                        }
                    }

                    if (isok) {
                        tankAis[i].isAlive = true;
                        tankAis[i].setDirection(Global.random.nextInt(CDirections.NUMBER_DIRECTION));
                        lastTanks--;
                        currentTank++;
                        break;
                    }
                }
            }

        }
    }
    //
    // end initialize
    //
    //
    // check game change
    //

    private void checkGameOver() {
        if (numberOfLife <= 0) { //gameover
            GameEngine.getInst().attach(new GameOverView(this));
        } else { // reset new life

            for (Object o : TankMap.getInst().listTankPosition) {
                Vector3 v = (Vector3) o;

                playerTank.reset(v, CDirections.UP);

                boolean isOK = true;
                //check
                for (int i = 0; i < MAX_CURRENT_AI; i++) {
                    if (tankAis[i].isAlive) {
                        if (tankAis[i].getBound().isIntersect(playerTank.getBound())) {
                            isOK = false;
                            break;
                        }
                    }
                }

                if (isOK == true) {
                    numberOfLife--;
                    break;
                }
            }
        }
    }

    private void checkLevelComplete() {
        if (lastTanks <= 0 && currentTank <= 0) { //complete
            GameEngine.getInst().attach(new NextLevelView(this));
        }
    }

    //
    // end check game change
    //
    //
    // check collision
    //
    private boolean checkTankCollision(Tank tank) {
        CRectangle rectTank = tank.getBound();
        if (tank.isAlive == false) {
            System.out.println("check collision DEAD tank @@@@@@@@@@@@@@@@@@@@@@");
            return false;
        }

        if (tank == playerTank) { //check player vs tankAis
            for (int i = 0; i < MAX_CURRENT_AI; i++) {
                if (tankAis[i].isAlive) { //is alive
                    boolean isCollide = rectTank.isIntersect(tankAis[i].getBound());
                    if (isCollide == true) {
                        return true;
                    }
                }
            }
        } else { //tankAi
            //tankAis vs playerTank
            if (playerTank.isAlive) {
                if (rectTank.isIntersect(playerTank.getBound())) {
                    return true;
                }
            }

            //tankAi vs tankAi
            for (int i = 0; i < MAX_CURRENT_AI; i++) {
                if (tankAis[i].isAlive) { //is alive
                    if (tankAis[i] != tank) { //not the same
                        if (rectTank.isIntersect(tankAis[i].getBound())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void checkBulletCollision() {
        //player's Bullets
        for (int i = 0; i < Tank.TANK_NUMBER_BULLETS; i++) {
            TankBullet bullet = playerTank.bullets[i];

            if (bullet.isAlive) {

                //vs Boss
                if (bullet.getBound().isIntersect(boss.getBound())) {
                    boss.explode();
                    boss.isAlive = false;
                    this.isPause = true;
                    GameEngine.getInst().attach(new GameOverView(this));
                }

                //tankAis
                for (int j = 0; j < MAX_CURRENT_AI; j++) {

                    //vs tankAis
                    TankAI tankAi = tankAis[j];
                    if (tankAi.isAlive && bullet.isAlive) {
                        if (tankAi.getBound().isIntersect(bullet.getBound())) {
                            //set isdead
                            tankAi.isAlive = false;
                            bullet.isAlive = false;
                            currentTank--;

                            //particle
                            tankAi.explode();

                            //Global.score
                            Global.score += SCORE_DELTA;

                            //check Global.level complete
                            this.checkLevelComplete();

                            break;
                        }
                    }

                    //vs tankAis's bullets
                    for (int k = 0; k < Tank.TANK_NUMBER_BULLETS; k++) {
                        TankBullet aiBullet = tankAi.bullets[k];
                        if (aiBullet.isAlive && bullet.isAlive) {
                            if (aiBullet.getBound().isIntersect(bullet.getBound())) {
                                //set is dead
                                aiBullet.isAlive = false;
                                bullet.isAlive = false;

                                //particle
                                bullet.explode();

                                break;
                            }
                        }
                    }

                    //optimize
                    if (bullet.isAlive == false) {
                        break;
                    }
                }
            }
        }

        //tankAis's Bullets
        for (int i = 0; i < MAX_CURRENT_AI; i++) {
            TankAI tankAi = tankAis[i];

            for (int j = 0; j < Tank.TANK_NUMBER_BULLETS; j++) {

                TankBullet aiBullet = tankAi.bullets[j];


                //vs Boss
                if (aiBullet.getBound().isIntersect(boss.getBound())) {
                    boss.explode();
                    boss.isAlive = false;
                    this.isPause = true;
                    GameEngine.getInst().attach(new GameOverView(this));
                }

                //playerTank
                if (aiBullet.isAlive && playerTank.isAlive) {
                    if (aiBullet.getBound().isIntersect(playerTank.getBound())) {
                        //set dead
                        aiBullet.isAlive = false;
                        playerTank.isAlive = false;

                        //particle
                        playerTank.explode();

                        //reset player or game over
                        this.checkGameOver();
                    }
                }
            }
        }
    }

    //
    // end check collision
    //
    public void update(long dt) {
        if (isPause) {
            return;
        }

        handleInput();
        //check bullet collisiotn
        this.checkBulletCollision();


        //tank
        playerTank.update(dt);

        //tankAI
        createNewAi();

        //update ai
        for (int i = 0; i < MAX_CURRENT_AI; i++) {
            tankAis[i].update(dt);

            if (tankAis[i].isAlive) {
                if (this.checkTankCollision(tankAis[i])) {
                    tankAis[i].rollBack();
                    tankAis[i].randomNewDirection();
                }
            }
        }

        //particle
        ParticalManager.getInstance().Update();
    }

    public void display() {
        //
        GL gl = Global.drawable.getGL();
        GLU glu = new GLU();
        gl.glLoadIdentity();
        gl.glEnable(GL.GL_LIGHTING);

        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GL.GL_POLYGON_SMOOTH);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL.GL_MULTISAMPLE);

        glu.gluLookAt(
                camera.mPos.x, camera.mPos.y, camera.mPos.z,
                camera.mView.x, camera.mView.y, camera.mView.z,
                camera.mUp.x, camera.mUp.y, camera.mUp.z);

        // skybox origin should be same as camera position
        m_skybox.Render(camera.mPos.x, camera.mPos.y, camera.mPos.z);

        //tank
        playerTank.draw();

        //tankAis
        for (int i = 0; i < MAX_CURRENT_AI; i++) {
            tankAis[i].draw();
        }

        //map
        TankMap.getInst().Render();

        boss.draw();

        //particle
        ParticalManager.getInstance().Draw(gl, 45);

        gl.glDisable(GL.GL_LIGHTING);

        //draw info
        float scale = 0.7f;
        writer.Render("LEVEL  " + Global.level, pLevel.x, pLevel.y, scale, scale, 1, 1, 1);
        writer.Render("AI    " + lastTanks, pAI.x, pAI.y, scale, scale, 1, 1, 1);
        writer.Render("LIFE " + numberOfLife, pLife.x, pLife.y, scale, scale, 1, 1, 1);
        writer.Render("SCORE  ", pScore.x, pScore.y, scale, scale, 1, 1, 1);
        writer.Render("" + Global.score, pScoreValue.x, pScoreValue.y, scale, scale, 1, 1, 1);
    }
}