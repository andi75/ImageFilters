/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;

/**
 *
 * @author andreas
 */
public class CustomFilter extends Filter
{

    @Override
    public void apply(int[] src, int[] dst, int width, int height) {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                double red = ((src[y * width + x] & 0x00ff0000) >> 16) / 255.0;
                double green = ((src[y * width + x] & 0x0000ff00) >> 8) / 255.0;
                double blue = ((src[y * width + x] & 0x000000ff) >> 0) / 255.0;

                // red, green und blue liegen jetzt zwischen 0.0 und 1.0

                // TODO: Rechne mit red, green, blue, um Deinen eigenen Filter zu implementieren
                // z.B.
                // red = 1.2 * red * red
                // oder
                // double tmp = blue; blue = green; green = tmp
                // oder ...
                // red = 0; green = 0;
                Color c = new Color( (float) red, (float) green, (float) blue);
                float hsv[] = { 0, 0, 0 };
                Color.RGBtoHSB(
                        (int) (red * 255),
                        (int) (green * 255),
                        (int) (blue * 255),
                        hsv);
                
                double h = hsv[0], s = hsv[1], v = hsv[2];
                
                h += 0.5;
                
                int cRGB = Color.HSBtoRGB( (float)h, (float)s, (float)v );
                
                // TODO: convert from hsv to rgb

                dst[y * width + x] = cRGB;
                /*
                dst[y * width + x] =
                        0xff000000 |
                    (int) clamp8(red * 255) << 16|
                    (int) clamp8(green * 255) << 8|
                    (int) clamp8(blue * 255) << 0;
                    */
            }
        }
    }
}
