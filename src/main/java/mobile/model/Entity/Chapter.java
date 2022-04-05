package mobile.model.Entity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;

@RestResource(exported=false)
@Document(collection = "chapters")
public class Chapter {
    @Id
    protected ObjectId _id;
    protected int chapnumber;
    protected String content;
    @DBRef
    protected Novel dautruyenId;
    protected String tenchap;
    @CreatedDate
    protected Date createAt;
    @LastModifiedDate
    protected Date updateAt;

    public Chapter() {
    }

    public Chapter(ObjectId _id, int chapnumber, String content, Novel dautruyenId, String tenchap, Date createAt, Date updateAt) {
        this._id = _id;
        this.chapnumber = chapnumber;
        this.content = content;
        this.dautruyenId = dautruyenId;
        this.tenchap = tenchap;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }

    public int getChapnumber() {
        return chapnumber;
    }

    public void setChapnumber(int chapnumber) {
        this.chapnumber = chapnumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Novel getDautruyenId() {
        return dautruyenId;
    }

    public void setDautruyenId(Novel dautruyenId) {
        this.dautruyenId = dautruyenId;
    }

    public String getTenchap() {
        return tenchap;
    }

    public void setTenchap(String tenchap) {
        this.tenchap = tenchap;
    }

    public Date getCreateAt() {        return createAt;    }

    public void setCreateAt(Date createAt) {        this.createAt = createAt;    }

    public Date getUpdateAt() {        return updateAt;    }

    public void setUpdateAt(Date updateAt) {        this.updateAt = updateAt;    }
}
