/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andreas
 */
public abstract class Filter {

    int qx = -1;
    int qy = -1;
    
    
    int clamp8(double d)
    {
        if(d < 0) return 0;
        if(d > 255) return 255;
        return (int) d;
    }
    
    void setX(int x) { this.qx = x; }
    void setY(int y) { this.qy = y; }
    
    public abstract void apply(int src[], int dst[], int width, int height);

}
