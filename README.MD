# JMongoDBReflection for JAVA
## Created by SKAIBlue
### Before using 사용하기 전에
This library need mongodb-drive 3.2.2<br>
You can download from [here](https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongodb-driver/3.2.2/mongodb-driver-3.2.2.jar)<br>
이 라이브러리는 mongodb-driver 3.2.2가 필요합니다.<br>
[여기](https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongodb-driver/3.2.2/mongodb-driver-3.2.2.jar)에서 다운받을 수 있습니다.

You can download this library from [here](https://github.com/SKAIBlue/JMongoDBReflection/raw/master/lib/MongoDBReflection.jar)<br>
이 라이브러리는 [여기](https://github.com/SKAIBlue/JMongoDBReflection/raw/master/lib/MongoDBReflection.jar)에서 다운받을 수 있습니다.

### Usage 사용법
First, you have to import package.<br>
먼저, 패키지를 import 해야합니다.
```JAVA
import com.inursoft.mongodbreflection.*;
```

Next, Connect your mongodb server.<br>
다음은 mongodb 서버에 연결해야 합니다.
```JAVA
/**
* Connect mongodb server
* port default 27017
* @param host database host name 
*/
MongoDB db = new MongoDB("localhost");

/**
* Connect mongodb server
* @param host database host name
* @param port databse host port
*/
MongoDB db = new MongoDB("localhost", 27017);
```

You can get database like this.<br>
아래와 같이 database 를 가져올 수 있습니다.
```JAVA
Database database = db.getDatabase(DB_NAME);
```

You can get collection like this.<br>
collection 은 다음과 같이 가져울 수 있습니다.
```JAVA
DBCollection collection = database.getCollection(COLLECTION_NAME);
```
or, You can get collection only one line like this.<br>
또는 아래와 같이 한번에 collection 을 가져올 수 있습니다. 
```JAVA
DBCollection collection = db.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);

DBCollection collection = db.getCollection(DB_NAME, COLLECTION_NAME); 
```

Finally, You can find, update, insert, delete data you want!<br>
if you want find data just do this.<br>
드디어! 원하는대로 데이터를 찾고, 갱신하고, 추가하고, 삭제할 수 있습니다.<br>
아래의 방법으로 데이터를 찾을 수 있습니다.
```JAVA
Document query = new Document();
query.append(/*query*/);
List<Type> results = db.find(Type.class, query);
```

if you want sort data by 'time' to ascending.<br>
만약 'time'으로 오름차순 정렬한 데이터를 얻고싶다면 아래의 방법을 사용하시면 됩니다.
```JAVA
Document query = new Document();
query.append(/*query*/);
List<Type> results = db.find(Type.class, query, Sorts.ascending("time"));
```

You can set data limits.<br>
찾을 데이터에 제한을 두고싶다면 아래처럼 사용하시면 됩니다.
```JAVA
Document query = new Document();
query.append(/*query*/);
List<Type> results = db.find(Type.class, query, 10);
```

