//
// RSAcrypter :: Responsible for encrypting/decrypting a file by using an .xml file
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.io.*;
import java.util.Objects;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class RSAcrypter
{
    private FileWriter output_file;
    private BufferedReader input_file_reader;
    private String key_file_name;
    private HugeInteger n, exp;
    public boolean doing_encryption;        //flags true if option performed was encryption, false otherwise


    /*Constructor that takes input file name, an output file name, and a file name which contains a key*/
    public RSAcrypter(String input_file_name, String key_file_name, String output_file_name)
            throws IOException, RedException, ParserConfigurationException, SAXException
    {
        FileReader input_file = new FileReader(input_file_name);
        this.output_file = new FileWriter(output_file_name);
        this.key_file_name = key_file_name;
        this.input_file_reader = new BufferedReader(input_file);

        n = new HugeInteger();
        exp = new HugeInteger();

        read_key();
        do_encrypt_or_decrypt();
    }//end constructor


    /*Parse the key file and look for tags which signal a encryption or decryption operation*/
    private void read_key() throws ParserConfigurationException, SAXException, RedException
    {
        try
        {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new File(key_file_name));
            Element rootElement = document.getDocumentElement();
            rootElement.normalize();

            String _n = Integer.toString(get_num_from_tag("nvalue", document));
            String _exp;

            if (!Objects.equals(_exp = Integer.toString(get_num_from_tag("evalue", document)), "-1"))
                doing_encryption = true; //didn't return -1, i.e found an e-value
            else _exp = Integer.toString(get_num_from_tag("dvalue", document));

            n.stringhugeint(_n);
            exp.stringhugeint(_exp);
        }
        catch (java.io.IOException e)
        {
            throw new RedException(
                    "Unable to read key file \"" + key_file_name + "\".\n" +
                    "Technical error information: " + e.toString());
        }
    }//end read_key(...)


    /*Pull information from specified tag*/
    private int get_num_from_tag(String tagName, Document document) throws RedException
    {
        NodeList tagList = document.getElementsByTagName(tagName);
        if (tagList.getLength() != 1) return -1;
        Node tagNode = tagList.item(0);
        NodeList childList = tagNode.getChildNodes();
        Node textNode = childList.item(0);
        String valueString = textNode.getNodeValue().trim();

        try
        {
            int value = Integer.valueOf(valueString);
            if (value < 1) throw new RedException("Keyfile format error: " + tagName + " must be >0");
            return value;
        }
        catch (NumberFormatException e)
        {
            throw new RedException(
                    "Keyfile format error: expected integer in " + tagName + " but found \"" + valueString + "\"");
        }
    }//end get_num_from_tag(...)


    /*The algorithm for encrypting is the same:
     OutputNumber = (InputNumber)^exp mod n*/
    private void do_encrypt_or_decrypt()
    {
        try
        {
            for (String line = input_file_reader.readLine(); line != null; line = input_file_reader.readLine())
            {
                System.out.println("input number: " + line + "\n");
                line = convert_in_to_out(line, n, exp);
                if ((line.length() % 2 != 0) && !doing_encryption)
                    line = '0' + line;

                System.out.println("output number: " + line + "\n");
                output_file.write(line + "\n");
            }
            input_file_reader.close();
            output_file.close();
        }
        catch (Exception ex)
        {
            System.out.println("Can't open to file");
        }
    }//end do_encrypt_or_decrypt()


    /*Convert inputnumber to outputnumber using HugeInteger class*/
    public String convert_in_to_out(String inputnum, HugeInteger n, HugeInteger exp)
    {
        HugeInteger inputnumber = new HugeInteger();
        inputnumber.stringhugeint(inputnum);

        HugeInteger addone = new HugeInteger();
        addone.stringhugeint("1");

        HugeInteger outputnumber = new HugeInteger();
        outputnumber.stringhugeint("1");

        HugeInteger i = new HugeInteger();
        i.filledwithzero(1);

        while (i.islessthan(exp))
        {
            outputnumber = outputnumber.timeshugeint(inputnumber);
            outputnumber = outputnumber.modhugeint(n);
            i.addhugeint(addone);
        }
        return outputnumber.string();
    }//end convert_in_to_out(...)
}//end class