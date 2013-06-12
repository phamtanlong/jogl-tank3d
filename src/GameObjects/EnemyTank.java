/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import myjogl.utils.BoundSphere;
import myjogl.utils.Camera2;
import myjogl.utils.GLModel;
import myjogl.utils.Line;
import myjogl.utils.MathUtil;
import myjogl.utils.ModelLoaderOBJ;
import myjogl.utils.Vector3;

/**
 *
 * @author bu0i
 */
public class EnemyTank extends GameObject{
    private GLModel m_dauxe;
    public Camera2 TankCamera;
    public Vector3 shootDirect; // Vector ban 
    float gocQuay = 0;
    public EnemyTank(Vector3 _pos, Vector3 _dir, float _vel, float _scale) {
        super(_pos, _dir, _vel);
        m_modelDirect = new Vector3(0, 0, 1);// Model xe tank c� huong mac dinh l� theo truc Z
        m_scale = _scale;
    }
    
    public void Init(GLAutoDrawable drawable) {
        super.Init(drawable, "data/model/obj/Test/ThanXe.obj",
                "data/model/obj/Test/ThanXe.mtl", "data/model/obj/Test/untitled.jpg");
        m_dauxe = ModelLoaderOBJ.LoadModel("data/model/obj/Test/DauXe.obj",
                "data/model/obj/Test/DauXe.mtl", "data/model/obj/Test/untitled.jpg", drawable);
        
        TankCamera = new Camera2(0, 0, 0, Math.toRadians(80), Math.toRadians(90), 13);
        
        
        // Init Bound
        // Cai nay tuy tung model phai lam tay thoi may bac
        
        Vector3 mid = m_model.MidPoint;
        
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x,
                mid.y - 0.5f, mid.z + 0.2f), 1.2f)); // Cuc o giua
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x,
                mid.y - 0.5f, mid.z + 1.2f), 1.2f));// Cuc o giua
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x + 1.1f,
                mid.y - 0.9f, mid.z + 2.9f), 0.55f));// Cuc o truoc
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x - 1.3f,
                mid.y - 0.9f, mid.z + 2.9f), 0.55f));// Cuc o truoc
        
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x + 1.1f,
                mid.y - 0.9f, mid.z - 3.35f), 0.55f));// Cuc o sau
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x - 1.3f,
                mid.y - 0.9f, mid.z - 3.35f), 0.55f));// Cuc o sau
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x - 0.15f,
                mid.y - 0.9f, mid.z - 3.35f), 0.55f));// Cuc o sau giua
        
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x + 0.8f,
                mid.y - 0.85f, mid.z - 2.0f), 0.81f));// Cuc o sau giua
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x - 1.0f,
                mid.y - 0.85f, mid.z - 2.0f), 0.81f));// Cuc o sau giua
        
        BoundSphereObject.add(new BoundSphere(new Vector3(mid.x - 0.2f,
                mid.y - 0.85f, mid.z - 1.2f), 0.81f));// Cuc o sau giua
    }
    
    @Override
    public void Update(float gameTime) {
        super.Update(gameTime);
        TankCamera.Update(this);
        CalculateShootDirect();
        CalculateAngleDiret();
    }
    
    @Override
    public void Draw(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glPushMatrix();
        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(10, 0, 0);
        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0, 10, 0);
        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0, 0, 10);
        gl.glEnd();
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        float trucX = 0;
        float trucZ = -0.8f;
        //super.Draw(drawable);
        gl.glTranslatef(m_position.x, m_position.y, m_position.z);
        double angle = MathUtil.Angle2Vector(m_direct, m_modelDirect); // T�m g�c quay X la rong, z la dai
        if (m_direct.x <0)
            angle =-angle;
        
        gl.glRotatef((float)Math.toDegrees(angle), 0, 1, 0);
        gl.glScalef(m_scale, m_scale, m_scale);
        //gl.glColor3f(Color.x, Color.y, Color.z);
        m_model.opengldraw(drawable);
        
        
        gl.glPushMatrix();
            gl.glTranslatef(-trucX, 0, -trucZ);
            gl.glRotatef(-(float)Math.toDegrees(gocQuay), 0, 1, 0);
            gl.glTranslatef(trucX, 0, trucZ);

            gl.glTranslatef(0, 1.45f, 3.0f);
            m_dauxe.opengldraw(drawable);
        gl.glPopMatrix();
        GLU glu = new GLU();
        gl.glTranslatef(0, 3.5f, 0);
        //glu.gluSphere(glu.gluNewQuadric(), 0.1, 50, 45);
        gl.glPopMatrix();
    }
    
    public void CalculateShootDirect() {
        shootDirect = MathUtil.SubVector(
                new Vector3((float)TankCamera.lookAtX, (float)TankCamera.lookAtY, (float)TankCamera.lookAtZ),
                new Vector3((float)TankCamera.x, (float)TankCamera.y, (float)TankCamera.z));
    }
    
    public void CalculateAngleDiret() {
        Vector3 temp = new Vector3(shootDirect);
        temp.y = 0;
        gocQuay = 0;
        
        gocQuay = (float)MathUtil.Angle2Vector(temp, m_direct);
        //float a = (float)MathUtil.ScrossProduct(temp, m_direct);
        Vector3 temp2 = new Vector3();
        temp2.x = m_direct.y * temp.z - temp.y * m_direct.z; // Du roi
        temp2.z = - (m_direct.x * temp.y - temp.x * m_direct.y); // Du roi
        temp2.y = m_direct.x * temp.z - temp.x * m_direct.z;
        //System.err.print("\n" + temp2.y);
        if (temp2.y < 0)
                gocQuay =-gocQuay;
        
    }
    
    public void TurnLeft() {
        final float delta = 0.05f;
        float goc;
        if (m_direct.z == 0) {
            if (m_direct.x > 0)
                goc = (float)Math.PI / 2;
            else
                goc  = (float) -Math.PI / 2;
        } else {
            goc = (float)Math.atan(m_direct.x / m_direct.z);
            if (m_direct.z < 0)
                goc += Math.PI;
        }
        
        float directC = (float)Math.sqrt(m_direct.x*m_direct.x + m_direct.z*m_direct.z);
        goc += delta;
        
        m_direct.z = (float)Math.cos(goc) * directC;
        m_direct.x = (float)Math.sin(goc) * directC;
    }
    
    public void TurnRight() {
        //System.err.print("\nTurn Righ");
        final float delta = 0.05f;
        float goc;
        if (m_direct.z == 0) {
            if (m_direct.x > 0)
                goc = (float)Math.PI / 2;
            else
                goc  = (float) -Math.PI / 2;
        } else {
            goc = (float)Math.atan(m_direct.x / m_direct.z);
            if (m_direct.z < 0)
                goc += Math.PI;
        }
        
        float directC = (float)Math.sqrt(m_direct.x*m_direct.x + m_direct.z*m_direct.z);
        goc -= delta;
        
        m_direct.z = (float)Math.cos(goc) * directC;
        m_direct.x = (float)Math.sin(goc) * directC;
    }
    
    public Line GetLineBullet() {
        return new Line(new Vector3((float)TankCamera.lookAtX, (float)TankCamera.lookAtY, (float)TankCamera.lookAtZ),
                MathUtil.SubVector(new Vector3((float)TankCamera.lookAtX, (float)TankCamera.lookAtY, (float)TankCamera.lookAtZ),
                new Vector3((float)TankCamera.x, (float)TankCamera.y, (float)TankCamera.z)));
    }
   
    public void SetTankVel(float vel) {
        this.m_v = vel;
    }
}