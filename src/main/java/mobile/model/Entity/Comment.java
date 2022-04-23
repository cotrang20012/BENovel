package mobile.model.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestResource(exported=false)
@Document(collection = "comments")
public class Comment {
    @JsonIgnore
    @Id
    protected ObjectId _id;
    protected ObjectId parentId;
    protected List<Comment> listChild;
    protected String urltruyen;
    @DBRef
    protected User user;
    protected int numChild;
    protected int depth;
    protected String content;
    protected Date createdate;
    public Date getCreatedate() {return createdate;}
    public void setCreatedate(Date createdate) {this.createdate = createdate;}
    public Comment() {
    }

    public Comment(String urltruyen, User user, ObjectId parentId, String content) {
        this._id = new ObjectId();
        this.parentId = parentId;
        this.listChild = new ArrayList<>();
        this.urltruyen = urltruyen;
        this.user = user;
        this.numChild = 0;
        this.depth = 0;
        this.content = content;
        this.createdate = new Date();
    }

    public Comment(ObjectId _id, ObjectId parentId, List<Comment> listChild, String urltruyen, User user, int numChild, int depth, String content) {
        this._id = _id;
        this.parentId = parentId;
        this.listChild = listChild;
        this.urltruyen = urltruyen;
        this.user = user;
        this.numChild = numChild;
        this.depth = depth;
        this.content = content;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getParentId() {
        return parentId;
    }

    public void setParentId(ObjectId parentId) {
        this.parentId = parentId;
    }

    public List<Comment> getListChild() {
        return listChild;
    }

    public void setListChild(List<Comment> listChild) {
        this.listChild = listChild;
    }

    public String getUrltruyen() {
        return urltruyen;
    }

    public void setUrltruyen(String urltruyen) {
        this.urltruyen = urltruyen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumChild() {
        return numChild;
    }

    public void setNumChild(int numChild) {
        this.numChild = numChild;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}