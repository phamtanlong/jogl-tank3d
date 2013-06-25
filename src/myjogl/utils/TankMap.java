/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myjogl.utils;

import com.sun.opengl.util.texture.Texture;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import myjogl.Global;
import myjogl.gameobjects.CRectangle;

public class TankMap {

    public static final float TILE_WIDTH = 1;
    public static final float TILE_HEIGHT = 2;
    public static final float TILE_BORDER = 2;
    //
    public static final float PLANE_HEIGHT = -0.02f;
    public static final float WATER_HEIGHT = -0.01f;
    //
    public byte[][] board;
    public int width;
    public int height;
    //
    public ArrayList listTankAiPosition;
    public ArrayList listTankPosition;
    public Vector3 bossPosition;
    public Vector3 bossAiPosition;
    //
    private static Texture ttGachTuong = null;
    private static Texture ttGachTuong2 = null;
    private static Texture ttGachMen = null;
    private static Texture ttGachMen1 = null;
    private static Texture ttGachMen2 = null;
    private static Texture ttGachMen3 = null;
    private static Texture ttRockTop = null;
    private static Texture ttRockEdge = null;
    private static Texture ttWater = null;
    //
    final float[] redLightColor = {1.0f, 1.0f, 1.0f, 1.0f}; //red
    final float[] redLightPos = {0.0f, 0.0f, 0.0f, 1.0f};
    //
    private static TankMap instance = null;
    private int listTop;
    private int listNonTopNonBottom;
    private int listBottom;
    private int listCube;
    //

    public static TankMap getInst() {
        if (instance == null) {
            instance = new TankMap();
        }

        return instance;
    }

    private TankMap() {
        ttGachTuong = ResourceManager.getInst().getTexture("data/game/gach_tuong.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);
        ttGachTuong2 = ResourceManager.getInst().getTexture("data/game/gach_tuong2.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);

        ttGachMen1 = ResourceManager.getInst().getTexture("data/game/gach_men1.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);
        ttGachMen2 = ResourceManager.getInst().getTexture("data/game/gach_men2.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);
        ttGachMen3 = ResourceManager.getInst().getTexture("data/game/gach_men3.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);

        ttRockTop = ResourceManager.getInst().getTexture("data/game/rock_top.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);
        ttRockEdge = ResourceManager.getInst().getTexture("data/game/rock_edge.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);
        ttWater = ResourceManager.getInst().getTexture("data/game/water.png", true, GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_LINEAR_MIPMAP_LINEAR, GL.GL_LINEAR_MIPMAP_LINEAR);

        //
        listTankPosition = new ArrayList();
        listTankAiPosition = new ArrayList();

        // Init list
        GL gl = Global.drawable.getGL();

        listTop = gl.glGenLists(1);
        gl.glNewList(listTop, GL.GL_COMPILE);
        //TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH
        gl.glBegin(GL.GL_QUADS);        // Draw The Cube Using quads
        {
            gl.glNormal3f(0, 1, 0);
            //glColor3f(0.0f,1.0f,0.0f);    // Color Blue
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Top Right Of The Quad (Top)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Top Left Of The Quad (Top)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Bottom Left Of The Quad (Top)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Bottom Right Of The Quad (Top)
        }
        gl.glEnd();
        gl.glEndList();
        ///
        listNonTopNonBottom = gl.glGenLists(1);
        gl.glNewList(listNonTopNonBottom, GL.GL_COMPILE);

        gl.glBegin(GL.GL_QUADS);        // Draw The Cube Using quads
        {
            gl.glNormal3f(0, 0, 1);
            //glColor3f(1.0f,0.0f,0.0f);    // Color Red    
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Front)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Front)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, 0, TILE_WIDTH);    // Bottom Left Of The Quad (Front)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, TILE_WIDTH);    // Bottom Right Of The Quad (Front)

            gl.glNormal3f(0, 0, -1);
            //glColor3f(1.0f,1.0f,0.0f);    // Color Yellow
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, 0, 0);    // Top Right Of The Quad (Back)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, 0, 0);    // Top Left Of The Quad (Back)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Bottom Left Of The Quad (Back)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Bottom Right Of The Quad (Back)

            gl.glNormal3f(-1, 0, 0);
            //glColor3f(0.0f,0.0f,1.0f);    // Color Blue
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Left)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Top Left Of The Quad (Left)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, 0, 0);    // Bottom Left Of The Quad (Left)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(0, 0, TILE_WIDTH);    // Bottom Right Of The Quad (Left)

            gl.glNormal3f(1, 0, 0);
            //glColor3f(1.0f,0.0f,1.0f);    // Color Violet
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Top Right Of The Quad (Right)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Right)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, TILE_WIDTH);    // Bottom Left Of The Quad (Right)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, 0);    // Bottom Right Of The Quad (Right)
        }
        gl.glEnd();            // End Drawing The Cube
        gl.glEndList();

        // List Rock
        listBottom = gl.glGenLists(1);
        gl.glNewList(listBottom, GL.GL_COMPILE);

