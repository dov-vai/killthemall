package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// /**
//  * @author oguz
//  */
// public class Bullet implements GameObject {

//     private Vector2 position;
//     private float size;
//     private float angle;
//     private boolean visible = true;

//     private float ttlCounter = 0;
//     private int id;
//     private Rectangle boundRect;

//     public Bullet(float x, float y, float size, float angle, int id) {
//         this.position = new Vector2(x, y);
//         this.size = size;
//         this.angle = angle;
//         this.id = id;

//         this.boundRect = new Rectangle(x, y, size, size);
//     }

//     @Override
//     public void update(float deltaTime) {

//         float speed = deltaTime * 800;
//         this.ttlCounter += deltaTime;

//         float dx = (float) Math.cos(angle);
//         float dy = (float) Math.sin(angle);

//         position.y -= speed * dy;
//         position.x += speed * dx;
//         if (ttlCounter > 2) {
//             visible = false;
//             ttlCounter = 0;
//         }

//         this.boundRect.x = position.x;
//         this.boundRect.y = position.y;
//     }

//     public Vector2 getPosition() {
//         return position;
//     }

//     public void setPosition(Vector2 position) {
//         this.position = position;
//     }

//     public float getSize() {
//         return size;
//     }

//     public void setSize(float size) {
//         this.size = size;
//     }

//     @Override
//     public boolean isVisible() {
//         return visible;
//     }

//     @Override
//     public void setVisible(boolean visible) {
//         this.visible = visible;
//     }

//     public int getId() {
//         return id;
//     }

//     public Rectangle getBoundRect() {

//         return boundRect;
//     }

// }

public interface Bullet {

    void update(float deltaTime);
    Vector2 getPosition();
    float getSize();
    boolean isVisible();
    void setVisible(boolean visible);
    int getId();
    Rectangle getBoundRect();

    public static class StandardBullet implements Bullet {
        
        private Vector2 position;
        private float size;
        private float angle;
        private boolean visible = true;
        private float ttlCounter = 0;
        private int id;
        private Rectangle boundRect;
        
        private static final int SPEED = 800;

        public StandardBullet(float x, float y, float size, float angle, int id) {
            this.position = new Vector2(x, y);
            this.size = size;
            this.angle = angle;
            this.id = id;
            this.boundRect = new Rectangle(x, y, size, size);
        }

        @Override
        public void update(float deltaTime) {
            float speed = deltaTime * SPEED;
            this.ttlCounter += deltaTime;

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            position.y -= speed * dy;
            position.x += speed * dx;
            
            if (ttlCounter > 2) {
                visible = false;
                ttlCounter = 0;
            }

            this.boundRect.x = position.x;
            this.boundRect.y = position.y;
        }

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getSize() {
            return size;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Rectangle getBoundRect() {
            return boundRect;
        }
    }

    public static class FastBullet implements Bullet {
        
        private Vector2 position;
        private float size;
        private float angle;
        private boolean visible = true;
        private float ttlCounter = 0;
        private int id;
        private Rectangle boundRect;
        
        private static final int SPEED = 1200;  // faster speed

        public FastBullet(float x, float y, float size, float angle, int id) {
            this.position = new Vector2(x, y);
            this.size = size;
            this.angle = angle;
            this.id = id;
            this.boundRect = new Rectangle(x, y, size, size);
        }

        @Override
        public void update(float deltaTime) {
            float speed = deltaTime * SPEED;
            this.ttlCounter += deltaTime;

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            position.y -= speed * dy;
            position.x += speed * dx;
            
            if (ttlCounter > 1.5f) {  // dies faster
                visible = false;
                ttlCounter = 0;
            }

            this.boundRect.x = position.x;
            this.boundRect.y = position.y;
        }

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getSize() {
            return size;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Rectangle getBoundRect() {
            return boundRect;
        }
    }

    public static class HeavyBullet implements Bullet {
        
        private Vector2 position;
        private float size;
        private float angle;
        private boolean visible = true;
        private float ttlCounter = 0;
        private int id;
        private Rectangle boundRect;
        
        private static final int SPEED = 500;  // slower speed

        public HeavyBullet(float x, float y, float size, float angle, int id) {
            this.position = new Vector2(x, y);
            this.size = size * 1.5f;  // bigger size
            this.angle = angle;
            this.id = id;
            this.boundRect = new Rectangle(x, y, this.size, this.size);
        }

        @Override
        public void update(float deltaTime) {
            float speed = deltaTime * SPEED;
            this.ttlCounter += deltaTime;

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            position.y -= speed * dy;
            position.x += speed * dx;
            
            if (ttlCounter > 3) {  // lives longer
                visible = false;
                ttlCounter = 0;
            }

            this.boundRect.x = position.x;
            this.boundRect.y = position.y;
        }

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getSize() {
            return size;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Rectangle getBoundRect() {
            return boundRect;
        }
    }

}
