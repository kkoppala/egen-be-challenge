package com.egen.weighttracker.database;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class MongoMorphiaDBHelper {	

	private static Datastore datastore =null;	
	private static MongoMorphiaDBHelper instance;

	private MongoMorphiaDBHelper() {

		Morphia morphia = new Morphia();

		morphia.mapPackage("com.egen.weighttracker.entity");

		datastore = morphia.createDatastore(new MongoClient(), "WeightTracker"); 
		datastore.getDB().dropDatabase();
		datastore.ensureIndexes();

	}

	public static MongoMorphiaDBHelper getInstance(){
		if(instance == null){
			instance = new MongoMorphiaDBHelper();
		}
		return instance;
	}

	public Datastore getDatastore() {
		return datastore;
	}
}
