
import java.awt.Color;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andreas
 */
public class LinearColorFilter extends Filter
{
    double m[] = {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };
    
    static double mCMYK[] = {
        0, -1, -1, 1,
        -1, 0, -1, 1,
        -1, -1, 0, 1,
        0, 0, 0, 1
    };
            
    public void apply(int src[], int dst[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                double cin[] = { 0, 0, 0, 1 };
                cin[0] = ((src[y * width + x] & 0x00ff0000) >> 16) / 255.0;
                cin[1] = ((src[y * width + x] & 0x0000ff00) >> 8) / 255.0;
                cin[2] = ((src[y * width + x] & 0x000000ff) >> 0) / 255.0;

                double cout[] = { 0, 0, 0, 0 };
                
                m = mCMYK;
                
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        cout[i] += cin[j] * m[i * 4 + j];
                    }
                }
                
                double red = cout[0] / cout[3];
                double green = cout[1] / cout[3];
                double blue = cout[2] / cout[3];
                
                if(x == qx && y == qy)
                {
                    System.out.println(
                            "(" + cin[0] + ", " + cin[1] + ", " + cin[2] + ") => (" +
                            red + ", " + green + ", " + blue + ")");
                }
                dst[y * width + x] =
                        0xff000000 |
                    (int) clamp8(red * 255) << 16|
                    (int) clamp8(green * 255) << 8|
                    (int) clamp8(blue * 255) << 0;
            }
        }
    }
}