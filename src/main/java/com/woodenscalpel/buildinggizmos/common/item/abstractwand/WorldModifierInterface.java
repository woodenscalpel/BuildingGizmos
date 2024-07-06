package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

public interface WorldModifierInterface {
    void setBlockQueue();
    void pushBlockQueue();
    void popBlockQueue();
    void processBlock();
}
