

import java.awt.*;
import java.awt.image.*;
import static java.awt.Color.*;

public class ConvertedImage extends java.awt.Canvas
{
    Image src;
    Image dst;
    Filter filter;
    ConvertedImage(Filter filter)
    {
        this.filter = filter;
        setSize(200, 400);
    }

    @Override
    public void paint(Graphics g)
    {
        if(dst == null)
        {
            g.setColor(Color.red);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        else
        {
            g.drawImage(dst, 0, 0, null);
        }
    }

    void setSource(Image src) {
        this.src = src;
        if(src != null)
            this.setSize(src.getWidth(null), src.getHeight(null));
        else
            setSize(200, 400);
    }

    void runFilter() {
        if(src == null)
            return;

        int w = src.getWidth(null);
        int h = src.getHeight(null);

        int src_pixels[] = new int[w * h];
        int dst_pixels[] = new int[w * h];
        PixelGrabber grabber =
            new PixelGrabber(src, 0, 0,
                             w, h, src_pixels, 0, w);
        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
        filter.apply(src_pixels, dst_pixels, w, h);
        dst = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, dst_pixels, 0, w));
    }
}