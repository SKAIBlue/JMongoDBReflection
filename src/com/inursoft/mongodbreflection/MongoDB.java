package com.inursoft.mongodbreflection;


import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.oracle.javafx.jmx.json.JSONException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 2017. 1. 21..
 */
public class MongoDB {


    private MongoClient client;

    private String host;

    private int port;


    public MongoDB(String host)
    {
        this.host = host;
        port = 27017;
    }



    public MongoDB(String host, int port)
    {
        this.host = host;
        this.port = port;
    }






    public Database getDatabase(String name)
    {
        return new Database(name);
    }





    public DBCollection getCollection(String dbName, String collectionName)
    {
        return (new Database(dbName)).getCollection(collectionName);
    }




    /**
     * 데이터베이스 서버와 연결을 종료합니다.
     */
    public void disconnect()
    {
        if(client != null)
        {
            client.close();
            client = null;
        }

    }





    /**
     * 데이터베이스 서버에 연결합니다.
     */
    public void connect()
    {
        if(client == null)
        {
            client = new MongoClient(host, port);
        }
    }








    /**
     * 오브젝트가 가지는 제네릭의 타입을 가져옵니다
     * @param obj 오브젝트
     * @return 제네릭의 타입
     */
    private Class getGenericType(Object obj)
    {
        return getGenericType(obj.getClass());
    }



    /**
     * 이 타입이 가지는 제네릭의 타입을 가져옵니다
     * @param tClass 오브젝트
     * @return 제네릭의 타입
     */
    private Class getGenericType(Class tClass)
    {
        return getGenericType((ParameterizedType) tClass.getGenericSuperclass());
    }


    /**
     * 이 필드가 가지는 제네릭의 타입을 가져옵니다
     * @param field 필드
     * @return 제네릭의 타입
     */
    private Class getGenericType(Field field)
    {
        return getGenericType((ParameterizedType) field.getGenericType());
    }


    /**
     * 제네릭의 타입을 가져옵니다
     * @param pt
     * @return 제네릭의 타입
     */
    private Class getGenericType(ParameterizedType pt)
    {
        Type[] t = pt.getActualTypeArguments();
        if(t.length > 0)
        {
            if(t[0] instanceof Class)
            {
                return (Class)t[0];
            }
        }
        return null;
    }


    private boolean isInteger(String typeName)
    {
        return "Integer".equals(typeName);
    }

    private boolean isDouble(String typeName)
    {
        return "Double".equals(typeName);
    }

    private boolean isString(String typeName)
    {
        return "String".equals(typeName);
    }

    private boolean isBoolean(String typeName)
    {
        return "Boolean".equals(typeName);
    }

    private boolean isLong(String typeName)
    {
        return "Long".equals(typeName);
    }

    private boolean isObject(String typeName)
    {
        return "Object".equals(typeName);
    }



    /**
     * 필드가 기본 타입인지 검사합니다
     * @param field 필드
     * @return 기본타입일 경우 true
     */
    private boolean isDefaultType(Field field)
    {
        return isDefaultType(field.getType().getSimpleName());
    }



    /**
     * 클래스가 기본 타입인지 검사합니다
     * @param tClass 클래스
     * @return 기본 타입일 경우 true
     */
    private boolean isDefaultType(Class tClass)
    {
        return isDefaultType(tClass.getSimpleName());
    }


    /**
     * 오브젝트가 기본 타입인지 검사합니다
     * @param obj 오브젝트
     * @return 기본 타입일 경우 true
     */
    private boolean isDefaultType(Object obj)
    {
        return isDefaultType(obj.getClass());
    }





    /**
     * 클래스가 기본 타입인지 검사합니다
     * @param typeName 클래스 이름
     * @return 기본 타입일 경우 true
     */
    private boolean isDefaultType(String typeName)
    {
        return "Integer".equals(typeName) ||
                "Double".equals(typeName) ||
                "String".equals(typeName) ||
                "Boolean".equals(typeName) ||
                "Long".equals(typeName) ||
                "Date".equals(typeName) ||
                "Object".equals(typeName);
    }

