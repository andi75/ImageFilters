


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class LinearFilterDialog extends FilterDialog implements DocumentListener, ItemListener, ActionListener {
    LinearColorFilter filter;
    FilterView view;

    int width = 4;

    JCheckBox checkBox;
    JComboBox comboBox;
    
    LinearFilterDialog(LinearColorFilter filter, FilterView view) {
        this.filter = filter;
        this.view = view;

        JPanel kernelPanel = new JPanel();
        kernelPanel.setLayout(new GridLayout(width, width));
        for(int y = 0; y < width; y++)
        {
            for(int x = 0; x < width; x++)
            {
                JTextField tf = new JTextField();
                tf.setColumns(4);
                tf.setText(new Double(filter.m[y * width + x]).toString());
                tf.getDocument().addDocumentListener(this);
                tf.getDocument().putProperty("index", new Integer(y * width + x));
                tf.getDocument().putProperty("tf", tf);
                kernelPanel.add(tf);
            }
        }
        
        JPanel checkboxPanel = new JPanel();
        checkBox = new JCheckBox("use HSV");
        checkBox.setSelected(filter.useHSV);
        checkBox.addItemListener(this);
        checkboxPanel.add(checkBox);
        
        this.setLayout(new BorderLayout());
        this.add(kernelPanel, BorderLayout.CENTER);
        this.add(checkboxPanel, BorderLayout.SOUTH);
 
        this.pack();
        this.setVisible(true);
    }

    public void insertUpdate(DocumentEvent de) { update(de); }
    public void removeUpdate(DocumentEvent de) { update(de); }
    public void changedUpdate(DocumentEvent de) { }

    public void itemStateChanged(ItemEvent ie) {
        if(ie.getSource() == checkBox)
        {
            filter.useHSV = checkBox.isSelected();
            view.updateDst();
        }
    }
    
    void update(DocumentEvent de)
    {
        try
        {
            double d = new Double(de.getDocument().getText(0, de.getDocument().getLength())).doubleValue();
            Integer index = (Integer)de.getDocument().getProperty(new String("index"));
            if(index != null)
            {
                filter.m[ index.intValue() ] = d;
                // System.out.println(d);
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


    public void actionPerformed(ActionEvent e) {
    }
}
