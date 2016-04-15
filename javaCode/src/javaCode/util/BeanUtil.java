package javaCode.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
	
	/**
	 * 将对象属性转成Map对象返回
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> describe(Object obj) {
		if (obj instanceof Map)
			return null;
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		PropertyDescriptor[] descriptors = getPropertyDescriptors(obj.getClass());
		
		for(int i = 0; i < descriptors.length; i++ ) {
			String name = descriptors[i].getName();
			Method readMethod = descriptors[i].getReadMethod();
			
			if (readMethod != null) {
				try {
					map.put(name, readMethod.invoke(obj, new Object[]{}));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
		BeanInfo beanInfo = null;
		
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			return (new PropertyDescriptor[0]);
		}
		
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		
		if (descriptors == null) {
			descriptors = new PropertyDescriptor[0];
		}
		
		return descriptors;
	}
}
