package net.mbl.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by mbl on 26/09/2017.
 */
public class Seri implements Serializable
{

  String name;
  /*transient*/ Boy boy;
  public Seri(String name){
    this.name = name;
  }
  public static void main(String[] args)
  {
    Seri st = new Seri("Tom");
     st.boy  =new Boy(st,"bbb");
    File file = new File("/tmp/seri.txt");
    try
    {
      file.createNewFile();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    try
    {
      //Student对象序列化过程
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(st);
      oos.flush();
      oos.close();
      fos.close();

      //Student对象反序列化过程
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Seri st1 = (Seri) ois.readObject();
      System.out.println("name = " + st1.getName());
      System.out.println(st1.boy.name);

      ois.close();
      fis.close();
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  String getName() {
    return this.name;
  }
}

class Boy implements Serializable{
  String name ;
  Seri mSeri;
  public Boy(Seri seri, String name) {
    this.mSeri = seri;
    this.name = name;
  }
}
