package com.xkx.util;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CglibBeanUtil {

        public Object object = null;

        public BeanMap beanMap = null;

        public CglibBeanUtil() {
            super();
        }
        @SuppressWarnings("unchecked")
        public CglibBeanUtil(Map propertyMap) {
            this.object = generateBean(propertyMap);
            this.beanMap = BeanMap.create(this.object);
        }

        public void setValue(String property, Object value) {
            beanMap.put(property, value);
        }

        public Object getValue(String property) {
            return beanMap.get(property);
        }

        public Object getObject() {
            return this.object;
        }
        @SuppressWarnings("unchecked")
        private Object generateBean(Map propertyMap) {
            BeanGenerator generator = new BeanGenerator();
            Set keySet = propertyMap.keySet();
            for (Iterator i = keySet.iterator(); i.hasNext();) {
                String key = (String) i.next();
                generator.addProperty(key, (Class) propertyMap.get(key));
            }
            return generator.create();
        }

}
