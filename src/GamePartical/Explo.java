/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**************************************************************************** 
 Author   :   tieunun - Nguyen Ngoc Thanh Huy
 Written for OpenGL Game Programming
*****************************************************************************/
package GamePartical;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import myjogl.utils.ResourceManager;
import myjogl.utils.Vector3;

/**
 *
 * @author TIEUNUN
 */
public class Explo extends ParticleEngine {
    private final float SEED = 0.0f;
    private final Vector3 VECLOCITY = new Vector3(2.0f, 2.0f, 2.0f);
    private final Vector3 VECLOCITY_VARIATION = new Vector3(1.0f, 1.0f, 1.0f);
    private final Vector3 GRAVITY = new Vector3(0.0f, 0.0f, 0.0f);
    private final float PARTICLE_SIZE = 25.0f;
    private final float PARTICLE_SIZE_DELTA = 10.5f;
    private final Vector3 COLOR = new Vector3(1.0f, 0.5f, 0.2f);
    private final int MAXPARTICLES = 300;
    Texture[] m_texture;
    int m_textureCount;
    int count = 0;
    int countTime;
    
    
    public Explo(Vector3 _origin, float elapsedTime, float scale) {
        m_origin = _origin;
        m_scale = scale;
        m_elapsedTime = elapsedTime;
        this.m_maxParticles = MAXPARTICLES;
        this.Init();
        this.Emit(5);
    }
    
    public void LoadingTexture() {
        
        m_textureCount = 4;
        m_texture = new Texture[m_textureCount];
        
        //Load resource
        //jundat
        for(int i = 0; i < m_textureCount; i ++) {
            //m_texture[i] = ResourceManagerTest.getInstance().explo[i];
            m_texture[i] = ResourceManager.getInst().getTexture("data/particle/Explo_" + i + ".png");
        }
    }
    
    static Random random = new Random();
    
