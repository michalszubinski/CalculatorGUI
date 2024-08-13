import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator implements ActionListener
{
    JFrame frame;
    JTextField textfield;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[11];
    JButton[] memoryButtons = new JButton[3];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JButton negButton, modButton, sqrButton, mcrButton;
    JButton mmiButton, madButton;
    JPanel panel;

    Font mainFont = new Font("DejaVu Sans Mono", Font.BOLD, 20);
    double num1 = 0;
    double num2 = 0;
    double result = 0;
    char operator;
    double mem = 0;
    int eventCounter = 0;
    int lastMcrEvent = 0;
    boolean refreshOnNextIfNumber = false;

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Look and feel not set");
        }
        Calculator calculator = new Calculator();
    }

    Calculator()
    {
        // Instantiate the frame
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 460);
        frame.setLayout(null);

        // Instantiate text field
        textfield = new JTextField();
        textfield.setBounds(20, 20, 360, 40);
        textfield.setFont(mainFont);
        textfield.setEditable(false);

        // Instantiate the panel
        panel = new JPanel();
        panel.setBounds(40, 80, 320, 320);
        panel.setLayout( new GridLayout(6,4,10,10) );

        // Memory buttons
        mcrButton = new JButton("MCR");
        mmiButton = new JButton("M-");
        madButton = new JButton("M+");

        memoryButtons[0]  = mcrButton;
        memoryButtons[1]  = mmiButton;
        memoryButtons[2]  = madButton;

        for (int i = 0; i < 3; ++i)
        {
            memoryButtons[i].addActionListener(this);
            memoryButtons[i].setFont(mainFont);
            memoryButtons[i].setFocusable(false);
        }

        // Function buttons
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("D");
        clrButton = new JButton("C");
        negButton = new JButton("-1");
        modButton = new JButton("%");
        sqrButton = new JButton("√");

        functionButtons[0]  = addButton;
        functionButtons[1]  = subButton;
        functionButtons[2]  = mulButton;
        functionButtons[3]  = divButton;
        functionButtons[4]  = decButton;
        functionButtons[5]  = equButton;
        functionButtons[6]  = delButton;
        functionButtons[7]  = clrButton;
        functionButtons[8]  = negButton;
        functionButtons[9]  = modButton;
        functionButtons[10] = sqrButton;

        for (int i = 0; i < 11; ++i)
        {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(mainFont);
            functionButtons[i].setFocusable(false);
        }

        // Number buttons
        for (int i = 0; i < 10; ++i)
        {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(mainFont);
            numberButtons[i].setFocusable(false);
        }


        // Pannel
        // Row 1
        panel.add(mcrButton);
        panel.add(mmiButton);
        panel.add(madButton);
        panel.add(delButton);
        // Row 2
        panel.add(clrButton);
        panel.add(sqrButton);
        panel.add(modButton);
        panel.add(negButton);
        // Row 3
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(divButton);
        // Row 4
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(mulButton);
        // Row 5
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(subButton);
        // Row 6
        panel.add(numberButtons[0]);
        panel.add(decButton);
        panel.add(equButton);
        panel.add(addButton);

        // Set frame as visible
        frame.add(panel);
        frame.add(textfield);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        ++eventCounter;

        // Number buttons
        for(int i = 0; i < 10; ++i)
            if (e.getSource() == numberButtons[i])
            {
                if (refreshOnNextIfNumber)
                {
                    textfield.setText("");
                    refreshOnNextIfNumber = false;
                }
                textfield.setText(textfield.getText().concat(String.valueOf(i)));
            }

        if (refreshOnNextIfNumber) refreshOnNextIfNumber = false;


        // Function buttons
        // ADD BUTTON
        if(e.getSource() == addButton) TwoNumberFun('+');

        // SUBTRACT BUTTON
        if(e.getSource() == subButton) TwoNumberFun('-');

        // MULTIPLY BUTTON
        if(e.getSource() == mulButton) TwoNumberFun('*');

        // DIVIDE BUTTON
        if(e.getSource() == divButton) TwoNumberFun('/');

        // DECIMAL PLACE BUTTON
        if(e.getSource() == decButton) textfield.setText(textfield.getText().concat("."));

        // EQUAL BUTTON
        if(e.getSource() == equButton) equButtonFun();

        // BACKSPACE BUTTON
        if(e.getSource() == delButton) delButtonFun();

        // CLEAR BUTTON
        if(e.getSource() == clrButton) textfield.setText("");

        // NEGATIVE INVERT BUTTON
        if(e.getSource() == negButton) negButtonFun();

        // MODULO BUTTON
        if(e.getSource() == modButton) TwoNumberFun('%');

        // SQUARE ROOT BUTTON
        if(e.getSource() == sqrButton) sqrtButtonFun();

        // MCR BUTTON
        if(e.getSource() == mcrButton)
        {
            textfield.setText(String.valueOf(mem));

            int mem_int = (int)mem;
            if(Double.valueOf(mem_int) == mem)
                textfield.setText(String.valueOf(mem_int));
            else
                textfield.setText(String.valueOf(mem));

            if(lastMcrEvent + 1 == eventCounter) mem = 0;
            lastMcrEvent = eventCounter;
            refreshOnNextIfNumber = true;
        }

        // M+ BUTTON
        if(e.getSource() == madButton) memoryInsertFun('+');

        // M- BUTTON
        if(e.getSource() == mmiButton) memoryInsertFun('-');
    }

    private boolean memoryInsertFun(char argOperator)
    {
        try{
            double addToMemory = Double.parseDouble(textfield.getText());
            if (argOperator == '-') addToMemory *= -1;
            mem += addToMemory;
            textfield.setText("");
        }
        catch (Exception ex){
            return true;
        }
        return false;
    }

    private void sqrtButtonFun()
    {
        try{
            num1 = Double.parseDouble(textfield.getText());
        }
        catch (Exception ex){
            return;
        }
        num1 = Math.sqrt(num1);
        int temp_num1_int = (int)num1;
        if(Double.valueOf(temp_num1_int) == num1)
            textfield.setText(String.valueOf(temp_num1_int));
        else
            textfield.setText(String.valueOf(num1));
    }

    private boolean negButtonFun()
    {
        try{
            double temp = Double.parseDouble(textfield.getText());
            try{
                int temp_int = Integer.parseInt(textfield.getText());
                temp_int *= -1;
                textfield.setText(String.valueOf(temp_int));
            }
            catch(Exception ex){
                temp *= -1;
                textfield.setText(String.valueOf(temp));
            }
        }
        catch (Exception ex){
            return true;
        }
        return false;
    }

    private void delButtonFun()
    {
        String string = textfield.getText();
        textfield.setText("");
        for (int i = 0; i < string.length()-1; ++i)
            textfield.setText(textfield.getText() + string.charAt(i));
    }

    private boolean equButtonFun()
    {
        try{
            num2 = Double.parseDouble(textfield.getText());
        }
        catch (Exception ex){
            return true;
        }

        switch(operator)
        {
            case '+':
                result = num1+num2;
                break;
            case '-':
                result = num1-num2;
                break;
            case '*':
                result = num1*num2;
                break;
            case '/':
                if (num2 == 0){
                    result = 0;
                    JOptionPane.showMessageDialog(null,
                            "You can not divide by 0",
                            "Math error detected",
                            JOptionPane.WARNING_MESSAGE);
                }
                else result = num1/num2;
                break;
            case '%':
                int temp_num1 = (int)num1;
                int temp_num2 = (int)num2;
                if(Double.valueOf(temp_num1) != num1)
                {
                    JOptionPane.showMessageDialog(null,
                            "The first number in the expression is not an integer!",
                            "Math error detected",
                            JOptionPane.WARNING_MESSAGE);
                    result = 0;
                }
                else if(Double.valueOf(temp_num2) != num2){
                    JOptionPane.showMessageDialog(null,
                            "The second number in the expression is not an integer!",
                            "Math error detected",
                            JOptionPane.WARNING_MESSAGE);
                    result = 0;
                }
                else
                    result = num1%num2;
                break;
        }
        int temp_int = (int)result;
        if(Double.valueOf(temp_int) == result)
            textfield.setText(String.valueOf(temp_int));
        else
            textfield.setText(String.valueOf(result));
        return false;
    }

    private boolean TwoNumberFun(char argOperator)
    {
        try
        {
            num1 = Double.parseDouble(textfield.getText());
        } catch (Exception ex)
        {
            return true;
        }
        operator = argOperator;
        textfield.setText("");
        return false;
    }
}
