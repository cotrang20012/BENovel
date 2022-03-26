package mobile.model.Entity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported=false)
@Document(collection = "Reading")
public class Reading {
    @Id
    protected ObjectId _id;
    protected ObjectId dautruyenId;
    protected ObjectId chapterId;
    protected ObjectId userId;
    protected int chapnumber;
    protected String url;
    
    public Reading() {
    	
    }
    
	public Reading(ObjectId dautruyenId, ObjectId chapterId, ObjectId userId, int chapnumber, String url) {
		this._id = new ObjectId();
		this.dautruyenId = dautruyenId;
		this.chapterId = chapterId;
		this.userId = userId;
		this.chapnumber = chapnumber;
		this.url = url;
	}

	public Reading(ObjectId _id, ObjectId dautruyenId, ObjectId chapterId, ObjectId userId, int chapnumber,
			String url) {
		this._id = _id;
		this.dautruyenId = dautruyenId;
		this.chapterId = chapterId;
		this.userId = userId;
		this.chapnumber = chapnumber;
		this.url = url;
	}

	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	public ObjectId getDautruyenId() {
		return dautruyenId;
	}
	public void setDautruyenId(ObjectId dautruyenId) {
		this.dautruyenId = dautruyenId;
	}
	public ObjectId getChapterId() {
		return chapterId;
	}
	public void setChapterId(ObjectId chapterId) {
		this.chapterId = chapterId;
	}
	public ObjectId getUserId() {
		return userId;
	}
	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	public int getChapnumber() {
		return chapnumber;
	}
	public void setChapnumber(int chapnumber) {
		this.chapnumber = chapnumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
}