


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class ConvolutionFilterDialog extends FilterDialog implements DocumentListener, ItemListener, ActionListener {
    ConvolutionFilter filter;
    FilterView view;

    JCheckBox checkBox;
    JComboBox comboBox;

    String options[] = { "3x3", "5x5", "7x7" };
    int width[] = { 3, 5, 7 };

    ConvolutionFilterDialog(ConvolutionFilter filter, FilterView view) {
        this.filter = filter;
        this.view = view;

        JPanel kernelPanel = new JPanel();
        kernelPanel.setLayout(new GridLayout(filter.N, filter.N));
        for(int y = 0; y < filter.N; y++)
        {
            for(int x = 0; x < filter.N; x++)
            {
                JTextField tf = new JTextField();
                tf.setColumns(4);
                tf.setText(new Double(filter.kernel[y * filter.N + x]).toString());
                tf.getDocument().addDocumentListener(this);
                tf.getDocument().putProperty("index", new Integer(y * filter.N + x));
                tf.getDocument().putProperty("tf", tf);
                kernelPanel.add(tf);
            }
        }
        this.setLayout(new BorderLayout());
        this.add(kernelPanel, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));

        JPanel offsetPanel = new JPanel();
        offsetPanel.add(new JLabel("Offset:"));
        JTextField tf = new JTextField();
        tf.setColumns(4);
        tf.setText(new Integer(filter.offset).toString());
        tf.getDocument().putProperty("tf", tf);
                    offsetPanel.add(tf);
        tf.getDocument().addDocumentListener(this);
        offsetPanel.add(tf);
        optionsPanel.add(offsetPanel);

        JPanel checkboxPanel = new JPanel();
        checkBox = new JCheckBox("Normalize");
        checkBox.setSelected(filter.normalize);
        checkBox.addItemListener(this);
        checkboxPanel.add(checkBox);
        optionsPanel.add(checkboxPanel);

        JPanel comboPanel = new JPanel();
        JLabel comboLabel = new JLabel("Filter-Size:");
        comboBox = new JComboBox(options);
        for(int i = 0; i < width.length; i++)
        {
            if(filter.N == width[i])
                comboBox.setSelectedIndex(i);
        }
        
        comboBox.addActionListener(this);
        comboPanel.add(comboLabel);
        comboPanel.add(comboBox);
        optionsPanel.add(comboPanel);

        this.add(optionsPanel, BorderLayout.SOUTH);
 
        this.pack();
        this.setVisible(true);
    }

    public void insertUpdate(DocumentEvent de) { update(de); }
    public void removeUpdate(DocumentEvent de) { update(de); }
    public void changedUpdate(DocumentEvent de) { }

    void update(DocumentEvent de)
    {
        try
        {
            double d = new Double(de.getDocument().getText(0, de.getDocument().getLength())).doubleValue();
            Integer index = (Integer)de.getDocument().getProperty(new String("index"));
            if(index != null)
            {
                filter.kernel[ index.intValue() ] = d;
                // System.out.println(d);
            }
            else
            {
                filter.offset = new Integer(de.getDocument().getText(0, de.getDocument().getLength())).intValue();
            }
            JTextField tf = (JTextField) de.getDocument().getProperty(new String("tf"));
            tf.setBackground(Color.white);
            view.updateDst();

        }
        catch(Exception e)
        {
            JTextField tf = (JTextField) de.getDocument().getProperty(new String("tf"));
            tf.setBackground(Color.red);
            System.out.println(e);
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        if(ie.getSource() == checkBox)
        {
            filter.normalize = checkBox.isSelected();
            view.updateDst();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == comboBox)
        {
            int selected = ((JComboBox)(e.getSource())).getSelectedIndex();
            if(width[selected] != filter.N)
            {
                filter.resize(width[selected]);
                view.updateDst();
                new ConvolutionFilterDialog(filter, view);
                this.dispose();
            }
        }
    }
}
