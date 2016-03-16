//
// HugeInteger :: Responsible for holding numbers bigger than MAX_INT and performing operations on these numbers
//
// <<Filip Variciuc>> <<Constantino Rodriguez>>
// U. of Illinois, Chicago
// CS342, Spring 2016
//

public class HugeInteger
{
    private int[] digits;


    // string into array of int
    public void stringhugeint(String inputnum)
    {
        int size = inputnum.length();
        digits = new int[size];

        for (int x = size - 1; x >= 0; x--)
        {
            digits[size - 1 - x] = inputnum.charAt(x) - '0';
        }
    }


    // integer into array of int
    public void integerhugeint(int inputnum)
    {
        String input;
        input = Integer.toString(inputnum);
        stringhugeint(input);

    }


    // adding two HugeInteger
    // adding same length = pass
    // bigger length + smaller length = pass
    // smaller length + bigger length = pass
    // overwrote num 1 num1.addhugint(num2)
    public HugeInteger addhugeint(HugeInteger hi)
    {
        int carry = 0;
        int size = digits.length;
        int diffsize = 1;

        // check the length of the two array
        if (hi.digits.length != size)
        {
            if (hi.digits.length > digits.length)
            {
                diffsize = hi.digits.length - size;
                expand(size, diffsize);
                size = digits.length;
            }
            else
            {
                diffsize = size - hi.digits.length;
                hi.expand(hi.digits.length, diffsize);
            }
        }

        for (int i = 0; i < size; i++)
        {
            digits[i] = digits[i] + hi.digits[i] + carry;
            if (digits[i] < 10)
            {
                carry = 0;
            }
            else
            {
                digits[i] = digits[i] - 10;
                carry = 1;
            }
        }
        if (carry > 0)
        {
            this.expand(size, diffsize);
            digits[size] = carry;
        }

        if (digits[digits.length - 1] == 0)
        {
            int i = 1;
            int offset = 0;
            while (digits[digits.length - 1 - i] == 0)
            {
                offset += 1;
                i++;
            }
            decrease(digits.length - 1, -offset);

        }
        return this;
    }


    // subtracting
    // only work for num1 >= num2
    //num1- num2 num1.subtracthugeint(num2)
    // overwrote num1
    public HugeInteger subtracthugeint(HugeInteger hi)
    {
        int borrow = 0;
        int size = digits.length;
        int diffsize;

        if (this.isequalto(hi))
        {
            filledwithzero(1);
            return this;

        }

        if (islessthan(hi))
        {
            return this;
        }
        if (size != hi.digits.length)
        {
            diffsize = size - hi.digits.length;
            hi.expand(hi.digits.length, diffsize);
        }
        for (int i = 0; i < size; i++)
        {
            digits[i] -= hi.digits[i] + borrow;
            if (digits[i] >= 0)
            {
                borrow = 0;
            }
            else
            {
                digits[i] += 10;
                borrow = 1;
            }
        }
        // get rid of leading zeroes
        if (digits[size - 1] == 0)
        {
            int i = 1;
            int offset = 0;
            while (digits[size - i] == 0)
            {
                offset += 1;
                i++;
            }
            decrease(size, -offset);
        }
        // get rid of leading zeroes for hi
        if (hi.digits[hi.digits.length - 1] == 0)
        {
            int i = 1;
            int offset = 0;
            while (hi.digits[hi.digits.length - i] == 0)
            {
                offset += 1;
                i++;
            }
            hi.decrease(hi.digits.length, -offset);
        }

        return this;
    }


    // multply two numbers
    //
    public HugeInteger timeshugeint(HugeInteger hi)
    {
        int carry;
        int result;
        int finalsize = (digits.length + hi.digits.length);
        HugeInteger finalproduct = new HugeInteger();


        finalproduct.filledwithzero(finalsize);
        int k = 0;
        for (int i = 0; i < digits.length; i++)
        {
            for (int j = 0; j < hi.digits.length; j++)
            {
                result = (digits[i] * hi.digits[j]);
                if (result < 10)
                {
                    carry = 0;
                }
                else
                {
                    carry = leftover(result) / 10;
                    result = result - leftover(result);
                }

                finalproduct.digits[j + k] += result;
                finalproduct.digits[j + k + 1] += carry;

                if (finalproduct.digits[j + k] >= 10)
                {
                    carry = leftover(finalproduct.digits[j + k]) / 10;
                    finalproduct.digits[j + k] = finalproduct.digits[j + k] - leftover(finalproduct.digits[j + k]);
                    finalproduct.digits[j + k + 1] += carry;
                }
                if (finalproduct.digits[j + k + 1] >= 10)
                {
                    carry = leftover(finalproduct.digits[j + k + 1]) / 10;
                    finalproduct.digits[j + k + 1] = finalproduct.digits[j + k + 1] - leftover(
                            finalproduct.digits[j + k + 1]);
                    finalproduct.digits[j + k + 2] += carry;
                }

            }
            k++;
        }
        if (finalproduct.digits[finalsize - 1] == 0)
        {
            int i = 1;
            int offset = 0;
            while (finalproduct.digits[finalsize - i] == 0)
            {
                offset += 1;
                i++;
            }
            finalproduct.decrease(finalsize, -offset);

        }
        return finalproduct;
    }


