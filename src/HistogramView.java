import java.awt.*;
import java.awt.image.*;

public class HistogramView extends Canvas
{
    Histogram histogram;

    Image img;

    int h = 200;
    int N = 256;

    public HistogramView(Histogram histogram)
    {
        this.histogram = histogram;
        this.setSize(N, h);
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, N, h);
        
        if(img != null)
            g.drawImage(img, 0, 0, null);
    }

    public void updateData()
    {
        int data[] = new int[N * h];
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < h; j++)
            {
                int k = 1;
                if(histogram.red[i] * h / histogram.maxCount * k > j)
                    data[(h - j - 1) * N + i] |= 0xffff0000;
                if(histogram.green[i] * h / histogram.maxCount * k > j)
                    data[(h - j - 1) * N + i] |= 0xff00ff00;
                if(histogram.blue[i] * h / histogram.maxCount * k > j)
                    data[(h - j - 1) * N + i] |= 0xff0000ff;
                // if(histogram.summed[i] * h / histogram.maxCount * k > j)
                //    data[(h - j - 1) * N + i] |= 0xff7f7f7f;
            }
        }
        img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(N, h, data, 0, N));
    }
}
