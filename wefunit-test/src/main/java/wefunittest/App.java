package wefunittest;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App{

    public static void main( String[] args ){

        System.out.println("--- --- --- Before");

        try{
            throw new AssertionError();
        }catch (Throwable e){
            System.out.println(e.getStackTrace()[0].getClassName());
        }

    }
}