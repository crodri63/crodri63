//
// GUI :: Responsible for displaying the interface for the user. Utilizes all other classes
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import org.xml.sax.SAXException;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class GUI extends JFrame
{
    private JButton browse_button_1_txt;
    private JButton browse_button_2_txt;
    private JButton browse_button_3_txt;
    private JButton browse_button_4_xml;
    private JTextField prime_1_txtfield;
    private JTextField prime_2_txtfield;
    private JTextField rsc_directory_txtfield;
    private JTextField block_size_txt;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField out_public_key_txtfield;
    private JTextField out_private_key_txtfield;
    private JTextField blocking_output_txtfield;
    private JTextField crypt_output_txtfield;
    private JRadioButton auto_generate_prime_radio;


    public static void main(String[] args)
    {
        try
        {
            GUI frame = new GUI();
            frame.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//end main()


    public GUI() throws RedException, IOException, NumberFormatException
    {
        setResizable(false);
        setTitle("RSA Encryption Tool - CS342");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 433);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);


        JRadioButton user_enters_primes_radio = new JRadioButton("Enter Prime #'s");
        user_enters_primes_radio.addActionListener(arg0 -> {
            prime_1_txtfield.setEnabled(true);
            prime_2_txtfield.setEnabled(true);
            rsc_directory_txtfield.setEnabled(false);
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(user_enters_primes_radio);
        user_enters_primes_radio.setBounds(8, 70, 117, 25);
        contentPane.add(user_enters_primes_radio);


        JButton create_keys_jbutton = new JButton("Create Keys");
        create_keys_jbutton.setToolTipText("Creates keys in the working directory");
        create_keys_jbutton.setBounds(326, 117, 111, 25);
        create_keys_jbutton.addActionListener(e -> {
            try
            {
                if (user_enters_primes_radio.isSelected())
                {
                    if (Objects.equals(prime_1_txtfield.getText(), "") ||
                        Objects.equals(prime_2_txtfield.getText(), ""))
                        throw new RedException("One or more prime number fields are empty. Enter both prime " +
                                               "numbers.");
                    else
                        new KeyGen(Integer.parseInt(prime_1_txtfield.getText()),
                                Integer.parseInt(prime_2_txtfield.getText()),
                                out_public_key_txtfield.getText() + ".xml",
                                out_private_key_txtfield.getText() + ".xml");
                }
                else if (auto_generate_prime_radio.isSelected())
                {
                    if (Objects.equals(rsc_directory_txtfield.getText(), ""))
                        throw new RedException("Prime Number .rsc not specified. Browse for one or enter a file " +
                                               "name.");

                    new KeyGen(rsc_directory_txtfield.getText(),
                            out_public_key_txtfield.getText() + ".xml",
                            out_private_key_txtfield.getText() + ".xml");
                }
                action_success("Created Keys Successfully.");
            }
            catch (IOException | RedException | NumberFormatException ignored)
            {
            }
        });

        contentPane.setLayout(null);
        contentPane.add(create_keys_jbutton);


        JButton block_file_jbutton = new JButton("Block File");
        block_file_jbutton.addActionListener(e -> {
            try
            {
                if (Objects.equals(textField_3.getText(), ""))
                    throw new RedException("Input Text File field is empty. Browse for a file.");
                else if (Objects.equals(block_size_txt.getText(), ""))
                    throw new RedException("Block size field is empty. Enter a block size.");
                else if (Objects.equals(blocking_output_txtfield.getText(), ""))
                    throw new RedException("Output Text File field is empty. Enter the output file name.");
                else
                {
                    new RSAblocker(textField_3.getText(),
                            Integer.parseInt(block_size_txt.getText()),
                            blocking_output_txtfield.getText() + ".txt");

                    action_success("File blocked successfully.");
                }
            }
            catch (RedException | IOException ignored)
            {
            }
        });

        block_file_jbutton.setBounds(326, 186, 111, 25);
        contentPane.add(block_file_jbutton);

        JButton unblock_file_jbutton = new JButton("Unblock File");
        unblock_file_jbutton.addActionListener(e -> {
            try
            {
                if (Objects.equals(textField_3.getText(), ""))
                    throw new RedException("Input Text File field is empty. Browse for a file.");
                else
                {
                    new RSAblocker(textField_3.getText(),
                            blocking_output_txtfield.getText() + ".txt");

                    action_success("File unblocked successfully.");
                }
            }
            catch (RedException | IOException ignored)
            {
            }
        });

        unblock_file_jbutton.setBounds(326, 224, 111, 25);
        contentPane.add(unblock_file_jbutton);

        JButton encrypt_decrypt_jbutton = new JButton("<HTML>Encrypt/ <p> \r\nDecrypt </HTML>");
        encrypt_decrypt_jbutton.setToolTipText("Encrypt or Decrypt based on values found in key file");
        encrypt_decrypt_jbutton.addActionListener(e -> {
            try
            {
                if (Objects.equals(textField_4.getText(), ""))
                    throw new RedException("Input Text File field is empty. Browse for a file.");
                if (Objects.equals(textField_5.getText(), ""))
                    throw new RedException("Key File field is empty. Browse for a file.");
                else
                {
                    RSAcrypter rsa_crypter = new RSAcrypter(textField_4.getText(),
                            textField_5.getText(),
                            crypt_output_txtfield.getText() + ".txt");

                    if (rsa_crypter.doing_encryption)
                        action_success("**Public Key detected**\nFile encrypted successfully.");
                    else action_success("**Private Key detected**\nFile decrypted successfully.");
                }
            }
            catch (RedException | SAXException | ParserConfigurationException | IOException ignored)
            {
            }
        });
        encrypt_decrypt_jbutton.setBounds(326, 295, 111, 41);
        contentPane.add(encrypt_decrypt_jbutton);

        browse_button_1_txt = new JButton("...");
        browse_button_1_txt.addActionListener(this::handleBrowse);
        browse_button_1_txt.setToolTipText("Browse for .rsc file containing prime numbers");
        browse_button_1_txt.setBounds(148, 35, 32, 23);
        contentPane.add(browse_button_1_txt);

        prime_1_txtfield = new JTextField();
        prime_1_txtfield.setToolTipText("");
        prime_1_txtfield.setBounds(49, 96, 77, 22);
        contentPane.add(prime_1_txtfield);
        prime_1_txtfield.setColumns(10);

        prime_2_txtfield = new JTextField();
        prime_2_txtfield.setBounds(49, 118, 77, 22);
        contentPane.add(prime_2_txtfield);
        prime_2_txtfield.setColumns(10);

        JLabel prime_1_label = new JLabel("p");
        prime_1_label.setBounds(27, 99, 21, 16);
        contentPane.add(prime_1_label);

        JLabel prime_2_label = new JLabel("q");
        prime_2_label.setBounds(27, 121, 21, 16);
        contentPane.add(prime_2_label);

        auto_generate_prime_radio = new JRadioButton("Auto-Generate Primes (.rsc)");
        auto_generate_prime_radio.addActionListener(e -> {
            prime_1_txtfield.setEnabled(false);
            prime_2_txtfield.setEnabled(false);
            rsc_directory_txtfield.setEnabled(true);
        });
        auto_generate_prime_radio.setSelected(true);

        buttonGroup.add(auto_generate_prime_radio);
        auto_generate_prime_radio.setBounds(8, 9, 189, 25);
        contentPane.add(auto_generate_prime_radio);

        JSeparator separator = new JSeparator();
        separator.setBounds(-5, 262, 442, 2);
        contentPane.add(separator);

        rsc_directory_txtfield = new JTextField();

        rsc_directory_txtfield.setText("prime_numbers.rsc");
        rsc_directory_txtfield.setBounds(31, 34, 116, 22);
        contentPane.add(rsc_directory_txtfield);
        rsc_directory_txtfield.setColumns(10);

        block_size_txt = new JTextField();
        block_size_txt.setBounds(32, 227, 77, 22);
        contentPane.add(block_size_txt);
        block_size_txt.setColumns(10);

        JLabel block_size_label = new JLabel("Blocking Size");
        block_size_label.setBounds(32, 208, 77, 16);
        contentPane.add(block_size_label);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(0, 155, 442, 2);
        contentPane.add(separator_1);

        JTextField authors_txtfield = new JTextField();
        authors_txtfield.setEditable(false);
        authors_txtfield.setFont(new Font("Arial", Font.PLAIN, 9));
        authors_txtfield.setText("created by Filip Variciuc and Constantino Rodriguez");
        authors_txtfield.setBounds(117, 381, 224, 22);
        contentPane.add(authors_txtfield);
        authors_txtfield.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setBounds(31, 185, 116, 22);
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        browse_button_2_txt = new JButton("...");
        browse_button_2_txt.setToolTipText("Browse for input .txt file containing a message");
        browse_button_2_txt.addActionListener(this::handleBrowse);
        browse_button_2_txt.setBounds(148, 185, 32, 23);
        contentPane.add(browse_button_2_txt);

        JLabel input_txt_file_label = new JLabel("Input Text File (.txt)");
        input_txt_file_label.setBounds(35, 169, 114, 16);
        contentPane.add(input_txt_file_label);

        JLabel select_blocked_file_label = new JLabel("Input Blocked Text File (.txt)");
        select_blocked_file_label.setBounds(31, 276, 161, 16);
        contentPane.add(select_blocked_file_label);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(31, 292, 116, 22);
        contentPane.add(textField_4);

        browse_button_3_txt = new JButton("...");
        browse_button_3_txt.setToolTipText("Browse for input .txt blocked file");
        browse_button_3_txt.addActionListener(this::handleBrowse);
        browse_button_3_txt.setBounds(148, 292, 32, 23);
        contentPane.add(browse_button_3_txt);

        JLabel select_key_file_label = new JLabel("Key File (.xml)");
        select_key_file_label.setBounds(34, 321, 92, 16);
        contentPane.add(select_key_file_label);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(31, 341, 116, 22);
        contentPane.add(textField_5);

        browse_button_4_xml = new JButton("...");
        browse_button_4_xml.addActionListener(this::handleBrowse);
        browse_button_4_xml.setToolTipText("Browse for .xml private or public key");
        browse_button_4_xml.setBounds(149, 340, 32, 23);
        contentPane.add(browse_button_4_xml);

        JLabel output_public_key_filename_label = new JLabel("Output Public Key Filename");
        output_public_key_filename_label.setBounds(216, 13, 155, 16);
        contentPane.add(output_public_key_filename_label);

        JLabel output_private_key_filename_label = new JLabel("Output Private Key Filename");
        output_private_key_filename_label.setBounds(216, 60, 161, 16);
        contentPane.add(output_private_key_filename_label);

        out_public_key_txtfield = new JTextField();
        out_public_key_txtfield.setText("pub_key");
        out_public_key_txtfield.setBounds(216, 31, 77, 22);
        contentPane.add(out_public_key_txtfield);
        out_public_key_txtfield.setColumns(10);

        out_private_key_txtfield = new JTextField();
        out_private_key_txtfield.setText("priv_key");
        out_private_key_txtfield.setBounds(216, 78, 77, 22);
        contentPane.add(out_private_key_txtfield);
        out_private_key_txtfield.setColumns(10);


        JLabel xml_label_1 = new JLabel(".xml");
        xml_label_1.setBounds(293, 34, 56, 16);
        contentPane.add(xml_label_1);

        JLabel xml_label_2 = new JLabel(".xml");
        xml_label_2.setBounds(294, 80, 56, 16);
        contentPane.add(xml_label_2);

        JLabel output_filename_2 = new JLabel("Output Filename");
        output_filename_2.setBounds(216, 276, 94, 16);
        contentPane.add(output_filename_2);

        JLabel output_filename_1 = new JLabel("Output Filename");
        output_filename_1.setBounds(216, 171, 155, 16);
        contentPane.add(output_filename_1);

        blocking_output_txtfield = new JTextField();
        blocking_output_txtfield.setText("blocking_out");
        blocking_output_txtfield.setColumns(10);
        blocking_output_txtfield.setBounds(216, 189, 77, 22);
        contentPane.add(blocking_output_txtfield);

        JLabel txt_label_1 = new JLabel(".txt");
        txt_label_1.setBounds(293, 192, 56, 16);
        contentPane.add(txt_label_1);

        crypt_output_txtfield = new JTextField();
        crypt_output_txtfield.setText("crypt_out");
        crypt_output_txtfield.setColumns(10);
        crypt_output_txtfield.setBounds(216, 292, 77, 22);
        contentPane.add(crypt_output_txtfield);

        JLabel label = new JLabel(".txt");
        label.setBounds(293, 295, 56, 16);
        contentPane.add(label);


        JOptionPane.showMessageDialog(this, "Note: to have reasonable run time, run with blocking size 2 or smaller. " +
                                            "Our implementation of HugeInteger is not optimal by any means.");
    }//end constructor


    /*Handles clicks on the browse buttons,
      and filters the type of files that the user can choose from depending on the button clicked*/
    public void handleBrowse(ActionEvent e)
    {
        JFileChooser fileChooser = new JFileChooser();

        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        FileNameExtensionFilter filter;

        if (e.getSource() == browse_button_1_txt) filter = new FileNameExtensionFilter("RSC files (.rsc)", "rsc");
        else if (e.getSource() == browse_button_4_xml) filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
        else filter = new FileNameExtensionFilter("TXT files (.txt)", "txt");

        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();

            if (e.getSource() == browse_button_1_txt)
                rsc_directory_txtfield.setText(selectedFile.getName());
            if (e.getSource() == browse_button_2_txt)
                textField_3.setText(selectedFile.getName());
            if (e.getSource() == browse_button_3_txt)
                textField_4.setText(selectedFile.getName());
            if (e.getSource() == browse_button_4_xml)
                textField_5.setText(selectedFile.getName());
        }
    }//end handleBrowse(...)


    /*Show green background on the JOptionPane indicating success*/
    private void action_success(String description)
    {
        Object paneBG = UIManager.get("OptionPane.background");
        Object panelBG = UIManager.get("Panel.background");
        UIManager.put("OptionPane.background", new Color(0, 200, 60));
        UIManager.put("Panel.background", new Color(0, 200, 60));

        JOptionPane.showMessageDialog(this, description);

        UIManager.put("OptionPane.background", paneBG);
        UIManager.put("Panel.background", panelBG);
    }//end action_success(...)
}//end class()