        gl.glBegin(GL.GL_QUADS);        // Draw The Cube Using quads
        {
            gl.glNormal3f(0, -1, 0);
            //glColor3f(1.0f,0.5f,0.0f);    // Color Orange
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, WATER_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Bottom)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, WATER_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Bottom)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, WATER_HEIGHT, 0);    // Bottom Left Of The Quad (Bottom)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, WATER_HEIGHT, 0);    // Bottom Right Of The Quad (Bottom)
        }
        gl.glEnd();            // End Drawing The Cube
        gl.glEndList();

        //----------------------------
        listCube = gl.glGenLists(1);
        gl.glNewList(listCube, GL.GL_COMPILE);

        gl.glBegin(GL.GL_QUADS);        // Draw The Cube Using quads
        {
            gl.glNormal3f(0, 1, 0);
            //glColor3f(0.0f,1.0f,0.0f);    // Color Blue
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Top Right Of The Quad (Top)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Top Left Of The Quad (Top)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Bottom Left Of The Quad (Top)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Bottom Right Of The Quad (Top)
            
            gl.glNormal3f(0, -1, 0);
            //glColor3f(1.0f,0.5f,0.0f);    // Color Orange
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, WATER_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Bottom)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, WATER_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Bottom)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, WATER_HEIGHT, 0);    // Bottom Left Of The Quad (Bottom)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, WATER_HEIGHT, 0);    // Bottom Right Of The Quad (Bottom)

            gl.glNormal3f(0, 0, 1);
            //glColor3f(1.0f,0.0f,0.0f);    // Color Red    
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Front)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Front)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, 0, TILE_WIDTH);    // Bottom Left Of The Quad (Front)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, TILE_WIDTH);    // Bottom Right Of The Quad (Front)

            gl.glNormal3f(0, 0, -1);
            //glColor3f(1.0f,1.0f,0.0f);    // Color Yellow
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, 0, 0);    // Top Right Of The Quad (Back)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, 0, 0);    // Top Left Of The Quad (Back)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Bottom Left Of The Quad (Back)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Bottom Right Of The Quad (Back)

            gl.glNormal3f(-1, 0, 0);
            //glColor3f(0.0f,0.0f,1.0f);    // Color Blue
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(0, TILE_HEIGHT, TILE_WIDTH);    // Top Right Of The Quad (Left)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(0, TILE_HEIGHT, 0);    // Top Left Of The Quad (Left)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0, 0, 0);    // Bottom Left Of The Quad (Left)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(0, 0, TILE_WIDTH);    // Bottom Right Of The Quad (Left)

            gl.glNormal3f(1, 0, 0);
            //glColor3f(1.0f,0.0f,1.0f);    // Color Violet
            gl.glTexCoord2f(1, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, 0);    // Top Right Of The Quad (Right)
            gl.glTexCoord2f(0.0f, 1);
            gl.glVertex3f(TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);    // Top Left Of The Quad (Right)
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, TILE_WIDTH);    // Bottom Left Of The Quad (Right)
            gl.glTexCoord2f(1, 0.0f);
            gl.glVertex3f(TILE_WIDTH, 0, 0);    // Bottom Right Of The Quad (Right)
        }
        gl.glEnd();            // End Drawing The Cube
        gl.glEndList();

        //----------------------------
    }

    //only use png file
    public void LoadMap(int mapNumber) {
        switch (mapNumber % 3) {
            case 0:
                ttGachMen = ttGachMen1;
                break;

            case 1:
                ttGachMen = ttGachMen2;
                break;

            case 2:
                ttGachMen = ttGachMen3;
                break;
        }

        String fileName = "data/map/MAP" + mapNumber + ".png";

        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedImage image = ImageIO.read(is); //file

            this.width = image.getWidth();
            this.height = image.getHeight();

            this.board = new byte[height][width];
            int color;
            byte red, green, blue, alpha;

            for (int i = 0; i < height; ++i) //z
            {
                for (int j = 0; j < width; ++j) //x
                {
                    color = image.getRGB(j, i); //x,z

                    alpha = (byte) ((color & 0xff000000) >> 24);
                    red = (byte) ((color & 0x00ff0000) >> 16);
                    green = (byte) ((color & 0x0000ff00) >> 8);
                    blue = (byte) ((color & 0x000000ff));

                    board[i][j] = blue;
                    //
                    if (blue == ID.TANK) {
                        listTankPosition.add(new Vector3(j, 0, i));
                    } else if (blue == ID.TANK_AI) {
                        listTankAiPosition.add(new Vector3(j, 0, i));
                    } else if (blue == ID.BOSS) {
                        bossPosition = new Vector3(j, 0, i);
                    } else if (blue == ID.BOSS_AI) {
                        bossAiPosition = new Vector3(j, 0, i);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Map.LoadMap: can not load map!");
            Logger.getLogger(TankMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param drawable
     * @param h height of cube
     * @param w width of cube
     */
    public void Render() { /*float w, float h*/
        GL gl = Global.drawable.getGL();

        //this.drawPlane(-(int) TILE_BORDER, -(int) TILE_BORDER, width + (int) TILE_BORDER, height + (int) TILE_BORDER);
        this.drawPlane(0, 0, width, height);
        //this.drawBorder();

        int blue = 0;
        for (int i = 0; i < height; ++i) //z
        {
            for (int j = 0; j < width; ++j) //x
            {
                blue = board[i][j];
                //
                if (blue == ID.BRICK) {
                    this.drawCube(j * TILE_WIDTH, 0, i * TILE_WIDTH,
                            TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
                } else if (blue == ID.ROCK) {
                    this.drawRock(j * TILE_WIDTH, 0, i * TILE_WIDTH,
                            TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
                } else if (blue == ID.WATER) {
                    this.drawWater(j * TILE_WIDTH, i * TILE_WIDTH,
                            TILE_WIDTH, TILE_WIDTH);
                }
            }
        }
    }

    public boolean isIntersect(CRectangle rect) {
        int x = (int) rect.x;
        int y = (int) rect.y;
        int r = Global.getUpper(rect.x + rect.w);
        int b = Global.getUpper(rect.y + rect.h);

        for (int i = y; i < b; i++) {
            for (int j = x; j < r; j++) {
                if (j >= 0 && j < width && i >= 0 && i < height) {
                    if (board[i][j] == ID.BRICK || board[i][j] == ID.ROCK) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Item that
     *
     * @return
     */
    public boolean isIntersectItem(CRectangle rect, int id) {
        int x = (int) rect.x;
        int y = (int) rect.y;
        int r = Global.getUpper(rect.x + rect.w);
        int b = Global.getUpper(rect.y + rect.h);

        for (int i = y; i < b; i++) {
            for (int j = x; j < r; j++) {
                if (j >= 0 && j < width && i >= 0 && i < height) {
                    if (board[i][j] == id) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param x
     * @param y meant z
     */
    public void delete(int z, int x) {
        if (x >= 0 && x < width && z >= 0 && z < height) {
            if (board[z][x] == ID.BRICK) {
                board[z][x] = 0;
            }
        }
    }

    private void drawBorder() {
        int border = (int) TILE_BORDER;
        for (int i = -border; i < height + border; ++i) //z
        {
            this.drawRock(-border, 0, i * TILE_WIDTH,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(-border + 1, 0, i * TILE_WIDTH,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(width + border - 1, 0, i * TILE_WIDTH,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(width + border - 2, 0, i * TILE_WIDTH,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
        }

        for (int i = -border; i < width + border; i++) {
            this.drawRock(i * TILE_WIDTH, 0, -border,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(i * TILE_WIDTH, 0, -border + 1,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(i * TILE_WIDTH, 0, height + border - 1,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
            this.drawRock(i * TILE_WIDTH, 0, height + border - 2,
                    TILE_WIDTH, TILE_HEIGHT, TILE_WIDTH);
        }
    }

    private void drawPlane(float x, float z, float w, float h) {
        GL gl = Global.drawable.getGL();

        ttGachMen.enable();
        ttGachMen.bind();

        gl.glBegin(GL.GL_QUADS);
        {
            gl.glNormal3f(0.0f, 1.0f, 0.0f);

            gl.glTexCoord2f(0, 0);
            gl.glVertex3f(x, PLANE_HEIGHT, z);

            gl.glTexCoord2f(w / 3, 0);
            gl.glVertex3f(x + w, PLANE_HEIGHT, z);

            gl.glTexCoord2f(w / 3, h / 3);
            gl.glVertex3f(x + w, PLANE_HEIGHT, z + h);

            gl.glTexCoord2f(0, h / 3);
            gl.glVertex3f(x, PLANE_HEIGHT, z + h);
        }
        gl.glEnd();
        ttGachMen.disable();

        //reset color
        gl.glColor4f(1, 1, 1, 1);
    }

    public void drawCube(float x, float y, float z,
            float sx, float sy, float sz) {
        GL gl = Global.drawable.getGL();

        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        ttGachTuong2.enable();
        ttGachTuong2.bind();
        gl.glCallList(listTop);
        ttGachTuong2.disable();

        ttGachTuong.enable();
        ttGachTuong.bind();
        gl.glCallList(listNonTopNonBottom);
        ttGachTuong.disable();

        gl.glPopMatrix();

        //reset color
        gl.glColor4f(1, 1, 1, 1);
    }

    public void drawRock(float x, float y, float z,
            float sx, float sy, float sz) {
        GL gl = Global.drawable.getGL();

        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        ttRockTop.enable();
        ttRockTop.bind();
        gl.glCallList(listTop);
        ttRockTop.disable();

        ttRockEdge.enable();
        ttRockEdge.bind();
        gl.glCallList(listNonTopNonBottom);
        ttRockEdge.disable();

        gl.glPopMatrix();

        //reset color
        gl.glColor4f(1, 1, 1, 1);
    }

    public void drawWater(float x, float z, float sx, float sz) {
        GL gl = Global.drawable.getGL();

        gl.glPushMatrix();
        gl.glTranslatef(x, 0, z);

        ttWater.enable();
        ttWater.bind();
        gl.glCallList(listBottom);
        ttWater.disable();

        gl.glPopMatrix();

        //reset color
        gl.glColor4f(1, 1, 1, 1);
    }
}