    @Override
    public void InitParticle(int index) {
        float rand;
        
        m_ParticleList[index] = new Particle();
        rand = Math.abs((random.nextFloat() * 2) - 1);
        m_ParticleList[index].life = 40.5f + rand/2.0f;
        m_ParticleList[index].seed = 0.05f;
        
        m_ParticleList[index].m_Position = new Vector3();
        
        rand = ((random.nextFloat() * 2) - 1);
        m_ParticleList[index].m_Position.x = m_origin.x + rand * 1 ;
        rand = ((random.nextFloat() * 2) - 1);
        m_ParticleList[index].m_Position.y = m_origin.y + rand * 1;
        rand = ((random.nextFloat() * 2) - 1);
        m_ParticleList[index].m_Position.z = m_origin.z + rand * 1;
        System.out.println("Explo " + index + " : X : " + m_ParticleList[index].m_Position.x + "Y : " + m_ParticleList[index].m_Position.y + "Z : " + m_ParticleList[index].m_Position.z);
        
        
        m_ParticleList[index].m_velocity = new Vector3();
        
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.x - m_origin.x >= 0)
            m_ParticleList[index].m_velocity.x = VECLOCITY.x + VECLOCITY_VARIATION.x * rand;
        else 
            m_ParticleList[index].m_velocity.x = -(VECLOCITY.x + VECLOCITY_VARIATION.x * rand);
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.y - m_origin.y >= 0)
            m_ParticleList[index].m_velocity.y = VECLOCITY.y + VECLOCITY_VARIATION.y * rand;
        else 
            m_ParticleList[index].m_velocity.y = -(VECLOCITY.y + VECLOCITY_VARIATION.y * rand);
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.z - m_origin.z >= 0)
            m_ParticleList[index].m_velocity.z = VECLOCITY.z + VECLOCITY_VARIATION.z * rand;
        else 
            m_ParticleList[index].m_velocity.z = -(VECLOCITY.z + VECLOCITY_VARIATION.z * rand);

        m_ParticleList[index].m_Gravity = new Vector3();
        
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.x - m_origin.x >= 0)
            m_ParticleList[index].m_Gravity.x = GRAVITY.x * rand;
        else m_ParticleList[index].m_Gravity.x = -(GRAVITY.x * rand);
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.y - m_origin.y >= 0)
            m_ParticleList[index].m_Gravity.y = GRAVITY.y * rand;
        else 
            m_ParticleList[index].m_Gravity.y = -(GRAVITY.y * rand);
        rand = Math.abs((random.nextFloat() * 2) - 1);
        if(m_ParticleList[index].m_Position.z - m_origin.z >= 0)
            m_ParticleList[index].m_Gravity.z = GRAVITY.z * rand;
        else m_ParticleList[index].m_Gravity.z = -(GRAVITY.z * rand);
        rand = Math.abs((random.nextFloat() * 2) - 1);
        m_ParticleList[index].m_size = PARTICLE_SIZE + 0.5f * rand;
        //m_ParticleList[index].m_sizeDelta = -(m_ParticleList[index].m_size / m_ParticleList[index].life);
        m_ParticleList[index].m_sizeDelta = PARTICLE_SIZE_DELTA;
        
        float tempRed,tempBlue,tempGreen,tempAlpha;
        
        tempRed = COLOR.x;
        rand = Math.abs((random.nextFloat() * 2) - 1);
        tempGreen = COLOR.y + rand * 0.3f;
        tempBlue = COLOR.z;
        tempAlpha = 0.5f;
        m_ParticleList[index].m_Color = new GLColor(tempRed,tempGreen,tempBlue,tempAlpha);
        
        tempRed = -10*(m_ParticleList[index].m_Color.red/2.0f)/m_ParticleList[index].life;
        tempGreen = -10*(m_ParticleList[index].m_Color.green/2.0f)/m_ParticleList[index].life;
        tempBlue = -10*(m_ParticleList[index].m_Color.blue/2.0f)/m_ParticleList[index].life;
        tempAlpha = -1.0f/m_ParticleList[index].life;
        m_ParticleList[index].m_ColorDelta  = new GLColor(tempRed,tempGreen,tempBlue,tempAlpha);
    }
    
    @Override
    public void Update() {

        //float m_elapsedTime = 0.05f;
        countTime++;
        if(countTime % 150 == 0)
            //this.Emit(5);
            m_isDie = true;
        for (int i = 0; i < m_numParticles;) {
            float rand = random.nextFloat();
            m_ParticleList[i].m_Position.x += m_elapsedTime * m_ParticleList[i].m_velocity.x * 2 * m_scale; //trai phai
            m_ParticleList[i].m_Position.y += m_elapsedTime * m_ParticleList[i].m_velocity.y * 2 * m_scale; // len xuong
            m_ParticleList[i].m_Position.z += m_elapsedTime * m_ParticleList[i].m_velocity.z * 2 * m_scale;// do sau

            m_ParticleList[i].m_velocity.x += m_elapsedTime * m_ParticleList[i].m_Gravity.x;
            m_ParticleList[i].m_velocity.y += m_elapsedTime * m_ParticleList[i].m_Gravity.y;
            m_ParticleList[i].m_velocity.z += m_elapsedTime * m_ParticleList[i].m_Gravity.z;

            m_ParticleList[i].life -= 15 * m_elapsedTime;
            m_ParticleList[i].m_size += m_elapsedTime * PARTICLE_SIZE_DELTA ;
            
            m_ParticleList[i].m_Color.red += m_ParticleList[i].m_ColorDelta.red * m_elapsedTime;
            m_ParticleList[i].m_Color.green += m_ParticleList[i].m_ColorDelta.green * m_elapsedTime;
            m_ParticleList[i].m_Color.blue += m_ParticleList[i].m_ColorDelta.blue * m_elapsedTime;
            m_ParticleList[i].m_Color.alpha += m_ParticleList[i].m_ColorDelta.alpha * m_elapsedTime;
            if (m_ParticleList[i].life <= 0) {
                m_ParticleList[i] = m_ParticleList[--m_numParticles];
            } else {
                i++;
            }
        }        
    }
    
    @Override
    public void Draw(GL gl, float Y) {
        gl.glEnable(GL.GL_TEXTURE);
        gl.glDepthMask(false);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        
        
        
        for (int i = 0; i < m_numParticles; ++i) {
            int temp = i % m_textureCount;
            m_texture[temp].enable();
            m_texture[temp].bind();
            gl.glPushMatrix();
            
            gl.glTranslatef(m_ParticleList[i].m_Position.x, m_ParticleList[i].m_Position.y, m_ParticleList[i].m_Position.z);
            gl.glRotatef(Y, 0, 1, 0);
            gl.glScalef(m_scale, m_scale, m_scale);
            gl.glBegin(GL.GL_QUADS);
            
            float size = m_ParticleList[i].m_size / 2;
            m_ParticleList[i].m_Color.set(gl);
            
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-size, -size, 0);

            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(size, -size, 0);

            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(size, size, 0);

            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-size, size, 0);
            
            gl.glEnd();
            gl.glPopMatrix();
        }
        
        gl.glDisable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthMask(true);
    }
}
