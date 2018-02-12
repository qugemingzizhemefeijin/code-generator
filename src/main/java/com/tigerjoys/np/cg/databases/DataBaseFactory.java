package com.tigerjoys.np.cg.databases;

import java.util.HashMap;
import java.util.Map;

public class DataBaseFactory {
	
	public static DataBaseFactory factory = new DataBaseFactory();
	
	private Map<Class<?> , AbstractDataBase> dataMap = new HashMap<Class<?> , AbstractDataBase>();
	
	public static DataBaseFactory getInstance(){
		return factory;
	}
	
	public <T extends AbstractDataBase> AbstractDataBase getDatabase(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		AbstractDataBase database = dataMap.get(clazz);
		if(database == null) {
			database = clazz.newInstance();
			dataMap.put(clazz, database);
		}
		
		return database;
	}

}
