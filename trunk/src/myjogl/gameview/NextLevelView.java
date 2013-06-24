/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myjogl.gameview;

import com.sun.opengl.util.texture.Texture;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import myjogl.GameEngine;
import myjogl.Global;
import myjogl.utils.Renderer;
import myjogl.utils.ResourceManager;
import myjogl.utils.Writer;

/**
 *
 * @author Jundat
 */
public class NextLevelView implements GameView {

    Point pBg = new Point(230, 132);
    Point pGame = new Point(230 + 291, 132 + 252);
    Point pOver = new Point(230 + 300, 132 + 165);
    Rectangle rectMenu = new Rectangle(230 + 30, 130, 202, 54);
    Rectangle rectRetry = new Rectangle(230 + 305, 130, 202, 54);
    //
    MenuItem itMenu;
    MenuItem itRetry;
    //
    MainGameView mainGameView;
    Texture ttBg;

    public NextLevelView(MainGameView mainGameView) {
        this.mainGameView = mainGameView;
        mainGameView.isPause = true;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void pointerPressed(MouseEvent e) {
        if (itMenu.contains(e.getX(), e.getY())) {
            itMenu.setIsClick(true);
        }

        if (itRetry.contains(e.getX(), e.getY())) {
            itRetry.setIsClick(true);
        }
    }

    public void pointerMoved(MouseEvent e) {
        if (itMenu.contains(e.getX(), e.getY())) {
            if (itMenu.isOver == false) {
                itMenu.setIsOver(true);
                GameEngine.sMouseMove.play(false);
            }
        } else {
            itMenu.setIsOver(false);
        }

        if (itRetry.contains(e.getX(), e.getY())) {
            if (itRetry.isOver == false) {
                itRetry.setIsOver(true);
                GameEngine.sMouseMove.play(false);
            }
        } else {
            itRetry.setIsOver(false);
        }
    }

    public void pointerReleased(MouseEvent e) {
        if (itMenu.contains(e.getX(), e.getY())) { //menu
            itMenu.setIsClick(true);
            GameEngine.sClick.play();
            //
            GameEngine.getInst().attach(new MenuView());
            GameEngine.getInst().detach(mainGameView);
            GameEngine.getInst().detach(this);
        } else if(rectRetry.contains(e.getX(), e.getY())) {
            itRetry.setIsClick(true);
            GameEngine.sClick.play();
            //
            mainGameView.isPause = false;
            mainGameView.loadLevel(Global.level + 1);
            GameEngine.getInst().detach(this);
        }
    }

    public void load() {
        ttBg = ResourceManager.getInst().getTexture("data/common/bg_dialog.png");
        //
        itMenu = new MenuItem(ResourceManager.getInst().getTexture("data/menu/btn.png"),
                ResourceManager.getInst().getTexture("data/menu/btn_press.png"));
        itRetry = new MenuItem(ResourceManager.getInst().getTexture("data/menu/btn.png"),
                ResourceManager.getInst().getTexture("data/menu/btn_press.png"));
        
        itMenu.SetPosition(rectMenu.x, rectMenu.y);
        itRetry.SetPosition(rectRetry.x, rectRetry.y);
        
        //
        GameEngine.getInst().saveHighscore();
    }

    public void unload() {
        ResourceManager.getInst().deleteTexture("data/common/bg_dialog.png");
    }

    public void update(long elapsedTime) {
    }

    public void display() {
        Renderer.Render(ttBg, pBg.x, pBg.y);
        //
        itMenu.Render();
        itRetry.Render();
        //
        GameEngine.writer.Render("LEVEL", pGame.x, pGame.y, 0.9f, 0.9f);
        GameEngine.writer.Render("COMPLETE", pOver.x-80, pOver.y, 0.9f, 0.9f);
        GameEngine.writer.Render("MENU", rectMenu.x + 24, rectMenu.y + 12, 0.85f, 0.85f);
        GameEngine.writer.Render("NEXT", rectRetry.x + 36, rectRetry.y + 12, 0.85f, 0.85f);
    }
}
