package com.egen.weighttracker.rules;

import java.sql.Timestamp;
import java.util.Date;

import org.bson.types.ObjectId;
import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.mongodb.morphia.Datastore;

import com.egen.weighttracker.controller.MetricController;
import com.egen.weighttracker.database.MongoMorphiaDBHelper;
import com.egen.weighttracker.entity.Alerts;

@Rule (name = "weight rule" )
public class WeightRules {
    private double weight;

    @Condition
    public boolean checkWeight() {        
        return weight < MetricController.underWeight || weight > MetricController.overWeight;
    }

    @Action
    public void createAlert() throws Exception {  
    	
        System.out.println("Alert Created");        
        Alerts alert = new Alerts();
        alert.setWeight(weight);
        if(weight < MetricController.underWeight){
        	alert.setMessage("Under Weight");
        } else {
        	alert.setMessage("Over Weight");
        }
        alert.setTimestamp(new Timestamp(new Date().getTime()));
        alert.setId(new ObjectId());
        Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();	
        datastore.save(alert);
    }

    public void setInput(double weight) {
        this.weight = weight;
    }
}

