package net.mbl.demo.stateless4j;

/**
 * 超级玛丽所有存在的状态state
 */
public enum CurrentState {
  /**
   * 初始化时小形态
   */
  SMALL,
  /**
   * 吃到一个蘑菇时的大形态
   */
  BIG,
  /**
   * 吃到花朵,可攻击形态
   */
  ATTACH,
  /**
   * godead
   */
  DEAD

}