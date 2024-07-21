package com.woodenscalpel.common.item.abstractwand;

public interface WorldModifierInterface {
    void setBlockQueue();
    void pushBlockQueue();
    void popBlockQueue();
    void processBlock();
}
