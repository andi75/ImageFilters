

public class ConvolutionFilter extends Filter
{

    int N = 3;
    double kernel[] = {
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0
    };
    boolean normalize = false;
    int offset;

    private int x_offset[] = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
    private int y_offset[] = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };

    public ConvolutionFilter(int width)
    {
        if(width == 3)
            return;
        else
            resize(width);
    }

    public void resize(int width)
    {
       if(width % 2 != 1)
            width += 1;

        N = width;

        kernel = new double[N * N];
        kernel[N / 2 * N + N / 2] = 1;

        x_offset = new int[N * N];
        y_offset = new int[N * N];

        for(int y = 0; y < N; y++)
        {
            for(int x = 0; x < N; x++)
            {
                x_offset[y * N + x] = (N + 1) / 2 - N + x;
                y_offset[y * N + x] = (N + 1) / 2 - N + y;
            }
        }
    }


    public void apply(int src[], int dst[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                double sum_r = 0;
                double sum_g = 0;
                double sum_b = 0;

                double weight = 0;
                for(int i = 0; i < N*N; i++)
                {
                    int src_x = (x + width + x_offset[i]) % width;
                    int src_y = (y + height + y_offset[i]) % height;
                    sum_r += kernel[i] * ((src[src_y * width + src_x] & 0x00ff0000) >> 16);
                    sum_g += kernel[i] * ((src[src_y * width + src_x] & 0x0000ff00) >> 8);
                    sum_b += kernel[i] * ((src[src_y * width + src_x] & 0x000000ff) >> 0);
                    weight += kernel[i];
                }
                if(normalize)
                {
                    dst[y * width + x] =
                        0xff000000 |
                        (int) clamp8(sum_r / weight) << 16|
                        (int) clamp8(sum_g / weight) << 8|
                        (int) clamp8(sum_b / weight) << 0;
                }
                else
                {
                dst[y * width + x] =
                        0xff000000 |
                    (int) clamp8(sum_r + offset) << 16|
                    (int) clamp8(sum_g + offset) << 8|
                    (int) clamp8(sum_b + offset) << 0;
                }
            }
        }
    }
}
