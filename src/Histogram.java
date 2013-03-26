import java.awt.*;
import java.awt.image.*;


public class Histogram {
    int red[];
    int green[];
    int blue[];
    int summed[];

    int maxCount = 0;

    int N = 256;

    public Histogram()
    {
        red = new int[N];
        green = new int[N];
        blue = new int[N];
        summed = new int[N];
    }

    public void build(Image src)
    {
        for(int i = 0; i < N; i++)
        {
            red[i] = green[i] = blue[i] = 0;
        }
        maxCount = 0;

        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int src_pixels[] = new int[w * h];
        PixelGrabber grabber = new PixelGrabber(src, 0, 0,
                             w, h, src_pixels, 0, w);
        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                int r = (src_pixels[y * w + x] & 0x00ff0000) >> 16;
                int g = (src_pixels[y * w + x] & 0x0000ff00) >> 8;
                int b = (src_pixels[y * w + x] & 0x000000ff) >> 0;

                red[r]++;
                green[g]++;
                blue[b]++;
                summed[(r+g+b)/3]++;

                if(r+g+b == 0)
                    continue;

                if(red[r] > maxCount) maxCount = red[r];
                if(green[g] > maxCount) maxCount = green[g];
                if(blue[b] > maxCount) maxCount = blue[b];
                // if(summed[(r+g+b)/3] > maxCount) maxCount = summed[(r+g+b)/3];
            }
        }
        System.out.println("maxCount: " + maxCount);
        maxCount = w * h / 16;
        System.out.println("setting maxCount to " + maxCount);
    }
}