    private boolean isObjectId(Field field)
    {
        return isObjectId(field.getType());
    }


    private boolean isObjectId(Class tClass)
    {
        return isObjectId(tClass.getSimpleName());
    }

    private boolean isObjectId(Object obj)
    {
        return isObjectId(obj.getClass());
    }


    private boolean isObjectId(String typeName)
    {
        return "ObjectId".equals(typeName);
    }



    /**
     * 필드가 리스트인지 검사합니다
     * @param field 필드
     * @return 리스트일 경우 true
     */
    private boolean isList(Field field)
    {
        return isList(field.getType().getSimpleName());
    }


    /**
     * 오브젝트가 리스트인지 검사합니다
     * @param obj 오브젝트
     * @return 리스트일 경우 true
     */
    private boolean isList(Object obj)
    {
        return isList(obj.getClass());
    }


    /**
     * 클래스가 리스트인지 검사합니다
     * @param tClass 클래스
     * @return 리스트일 경우 true
     */
    private boolean isList(Class tClass)
    {
        return isList(tClass.getSimpleName());
    }



    /**
     * 클래스가 리스트인지 검사합니다
     * @param typeName 클래스 이름
     * @return 리스트일 경우 true
     */
    private boolean isList(String typeName)
    {
        return "List".equals(typeName) || "ArrayList".equals(typeName);
    }


