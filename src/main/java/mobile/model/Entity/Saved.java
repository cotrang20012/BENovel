package mobile.model.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported=false)
@Document(collection = "saveds")
public class Saved {
    @Id
    protected ObjectId _id;
    @DBRef
    protected User user;
    @DBRef
    protected Novel novel;
    public ObjectId getId() {return _id;}
    public void setId(ObjectId _id) {this._id = _id;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public Novel getNovel() {return novel;}
    public void setNovel(Novel novel) {this.novel = novel;}
    public Saved() {
    }
    public  Saved(ObjectId id, User user, Novel novel){
        this._id = id;
        this.user=user;
        this.novel=novel;
    }
    public Saved(User user, Novel novel){
        this._id = new ObjectId();
        this.user=user;
        this.novel=novel;
    }

}