    // return (a / b) = c  == a.dividehugeint(b)
    // a will be overwritten as a reminder from division
    // c is quotient
    public HugeInteger dividehugeint(HugeInteger hi)
    {
        HugeInteger result = new HugeInteger();
        HugeInteger addone = new HugeInteger();
        addone.integerhugeint(1);
        result.filledwithzero(1);

        while (ismorethan(hi) || isequalto(hi))
        {
            subtracthugeint(hi);
            result.addhugeint(addone);

        }
        return result;
    }


    // return a % b a.modhugeint(b)
    // a will be overwritten
    public HugeInteger modhugeint(HugeInteger hi)
    {

        while (ismorethan(hi) || isequalto(hi))
        {
            subtracthugeint(hi);

        }
        return this;
    }


    // return -> is a < b  ? a.islessthan(b)
    public boolean islessthan(HugeInteger hi)
    {
        if (isequalto(hi))
        {
            return false;
        }

        if (digits.length < hi.digits.length)
        {
            return true;
        }
        if (digits.length > hi.digits.length)
        {
            return false;
        }
        int size1 = digits.length;
        for (int i = size1 - 1; i >= 0; i--)
        {
            if (digits[i] < hi.digits[i])
            {
                return true;
            }
            if (digits[i] > hi.digits[i])
            {
                return false;
            }

        }
        return true;
    }


    // return -> is a > b  ? a.islessthan(b)
    public boolean ismorethan(HugeInteger hi)
    {
        if (isequalto(hi))
        {
            return false;
        }

        return !this.islessthan(hi);
    }


    //check if a == b
    //a.isequalto(b)
    public boolean isequalto(HugeInteger hi)
    {
        int size1 = digits.length;
        int size2 = hi.digits.length;
        if (size1 != size2)
        {
            return false;
        }
        for (int i = 0; i < size1; i++)
        {
            if (digits[i] != hi.digits[i])
            {
                return false;
            }
        }
        return true;
    }


    // grow the array
    public void expand(int oldsize, int changeinsize)
    {
        int[] temp = new int[oldsize + changeinsize];
        System.arraycopy(digits, 0, temp, 0, oldsize);
        digits = temp;
    }


    // make array smaller
    public void decrease(int oldsize, int changesize)
    {
        int[] temp = new int[oldsize + changesize];
        System.arraycopy(digits, 0, temp, 0, oldsize + changesize);
        digits = temp;
    }


    // display number
    public void displaynum()
    {
        System.out.println("this hugint contain");
        int size = digits.length;
        for (int x = 0; x < size; x++)
        {
            System.out.println(x + " position is . " + digits[x]);
        }
    }


    // display number
    public void display()
    {
        int size = digits.length;
        String name;
        name = "";
        for (int x = size - 1; x >= 0; x--)
        {
            name += digits[x];
        }
        System.out.println(name);
    }


    public String string()
    {
        int size = digits.length;
        String name;
        name = "";
        for (int x = size - 1; x >= 0; x--)
        {
            name += digits[x];
        }
        return name;
    }


    // filled a array of any size with zero
    public void filledwithzero(int arraysize)
    {
        digits = new int[arraysize];
        for (int i = 0; i < arraysize; i++)
        {
            digits[i] = 0;
        }
    }


    // leftover is to determine how much to subtract ,
    // example : leftover(55) -> 55 - 50 = 5
    // leftover(55) return 50
    public int leftover(int num)
    {
        if (num < 10)
        {
            return 0;
        }
        if (num < 20)
        {
            return 10;
        }
        else if (num < 30)
        {
            return 20;
        }
        else if (num < 40)
        {
            return 30;
        }
        else if (num < 50)
        {
            return 40;
        }
        else if (num < 60)
        {
            return 50;
        }
        else if (num < 70)
        {
            return 60;
        }
        else if (num < 80)
        {
            return 70;
        }
        else if (num < 90)
        {
            return 80;
        }
        else if (num < 100)
        {
            return 90;
        }
        else if (num < 110)
        {
            return 100;
        }
        return 0;
    }

}
