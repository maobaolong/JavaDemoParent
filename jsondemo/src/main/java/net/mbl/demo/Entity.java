package net.mbl.demo;

import java.util.List;

/**
 * net.mbl.demo <br>
 * <p>
 * Copyright: Copyright (c) 17-4-6 下午2:17
 * <p>
 * Company: 京东
 * <p>
 *
 * @author maobaolong@jd.com
 * @version 1.0.0
 */
public class Entity {
  public Entity(){}
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  private int code;
  private boolean success;
  private Data data;

  
  
  
 
}
