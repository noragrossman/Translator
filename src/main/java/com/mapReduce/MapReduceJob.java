// MapReduceJob.java

package com.mapReduce;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceJobException;
import com.google.appengine.tools.mapreduce.MapReduceResult;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.MapSettings;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.Marshallers;
import com.google.appengine.tools.mapreduce.inputs.ConsecutiveLongInput;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.inputs.DatastoreKeyInput;
import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;
import com.google.appengine.tools.mapreduce.outputs.InMemoryOutput;
import com.google.appengine.tools.pipeline.FutureValue;
import com.google.appengine.tools.pipeline.Job0;
import com.google.appengine.tools.pipeline.Job1;
import com.google.appengine.tools.pipeline.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
public class MapReduceJob extends Job0<Void> {
    private final String bucket;
    private final String datastoreType;
    private final int shardCount;
    private final int entities;
    private final int bytesPerEntity;
    
    public ChainedMapReduceJob(String bucket, String datastoreType, int shardCount, int entities,
      int bytesPerEntity) {
        this.bucket = bucket;
        this.datastoreType = datastoreType;
        this.shardCount = shardCount;
        this.entities = entities;
        this.bytesPerEntity = bytesPerEntity;
    }
    
    @Override
    public FutureValue<Void> run() throws Exception {
        MapSettings settings = getSettings();
        
        FutureValue<MapReduceResult<Void>> createFuture = futureCall(
            new MapJob<>(getCreationJobSpec(bytesPerEntity, entities, shardCount), settings));
        
        return futureCall(new LogResults(), createFuture);
    }
            
    private MapSettings getSettings() {
        // [START mapSettings]
        MapSettings settings = new MapSettings.Builder()
            .setWorkerQueueName("mapreduce-workers")
            .setModule("mapreduce")
            .build();
        // [END mapSettings]
        return settings;
    }
            
    private MapSpecification<Long, Entity, Void> getCreationJobSpec(int bytesPerEntity, int entities,
      int shardCount) {
        // [START mapSpec]
        MapSpecification<Long, Entity, Void> spec = new MapSpecification.Builder<>(
            new ConsecutiveLongInput(0, entities, shardCount),
            new EntityCreator(datastoreType, bytesPerEntity),
            new DatastoreOutput())
            .setJobName("Create MapReduce entities")
            .build();
        // [END mapSpec]
        return spec;
      }
}
*/