package com.xkx.util;

import java.util.List;

public class CollectionUtil {

    public static void removeAllElementFromList(List<Object> list){
        for(int index=0;index<list.size();index++){
            list.remove(index);
        }
    }
}
