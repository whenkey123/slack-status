package com.apple.slack.status.database.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseManager {

    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private MongoCollection<Document> slackUsersCollection;

    @Autowired
    public DatabaseManager(final MongoDatabase mongoDatabase) {
        slackUsersCollection = mongoDatabase.getCollection("slackUsers");
    }

    public void saveSlackUser(final String dsId, final String username, final String token, final String scopes) {
        if (!slackUserExists(username)) {
            final Document slackUser = new Document("_id", dsId);
            slackUser.append("username", username);
            slackUser.append("token", token);
            slackUser.append("scopes", scopes);
            slackUsersCollection.insertOne(slackUser);
        }else {
            final Document slackUser = new Document("username", username);
            slackUser.append("token", token);
            slackUser.append("scopes", scopes);
            slackUsersCollection.updateOne(Filters.eq("_id", dsId), slackUser);
        }
    }

    public boolean slackUserExists(final String dsId) {
        return getXoxpToken(dsId) != null;
    }

    public String getXoxpToken(final String dsId) {
        final Document slackUser = slackUsersCollection.find(Filters.eq("_id", dsId)).first();
        if (slackUser != null && slackUser.containsKey("token")) {
            return slackUser.getString("token");
        }
        return null;
    }
}
