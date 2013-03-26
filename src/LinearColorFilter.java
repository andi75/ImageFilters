
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
    boolean useHSV = false;
    
    double m[] = {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };
    
    static double mCMYK[] = {
        -1, 0, 0, 1,
        0, -1, 0, 1,
        0, 0, -1, 1,
        0, 0, 0, 1
    };
    
    public LinearColorFilter()
    {
        for(int i = 0; i < 16; i++)
        {
            m[i] = mCMYK[i];
        }
    }
            
    public void apply(int src[], int dst[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                float cin[] = { 0, 0, 0, 1 };
                
                
                if(useHSV)
                {
                    Color.RGBtoHSB(
                            ((src[y * width + x] & 0x00ff0000) >> 16),
                            ((src[y * width + x] & 0x0000ff00) >> 8),
                            ((src[y * width + x] & 0x000000ff) >> 0),
                            cin
                    );
                }
                else
                {
                    cin[0] = ((src[y * width + x] & 0x00ff0000) >> 16) / 255.0f;
                    cin[1] = ((src[y * width + x] & 0x0000ff00) >> 8) / 255.0f;
                    cin[2] = ((src[y * width + x] & 0x000000ff) >> 0) / 255.0f;                    
                }
                
                float cout[] = { 0, 0, 0, 0 };
                
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        cout[i] += cin[j] * m[i * 4 + j];
                    }
                }
                
                if(x == qx && y == qy)
                {
                    System.out.println(
                            "(" +
                            cin[0] + ", " + cin[1] + ", " +
                            cin[2] + ", " + cin[3] +
                            ") => (" +
                            cout[0] + ", " + cout[1] + ", " +
                            cout[2] + ", " + cout[3] + 
                            ")"
                    );
                }
                
                
                if(useHSV)
                {
                    dst[y * width + x] = Color.HSBtoRGB(
                            (cout[0] / cout[3]) - (float) Math.floor(cout[0] / cout[3]) ,
                             Math.max(0, Math.min(1, cout[1] / cout[3])) ,
                            Math.max(0, Math.min(1, cout[2] / cout[3])));
                }
                else
                {
                    double red = cout[0] / cout[3];
                    double green = cout[1] / cout[3];
                    double blue = cout[2] / cout[3];

                    dst[y * width + x] =
                            0xff000000 |
                        (int) clamp8(red * 255) << 16|
                        (int) clamp8(green * 255) << 8|
                        (int) clamp8(blue * 255) << 0;
                }
            }
        }
    }
}