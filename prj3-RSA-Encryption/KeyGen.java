//
// KeyGen :: Responsible for displaying the interface for the user. Utilizes all other classes
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

import java.io.*;
import java.util.*;

public class KeyGen
{
    private String public_key_filename, private_key_filename;    //.xml files that will hold the public/private keys
    private String rsc_filename;                                 //.rsc file that stores 30 random prime numbers
    private int p, q, n, e, d;                                   // variables used in key generation algorithm


    /*Constructor that a filename containing prime numbers from the user*/
    public KeyGen(String rsc_filename, String public_key_filename, String private_key_filename)
            throws RedException, IOException
    {
        this.rsc_filename = rsc_filename;
        this.public_key_filename = public_key_filename;
        this.private_key_filename = private_key_filename;
        get_primes_from_rsc_file();
        start();
    }//end constructor


    /*Constructor that takes p and q directly from the user*/
    public KeyGen(Integer p, Integer q, String public_key_filename, String private_key_filename)
            throws RedException, IOException
    {
        try
        {
            this.p = p;
            this.q = q;
            this.public_key_filename = public_key_filename;
            this.private_key_filename = private_key_filename;
            start();
        }
        catch (NumberFormatException e)
        {
            throw new RedException("Prime number text fields are malformed. Try again.");
        }
    }//end constructor


    /*Start method to carry out key generation*/
    private void start() throws RedException, IOException
    {
        verify_prime_numbers();
        generate_keys();
        write_keys_to_files();
    }//end start()


    /*Process and validate class members p and q*/
    private void verify_prime_numbers() throws RedException
    {
        //at this point, prime numbers have been selected, either user-specified or from file. Check them for errors
        if (!is_prime(p)) throw new RedException(p + " is not prime.  You must type a prime number.");
        if (!is_prime(q)) throw new RedException(q + " is not prime.  You must type a prime number.");
        if (p == q) throw new RedException("You may not enter the same number twice.");
        if (p * q <= 127) throw new RedException("The product of the numbers must be greater than 127.");
        if (p <= 1 || q <= 1) throw new RedException("p and q can not be negative numbers nor can they equal 1.");
    }//end verify_prime_numbers(...)


    /*Open a .rsc file (prime_numbers.rsc by default) and extract 2 different prime numbers*/
    private void get_primes_from_rsc_file() throws IOException, RedException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(rsc_filename));
            List<String> lines = new ArrayList<>(); //storing the numbers in a list
            String line;                            //current line
            while ((line = reader.readLine()) != null)
                lines.add(line);
            reader.close();
            do //all numbers have been read into the list, now get two random lines and store them into p and q
            {
                Random r = new Random();
                String randomLine1 = lines.get(r.nextInt(lines.size()));
                String randomLine2 = lines.get(r.nextInt(lines.size()));
                p = Integer.parseInt(randomLine1);
                q = Integer.parseInt(randomLine2);
            } while (p == q); //make sure p and q are different
        }
        catch (IOException e)
        {
            throw new RedException(
                    "Error reading prime numbers from " + rsc_filename + ". Ensure the file " + "exists");
        }
    }//end get_primes_from_rsc_file()


    /*Apply algorithm using p and q to create n,e,d*/
    private void generate_keys() throws RedException
    {
        n = p * q;
        int phi = (p - 1) * (q - 1);

        // look for biggest e
        int _e = n - 1;
        while (_e == phi || !are_co_prime(_e, phi))
        {
            if (_e == 1) throw new RedException("Unable to find valid value for e");
            _e--;
        }
        e = _e;

        // search for d
        int _d = 3;
        while ((e * _d) % phi != 1)
        {
            if (_d == n || _d == phi) throw new RedException("Unable to find valid value for d");
            _d++;
        }
        d = _d;
    }//end generate_keys()


    /*Write all results to a public and private key file, both in .xml format*/
    private void write_keys_to_files() throws IOException, RedException
    {
        if (public_key_filename.equalsIgnoreCase(private_key_filename))
            throw new RedException("The public and private key filenames cannot match.");

        try
        {
            FileWriter pubKeyFile = new FileWriter(public_key_filename);
            pubKeyFile.write(
                    "<rsakey>\r\n" +
                    "\t<evalue>" + Integer.toString(e) + "</evalue>\r\n" +
                    "\t<nvalue>" + Integer.toString(n) + "</nvalue>\r\n" +
                    "</rsakey>\r\n");
            pubKeyFile.close();

            FileWriter privKeyFile = new FileWriter(private_key_filename);
            privKeyFile.write(
                    "<rsakey>\r\n" +
                    "\t<dvalue>" + Integer.toString(d) + "</dvalue>\r\n" +
                    "\t<nvalue>" + Integer.toString(n) + "</nvalue>\r\n" +
                    "</rsakey>\r\n");
            privKeyFile.close();
        }
        catch (IOException e)
        {
            throw new RedException("Error writing to files in KeyGen. Ensure the file exists and is not in use.");
        }
    }//end write_keys_to_files()


    /*This method returns true if an integer n is prime, and false otherwise*/
    private boolean is_prime(int n)
    {
        for (int i = 2; i < n; ++i)
            if (n % i == 0)
                return false;
        return true;
    }//end is_prime(...)


    /*This method returns true if the largest common denominator of two integers x and y is 1, and false otherwise */
    private boolean are_co_prime(int x, int y)
    {   //Euclid's method
        while (y != 0)
        {
            int a = y;
            y = x % y;
            x = a;
        }
        return x == 1;
    }//end are_co_prime(...)
}//end class()