    /**
     * 모든 멤버 변수를 도큐먼트로 만듭니다.
     * @param o
     * @return
     */
    public Document toDocument(Object o)
    {
        Class tClass = o.getClass();
        Document root = new Document();
        Field[] fields = tClass.getFields();

        for(int i = 0 ; i < fields.length ; i+=1)
        {
            Field field = fields[i];
            String name = field.getName();

            try {
                Object val = field.get(o);
                if(isDefaultType(field) || isObjectId(field))
                {
                    root.put(name, val);
                }
                else if(isList(field))
                {
                    List list = (List)val;
                    List<Document> documents = new ArrayList();
                    for(int j = 0 ; j < list.size(); j+=1)
                    {
                        Object listValue = list.get(j);
                        documents.add(toDocument(listValue));
                    }
                    root.put(name, documents);
                }
                else
                {
                    root.put(name, toDocument(val));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return root;
    }


    /**
     * _id를 제외한 매개 변수를 도큐먼트로 만듭니다.
     * @param o
     * @return
     */
    public Document toDocumentIgnoreId(Object o)
    {
        Class tClass = o.getClass();
        Document root = new Document();
        Field[] fields = tClass.getFields();

        for(int i = 0 ; i < fields.length ; i+=1)
        {
            Field field = fields[i];
            String name = field.getName();

            try {
                Object val = field.get(o);
                if(isDefaultType(field))
                {
                    root.put(name, val);
                }
                else if(isList(field))
                {
                    List list = (List)val;
                    List<Document> documents = new ArrayList();
                    for(int j = 0 ; j < list.size(); j+=1)
                    {
                        Object listValue = list.get(j);
                        documents.add(toDocument(listValue));
                    }
                    root.put(name, documents);
                }
                else if(isObjectId(field))
                {

                }
                else
                {
                    root.put(name, toDocument(val));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return root;
    }



    public Object toObject(Class t, Document document)
    {
        Object o = null;
        try {
            o = t.newInstance();
        } catch (InstantiationException e) {
            System.out.println("인스턴스를 생성할 수 없습니다. 기본 생성자가 필요합니다.");
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        Field[] fields = t.getFields();

        for(int i = 0 ; i < fields.length ; i+=1)
        {
            Field field = fields[i];
            String name = field.getName();
            try {
                Object value = document.get(name);
                String typeName = field.getType().getSimpleName();
                if(isDefaultType(typeName) || isObjectId(typeName))
                {
                    field.set(o, value);
                }
                else if(isList(typeName))
                {
                    List list = new ArrayList();
                    List<Document> array = (List)value;
                    int length = array.size();
                    Type innerType = getGenericType(field);
                    for(int j = 0 ; j < length ; j+=1)
                    {
                        list.add(toObject((Class)innerType, array.get(j)));
                    }
                    field.set(o, list);
                }
                else
                {
                    field.set(o, toObject(field.getType(), (Document)value));
                }
            } catch (JSONException e) {
                System.out.println(String.format("JSON 에서 field 이름 %s를 찾을 수 없습니다.", name));
            } catch (IllegalAccessException e) {
                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));
            } catch (IllegalArgumentException e) {
                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));
            }
        }
        return o;
    }



    public class Database
    {


        private MongoDatabase db;



        public Database(String dbName)
        {
            db = client.getDatabase(dbName);
        }


        public Database(MongoDatabase db)
        {
            this.db = db;
        }


        public DBCollection getCollection(String collectionName)
        {
            return new DBCollection(db, collectionName);
        }


        public void drop()
        {
            db.drop();
        }



    }




    public class DBCollection
    {


        private MongoCollection collection;




        public DBCollection(MongoDatabase db, String collectionName)
        {
            collection = db.getCollection(collectionName);
        }




        public DBCollection(MongoCollection collection)
        {
            this.collection = collection;

        }




        public void drop()
        {
            collection.drop();
        }





        public void insert(Object o)
        {
            collection.insertOne(toDocumentIgnoreId(o));
        }





        public void insert(List list)
        {
            for(int i = 0 ; i< list.size() ; i+=1)
            {
                insert(list.get(i));
            }
        }



        public void update(List list)
        {
            for(int i = 0 ; i < list.size(); i+=1)
            {
                update(list.get(i));
            }
        }



        public void update(Object o)
        {
            Document query = new Document();
            try {
                Field field = o.getClass().getField("_id");
                query.append("_id", field.get(o));
                update(query, o);
            } catch (NoSuchFieldException e) {
                System.out.println("_id 가 존재하지 않습니다.");
            } catch (IllegalAccessException e) {
                System.out.println("_id 를 가져올 수 없습니다.");
            }

        }



        public void update(Document query, Object o)
        {
            collection.updateOne(query, toDocument(o));
        }


        /**
         * 쿼리와 일치하는 데이터 하나를 삭제합니다.
         * @param query
         */
        public void deleteOne(Document query)
        {
            collection.deleteOne(query);
        }


        /**
         * 쿼리와 일치하는 모든 데이터를 삭제합니다.
         * @param query 쿼리
         */
        public void deleteMany(Document query)
        {
            collection.deleteMany(query);
        }


        /**
         * 데이터베이스에서 데이터를 찾습니다.
         * @param t 타입
         * @param query 쿼리
         * @return
         */
        public List find(Class t, Bson query)
        {
            FindIterable iterator = collection.find(query);
            return find(t, iterator);
        }


        /**
         * 데이터베이스에서 데이터를 찾습니다.
         * @param t 타입
         * @param query 쿼리
         * @param limit 정렬
         * @return
         */
        public List find(Class t, Bson query, int limit)
        {
            FindIterable iterator = collection.find(query);
            iterator.limit(limit);
            return find(t, iterator);
        }


        /**
         * 데이터베이스에서 데이터를 찾습니다.
         * @param t 타입
         * @param query 쿼리
         * @param sort 정렬
         * @return
         */
        public List find(Class t, Bson query, Bson sort)
        {
            FindIterable iterator = collection.find(query);
            iterator.sort(sort);
            return find(t, iterator);
        }


        /**
         * 데이터베이스에서 데이터를 찾습니다.
         * @param t 타입
         * @param query 쿼리
         * @param sort 정렬
         * @param limit 개수 제한
         * @return
         */
        public List find(Class t, Bson query, Bson sort, int limit)
        {
            FindIterable iterator = collection.find(query);
            iterator.sort(sort);
            iterator.limit(limit);
            return find(t, iterator);
        }


        public List find(Class t, FindIterable iterable)
        {
            List list = new ArrayList();
            iterable.forEach((Block) o -> list.add(toObject(t, (Document)o)));
            return list;
        }



    }



}

