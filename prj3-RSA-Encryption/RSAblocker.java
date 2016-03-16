//
// RSAblocker :: Responsible for blocking/unblocking a .txt file
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.util.Scanner;
import java.io.*;

public class RSAblocker
{
    private String input_file_name, output_file_name;   //names of the input and output files
    private boolean performing_blocking_op;             //triggers different manipulation of the text file
    private int blocking_size;                          //blocking size if performing_blocking_op = true


    /*Constructor that calls a blocking operation, taking an input and output filename and blocking size*/
    public RSAblocker(String input_file_name, int blocking_size, String output_file_name) throws RedException, IOException
    {
        this.input_file_name = input_file_name;
        this.blocking_size = blocking_size;
        this.output_file_name = output_file_name;
        this.performing_blocking_op = true;
        do_block();
    }//end constructor


    /*Constructor that calls an unblocking operation, taking only an input and output filename*/
    public RSAblocker(String input_file_name, String output_file_name) throws RedException, IOException
    {
        this.input_file_name = input_file_name;
        this.output_file_name = output_file_name;
        this.performing_blocking_op = false;
        do_unblock();
    }//end constructor


    /*Block the text file into another text file*/
    private void do_block() throws RedException, IOException
    {
        String input_file_contents = open_and_return_file_contents(input_file_name);
        if (input_file_contents != null)
        {
            try
            {
                FileWriter out_file = new FileWriter(output_file_name);

                // check how many 0 needed to padded
                while ((input_file_contents.length() % blocking_size) != 0)
                    input_file_contents += Character.toString((char) 27);

                // get ascii - 27
                for (int block_count = 0; block_count < input_file_contents.length(); block_count += blocking_size)
                {
                    int pos = 0, y = block_count;
                    String current_line = "";

                    while (pos++ < blocking_size)
                    {
                        String outputnum;
                        int value = input_file_contents.charAt(y++) - 27;
                        if (value < 0)
                            value += 27;
                        if (value < 10)      // check for single digit to add 0 in front of digit
                            outputnum = '0' + Integer.toString(value);
                        else outputnum = Integer.toString(value);

                        current_line = outputnum + current_line;
                    }
                    out_file.write(current_line + '\n'); //write the current_line to file
                }//end for(...)
                out_file.close();
            }//end try{}
            catch (IOException ex)
            {
                throw new RedException("Error writing to file: " + output_file_name);
            }
        }//end if()
    }//end do_block(...)


    /*Unblock the text file into another text file*/
    private void do_unblock() throws RedException, IOException
    {
        String input_file_contents = open_and_return_file_contents(input_file_name);
        if (input_file_contents != null)
        {
            try
            {
                FileWriter out_file = new FileWriter(output_file_name);
                String current_line = "";

                for (int i = 0; i < input_file_contents.length(); i += 2)
                {
                    final int ascii_1 = Character.getNumericValue(input_file_contents.charAt(i));
                    final int ascii_2 = Character.getNumericValue(input_file_contents.charAt(i + 1));
                    if (ascii_1 != 0 || ascii_2 != 0)
                        current_line = convert_number_to_ascii(ascii_1, ascii_2) + current_line;
                }
                out_file.write(current_line);
                out_file.close();
            }//end try{}
            catch (IOException | StringIndexOutOfBoundsException e)
            {
                throw new RedException(
                        "Error writing to file: " + input_file_name +
                        "\nFile does not exist, or existing file is not blocked.");
            }
        }
    }//end do_unblock(...)


    /*Open the file named file_name and return the contents of the file in one string*/
    private String open_and_return_file_contents(String file_name) throws RedException
    {
        String read_so_far = "";
        try  // open and read in the file, storing the entire file into string line
        {
            File infile = new File(file_name);
            Scanner sc = new Scanner(infile);
            while (sc.hasNextLine())
                if (!performing_blocking_op) //lay everything out in one line
                    read_so_far = sc.nextLine() + read_so_far;
                else //break the stream into multiple lines
                {
                    read_so_far += sc.nextLine();
                    if (sc.hasNextLine())
                        read_so_far += '\n';
                }
            sc.close();
        }//end try{}
        catch (IOException ex)
        {
            throw new RedException("Error opening/reading the file: " + file_name);
        }
        return read_so_far;
    }//end open_and_return_file_contents(...)


    /*Convert the number pair x and y to an ascii value casted to char*/
    private char convert_number_to_ascii(final int x, final int y)
    {
        int value = ((x * 10) + y);
        if (value == 0 || value == 11 || value == 9 || value == 10 || value == 13)
            return (char) value;
        else return (char) (value + 27);
    }//end convert_number_to_ascii(...)
}//end class