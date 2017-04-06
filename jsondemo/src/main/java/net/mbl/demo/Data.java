package net.mbl.demo;

public class Data{
    
    CPLY cply;

    public Data() {
    }
    public Data(CPLY cply) {
      this.cply = cply;
    }
    public void setCply(CPLY cply) {
      this.cply = cply;
    }

    public CPLY getCply() {
      return cply;
    }
  }