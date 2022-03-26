package mobile.model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@RestResource(exported=false)
@Document(collection = "User")
public class User {
    @JsonIgnore
    @Id
    protected  ObjectId _id;
    protected  String username;
    protected  String email;
    @JsonIgnore
    protected  String password;
    protected  String tenhienthi;
    protected   Date  birthdate;
    protected   Date createdate;
    protected  String image;
    protected Boolean active;
    protected  String status;
    @DBRef
//    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    protected  Set<Role> roles = new HashSet<>();

    public User(ObjectId _id, String username, String email, String password, String tenhienthi, Date birthdate, Date createdate, String image, Boolean active, String status, Set<Role> roles) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.tenhienthi = tenhienthi;
        this.birthdate = birthdate;
        this.createdate = createdate;
        this.image = image;
        this.active = active;
        this.status = status;
        this.roles = roles;
    }

    public User() {
    }
    public User(String username, String email, String password) {
        this._id = new ObjectId();
        this.username = username;
        this.email = email;
        this.password = password;
        this.tenhienthi= username;
        this.birthdate = new Date();
        this.createdate = new Date();
        this.image ="";
        this.active = false;
        this.status="None";
    }



    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTenhienthi() {
        return tenhienthi;
    }

    public void setTenhienthi(String tenhienthi) {
        this.tenhienthi = tenhienthi;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}