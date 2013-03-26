

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

public class FilterView  implements
        MouseListener, ActionListener, MouseMotionListener, KeyListener
{
    JFrame frame;
    SourceImage src;
    ConvertedImage dst;
    ConvolutionFilter convFilter;
    CustomFilter customFilter;
    LinearColorFilter linearColorFilter;
    JButton button;
    JComboBox dropdown;
    Histogram histSrc;
    Histogram histDst;
    HistogramView histSrcView;
    HistogramView histDstView;
    FilterDialog dialog;

    public static void main(String argv[])
    {
        new FilterView();
    }

    public FilterView()
    {
        frame = new JFrame("FilterView");
        frame.setLayout(new FlowLayout());
        Panel p1 = new Panel();
        Panel p2 = new Panel();
        Panel p3 = new Panel();
        Panel p4 = new Panel();
        src = new SourceImage();
        src.addMouseListener(this);
        src.addMouseMotionListener(this);
        src.addKeyListener(this);

        convFilter = new ConvolutionFilter(3);
        customFilter = new CustomFilter();
        linearColorFilter = new LinearColorFilter();

        String entries[] = { "Convolution-Filter", "Custom-Filter", "LinearColorFilter" };
        dropdown = new JComboBox(entries);
        dropdown.addActionListener(this);
        // dropdown.setEnabled(false);
        
        p4.add(dropdown);

        button = new JButton();
        button.setText("Edit current Filter");
        button.addActionListener(this);
        // button.setEnabled(false);
        p4.add(button);
        p4.setLayout(new GridLayout(2, 1));

        dst = new ConvertedImage(convFilter);
        dst.addMouseMotionListener(this);
        src.addKeyListener(this);

        histSrc = new Histogram();
        histSrcView = new HistogramView(histSrc);
        histDst = new Histogram();
        histDstView = new HistogramView(histDst);

        p3.setLayout(new GridLayout(2, 1));
        Panel p5 = new Panel();
        p5.setLayout(new BorderLayout());
        p5.add(new Label("Histogramm original"), BorderLayout.NORTH);
        p5.add(histSrcView, BorderLayout.CENTER);

        Panel p6 = new Panel();
        p6.setLayout(new BorderLayout());
        p6.add(new Label("Histogramm filtered"), BorderLayout.NORTH);
        p6.add(histDstView, BorderLayout.CENTER);

        p3.add(p5);
        p3.add(p6);

        p1.add(src);
        p2.add(dst);
        frame.add(p1);
        frame.add(p2);
        frame.add(p4);
        frame.add(p3);

        frame.pack();
        frame.setVisible(true);
    }

    public void updateSrc()
    {
        histSrc.build(src.src);
        histSrcView.updateData();
        src.repaint();
        histDstView.repaint();
    }
    public void updateDst()
    {
        dst.runFilter();
        histDst.build(dst.dst);
        histDstView.updateData();
        dst.repaint();
        histDstView.repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == button)
        {
            // System.out.println("Button has been clicked");
            if(dialog == null && dst != null && dst.filter == convFilter)
                dialog = new ConvolutionFilterDialog(convFilter, this);
            else if(dialog == null && dst != null && dst.filter == linearColorFilter)
            {
                dialog = new LinearFilterDialog(linearColorFilter, this);
            }
        }
        if (ae.getSource() == dropdown)
        {
            switch (dropdown.getSelectedIndex())
            {
                case 0:
                    dst.filter = convFilter;
                    button.setEnabled(true);
                    break;
                case 1:
                    dst.filter = customFilter;
                    button.setEnabled(false);
                    break;
                case 2:
                    dst.filter = linearColorFilter;
                    button.setEnabled(true);
                    break;
            }
            if (dialog != null)
            {
                dialog.setVisible(false);
                dialog = null;
            }
            updateDst();
        }
    }


    public void mouseClicked(MouseEvent me) {
        // System.out.println("Click-Count: " + me.getClickCount());
        if(me.getClickCount() == 2 && me.getSource() == src)
        {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(frame);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                // System.out.println("You chose to open this file: " +
                //    chooser.getSelectedFile().getName());
                File sourceimage = chooser.getSelectedFile().getAbsoluteFile();
                Image img = null;
                try
                {
                    img = ImageIO.read(sourceimage);
                    while(img.getWidth(null) < 1)
                        Thread.sleep(10);
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
                src.setImage(img);
                dst.setSource(src.getImage());
                frame.pack();
                updateSrc();
                updateDst();
            }
        }
    }

    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

    public void mouseDragged(MouseEvent me)
    {
    }

    public void mouseMoved(MouseEvent me)
    {
        dst.filter.setX(me.getX());
        dst.filter.setY(me.getY());
        // System.out.println("moved to " + me.getX() + ", " + me.getY());
    }

    public void keyTyped(KeyEvent ke)
    {
        if(ke.getKeyChar() == ' ')
        {
            System.out.println("update");
            updateDst();
        }
    }

    public void keyPressed(KeyEvent ke)
    {
    }

    public void keyReleased(KeyEvent ke)
    {
    }
}
