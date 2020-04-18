package com.xh.test.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.xh.test.base.Log;
import org.bson.BsonDocument;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MongoUtil
 * @Description:            mongo查询工具
 * @Author Sniper
 * @Date 2019/4/17 10:55
 */
public class MongoUtil {

    private static final String CLASS_NAME = MongoUtil.class.getName();
    private static MongoClient mongoClient;

    /**
     *
     * @Description:                        mongodb查询数据
     * @param dbSource						数据库连接数据源
     * @param collection				    表名
     * @param queryJson						查询json串,如:{"find":{查询条件,可为空},"projection":{返回指定字段},"sort":{排序},"skip":{分页},"limit":1}
     * @return String
     */
    public static String query(String dbSource, String database, String collection, String queryJson) {
        Log.info(CLASS_NAME, "查询表名: {},查询语句: {}", collection, queryJson);
        JSONArray array = new JSONArray();
        MongoCursor<Document> cursor = null;
        try {
            MongoDatabase db = getDatabase(dbSource, database);
            MongoCollection<Document> coll = db.getCollection(collection);
            FindIterable<Document> doc;
            JSONObject query = JSONObject.parseObject(queryJson);
            if (queryJson.contains("find")) {
                String findParams = query.getString("find");
                BsonDocument findDoc = BsonDocument.parse(findParams);
                doc = coll.find(findDoc);
            } else {
                doc = coll.find();
            }
            for (String key : query.keySet()) {
                switch (key) {
                    case "projection" :
                        String projectionParams = query.getString("projection");
                        BsonDocument projectionDoc = BsonDocument.parse(projectionParams);
                        doc = doc.projection(projectionDoc);
                    case "sort" :
                        String sortParams = query.getString("sort");
                        BsonDocument sortDoc = BsonDocument.parse(sortParams);
                        doc = doc.sort(sortDoc);
                    case "skip" :
                        int skip = query.getIntValue("skip");
                        doc = doc.skip(skip);
                    case "limit" :
                        int limit = query.getIntValue("limit");
                        doc = doc.limit(limit);
                }
            }
            cursor = doc.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                array.add(document.toJson());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeMongoClient();
        }
        return array.toJSONString();
    }

    /**
     *
     * @Description:        mongodb查询记录条数
     * @param dbSource		数据库连接数据源
     * @param collection	表名
     * @param queryJson		查询json串,如:{"find":{查询条件,可为空},"projection":{返回指定字段},"sort":{排序},"skip":{分页},"limit":1}
     * @return long			返回count数
     */
    public static long count(String dbSource, String database, String collection, String queryJson) {
        Log.info(CLASS_NAME, "查询表名: {},查询语句: {}", collection, queryJson);
        try {
            MongoDatabase db = getDatabase(dbSource, database);
            MongoCollection<Document> coll = db.getCollection(collection);
            JSONObject query = JSONObject.parseObject(queryJson);
            if (query.containsKey("find")) {
                JSONObject findParams = query.getJSONObject("find");
                BsonDocument findDoc = BsonDocument.parse(findParams.toString());
                return coll.count(findDoc);
            } else {
                return coll.count();
            }
        } finally {
            closeMongoClient();
        }
    }

    /**
     * @description:                                mongo聚合查询
     * @param dbSource                              数据库连接数据源
     * @param collection                            表名
     * @param aggregateJson                         聚合查询json串
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Sniper
     * @date 2019/4/17 11:03
     */
    public static String aggregate(String dbSource, String database, String collection, String aggregateJson) {
        Log.info(CLASS_NAME, "查询表名: {},查询语句: {}", collection, aggregateJson);
        JSONArray array = new JSONArray();
        MongoCursor<Document> cursor = null;
        try {
            MongoDatabase db = getDatabase(dbSource, database);
            MongoCollection<Document> coll = db.getCollection(collection);
            JSONArray aggregateArray = JSONArray.parseArray(aggregateJson);
            List<BsonDocument> bsonList = new ArrayList<>();
            for (Object temp : aggregateArray) {
                BsonDocument bson = BsonDocument.parse(temp.toString());
                bsonList.add(bson);
            }
            AggregateIterable<Document> docIterator = coll.aggregate(bsonList);
            cursor = docIterator.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                array.add(document.toJson());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeMongoClient();
        }
        return array.toJSONString();
    }

    /**
     * @description:    删除数据库所有表
     * @param dbSource  数据库连接数据源
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:31
     */
    public static void dropAllCollections(String dbSource, String database) {
        MongoCursor<String> cursor = null;
        try {
            MongoDatabase db = getDatabase(dbSource, database);
            MongoIterable<String> names = db.listCollectionNames();
            cursor = names.iterator();
            while (cursor.hasNext()) {
                String collectionNameTemp = cursor.next();
                db.getCollection(collectionNameTemp).drop();
                Log.info(CLASS_NAME, "Collection: " + collectionNameTemp + "has been droped!");
            }
            Log.info(CLASS_NAME, "All collection has been droped!");
        } finally {
            if (cursor != null)
                cursor.close();
            closeMongoClient();
        }
    }

    /**
     * @description:                删除单张表
     * @param dbSource              数据库连接数据源
     * @param collectionNames       表名
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:30
     */
    public static void dropCollection(String dbSource, String database, String [] collectionNames) {
        MongoDatabase db = getDatabase(dbSource, database );
        for (String collectionName : collectionNames) {
            db.getCollection(collectionName).drop();
            Log.info(CLASS_NAME, "Collection: " + collectionName + "has been droped!");
        }
    }

    /**
     *
     * @description:            创建MongoClient实例
     * @param dbSource			数据库连接数据源
     * @return MongoClient		返回MongoClient实例
     */
    private static MongoDatabase getDatabase(String dbSource, String database) {
        Log.info(CLASS_NAME, "开始连接MongoDB,当前dbSource: {}", dbSource);
        JSONObject source = JSONObject.parseObject(dbSource);
        String host = source.getString("host");
        int port = source.getIntValue("port");
        String authDB = source.getString("authDB");
        String userName = source.getString("userName");
        String password = source.getString("password");
        if(mongoClient == null) {
            ServerAddress serverAddress = new ServerAddress(host, port);
            if (userName != null && password != null) {
                MongoCredential credential = MongoCredential.createCredential(userName, authDB, password.toCharArray());
                mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
            } else {
                mongoClient = new MongoClient(serverAddress);
            }
        }
        Log.info(CLASS_NAME, "MongoDB连接成功,当前所在数据库: {}", database);
        return mongoClient.getDatabase(database);
    }

    /**
     *
     * @description:    关闭Mongo数据库连接
     * @return void
     */
    private static void closeMongoClient() {
        if(mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
