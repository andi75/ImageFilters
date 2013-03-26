

import java.awt.*;
import static java.awt.Color.*;

public class SourceImage extends java.awt.Canvas
{
    Image src;

    SourceImage()
    {
        this.setSize(200, 400);
    }

    public Image getImage()
    {
        return src;
    }

    public void setImage(Image src)
    {
        this.src = src;
        if(src != null)
            this.setSize(src.getWidth(null), src.getHeight(null));
        else
            this.setSize(200, 400);
    }

    @Override
    public void paint(Graphics g)
    {
        if(src != null)
        {
            g.drawImage(src, 0, 0, null);
        }
        else
        {
            g.setColor(Color.blue);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.white);
            g.drawString("Doubleclick to load image", 20, 200);
        }
    }
}
