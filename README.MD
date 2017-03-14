# JMongoDBReflection for JAVA
## Created by SKAIBlue
### Before using 사용하기 전에
This library need mongodb-drive 3.2.2<br>
You can download from [here](https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongodb-driver/3.2.2/mongodb-driver-3.2.2.jar)<br><br>

You can download this library from [here](https://github.com/SKAIBlue/JMongoDBReflection/raw/master/lib/MongoDBReflection.jar)

### Usage 사용법
First, you have to import package
```JAVA
import com.inursoft.mongodbreflection.*;
```

Next, Connect your mongodb server
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

You can get database like this.
```JAVA
Database database = db.getDatabase(DB_NAME);
```

You can get collection like this.
```JAVA
DBCollection collection = database.getCollection(COLLECTION_NAME);
```
or, You can get collection only one line like this.
```JAVA
DBCollection collection = db.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);

DBCollection collection = db.getCollection(DB_NAME, COLLECTION_NAME); 
```

